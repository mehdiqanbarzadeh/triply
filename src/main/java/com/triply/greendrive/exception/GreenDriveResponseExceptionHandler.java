package com.triply.greendrive.exception;

import com.triply.greendrive.config.infrastructure.GeneralResponse;
import com.triply.greendrive.config.infrastructure.ResponseService;
import com.triply.greendrive.config.infrastructure.Result;
import com.triply.greendrive.config.infrastructure.ResultStatus;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * This class tries to handle all probable exceptions to map
 */
@Slf4j
@ControllerAdvice
public class GreenDriveResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        logger.error("validation exception {}", ex);
        return new ResponseEntity<>(
                new GeneralResponse(invalidResult(ResultStatus.UNKNOWN, parseMessage(ex))),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseService> handleBusinessException(BusinessException ex, WebRequest request) {
        logger.error(ex.getResultStatus().getDescription(), ex);
        return ResponseEntity.unprocessableEntity().contentType(MediaType.APPLICATION_JSON)
                .body(new GeneralResponse(ex.getResultStatus()));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public final ResponseEntity<GeneralResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(new GeneralResponse(new Result(ResultStatus.INVALID_PARAMETER)));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<GeneralResponse> handleHttpClientErrorException(HttpClientErrorException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(new GeneralResponse(new Result(ResultStatus.INVALID_PARAMETER)));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<GeneralResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(new GeneralResponse(ResultStatus.USER_NOT_FOUND_EXCEPTION),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public final ResponseEntity<ResponseService> handleDuplicateKeyException(DataIntegrityViolationException ex) {
        logger.error(ResultStatus.UNKNOWN.getDescription(), ex);
        return new ResponseEntity<>(new GeneralResponse(ResultStatus.UNKNOWN),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({CompanyExistException.class, VehicleExistException.class})
    public ResponseEntity<Object> handleCompanyExistException(CompanyExistException ex) {
        GeneralResponse response = new GeneralResponse(ex.getResultStatus());
        response.getResult().setMessage(ex.getMessage());
        return ResponseEntity.unprocessableEntity().body(response);
    }


    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ResponseService> handleGeneralException(Throwable throwable, WebRequest request) {
        logger.error(ResultStatus.UNKNOWN.getDescription(), throwable);
        return new ResponseEntity<>(new GeneralResponse(invalidResult(ResultStatus.UNKNOWN,
                ResultStatus.UNKNOWN.getDescription())), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String parseMessage(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (!StringUtils.isEmpty(error.getDefaultMessage())) {
                sb.append(error.getDefaultMessage()).append("\r\n");
            }
        }
        return "sb.toString().trim()";
    }

    private Result invalidResult(ResultStatus status, String message) {
        Result result = new Result();
        result.setMessage(message);
        result.setStatus(status.getStatus());
        result.setTitle(status);
        return result;
    }

}
