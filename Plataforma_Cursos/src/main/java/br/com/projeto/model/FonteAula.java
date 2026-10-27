package br.com.projeto.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "fontes_aula")
public class FonteAula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo_referencia", nullable = false, length = 200)
    private String tituloReferencia;

    @Column(name = "url_ou_descricao", length = 300)
    private String urlOuDescricao;

    // Muitas Fontes pertencem a uma Aula (Chave Estrangeira aula_id)
    @ManyToOne(optional = false)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    public FonteAula() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTituloReferencia() {
        return tituloReferencia;
    }

    public void setTituloReferencia(String tituloReferencia) {
        this.tituloReferencia = tituloReferencia;
    }

    public String getUrlOuDescricao() {
        return urlOuDescricao;
    }

    public void setUrlOuDescricao(String urlOuDescricao) {
        this.urlOuDescricao = urlOuDescricao;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FonteAula that = (FonteAula) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}