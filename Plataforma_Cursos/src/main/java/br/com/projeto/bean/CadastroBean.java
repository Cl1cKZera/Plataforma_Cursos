package br.com.projeto.bean;

import br.com.projeto.service.AutenticacaoService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class CadastroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String senha;
    private String confirmarSenha;

    private final AutenticacaoService autenticacaoService;

    public CadastroBean() {
        this.autenticacaoService = new AutenticacaoService();
    }

    public String cadastrar() {
        // Validação da confirmação de senha
        if (senha == null || !senha.equals(confirmarSenha)) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "As senhas não conferem."));
            return null;
        }

        try {
            autenticacaoService.cadastrarUsuario(nome, email, senha);
            
            // Coloca mensagem de sucesso que sobrevive ao redirecionamento
            FacesContext.getCurrentInstance().getExternalContext().getFlash()
                .setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cadastro realizado com sucesso! Faça seu login."));

            return "login?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro no Cadastro", e.getMessage()));
            return null;
        }
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
}