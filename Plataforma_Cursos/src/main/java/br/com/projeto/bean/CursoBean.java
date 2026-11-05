package br.com.projeto.bean;

import br.com.projeto.model.Curso;
import br.com.projeto.service.CursoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CursoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginBean loginBean;

    private final CursoService cursoService;

    private List<Curso> cursos;
    private Curso cursoSelecionado;

    public CursoBean() {
        this.cursoService = new CursoService();
    }

    @PostConstruct
    public void init() {
        carregarCursos();
    }

    // Carrega a listagem adequada dependendo se é Administrador ou Aluno
    public void carregarCursos() {
        if (loginBean != null && loginBean.isAdmin()) {
            this.cursos = cursoService.listarTodosParaAdmin();
        } else {
            this.cursos = cursoService.listarPublicadosParaAluno();
        }
    }

    // Prepara um novo curso para preenchimento no diálogo
    public void novoCurso() {
        this.cursoSelecionado = new Curso();
    }

    // Prepara um curso existente para alteração
    public void prepararEdicao(Curso curso) {
        this.cursoSelecionado = curso;
    }

    // Salva ou atualiza o curso
    public void salvar() {
        try {
            cursoService.salvar(this.cursoSelecionado);
            carregarCursos();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Curso salvo com sucesso!"));

            // Fecha o diálogo do PrimeFaces
            PrimeFaces.current().executeScript("PF('dialogCurso').hide();");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    // Ação de Publicar / Despublicar
    public void alternarPublicacao(Curso curso) {
        try {
            Curso atualizado = cursoService.alternarPublicacao(curso.getId());
            carregarCursos();

            String status = atualizado.isPublicado() ? "publicado" : "despublicado";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Status Atualizado",
                            "Curso " + status + " com sucesso!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage()));
        }
    }

    // Getters e Setters
    public List<Curso> getCursos() {
        return cursos;
    }

    public Curso getCursoSelecionado() {
        return cursoSelecionado;
    }

    public void setCursoSelecionado(Curso cursoSelecionado) {
        this.cursoSelecionado = cursoSelecionado;
    }
}

