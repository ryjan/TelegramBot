package org.ryjan.telegram.controllers;

import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    RedisTemplate<String, User> redisUserDatabaseTemplate;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserService userService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        User user = userService.findUser(userId);
        try {
            userService.delete(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("{username}")
    public String getIdByUsername(@PathVariable String username) {
        return String.valueOf(userService.findUser(username).getId());
    }

    @PatchMapping("{userId}/group")
    public ResponseEntity<String> setUserGroup(@PathVariable Long userId, @RequestParam String userGroup) {
        User user = userService.findUser(userId);
        user.setUserGroup(UserPermissions.valueOf(userGroup.toUpperCase()));
        userService.setUserInRedis(user);
        userService.processAndSendUser(user);
        return ResponseEntity.ok("Successfully!\n User group switched to" + userGroup);
    }

    @GetMapping("/get/user-groups")
    public UserPermissions[] getAllUserGroups() {
        return UserPermissions.values();
    }
}
