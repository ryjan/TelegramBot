package org.ryjan.telegram.model.groups;

import jakarta.persistence.*;

@Entity
public class GroupMemberList {

    @Id
    private Long id;
    private Long userId;
    private String username;
    private String userGroup;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "id")
    private Groups group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
}
