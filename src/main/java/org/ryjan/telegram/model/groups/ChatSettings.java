package org.ryjan.telegram.model.groups;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_settings", schema = "groups")
public class ChatSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_settings_seq")
    @SequenceGenerator(
            name = "chat_settings_seq",
            sequenceName = "chat_settings_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    private long id;

    @Column(name = "group_id", insertable = false, updatable = false)
    private long groupId;
    private String settingKey;
    private String settingValue;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups group;

    public ChatSettings() {

    }

    public ChatSettings(String settingKey, String settingValue, Groups group) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.group = group;
        this.groupId = group.getId();
    }

    public void addSetting(String settingKey, String settingValue) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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
