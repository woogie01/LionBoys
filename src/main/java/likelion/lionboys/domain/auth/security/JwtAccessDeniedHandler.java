package likelion.lionboys.domain.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.lionboys.global.exception.error.GlobalErrorCode;
import likelion.lionboys.global.response.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 권한 없는 사용자 접근 처리
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(GlobalErrorCode.FORBIDDEN.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        ApiResponse<?> body = ApiResponse.fail(GlobalErrorCode.FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

}