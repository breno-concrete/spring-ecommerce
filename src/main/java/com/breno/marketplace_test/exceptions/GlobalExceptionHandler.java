package com.breno.marketplace_test.exceptions;

import com.breno.marketplace_test.dtos.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExists(
            UserAlreadyExistsException ex
    ) {

        log.warn("Tentativa de cadastro recusada. Usuário já existe: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDTO(
                        "CONFLICT",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(
            UserNotFoundException ex
    ) {
        log.warn("Busca por usuário falhou. Não encontrado: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(
                        "NOT_FOUND",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(
            InvalidCredentialsException ex
    ) {

        log.warn("Credenciais inválidas: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        "UNAUTHORIZED",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidToken(
            InvalidTokenException ex
    ) {
        log.warn("Acesso negado. Token JWT inválido: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        "UNAUTHORIZED",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Erro de validação nos dados enviados pelo cliente: {}", message);


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(
                        "VALIDATION_ERROR",
                        message,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> accessDeniedException(
            AccessDeniedException ex
    ) {
        log.warn("Acesso negado: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDTO(
                        "FORBIDDEN",
                        "Você não tem permissão para acessar este recurso.",
                        LocalDateTime.now()
                ));
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> illegalStateException(
            IllegalStateException ex){
        ErrorResponseDTO error = new ErrorResponseDTO(
                "Invalid State",
                ex.getMessage(),
                LocalDateTime.now()

        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex
    ) {
        log.error("ERRO INTERNO INESPERADO NO SERVIDOR: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(
                        "INTERNAL_SERVER_ERROR",
                        // Boa prática: NUNCA retorne o ex.getMessage() de um erro genérico pro usuário.
                        // Pode vazar dados do banco ou detalhes da infraestrutura. Devolva uma mensagem fixa.
                        "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde.",
                        LocalDateTime.now()
                ));
    }
}