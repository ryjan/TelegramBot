package org.ryjan.telegram.database;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
@Table (name = "bank")
public class BankDatabase {

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
    private UserDatabase userDatabase;

    public BankDatabase() {
        this.gems = new BigDecimal(0);
        this.coins = new BigDecimal(0);
    }

    public BankDatabase(String tag) {
        this.tag = tag;
        this.gems = new BigDecimal(0);
        this.coins = new BigDecimal(0);
    }

    public BankDatabase(BigDecimal gems, BigDecimal coins) {
        this.gems = gems;
        this.coins = coins;
        this.userDatabase = new UserDatabase();
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

    public UserDatabase getUser() {
        return userDatabase;
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

    public void setUser(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public BankDatabase createBank(UserDatabase userDatabase) {
        BankDatabase bankDatabase = new BankDatabase();
        bankDatabase.setUser(userDatabase);
        bankDatabase.setTag(userDatabase.getUserTag());
        return bankDatabase;
    }

    @Override
    public String toString() {
        return "Bank [id=" + id + ",\n gems=" + gems + ",\n coins=" + coins + "]";
    }
}
