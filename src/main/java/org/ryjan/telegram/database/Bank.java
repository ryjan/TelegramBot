package org.ryjan.telegram.database;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
@Table (name = "bank")
public class Bank {

    @Id
    @Column (name = "id")
    private int id;
    @Column(name = "tag")
    private String tag;
    @Column (name = "gems")
    private BigDecimal gems;
    @Column (name = "coins")
    private BigDecimal coins;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    public Bank() {

    }

    public Bank(String tag) {
        this.tag = tag;
        this.gems = new BigDecimal(0);
        this.coins = new BigDecimal(0);
    }

    public Bank(BigDecimal gems, BigDecimal coins) {
        this.gems = gems;
        this.coins = coins;
        this.user = new User();
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public BigDecimal getGems() {
        return gems;
    }

    public BigDecimal getCoins() {
        return coins;
    }

    public User getUser() {
        return user;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setGems(BigDecimal gems) {
        this.gems = gems;
    }

    public void setCoins(BigDecimal coins) {
        this.coins = coins;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bank createBank(User user) {
        Bank bank = new Bank();
        bank.setUser(user);
        bank.setTag(user.getUserTag());
        return bank;
    }

    @Override
    public String toString() {
        return "Bank [id=" + id + ",\n gems=" + gems + ",\n coins=" + coins + "]";
    }
}
