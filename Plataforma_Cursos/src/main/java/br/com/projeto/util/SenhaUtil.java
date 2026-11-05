package br.com.projeto.util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {
    
    // Gera o hash criptografado com Salt para salvar no banco
    public static String gerarHash(String senhatextoPuro) {
        if (senhatextoPuro == null || senhatextoPuro.isBlank()) {
        throw new IllegalArgumentException("A senha não pode ser vazia.");
    }
    return BCrypt.hashpw(senhatextoPuro, BCrypt.gensalt(12));
}

// Valida se a senha digitada confere com o hash gravado no banco
    public static boolean verificarSenha(String senhaTextoPuro, String hashBanco) {
        if (senhaTextoPuro == null || hashBanco == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(senhaTextoPuro, hashBanco);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}