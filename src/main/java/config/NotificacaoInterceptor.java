package config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import service.NotificacaoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class NotificacaoInterceptor implements HandlerInterceptor {
    private final NotificacaoService notificacaoService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null && !request.getRequestURI().startsWith("/api/")) {
            // Adiciona as notificações apenas para páginas que não são APIs
            modelAndView.addObject("notificacoes", notificacaoService.listarNaoLidas());
        }
    }
} 