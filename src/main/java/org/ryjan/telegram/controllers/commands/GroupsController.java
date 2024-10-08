package org.ryjan.telegram.controllers.commands;

import org.ryjan.telegram.model.groups.Groups;
import org.ryjan.telegram.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/groups")
public class GroupsController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private RedisTemplate<String, Groups> redisTemplate;

    @PostMapping("/delete")
    public ResponseEntity<String> deleteGroup(@RequestParam Long groupId) {
        Groups group = groupService.findGroup(groupId);
        groupService.delete(group);
        redisTemplate.delete("groups:" + groupId);

        return ResponseEntity.ok("Group deleted successfully");
    }
}
