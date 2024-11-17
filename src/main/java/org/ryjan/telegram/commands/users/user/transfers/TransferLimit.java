package org.ryjan.telegram.commands.users.user.transfers;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ryjan.telegram.model.users.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class TransferLimit {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private User user;
    private BigDecimal dailyTransferAmount;
    private LocalDate lastTransferDate;
}
