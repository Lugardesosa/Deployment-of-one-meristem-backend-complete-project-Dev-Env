package org.meristem.oneapp.wealthservice.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.meristem.oneapp.wealthservice.constants.ErrorMessages;
import org.meristem.oneapp.wealthservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.wealthservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.wealthservice.exception.exceptions.UpstreamServiceException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static org.flywaydb.core.internal.util.StringUtils.hasText;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice implements MessageSourceAware {

    @Nullable
    private MessageSource messageSource;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return handleExceptionInternal(ex.getMessage(), HttpStatus.BAD_REQUEST, request, List.of());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        String error = !hasText(ex.getResourcePassed()) || !hasText(ex.getResourceName()) ? "The resource requested was not found" : (ex.getResourcePassed() + " with '" + ex.getResourceName() + "' not found");
        return handleExceptionInternal(ex.getMessage(), HttpStatus.NOT_FOUND, request, List.of(error));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorDetails> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return handleExceptionInternal(ErrorMessages.METHOD_NOT_SUPPORTED, HttpStatus.METHOD_NOT_ALLOWED, request, List.of(ex.getMethod() + " is not allowed", "Supported methods are: " + Arrays.toString(ex.getSupportedMethods())));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorDetails> handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        return handleExceptionInternal(ErrorMessages.NO_RESOURCE_FOUND, HttpStatus.NOT_FOUND, request, List.of(ex.getResourcePath() + " is not found"));
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    protected ResponseEntity<ErrorDetails> handlerMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        return handleExceptionInternal(ErrorMessages.MEDIA_TYPES_NOT_SUPPORTED, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request, List.of(ex.getContentType() + " is not supported",
                "Supported MediaTypes: " + ex.getSupportedMediaTypes()));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity<ErrorDetails> handleMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex,  HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ErrorMessages.MEDIA_TYPES_NOT_ACCEPTABLE, status, request, List.of());

    }

    @ExceptionHandler(MissingPathVariableException.class)
    protected ResponseEntity<ErrorDetails> handleMissingPathVariableException(MissingPathVariableException ex,  HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ErrorMessages.MISSING_PATH_VARIABLE, status, request, List.of(ex.getVariableName() + "is missing"));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<ErrorDetails> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request) {
        return handleExceptionInternal(ErrorMessages.MAX_UPLOAD_SIZE_EXCEEDED, HttpStatus.BAD_REQUEST, request, List.of("Max upload size " + ex.getMaxUploadSize() + ", exceeded"));
    }

    @ExceptionHandler(TypeMismatchException.class)
    protected ResponseEntity<ErrorDetails> handleTypeMismatch(TypeMismatchException ex, WebRequest request) {

        Object[] args = {ex.getPropertyName(), ex.getValue()};
        String defaultDetail = "Failed to convert '" + args[0] + "' with value: '" + args[1] + "'";
        String messageCode = ErrorResponse.getDefaultDetailMessageCode(TypeMismatchException.class, null);
        ProblemDetail body = createProblemDetail(ex, defaultDetail, messageCode, args, request);
        return handleExceptionInternal("Type Mismatch", HttpStatus.BAD_REQUEST, request, List.of(defaultDetail, messageCode, body.toString()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return handleExceptionInternal("Method Argument Not Valid", HttpStatus.BAD_REQUEST, request, errors);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorDetails> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, WebRequest request) {
        List<String> errors = List.of(ex.getParameterName() + " parameter is missing");
        return handleExceptionInternal("Missing Servlet Request Parameter", HttpStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorDetails> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        return handleExceptionInternal("Entered wrong values", HttpStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorDetails> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {

        List<String> errors = List.of(ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName());
        return handleExceptionInternal("Entered the wrong type", HttpStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorDetails> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        return handleExceptionInternal("Invalid request", HttpStatus.BAD_REQUEST, request, List.of("There is error in the request body"));
    }

    @ExceptionHandler(UpstreamServiceException.class)
    protected ResponseEntity<ErrorDetails> handleUpstreamServiceException(UpstreamServiceException ex, WebRequest request) {
        return handleExceptionInternal("Bad gateway", HttpStatus.SERVICE_UNAVAILABLE, request, List.of("Request could not be processed"));
    }

    @ExceptionHandler({AuthorizationDeniedException.class})
    protected ResponseEntity<ErrorDetails> handleAuthorizationDeniedException(AuthorizationDeniedException ex, WebRequest request) {
        return handleExceptionInternal("Unauthorized request", HttpStatus.UNAUTHORIZED, request, List.of("Your are not authorized to make this call"));
    }

    @ExceptionHandler({OAuth2AuthorizationException.class})
    protected ResponseEntity<ErrorDetails> handleOAuth2AuthorizationException(OAuth2AuthorizationException ex, WebRequest request) {
        return handleExceptionInternal("Unauthorized request", HttpStatus.UNAUTHORIZED, request, List.of("Your are not authorized to make this call"));
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return handleExceptionInternal("Unauthorized request", HttpStatus.FORBIDDEN, request, List.of(ex.getMessage()));
    }

    @ExceptionHandler({HandlerMethodValidationException.class})
    protected ResponseEntity<ProblemDetail> handleHandlerMethodValidationException(HandlerMethodValidationException ex, WebRequest request) {
        Map<String, Object> properties = new HashMap<>();
        for (int i = 0; i < ex.getParameterValidationResults().size(); i++) {
            ParameterValidationResult parameterValidationResult = ex.getParameterValidationResults().get(i);
            properties.put(parameterValidationResult.getMethodParameter().getParameter().getName(), String.format("%s is invalid. %s", parameterValidationResult.getArgument(), ex.getDetailMessageArguments()[i]));
        }

        return createProblemDetail(ex, HttpStatus.BAD_REQUEST, properties);
    }

    @ExceptionHandler(ResourceAccessException.class)
    protected ResponseEntity<ErrorDetails> handleResourceAccessException(ResourceAccessException ex, WebRequest request) {
        return handleExceptionInternal("Connection could not be completed", HttpStatus.SERVICE_UNAVAILABLE, request, List.of());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {

        log.error("Unhandled exception in request [{}]: {}", request.getDescription(false), ex.getMessage(), ex);
        String errorMessage = """
                An error occurred while processing the request:
                Kindly send a mail to help@one-meristem-app.com.
                """;
        return handleExceptionInternal(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, request, List.of());
    }

    private ResponseEntity<ErrorDetails> handleExceptionInternal(String ex, HttpStatus status, WebRequest request, List<String> errors) {
        ErrorDetails apiError =
                new ErrorDetails(LocalDateTime.now(), ex + "...", request.getDescription(false), errors);
        return new ResponseEntity<>(apiError, status);
    }

    private ResponseEntity<ProblemDetail> createProblemDetail(Exception ex, HttpStatus status, Map<String, Object> properties) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        detail.setProperties(properties);
        return new ResponseEntity<>(detail, status);
    }

    private ProblemDetail createProblemDetail(
            Exception ex, String defaultDetail, @Nullable String detailMessageCode,
            Object[] detailMessageArguments, WebRequest request) {

        ErrorResponse.Builder builder = ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, defaultDetail);
        if (detailMessageCode != null) {
            builder.detailMessageCode(detailMessageCode);
        }
        if (detailMessageArguments != null) {
            builder.detailMessageArguments(detailMessageArguments);
        }
        return builder.build().updateAndGetBody(this.messageSource, LocaleContextHolder.getLocale());
    }

    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
