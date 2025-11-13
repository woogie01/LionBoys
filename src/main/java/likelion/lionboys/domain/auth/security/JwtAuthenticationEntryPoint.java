package likelion.lionboys.domain.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.lionboys.global.exception.error.GlobalErrorCode;
import likelion.lionboys.global.response.ApiResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * AccessToken 없이 인증 필요한 요청 접근 처리
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(GlobalErrorCode.UNAUTHORIZED.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        ApiResponse<?> body = ApiResponse.fail(GlobalErrorCode.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

}