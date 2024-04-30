package ru.bezuglov.wallet.dto;

import lombok.*;
import ru.bezuglov.wallet.util.OperationType;

import java.util.UUID;

/**
 * Класс RequestWalletDto для изменения баланса кошелька.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestWalletDto {

    /**
     * Идентификатор кошелька.
     */
    private UUID walletId;

    /**
     * Сумма операции.
     */
    private Long amount;

    /**
     * Тип операции.
     */
    private OperationType operationType;
}
