package org.ryjan.telegram.model.groups;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "groups")
@Getter
@Setter
public class UserLevels {
    @Id
    private Long id;
}
