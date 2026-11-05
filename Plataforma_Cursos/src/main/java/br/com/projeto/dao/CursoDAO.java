package br.com.projeto.dao;

import br.com.projeto.model.Curso;
import br.com.projeto.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CursoDAO {

    // Salva novo curso ou atualiza curso existente
    public Curso salvarOuAtualizar(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Curso salvo;
            if (curso.getId() == null) {
                em.persist(curso);
                salvo = curso;
            } else {
                salvo = em.merge(curso);
            }
            em.getTransaction().commit();
            return salvo;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao persistir curso no banco de dados.", e);
        } finally {
            em.close();
        }
    }

    public Optional<Curso> buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Curso curso = em.find(Curso.class, id);
            return Optional.ofNullable(curso);
        } finally {
            em.close();
        }
    }

    // Listar todos os cursos para o painel adm
    public List<Curso> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c ORDER BY c.dataCriacao DESC", Curso.class);
            return query.getResultList();
        } finally {
            em.close();
        }
        
    }

        // listar apenas cursos publicados para a visao alunos
        public List<Curso> listarPublicados() {
            EntityManager em = JPAUtil.getEntityManager();
            try {
                TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c WHERE c.publicado = true ORDER BY c.dataCriacao DESC", Curso.class);
                return query.getResultList();
            } finally {
                em.close();
            }
        }
    }