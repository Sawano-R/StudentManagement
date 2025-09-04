package raisetech.StudentManagement.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleNotValidException(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .orElse("バリデーションエラーが発生しました");
    return ResponseEntity.badRequest().body(message);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> handleTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    String message = String.format("'%s'の値'%s'は'%s'の型で入力し直してください。",
        ex.getName(),
        ex.getValue(),
        ex.getRequiredType());
    return ResponseEntity.badRequest().body(message);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    String message = "";
    if (ex.getCause() instanceof InvalidFormatException) {
      InvalidFormatException cause = (InvalidFormatException) ex.getCause();
      message = String.format("'%s'は'%s'に入力できません。'%s'の型で入力してください。",
          cause.getValue().toString(),
          cause.getPath().getLast().getFieldName(),
          cause.getTargetType().getSimpleName());
    }
    return ResponseEntity.badRequest().body(message);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
    return ResponseEntity.badRequest().body("Invalid request: " + ex.getMessage());
  }
}
