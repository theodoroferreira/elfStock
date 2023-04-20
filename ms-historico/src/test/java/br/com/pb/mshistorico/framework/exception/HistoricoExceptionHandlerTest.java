package br.com.pb.mshistorico.framework.exception;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class HistoricoExceptionHandlerTest {

    @InjectMocks
    private HistoricoExceptionHandler exceptionHandler;

    private static final String message = "test message";

    @Test
    void handleExceptionInternal() {
        Exception ex = new Exception(message);
        WebRequest request = mock(WebRequest.class);

        HistoricoExceptionHandler handler = new HistoricoExceptionHandler();
        ResponseEntity<Object> response = handler.handleExceptionInternal(
                ex,
                null,
                null,
                HttpStatus.BAD_REQUEST,
                request
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(Collections.singletonList(message), errorResponse.getMessage());
    }

    @Test
    public void handle() {
        Exception ex1 = mock(Exception.class);
        GenericException ex2 = mock(GenericException.class);
        when(ex2.getMessageDTO()).thenReturn(message);
        when(ex2.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);

        HistoricoExceptionHandler handler = new HistoricoExceptionHandler();
        ResponseEntity<Object> response = handler.handle(ex1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(Collections.singletonList(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), errorResponse.getMessage());

        response = handler.handle(ex2);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        errorResponse = (ErrorResponse) response.getBody();
        assertEquals(Collections.singletonList(message), errorResponse.getMessage());
    }

    @Test
    public void handleDefault() {
        HistoricoExceptionHandler handler = new HistoricoExceptionHandler();
        ResponseEntity<Object> response = handler.handleDefault();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(Collections.singletonList(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), errorResponse.getMessage());
    }

    @Test
    public void handleGenericException() {
        GenericException ex = mock(GenericException.class);
        when(ex.getMessageDTO()).thenReturn(message);
        when(ex.getStatus()).thenReturn(HttpStatus.BAD_REQUEST);

        HistoricoExceptionHandler handler = new HistoricoExceptionHandler();
        ResponseEntity<Object> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(Collections.singletonList(message), errorResponse.getMessage());
    }

    @Test
    void handleMethodArgumentNotValid_withFieldError_returnsBadRequest() throws Exception {
        FieldError fieldError = new FieldError("objectName", "field", message);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldError()).thenReturn(fieldError);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        HistoricoExceptionHandler handler = new HistoricoExceptionHandler();
        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(ex, headers, status, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals(message, error.getMessage().get(0));
    }

    @Test
    void handleMethodArgumentNotValid_withoutFieldError_returnsBadRequest() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldError()).thenReturn(null);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

        HistoricoExceptionHandler handler = new HistoricoExceptionHandler();
        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(ex, headers, status, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), error.getMessage().get(0));
    }
}