package app;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import validation.ValidationErrors;

import java.util.List;

@RestControllerAdvice
public class ControllerErrorHandler {


    @ExceptionHandler
    public String handleErrors(Exception e) {
        return "internal error!";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrors handleValidationError(
            MethodArgumentNotValidException exception) {

        List<FieldError> errors = exception.getBindingResult().getFieldErrors();

        ValidationErrors result = new ValidationErrors();

        for (FieldError error : errors) {
            result.addFieldError(error);
        }

        return result;
    }

}
