package ru.bezuglov.wallet.dto;

import jakarta.validation.constraints.Positive;
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
@EqualsAndHashCode
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
    @Positive
    private Long amount;

    /**
     * Тип операции.
     */
    private OperationType operationType;
}
