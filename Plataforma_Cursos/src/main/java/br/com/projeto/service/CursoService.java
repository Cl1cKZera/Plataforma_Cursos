package br.com.projeto.service;

import br.com.projeto.dao.CursoDAO;
import br.com.projeto.model.Curso;
import java.io.Serializable;
import java.util.List;

public class CursoService implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CursoDAO cursoDAO;

    public CursoService() {
        this.cursoDAO = new CursoDAO();
    }

    // Salva ou atualiza um curso com validação de regras de negocio
    public Curso salvar(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("O curso não pode ser nulo.");
        }
        if (curso.getTitulo() == null || curso.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título do curso é obrigatório.");
        }

        // Garante que o texto fique sem espaços extras
        curso.setTitulo(curso.getTitulo().trim());
        if (curso.getDescricao() != null) {
            curso.setDescricao(curso.getDescricao().trim());
        }
        return cursoDAO.salvarOuAtualizar(curso);
    }

    // Alterna o status entre Publicado e Rascunho
    public Curso alternarPublicacao(Long id) {
        Curso curso = buscarPorId(id);
        curso.setPublicado(!curso.isPublicado());
        return cursoDAO.salvarOuAtualizar(curso);
    }

    public Curso buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do curso não informado.");
        }
        return cursoDAO.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado para o ID: " + id));
    }

    // Visão do Administrador (todos os cursos)
    public List<Curso> listarTodosParaAdmin() {
        return cursoDAO.listarTodos();
    }

    // Visão do Aluno (apenas cursos publicados)
    public List<Curso> listarPublicadosParaAluno() {
        return cursoDAO.listarPublicados();
    }
}