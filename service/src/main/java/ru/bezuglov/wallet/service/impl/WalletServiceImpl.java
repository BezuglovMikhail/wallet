package ru.bezuglov.wallet.service.impl;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
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
     * Изменение баланса кошелька:
     * пополнение кошелька, снятие доступных средств.
     */
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Wallet changeAccount(RequestWalletDto requestWalletDto) {
        Wallet wallet = findWallet(requestWalletDto.getWalletId());
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
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Wallet findById(UUID walletId) {
        Wallet wallet = findWallet(walletId);
        log.info("Получен баланс кошелька {}.", walletId);
        return wallet;
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
