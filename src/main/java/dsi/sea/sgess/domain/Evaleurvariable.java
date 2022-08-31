package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Evaleurvariable.
 */
@Entity
@Table(name = "evaleurvariable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "evaleurvariable")
public class Evaleurvariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valeur")
    private String valeur;

    @Column(name = "ligne")
    private Long ligne;

    @Column(name = "colonne")
    private Long colonne;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties(value = { "evaleurvariables", "suivant", "evariable", "egroupe" }, allowSetters = true)
    private Egroupevariable egroupevariable;

    @ManyToOne
    @JsonIgnoreProperties(value = { "evaleurvariables", "parent", "scodes", "slocalite" }, allowSetters = true)
    private Sstructure sstructure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evaleurvariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return this.valeur;
    }

    public Evaleurvariable valeur(String valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Long getLigne() {
        return this.ligne;
    }

    public Evaleurvariable ligne(Long ligne) {
        this.setLigne(ligne);
        return this;
    }

    public void setLigne(Long ligne) {
        this.ligne = ligne;
    }

    public Long getColonne() {
        return this.colonne;
    }

    public Evaleurvariable colonne(Long colonne) {
        this.setColonne(colonne);
        return this;
    }

    public void setColonne(Long colonne) {
        this.colonne = colonne;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Evaleurvariable isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Egroupevariable getEgroupevariable() {
        return this.egroupevariable;
    }

    public void setEgroupevariable(Egroupevariable egroupevariable) {
        this.egroupevariable = egroupevariable;
    }

    public Evaleurvariable egroupevariable(Egroupevariable egroupevariable) {
        this.setEgroupevariable(egroupevariable);
        return this;
    }

    public Sstructure getSstructure() {
        return this.sstructure;
    }

    public void setSstructure(Sstructure sstructure) {
        this.sstructure = sstructure;
    }

    public Evaleurvariable sstructure(Sstructure sstructure) {
        this.setSstructure(sstructure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evaleurvariable)) {
            return false;
        }
        return id != null && id.equals(((Evaleurvariable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evaleurvariable{" +
            "id=" + getId() +
            ", valeur='" + getValeur() + "'" +
            ", ligne=" + getLigne() +
            ", colonne=" + getColonne() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
