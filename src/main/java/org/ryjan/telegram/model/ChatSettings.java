package org.ryjan.telegram.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_settings", schema = "groups")
public class ChatSettings {

    @Id
    private Long id;
    @Column(unique = true)
    private String settingKey;
    private String settingValue;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "id")
    private Groups group;

    public ChatSettings() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public Groups getGroups() {
        return group;
    }

    public void setGroups(Groups groups) {
        this.group = groups;
    }
}
