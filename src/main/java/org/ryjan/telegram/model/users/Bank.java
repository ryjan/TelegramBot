package org.ryjan.telegram.model.users;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
@Table (name = "banks")
@Getter
@Setter
public class Bank {

    @Id
    private Long id;
    private String username;
    private BigDecimal gems;
    private BigDecimal coins;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @MapsId
    @Getter(AccessLevel.NONE)
    private User user;

    public Bank() {
        this.gems = new BigDecimal(0);
        this.coins = new BigDecimal(0);
    }

    public Bank(String username) {
        this.username = username;
        this.gems = new BigDecimal(0);
        this.coins = new BigDecimal(0);
    }

    public Bank(BigDecimal gems, BigDecimal coins) {
        this.gems = gems;
        this.coins = coins;
        this.user = new User();
    }

    @JsonBackReference
    public User getUser() {
        return user;
    }

    public Bank createBank(User user) {
        this.user = user;
        this.setUsername(user.getUsername());
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, gems, coins, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Bank bank = (Bank) obj;
        return Objects.equals(id, bank.id) && Objects.equals(username, bank.username) && Objects.equals(gems, bank.gems) &&
                Objects.equals(coins, bank.coins) && Objects.equals(user, bank.user);
    }

    @Override
    public String toString() {
        return "Bank:\n id=" + id + "\n gems=" + gems + "\n coins=" + coins + "]";
    }
}
