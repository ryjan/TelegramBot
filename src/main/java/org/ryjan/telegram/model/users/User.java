package org.ryjan.telegram.model.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ryjan.telegram.commands.users.user.UserPermissions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
@Table (name = "users")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    //@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "user_seq") // сделать присвоение через telegram.user.getId
    private Long id;
    private String username;
    private UserPermissions userGroup;
    private Integer level;
    private Double xp;
    private String createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private Bank bank = new Bank();

    public User() {

    }

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
        this.userGroup = UserPermissions.USER;
        this.level = 1;
        this.xp = 0.0;
        this.createdAt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());

        setBank(bank);
        bank.setUsername(this.username);
    }

    public void setBank(Bank bank) {
        this.bank = bank;
        bank.setUser(this);
    }

    public boolean isOwner() {
        return UserPermissions.OWNER.getName().equals(this.userGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userGroup, bank);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User other = (User) obj;
        return  Objects.equals(id, other.id) && Objects.equals(username, other.username) &&
                Objects.equals(userGroup, other.userGroup) && Objects.equals(bank, other.bank);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ",\n userTag=" + username + ",\n userGroup=" + userGroup + "]";
    }
}
