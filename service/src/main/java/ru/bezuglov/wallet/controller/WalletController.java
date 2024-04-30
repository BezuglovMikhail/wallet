package ru.bezuglov.wallet.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.bezuglov.wallet.dto.RequestWalletDto;
import ru.bezuglov.wallet.dto.ResponseWalletDto;
import ru.bezuglov.wallet.mapper.WalletMapper;
import ru.bezuglov.wallet.service.WalletService;

/**
 * Класс WalletController содержит эндпойнты, относящиеся к работе с кошельком:
 * добавление нового кошелька, изменение баланса, получение баланса.
 *
 * @author Михаил Безуглов
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    /**
     * Эндпойнт добавления нового кошелька.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWalletDto create() {
        return walletMapper.mapToDto(walletService.create());
    }

    /**
     * Эндпойнт изменения баланса кошелька.
     */
    @PostMapping("/wallet")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWalletDto changeAccount(@RequestBody RequestWalletDto requestWalletDto) {
        return walletMapper.mapToDto(walletService.changeAccount(requestWalletDto));
    }

    /**
     * Эндпойнт получения баланса по id кошелька.
     */
    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseWalletDto findById(@PathVariable UUID WALLET_UUID) {
        return walletMapper.mapToDto(walletService.findById(WALLET_UUID));
    }
}
