package com.breno.marketplace_test.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- Método Auxiliar para DRY (Don't Repeat Yourself) ---
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String typeUri, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(typeUri));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // --- Tratadores de Exceções Customizadas ---

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest request) {
        log.warn("Tentativa de cadastro recusada. Usuário já existe: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "Conflito de Dados",
                ex.getMessage(),
                "https://api.marketplace.com/errors/user-conflict",
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        log.warn("Busca por usuário falhou. Não encontrado: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Usuário não encontrado",
                ex.getMessage(),
                "https://api.marketplace.com/errors/not-found",
                request
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        log.warn("Credenciais inválidas: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas",
                ex.getMessage(),
                "https://api.marketplace.com/errors/invalid-credentials",
                request
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {
        log.warn("Acesso negado. Token JWT inválido: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Token inválido",
                ex.getMessage(),
                "https://api.marketplace.com/errors/invalid-token",
                request
        );
    }

    // --- Tratadores de Exceções Padrão do Spring/Java ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Erro de validação nos dados enviados pelo cliente.");

        ProblemDetail problemDetail = buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação",
                "Um ou mais campos estão inválidos. Verifique a lista de erros.",
                "https://api.marketplace.com/errors/validation-error",
                request
        );

        // Gera a lista estruturada de erros para o Frontend ler facilmente
        List<Map<String, String>> camposInvalidos = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> Map.of(
                        "campo", erro.getField(),
                        "motivo", erro.getDefaultMessage() != null ? erro.getDefaultMessage() : "Erro de validação"
                ))
                .toList();

        problemDetail.setProperty("invalidParams", camposInvalidos);

        return problemDetail;
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ProblemDetail handleForbiddenAccess(ForbiddenAccessException ex, HttpServletRequest request) {
        log.warn("Acesso proibido por validação de ownership: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.FORBIDDEN,
                "Acesso proibido",
                ex.getMessage(),
                "https://api.marketplace.com/errors/forbidden",
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Acesso negado: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                "Você não tem permissão para acessar este recurso.",
                "https://api.marketplace.com/errors/access-denied",
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail httpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Erro de leitura da mensagem HTTP: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "JSON Malformado",
                "O corpo da requisição está malformado ou contém dados inválidos.",
                "https://api.marketplace.com/errors/malformed-json",
                request
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail illegalStateException(IllegalStateException ex, HttpServletRequest request) {
        log.warn("Estado inválido: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Estado inválido",
                ex.getMessage(),
                "https://api.marketplace.com/errors/invalid-state",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("ERRO INTERNO INESPERADO NO SERVIDOR: ", ex);
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro Interno",
                "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde.",
                "https://api.marketplace.com/errors/internal-error",
                request
        );
    }

    // Tratando todas as exceções de "Recurso não encontrado"
    @ExceptionHandler({
            ProductNotFoundException.class,
            OrderNotFoundException.class,
            CategoryNotFoundException.class
    })
    public ProblemDetail handleResourceNotFound(RuntimeException ex, HttpServletRequest request) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                ex.getMessage(),
                "https://api.marketplace.com/errors/not-found",
                request
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ProblemDetail handleInsufficientStock(InsufficientStockException ex, HttpServletRequest request) {
        log.warn("Regra de negócio violada: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, //422
                "Estoque Insuficiente",
                ex.getMessage(),
                "https://api.marketplace.com/errors/insufficient-stock",
                request
        );
    }
}