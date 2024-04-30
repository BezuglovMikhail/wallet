package ru.bezuglov.wallet.service;

import ru.bezuglov.wallet.dto.RequestWalletDto;
import ru.bezuglov.wallet.model.Wallet;

import java.util.UUID;

/**
 * Интерфейс WalletService содержит методы действий с кошельком.
 *
 * @author Михаил Безуглов
 */
public interface WalletService {

    /**
     * Добавление нового кошелька.
     */
    Wallet create();

    /**
     * Изменение баланса кошелька.
     */
    Wallet changeAccount(RequestWalletDto requestWalletDto);

    /**
     * Получение баланса кошелька по его UUID.
     */
    Wallet findById(UUID WALLET_UUID);
}
