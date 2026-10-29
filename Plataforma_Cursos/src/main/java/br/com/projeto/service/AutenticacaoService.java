package br.com.projeto.service;

import br.com.projeto.dao.UsuarioDAO;
import br.com.projeto.model.PerfilUsuario;
import br.com.projeto.model.Usuario;
import br.com.projeto.util.SenhaUtil;
import java.io.Serializable;
import java.util.Optional;

public class AutenticacaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UsuarioDAO usuarioDAO;

    public AutenticacaoService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Regra de negócio para cadastrar novo usuário comum
    public Usuario cadastrarUsuario(String nome, String email, String senhaTextoPuro) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        if (senhaTextoPuro == null || senhaTextoPuro.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
        }

        // Verifica se o e-mail já está cadastrado no banco
        Optional<Usuario> existente = usuarioDAO.buscarPorEmail(email);
        if (existente.isPresent()) {
            throw new IllegalStateException("Já existe um usuário cadastrado com este e-mail.");
        }

        // Criptografa a senha com BCrypt antes de salvar
        String hash = SenhaUtil.gerarHash(senhaTextoPuro);

        Usuario novo = new Usuario();
        novo.setNome(nome.trim());
        novo.setEmail(email.trim().toLowerCase());
        novo.setSenhaHash(hash);
        novo.setPerfil(PerfilUsuario.USER); // Todo auto-cadastro é USER comum

        return usuarioDAO.salvar(novo);
    }

    // Regra de negócio para autenticar login
    public Usuario autenticar(String email, String senhaDigitada) {
        if (email == null || email.isBlank() || senhaDigitada == null || senhaDigitada.isBlank()) {
            throw new IllegalArgumentException("E-mail e senha são obrigatórios.");
        }

        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(email);

        // Se o usuário não existir ou a senha não bater com o BCrypt, falha a autenticação
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("E-mail ou senha incorretos.");
        }

        Usuario usuario = usuarioOpt.get();
        boolean senhaValida = SenhaUtil.verificarSenha(senhaDigitada, usuario.getSenhaHash());

        if (!senhaValida) {
            throw new IllegalArgumentException("E-mail ou senha incorretos.");
        }

        return usuario;
    }
}
