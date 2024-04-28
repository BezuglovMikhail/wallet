package ru.bezuglov.wallet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bezuglov.wallet.dto.RequestWalletDto;
import ru.bezuglov.wallet.exeption.exeptions.BadRequestException;
import ru.bezuglov.wallet.model.Wallet;
import ru.bezuglov.wallet.repository.WalletRepository;
import ru.bezuglov.wallet.service.WalletService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Wallet create() {
        Wallet newWallet = new Wallet();
        UUID uuid = UUID.randomUUID();
        newWallet.setId(uuid);
        return walletRepository.save(newWallet);
    }

    @Override
    public Wallet changeAccount(RequestWalletDto requestWalletDto) {
        Wallet wallet = walletRepository.getReferenceById(requestWalletDto.getWalletId());
        checkAmount(requestWalletDto.getAmount());
        switch (requestWalletDto.getOperationType()) {
            case DEPOSIT -> wallet.setAccount(rechargeAccount(wallet.getAccount(), requestWalletDto.getAmount()));
            case WITHDRAW -> wallet.setAccount(withdrawAmount(wallet.getAccount(), requestWalletDto.getAmount()));
        }
        log.info("Операция {} выполнена.", requestWalletDto.getOperationType());
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public Wallet getById(UUID walletId) {
        Wallet wallet = walletRepository.getReferenceById(walletId);
        log.info("Получен баланс кошелька {}.", walletId);
        return wallet;
    }

    private void checkAmount(Long amount) {
        if (amount == null) {
            throw new BadRequestException("Сумма изменения баланса не может быть пустой!");
        } else if (amount <= 0) {
            throw new BadRequestException("Сумма изменения баланса не может быть отрицательной или равной нулю!");
        }
    }

    private Long rechargeAccount(Long oldAccount, Long amount) {
        return oldAccount + amount;
    }

    private Long withdrawAmount(Long oldAccount, Long amount) {
        if (oldAccount >= amount) {
            return oldAccount - amount;
        } else {
            throw new BadRequestException("Сумма снятия превышает остаток средств на счете!");
        }
    }
}
