package org.ryjan.telegram.commands.user.transfers;

import jakarta.persistence.*;
import org.ryjan.telegram.model.UserDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transfer_limit")
public class TransferLimit {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private UserDatabase userDatabase;
    private BigDecimal dailyTransferAmount;
    private LocalDate lastTransferDate;

    public BigDecimal getDailyTransferAmount() {
        return this.dailyTransferAmount;
    }

    public void setDailyTransferAmount(BigDecimal dailyTransferAmount) {
        this.dailyTransferAmount = dailyTransferAmount;
    }

    public LocalDate getLastTransferDate() {
        return this.lastTransferDate;
    }

    public void setLastTransferDate(LocalDate lastTransferDate) {
        this.lastTransferDate = lastTransferDate;
    }

    public UserDatabase getUserDatabase() {
        return this.userDatabase;
    }

    public void setUserDatabase(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }
}
