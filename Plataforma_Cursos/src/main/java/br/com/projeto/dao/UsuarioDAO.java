package br.com.projeto.dao;

import br.com.projeto.model.Usuario;
import br.com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class UsuarioDAO {

    // Salva um novo usuário no banco com controle de transação
    public Usuario salvar(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            return usuario;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar usuário no banco de dados.", e);
        } finally {
            em.close();
        }
    }

    // Busca usuário pelo email (usado no login e na validação de Cadastro)
    public Optional<Usuario> buscarPorEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)",
                    Usuario.class);
            query.setParameter("email", email.trim());
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty(); // E-mail não encontrado no banco
        } finally {
            em.close();
        }
    }

    // Busca por ID primario
    public Optional<Usuario> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        } finally {
            em.close();
        }
    }
}
