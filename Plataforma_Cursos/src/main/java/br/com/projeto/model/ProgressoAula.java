package br.com.projeto.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "progresso_aulas", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_aula", columnNames = { "usuario_id", "aula_id" })
}

)
public class ProgressoAula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @Column(nullable = false)
    private boolean concluida = true;

    @Column(name = "data_conclusao", nullable = false)
    private LocalDateTime dataConclusao;

    public ProgressoAula() {
    }

    public ProgressoAula(Usuario usuario, Aula aula) {
        this.usuario = usuario;
        this.aula = aula;
        this.concluida = true;
        this.dataConclusao = LocalDateTime.now();
    }

    @PrePersist
    public void PrePersist() {
        if (this.dataConclusao == null) {
            this.dataConclusao = LocalDateTime.now();
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProgressoAula that = (ProgressoAula) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
