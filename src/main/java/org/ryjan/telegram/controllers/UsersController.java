package org.ryjan.telegram.controllers;

import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    RedisTemplate<String, UserDatabase> redisTemplate;

    @Autowired
    private UserService userService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        UserDatabase user = userService.findUser(userId);
        userService.delete(user);
        redisTemplate.delete(userService.CACHE_KEY + user);

        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/getIdByUsername")
    public Long getIdByUsername(@RequestParam String username) {
        return userService.findUser(username).getId();
    }
}
