package ru.bezuglov.wallet.mapper;

import org.mapstruct.Mapper;
import ru.bezuglov.wallet.dto.ResponseWalletDto;
import ru.bezuglov.wallet.model.Wallet;

/**
 * Класс WalletMapper содержит преобразование сущности.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring")
public interface WalletMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    ResponseWalletDto mapToDto(Wallet wallet);
}
