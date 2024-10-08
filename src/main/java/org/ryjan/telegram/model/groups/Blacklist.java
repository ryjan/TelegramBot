package org.ryjan.telegram.model.groups;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "blacklist", schema = "groups")
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blacklist_seq")
    @SequenceGenerator(
            name = "blacklist_seq",
            sequenceName = "blacklist_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    private Long id;

    @Column(name = "group_id", insertable = false, updatable = false)
    private Long groupId;
    private String groupName;
    private Long userId;
    private String userFirstname;
    private String username;
    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @JsonBackReference
    private Groups group;

    public Blacklist() {

    }

    public Blacklist(String groupName, Long userId, String username, String userFirstname) {
        this.groupName = groupName;
        this.userId = userId;
        this.userFirstname = userFirstname;
        this.username = username;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        this.createdAt = LocalDateTime.parse(dtf.format(LocalDateTime.now()), dtf);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
}
