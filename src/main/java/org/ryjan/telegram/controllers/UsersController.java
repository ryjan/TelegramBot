package org.ryjan.telegram.controllers;

import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    private RedisTemplate<String, User> redisUserDatabaseTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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
        userService.update(user);
        return ResponseEntity.ok("Successfully!\n User group switched to" + userGroup);
    }

    @GetMapping("/get/user-groups")
    public UserPermissions[] getAllUserGroups() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticated user: " + authentication.getName());
        System.out.println("User roles: " + authentication.getAuthorities());
        return UserPermissions.values();
    }
}
