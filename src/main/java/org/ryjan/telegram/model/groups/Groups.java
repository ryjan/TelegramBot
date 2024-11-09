package org.ryjan.telegram.model.groups;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ryjan.telegram.commands.groups.GroupPrivileges;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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
        this.createdAt = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(formatter);
        setBlacklists(blacklists);
    }

    public void addBlacklist(Blacklist blacklist) {

        blacklists.add(blacklist);
    }

    public void addChatSetting(ChatSettings chatSetting) {

        chatSettings.add(chatSetting);
        chatSetting.setGroups(this);
    }

}
