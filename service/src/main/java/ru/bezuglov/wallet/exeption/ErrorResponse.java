package ru.bezuglov.wallet.exeption;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Класс ErrorResponse предоставляет дополнительную информацию о возникшем исключении.
 *
 * @author Михаил Безуглов
 */
@Data
public class ErrorResponse {

    private HttpStatus status;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
