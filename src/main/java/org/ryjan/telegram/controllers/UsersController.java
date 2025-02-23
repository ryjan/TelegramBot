package org.ryjan.telegram.controllers;

import com.squareup.okhttp.Response;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        User user = userService.findUser(userId);
        try {
            userService.delete(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("{username}")
    public ResponseEntity<String> getIdByUsername(@PathVariable String username) {
        User user = userService.findUser(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok(String.valueOf(user.getId()));
    }

    @PatchMapping("{userId}/group")
    public ResponseEntity<String> setUserGroup(@PathVariable Long userId, @RequestParam String userGroup) {
        User user = userService.findUser(userId);
        user.setUserGroup(UserPermissions.valueOf(userGroup.toUpperCase()));
        userService.setUserInRedis(user);
        userService.save(user);
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
