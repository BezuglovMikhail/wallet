package ru.bezuglov.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.bezuglov.wallet.dto.RequestWalletDto;
import ru.bezuglov.wallet.dto.ResponseWalletDto;
import ru.bezuglov.wallet.mapper.WalletMapper;
import ru.bezuglov.wallet.model.Wallet;
import ru.bezuglov.wallet.service.WalletService;
import ru.bezuglov.wallet.util.OperationType;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class)
class WalletControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WalletService walletService;

    @MockBean
    private WalletMapper walletMapper;

    private ResponseWalletDto responseWalletDto_1;

    private ResponseWalletDto responseWalletDto_2;

    private RequestWalletDto requestWalletDto_1;

    private RequestWalletDto requestWalletDto_2;

    private RequestWalletDto requestWalletDto_3;

    private Wallet wallet_1;
    private Wallet wallet_2;

    @BeforeEach
    void setUp() {
        responseWalletDto_1 = ResponseWalletDto.builder()
                .id(UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20"))
                .account(0L)
                .build();

        responseWalletDto_2 = ResponseWalletDto.builder()
                .id(UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20"))
                .account(1000L)
                .build();

        requestWalletDto_1 = RequestWalletDto.builder()
                .walletId(UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20"))
                .amount(1000L)
                .operationType(OperationType.DEPOSIT)
                .build();

        requestWalletDto_2 = RequestWalletDto.builder()
                .walletId(UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20"))
                .amount(1000L)
                .operationType(OperationType.WITHDRAW)
                .build();

        requestWalletDto_3 = RequestWalletDto.builder()
                .walletId(UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20"))
                .amount(-100L)
                .operationType(OperationType.DEPOSIT)
                .build();

        wallet_1 = Wallet.builder()
                .id(UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20"))
                .account(0L)
                .build();

        wallet_2 = Wallet.builder()
                .id(UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20"))
                .account(1000L)
                .build();
    }

    @Test
    void changeAccountRecharge() throws Exception {
        when(walletService.changeAccount(requestWalletDto_1)).thenReturn(wallet_2);
        when(walletMapper.mapToDto(wallet_2)).thenReturn(responseWalletDto_2);

        mvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(requestWalletDto_1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseWalletDto_2.getId().toString()))
                .andExpect(jsonPath("$.account", is(responseWalletDto_2.getAccount()), Long.class));

        ResponseWalletDto responseWalletDtoTest = walletMapper
                .mapToDto(walletService.changeAccount(requestWalletDto_1));

        assertEquals(responseWalletDto_2, responseWalletDtoTest);
        Mockito.verify(walletService, Mockito.times(2)).changeAccount(requestWalletDto_1);
    }

    @Test
    void changeAccountWithdraw() throws Exception {
        when(walletService.changeAccount(requestWalletDto_2)).thenReturn(wallet_1);
        when(walletMapper.mapToDto(wallet_1)).thenReturn(responseWalletDto_1);

        mvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(requestWalletDto_2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseWalletDto_1.getId().toString()))
                .andExpect(jsonPath("$.account", is(responseWalletDto_1.getAccount()), Long.class));

        ResponseWalletDto responseWalletDtoTest = walletMapper
                .mapToDto(walletService.changeAccount(requestWalletDto_2));

        assertEquals(responseWalletDto_1, responseWalletDtoTest);
        Mockito.verify(walletService, Mockito.times(2)).changeAccount(requestWalletDto_2);
    }

    @Test
    void changeAccountFalse_NegativeAmount() throws Exception {
        mvc.perform(post("/api/v1/wallet")
                        .content(mapper.writeValueAsString(requestWalletDto_3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());

        Mockito.verify(walletService, Mockito.times(0)).changeAccount(requestWalletDto_3);
    }

    @Test
    void findById() throws Exception {
        UUID walletId = UUID.fromString("744b55b0-42a7-494a-93fb-567274e14f20");

        when(walletService.findById(walletId)).thenReturn(wallet_1);
        when(walletMapper.mapToDto(wallet_1)).thenReturn(responseWalletDto_1);

        mvc.perform(get("/api/v1/wallets/{WALLET_UUID}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseWalletDto_1.getId().toString()))
                .andExpect(jsonPath("$.account").value(responseWalletDto_1.getAccount()));

        Mockito.verify(walletService, Mockito.times(1)).findById(walletId);
        Mockito.verifyNoMoreInteractions(walletService);
    }
}