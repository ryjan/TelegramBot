package org.ryjan.telegram.database;

import jakarta.persistence.*;
import org.ryjan.telegram.utils.UserGroup;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
@Table (name = "users")
public class UserDatabase {

    @Id
    //@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "user_seq") // сделать присвоение через telegram.user.getId
    @Column (name = "user_id")
    private long id;
    @Column (name = "user_tag")
    private String userTag;
    @Column (name = "user_group")
    private String userGroup;

    @OneToOne(mappedBy = "userDatabase" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private BankDatabase bankDatabase;

    public UserDatabase() {

    }

    public UserDatabase(long id ,String userTag, UserGroup userGroup) {
        this.id = id;
        this.userTag = userTag;
        this.userGroup = userGroup.getDisplayname();
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

    @Override
    public String toString() {
        return "User [id=" + id + ",\n userTag=" + userTag + ",\n userGroup=" + userGroup + "]";
    }
}
