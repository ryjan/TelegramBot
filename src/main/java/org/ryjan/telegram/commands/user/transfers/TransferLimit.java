package org.ryjan.telegram.commands.user.transfers;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import org.ryjan.telegram.database.UserDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class TransferLimit {
    @Id
    private long id;

    @OneToOne
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
