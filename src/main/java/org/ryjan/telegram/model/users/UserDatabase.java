package org.ryjan.telegram.model.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.ryjan.telegram.commands.users.user.UserPermissions;
import org.ryjan.telegram.interfaces.Permissions;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
@Table (name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDatabase {

    @Id
    //@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "user_seq") // сделать присвоение через telegram.user.getId
    private Long id;
    private String userTag;
    private UserPermissions userGroup;
    private String createdAt;

    @OneToOne(mappedBy = "userDatabase", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private BankDatabase bankDatabase = new BankDatabase();

    public UserDatabase() {

    }

    public UserDatabase(Long id, String userTag) {
        this.id = id;
        this.userTag = userTag;
        this.userGroup = UserPermissions.USER;
        this.createdAt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());

        setBank(bankDatabase);
        bankDatabase.setTag(this.userTag);
    }

    public long getId() {
        return id;
    }

    public String getUserTag() {
        return userTag;
    }

    public Permissions getUserGroupAsPermission() {
        return userGroup;
    }

    public UserPermissions getUserGroup() {
        return userGroup;
    }

    public BankDatabase getBank() {
        return bankDatabase;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
        bankDatabase.setTag(this.userTag);
    }

    public void setUserGroup(UserPermissions userGroup) {
        this.userGroup = userGroup;
    }

    public void setBank(BankDatabase bankDatabase) {
        this.bankDatabase = bankDatabase;
        bankDatabase.setUser(this);
    }

    public boolean isOwner() {
        return UserPermissions.OWNER.getName().equals(this.userGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userTag, userGroup, bankDatabase);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserDatabase other = (UserDatabase) obj;
        return  Objects.equals(id, other.id) && Objects.equals(userTag, other.userTag) &&
                Objects.equals(userGroup, other.userGroup) && Objects.equals(bankDatabase, other.bankDatabase);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ",\n userTag=" + userTag + ",\n userGroup=" + userGroup + "]";
    }
}
