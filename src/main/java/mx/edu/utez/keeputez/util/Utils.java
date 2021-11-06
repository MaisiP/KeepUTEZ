package mx.edu.utez.keeputez.util;

import mx.edu.utez.keeputez.bean.ErrorMessage;
import mx.edu.utez.keeputez.bean.ObjectSuccessMessage;
import mx.edu.utez.keeputez.bean.SuccessMessage;
import mx.edu.utez.keeputez.bean.ListSuccessMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Utils {
    public static ResponseEntity<?> getResponseEntity(String message, HttpStatus httpStatus) {
        if (httpStatus.is2xxSuccessful()) {
            return new ResponseEntity<>(new SuccessMessage(message), httpStatus);
        } else {
            return new ResponseEntity<>(new ErrorMessage(message), httpStatus);
        }
    }

    public static ResponseEntity<?> getResponseEntityList(List<?> data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ListSuccessMessage(data), httpStatus);
    }

    public static ResponseEntity<?> getResponseEntityObject(Object data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ObjectSuccessMessage(data), httpStatus);
    }
}
