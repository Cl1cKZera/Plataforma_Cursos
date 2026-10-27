package br.com.projeto.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    // Cria a fábrica uma única vez para toda a aplicação (Singleton)
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("PlataformaPU");

    // Método que os DAOs chamarão para abrir uma sessão com o banco
    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    // Fecha a fábrica quando o servidor for desligado
    public static void closeFactory() {
        if (EMF != null && EMF.isOpen()) {
            EMF.close();
        }
    }
}