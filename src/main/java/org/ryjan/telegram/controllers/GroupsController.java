package org.ryjan.telegram.controllers;

import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
public class GroupsController {

    @Autowired
    private GroupService groupService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGroup(@RequestParam Long groupId) {
        Groups group = groupService.findGroup(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }

        groupService.delete(group);

        return ResponseEntity.ok("Group deleted successfully");
    }
}
