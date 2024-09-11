package org.ryjan.telegram.model.groups;

import jakarta.persistence.*;
import org.ryjan.telegram.commands.groups.GroupPrivileges;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups", schema = "groups")
public class Groups {

    @Id
    private Long id;
    private String groupName;
    private String privileges;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ChatSettings> chatSettings = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
    private List<Blacklist> blacklists = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GroupMemberList> groupMemberLists = new ArrayList<>();

    public Groups() {

    }

    public Groups(Long id, String groupName, GroupPrivileges privileges) {
        this.id = id;
        this.groupName = groupName;
        this.privileges = privileges.getDisplayName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.createdAt = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
        setBlacklists(blacklists);
    }

    public void addBlacklist(Blacklist blacklist) {

        blacklists.add(blacklist);
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

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ChatSettings> getChatSettings() {
        return chatSettings;
    }

    public void setChatSettings(List<ChatSettings> chatSettings) {
        this.chatSettings = chatSettings;
    }

    public List<Blacklist> getBlacklists() {
        return blacklists;
    }

    public void setBlacklists(List<Blacklist> blacklists) {
        this.blacklists = blacklists;
    }

    public List<GroupMemberList> getGroupMemberLists() {
        return groupMemberLists;
    }

    public void setGroupMemberLists(List<GroupMemberList> groupMemberLists) {
        this.groupMemberLists = groupMemberLists;
    }
}
