package org.ryjan.telegram.model.groups;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private String creatorId;
    private String creatorUsername;
    private String status;
    private String createdAt;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<ChatSettings> chatSettings = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<Blacklist> blacklists = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<GroupMemberList> groupMemberLists = new ArrayList<>();

    public Groups() {

    }

    public Groups(Long id, String groupName, GroupPrivileges privileges, String status,
                  String creatorId, String creatorUsername) {
        this.id = id;
        this.groupName = groupName;
        this.privileges = privileges.getDisplayName();
        this.status = status;
        this.creatorId = creatorId;
        this.creatorUsername = creatorUsername;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.createdAt = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter).toString();
        setBlacklists(blacklists);
    }

    public void addBlacklist(Blacklist blacklist) {

        blacklists.add(blacklist);
    }

    public void addChatSetting(ChatSettings chatSetting) {

        chatSettings.add(chatSetting);
        chatSetting.setGroups(this);
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GroupMemberList> getGroupMemberLists() {
        return groupMemberLists;
    }

    public void setGroupMemberLists(List<GroupMemberList> groupMemberLists) {
        this.groupMemberLists = groupMemberLists;
    }
}
