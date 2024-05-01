package ru.bezuglov.wallet.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.bezuglov.wallet.dto.RequestWalletDto;
import ru.bezuglov.wallet.exeption.exeptions.BadRequestException;
import ru.bezuglov.wallet.exeption.exeptions.NotFoundException;
import ru.bezuglov.wallet.model.Wallet;
import ru.bezuglov.wallet.repository.WalletRepository;
import ru.bezuglov.wallet.service.impl.WalletServiceImpl;
import ru.bezuglov.wallet.util.OperationType;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    private WalletService walletService;

    UUID walletId;
    Long _1000 = 1000L;
    Long _2000 = 2000L;
    Long _0 = 0L;

    Wallet wallet_1;
    Wallet wallet_2;

    Wallet wallet_3;

    RequestWalletDto requestWalletDto_DEPOSIT_True;

    RequestWalletDto requestWalletDto_WITHDRAW_True;

    RequestWalletDto requestWalletDto_WITHDRAW_False;

    @BeforeEach
    void setUp() {

        walletService = new WalletServiceImpl(walletRepository);

        walletId = UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20");

        wallet_1 = Wallet.builder()
                .id(walletId)
                .account(_0)
                .build();
        wallet_2 = Wallet.builder()
                .id(walletId)
                .account(_2000)
                .build();

        wallet_3 = Wallet.builder()
                .id(walletId)
                .account(_1000)
                .build();

        requestWalletDto_DEPOSIT_True = RequestWalletDto.builder()
                .walletId(walletId)
                .operationType(OperationType.DEPOSIT)
                .amount(_2000)
                .build();

        requestWalletDto_WITHDRAW_True = RequestWalletDto.builder()
                .walletId(walletId)
                .operationType(OperationType.WITHDRAW)
                .amount(_1000)
                .build();

        requestWalletDto_WITHDRAW_False = RequestWalletDto.builder()
                .walletId(walletId)
                .operationType(OperationType.WITHDRAW)
                .amount(_2000)
                .build();
    }

    @Test
    void changeAccount_DEPOSIT_True() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.ofNullable(wallet_1));
        when(walletRepository.save(wallet_1)).thenReturn(wallet_1);

        Wallet walletTest = walletService.changeAccount(requestWalletDto_DEPOSIT_True);

        assertEquals(wallet_1, walletTest);
        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
        Mockito.verify(walletRepository, Mockito.times(1)).save(wallet_1);
        Mockito.verifyNoMoreInteractions(walletRepository);
    }

    @Test
    void changeAccount_WITHDRAW_True() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.ofNullable(wallet_2));
        when(walletRepository.save(wallet_2)).thenReturn(wallet_2);

        Wallet walletTest = walletService.changeAccount(requestWalletDto_WITHDRAW_True);

        assertEquals(wallet_2, walletTest);
        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
        Mockito.verify(walletRepository, Mockito.times(1)).save(wallet_2);
        Mockito.verifyNoMoreInteractions(walletRepository);
    }

    @Test
    void changeAccount_WITHDRAW_False() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.ofNullable(wallet_3));
        BadRequestException ex = assertThrows(BadRequestException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                Wallet walletTest = walletService.changeAccount(requestWalletDto_WITHDRAW_False);
            }
        });

        Assertions.assertEquals("Сумма снятия превышает остаток средств на счете!", ex.getMessage());
        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
        Mockito.verify(walletRepository, Mockito.times(0)).save(wallet_3);
        Mockito.verifyNoMoreInteractions(walletRepository);
    }

    @Test
    void findById() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.ofNullable(wallet_2));

        Wallet walletTest = walletService.findById(walletId);

        assertEquals(wallet_2, walletTest);
        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
        Mockito.verifyNoMoreInteractions(walletRepository);
    }

    @Test
    void findById_False() {
        when(walletRepository.findById(walletId))
                .thenThrow(new NotFoundException("Кошелек с id = " + walletId + " не найден."));
        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws IOException {
                walletService.findById(walletId);
            }
        });

        Assertions.assertEquals("Кошелек с id = " + walletId + " не найден.", ex.getMessage());
        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
        Mockito.verifyNoMoreInteractions(walletRepository);
    }
}