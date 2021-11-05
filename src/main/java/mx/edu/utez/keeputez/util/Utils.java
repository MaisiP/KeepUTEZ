package mx.edu.utez.keeputez.util;

import mx.edu.utez.keeputez.bean.ErrorMessage;
import mx.edu.utez.keeputez.bean.SuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {
    public static ResponseEntity<?> getResponseEntity(String description, HttpStatus httpStatus) {
        if (httpStatus.is2xxSuccessful()) {
            return new ResponseEntity<>(new SuccessMessage(description), httpStatus);
        } else {
            return new ResponseEntity<>(new ErrorMessage(description), httpStatus);
        }
    }
}
