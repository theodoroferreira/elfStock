package br.com.pb.msestoque.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class GenericException extends RuntimeException {

    private HttpStatus status;
    private String messageDTO;
}
