package org.ryjan.telegram.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(schema = "#{T(org.ryjan.telegram.config.SchemaContextHolder).getSchemaName()}")
public class Blacklist {

    @Id
    private Long id;
    private String groupName;
    private Long userId;
    private String username;
    LocalDateTime createdAt;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "id")
    private Groups group;

    public Blacklist() {

    }

    public Blacklist(Long id, String groupName, Long userId, String username, LocalDateTime createdAt) {
        this.id = id;
        this.groupName = groupName;
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
}
