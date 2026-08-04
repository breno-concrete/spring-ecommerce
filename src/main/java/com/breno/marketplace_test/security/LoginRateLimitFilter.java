package com.breno.marketplace_test.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private final LettuceBasedProxyManager<String> proxyManager;

    private static final int CAPACITY = 5;
    private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (isLoginRequest(request)) {
            String ip = extractClientIp(request);
            Bucket bucket = resolveBucket("rate-limit:login:" + ip);
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

            if (!probe.isConsumed()) {
                long waitSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;
                log.warn("Rate limit excedido no login para IP: {}", ip);

                response.setStatus(429);
                response.setContentType("application/json");
                response.setHeader("Retry-After", String.valueOf(waitSeconds));
                response.getWriter().write(
                        "{\"error\":\"Muitas tentativas de login. Tente novamente em " + waitSeconds + "s.\"}"
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Bucket resolveBucket(String key) {
        BucketConfiguration config = BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(CAPACITY, REFILL_PERIOD))
                .build();
        return proxyManager.builder().build(key, () -> config);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/v1/auth/login")
                && request.getMethod().equalsIgnoreCase("POST");
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim(); // primeiro IP da cadeia = cliente original
        }
        return request.getRemoteAddr();
    }
}
