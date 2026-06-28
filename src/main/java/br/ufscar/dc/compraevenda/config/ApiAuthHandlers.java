package br.ufscar.dc.compraevenda.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ApiAuthHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {

    private boolean isApi(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri != null && uri.startsWith("/api");
    }

    @Override
    public void commence(HttpServletRequest request,
                           HttpServletResponse response,
                           org.springframework.security.core.AuthenticationException authException) throws IOException {
        if (!isApi(request)) {
            response.sendRedirect("/login?error=true");
            return;
        }
        writeJson(response, HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized: autenticação necessária ou expirada");
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!isApi(request)) {
            response.sendRedirect("/login?error=true");
            return;
        }
        writeJson(response, HttpServletResponse.SC_FORBIDDEN,
                "Forbidden: usuário não possui permissão (role) necessária");
    }

    private void writeJson(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");

        String json = "{\"status\":" + status + ",\"error\":\"" + escape(message) + "\"}";
        response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

