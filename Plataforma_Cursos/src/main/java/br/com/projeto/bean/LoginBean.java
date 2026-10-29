package br.com.projeto.bean;

import br.com.projeto.model.PerfilUsuario;
import br.com.projeto.model.Usuario;
import br.com.projeto.service.AutenticacaoService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named 
@SessionScoped 
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;
    private Usuario usuarioLogado;

    private final AutenticacaoService autenticacaoService;

    public LoginBean() {
        this.autenticacaoService = new AutenticacaoService();
    }

    // Ação acionada pelo botao de login
    public String login() {
        try {
            this.usuarioLogado = autenticacaoService.autenticar(email, senha);

            //limpa o campo de senha 
            this.senha = null;

            // redireciona para a pagina inicial com sessao ativa
            return "index?faces-redirect=true";
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro no Login", e.getMessage()));
            return null; // permanece na mesa tela com o erro
        }
    }

    // acao de logout: invalida a sessao HTTP
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.usuarioLogado = null;
        return "/login?faces-redirect=true";
    }

    // verifica se tem algum usuario logado na sessao atual
    public boolean isLogado() {
        return usuarioLogado != null;
    }

    // verifica se o usuario logado é adm
    public boolean isAdmin() {
        return isLogado() && usuarioLogado.getPerfil() == PerfilUsuario.ADMIN;
    }

        // Getters e Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
}
