package ru.bezuglov.wallet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bezuglov.wallet.dto.RequestWalletDto;
import ru.bezuglov.wallet.exeption.exeptions.BadRequestException;
import ru.bezuglov.wallet.exeption.exeptions.NotFoundException;
import ru.bezuglov.wallet.model.Wallet;
import ru.bezuglov.wallet.repository.WalletRepository;
import ru.bezuglov.wallet.service.WalletService;

import java.util.Optional;
import java.util.UUID;

/**
 * Класс WalletServiceImpl содержит бизнес логику работы с кошельком.
 *
 * @author Михаил Безуглов
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    /**
     * Создание нового кошелька.
     */
    @Override
    public Wallet create() {
        Wallet newWallet = new Wallet();
        UUID uuid = UUID.randomUUID();
        newWallet.setId(uuid);
        return walletRepository.save(newWallet);
    }

    /**
     * Изменение баланса кошелька:
     * пополнение кошелька, снятие доступных средств.
     */
    @Override
    public Wallet changeAccount(RequestWalletDto requestWalletDto) {
        Wallet wallet = findWallet(requestWalletDto.getWalletId());
        checkAmount(requestWalletDto.getAmount());
        switch (requestWalletDto.getOperationType()) {
            case DEPOSIT -> wallet.setAccount(rechargeAccount(wallet.getAccount(), requestWalletDto.getAmount()));
            case WITHDRAW -> wallet.setAccount(withdrawAmount(wallet.getAccount(), requestWalletDto.getAmount()));
        }
        log.info("Операция {} выполнена.", requestWalletDto.getOperationType());
        return walletRepository.save(wallet);
    }

    /**
     * Получение баланса кошелька по его id.
     */
    @Override
    @Transactional(readOnly = true)
    public Wallet findById(UUID walletId) {
        Wallet wallet = findWallet(walletId);
        log.info("Получен баланс кошелька {}.", walletId);
        return wallet;
    }

    /**
     * Проверка суммы изменения баланса.
     */
    private void checkAmount(Long amount) {
        if (amount == null) {
            throw new BadRequestException("Сумма изменения баланса не может быть пустой!");
        } else if (amount <= 0) {
            throw new BadRequestException("Сумма изменения баланса не может быть отрицательной или равной нулю!");
        }
    }

    /**
     * Пополнение баланса.
     */
    private Long rechargeAccount(Long oldAccount, Long amount) {
        return oldAccount + amount;
    }

    /**
     * Снятие доступных средств.
     */
    private Long withdrawAmount(Long oldAccount, Long amount) {
        if (oldAccount >= amount) {
            return oldAccount - amount;
        } else {
            throw new BadRequestException("Сумма снятия превышает остаток средств на счете!");
        }
    }

    /**
     * Поиск кошелька.
     */
    private Wallet findWallet(UUID walletId) {
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        if (wallet.isPresent()) {
            return wallet.get();
        } else {
            throw new  NotFoundException("Кошелек с id = " + walletId + " не найден.");
        }
    }
}
