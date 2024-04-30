package ru.bezuglov.wallet.exeption.exeptions;

/**
 * Класс BadRequestException собственное исключение,
 * возникает при получении некорректных данных от пользователя.
 *
 * @author Михаил Безуглов
 */
public class BadRequestException extends RuntimeException  {

    /**
     * Сообщение об ошибке.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
