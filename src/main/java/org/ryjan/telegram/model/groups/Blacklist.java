package org.ryjan.telegram.model.groups;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
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
    private String createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Groups group;

    public Blacklist() {

    }

    public Blacklist(String groupName, Long userId, String username, String userFirstname) {
        this.groupName = groupName;
        this.userId = userId;
        this.userFirstname = userFirstname;
        this.username = username;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.parse(dtf.format(LocalDateTime.now()), dtf);
        this.createdAt = now.toString();
    }
}
