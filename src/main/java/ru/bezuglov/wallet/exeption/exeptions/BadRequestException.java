package ru.bezuglov.wallet.exeption.exeptions;

public class BadRequestException extends RuntimeException  {
    public BadRequestException(String message) {
        super(message);
    }
}
