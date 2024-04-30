package ru.bezuglov.wallet.exeption.exeptions;

/**
 * Класс NotFoundException собственное исключение,
 * возникает в случае отсутствия кошелька с запрашиваемым id.
 *
 * @author Михаил Безуглов
 */
public class NotFoundException extends RuntimeException {

    /**
     * Сообщение об ошибке.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
