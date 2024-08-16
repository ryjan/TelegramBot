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
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column (name = "id")
    private int id;
    @Column (name = "gems")
    private BigDecimal gems;
    @Column (name = "coins")
    private BigDecimal coins;

    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    public Bank() {

    }

    public Bank(BigDecimal gems, BigDecimal coins) {
        this.gems = gems;
        this.coins = coins;
        user = new User();
    }

    public int getId() {
        return id;
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

    public void setGems(BigDecimal gems) {
        this.gems = gems;
    }

    public void setCoins(BigDecimal coins) {
        this.coins = coins;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Bank [id=" + id + ",\n gems=" + gems + ",\n coins=" + coins + "]";
    }
}
