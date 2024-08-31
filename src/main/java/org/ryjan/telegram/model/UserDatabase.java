package org.ryjan.telegram.model;

import jakarta.persistence.*;
import org.ryjan.telegram.commands.user.UserGroup;

import java.util.Objects;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
@Table (name = "users")
public class UserDatabase {

    @Id
    //@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "user_seq") // сделать присвоение через telegram.user.getId
    @Column (name = "user_id")
    private Long id;
    @Column (name = "user_tag")
    private String userTag;
    @Column (name = "user_group")
    private String userGroup;

    @OneToOne(mappedBy = "userDatabase" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private BankDatabase bankDatabase;

    public UserDatabase() {

    }

    public UserDatabase(long id, String userTag) {
        this.id = id;
        this.userTag = userTag;
        this.userGroup = UserGroup.USER.getDisplayname();
        this.bankDatabase = new BankDatabase();
        setBank(bankDatabase);
        bankDatabase.setTag(this.userTag);
    }

    public long getId() {
        return id;
    }

    public String getUserTag() {
        return userTag;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public BankDatabase getBank() {
        return bankDatabase;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
        bankDatabase.setTag(this.userTag);
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup.getDisplayname();
    }

    public void setBank(BankDatabase bankDatabase) {
        this.bankDatabase = bankDatabase;
        bankDatabase.setUser(this);
    }

    public boolean isOwner() {
        return UserGroup.OWNER.getDisplayname().equals(this.userGroup);
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
