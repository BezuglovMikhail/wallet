package ru.bezuglov.wallet.dto;

import lombok.*;

import java.util.UUID;

/**
 * Класс ResponseWalletDto для передачи информации об кошельке.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseWalletDto {

    /**
     * Идентификатор кошелька.
     */
    private UUID id;

    /**
     * Баланс кошелька.
     */
    private Long account;
}
