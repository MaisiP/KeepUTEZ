package mx.edu.utez.keeputez.controller;
import mx.edu.utez.keeputez.util.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> methodNotFoundException() {
        return Utils.getResponseEntity("Recurso no encontrado, revisa tu dirección y tipo de solicitud", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<?> usernameNotFoundException() {
        return Utils.getResponseEntity("Usuario del token no encontrado", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> accessDenied() {
        return Utils.getResponseEntity("Acceso denegado", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        ex.getFieldErrors().forEach(fieldError -> errorMessages.add(fieldError.getField() + ": " +
                fieldError.getDefaultMessage() + ". " +
                "Valor recibido: " + fieldError.getRejectedValue()));
        return Utils.getResponseEntityList(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> notBodyException() {
        return Utils.getResponseEntity("La solicitud no puede procesarse, posiblemente se deba a: " +
                "1.La solicitud no tiene cuerpo 2. los datos de los campos son inválidos para su tipo de dato" +
                "3. El cuerpo de la solicitud no es de tipo application/json", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<?> BadAuthenticationException() {
        return Utils.getResponseEntity("Método de solicitud no soportado", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex) {
        return Utils.getResponseEntity(ex.getCause() == null ? "El servidor no añadió detalles"
                : ex.getCause().toString().split(":", 2)[1], HttpStatus.INTERNAL_SERVER_ERROR);
    }
}