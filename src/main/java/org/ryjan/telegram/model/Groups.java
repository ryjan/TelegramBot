package org.ryjan.telegram.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups", schema = "groups")
public class Groups {

    @Id
    private Long id;
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ChatSettings> chatSettings = new ArrayList<>();

    public Groups() {

    }

    public Groups(Long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ChatSettings> getChatSettings() {
        return chatSettings;
    }

    public void setChatSettings(List<ChatSettings> chatSettings) {
        this.chatSettings = chatSettings;
    }
}
