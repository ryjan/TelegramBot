package org.ryjan.telegram.model.groups;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
