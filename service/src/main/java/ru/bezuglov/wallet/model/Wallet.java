package ru.bezuglov.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Класс кошелек содержит информацию о состоянии счета.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "wallets")
public class Wallet {

    /**
     * Идентификатор кошелька.
     */
    @Id
    private UUID id;

    /**
     * Баланс кошелька.
     */
    private long account;
}
