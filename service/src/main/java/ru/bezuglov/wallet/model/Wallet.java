package ru.bezuglov.wallet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@ToString
@EqualsAndHashCode
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
