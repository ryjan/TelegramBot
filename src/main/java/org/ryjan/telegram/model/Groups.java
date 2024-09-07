package org.ryjan.telegram.model;

import jakarta.persistence.*;
import org.ryjan.telegram.commands.groups.Privileges;

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



    public Groups() {

    }

    public Groups(Long id, String groupName, Privileges privileges) {
        this.id = id;
        this.groupName = groupName;
        this.privileges = privileges.getDisplayName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.createdAt = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
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

    public void setPrivileges(Privileges privileges) {
        this.privileges = privileges.getDisplayName();
    }

    public List<ChatSettings> getChatSettings() {
        return chatSettings;
    }

    public void setChatSettings(List<ChatSettings> chatSettings) {
        this.chatSettings = chatSettings;
    }
}
