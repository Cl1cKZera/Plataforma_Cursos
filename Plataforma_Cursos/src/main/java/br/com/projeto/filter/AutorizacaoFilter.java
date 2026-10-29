package br.com.projeto.filter;

import br.com.projeto.bean.LoginBean;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AutorizacaoFilter", urlPatterns = {"*.xhtml"})
public class AutorizacaoFilter implements Filter {

    @Inject 
    private LoginBean loginBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        // 1. Permite acesso a recursos estáticos do PrimeFaces (JS, CSS e imagens)
        boolean isRecursoEstatico = uri.contains("/jakarta.faces.resource/");

        // 2. Permite acesso às páginas públicas de autenticação
        boolean isPaginaPublica = uri.endsWith("/login.xhtml") ||
                                  uri.endsWith("/cadastro.xhtml") ||
                                  uri.equals(contextPath + "/") ||
                                  uri.equals(contextPath);

        // 3. Verifica se o usuário possui sessão ativa via CDI
        boolean isUsuarioAutenticado = (loginBean != null && loginBean.isLogado());

        if (isRecursoEstatico || isPaginaPublica || isUsuarioAutenticado) {
            // Adiciona cabeçalhos para evitar cache de páginas autenticadas no navegador
            if (isUsuarioAutenticado) {
                resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                resp.setHeader("Pragma", "no-cache");
                resp.setDateHeader("Expires", 0);
            }
            chain.doFilter(request, response);
        } else {
            // Bloqueia e redireciona para a tela de login
            resp.sendRedirect(contextPath + "/login.xhtml");
        }
    }
}
