package ru.bezuglov.wallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bezuglov.wallet.dto.RequestWalletDto;
import ru.bezuglov.wallet.dto.ResponseWalletDto;
import ru.bezuglov.wallet.mapper.WalletMapper;
import ru.bezuglov.wallet.service.WalletService;

import java.util.UUID;

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
     * Эндпойнт добавления нового кошелька
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWalletDto create() {
        return walletMapper.mapToDto(walletService.create());
    }

    @PostMapping("/wallet")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWalletDto changeAccount(@RequestBody RequestWalletDto requestWalletDto) {
        return walletMapper.mapToDto(walletService.changeAccount(requestWalletDto));
    }

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseWalletDto getById(@PathVariable UUID WALLET_UUID) {
        return walletMapper.mapToDto(walletService.getById(WALLET_UUID));
    }
}
