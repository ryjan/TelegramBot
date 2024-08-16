package org.ryjan.telegram.database;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column (name = "user_id")
    private int id;
    @Column (name = "user_tag")
    private String userTag;
    @Column (name = "user_group")
    private String userGroup;

    @OneToOne(mappedBy = "user" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Bank bank;

    public User() {

    }

    public User(String userTag, String userGroup) {
        this.userTag = userTag;
        this.userGroup = userGroup;
        this.bank = new Bank(userTag);
        setBank(bank);
    }

    public int getId() {
        return id;
    }

    public String getUserTag() {
        return userTag;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public Bank getBank() {
        return bank;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
        bank.setUser(this);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ",\n userTag=" + userTag + ",\n userGroup=" + userGroup + "]";
    }
}
