package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Egroupe.
 */
@Entity
@Table(name = "egroupe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "egroupe")
public class Egroupe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titregroupe")
    private String titregroupe;

    @Column(name = "ordregroupe")
    private Long ordregroupe;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "egroupe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "evaleurvariables", "suivant", "evariable", "egroupe" }, allowSetters = true)
    private Set<Egroupevariable> egroupevariables = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "egroupes", "parent", "ecampagne", "typestructure" }, allowSetters = true)
    private Equestionnaire equestionnaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Egroupe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitregroupe() {
        return this.titregroupe;
    }

    public Egroupe titregroupe(String titregroupe) {
        this.setTitregroupe(titregroupe);
        return this;
    }

    public void setTitregroupe(String titregroupe) {
        this.titregroupe = titregroupe;
    }

    public Long getOrdregroupe() {
        return this.ordregroupe;
    }

    public Egroupe ordregroupe(Long ordregroupe) {
        this.setOrdregroupe(ordregroupe);
        return this;
    }

    public void setOrdregroupe(Long ordregroupe) {
        this.ordregroupe = ordregroupe;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Egroupe isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Egroupevariable> getEgroupevariables() {
        return this.egroupevariables;
    }

    public void setEgroupevariables(Set<Egroupevariable> egroupevariables) {
        if (this.egroupevariables != null) {
            this.egroupevariables.forEach(i -> i.setEgroupe(null));
        }
        if (egroupevariables != null) {
            egroupevariables.forEach(i -> i.setEgroupe(this));
        }
        this.egroupevariables = egroupevariables;
    }

    public Egroupe egroupevariables(Set<Egroupevariable> egroupevariables) {
        this.setEgroupevariables(egroupevariables);
        return this;
    }

    public Egroupe addEgroupevariable(Egroupevariable egroupevariable) {
        this.egroupevariables.add(egroupevariable);
        egroupevariable.setEgroupe(this);
        return this;
    }

    public Egroupe removeEgroupevariable(Egroupevariable egroupevariable) {
        this.egroupevariables.remove(egroupevariable);
        egroupevariable.setEgroupe(null);
        return this;
    }

    public Equestionnaire getEquestionnaire() {
        return this.equestionnaire;
    }

    public void setEquestionnaire(Equestionnaire equestionnaire) {
        this.equestionnaire = equestionnaire;
    }

    public Egroupe equestionnaire(Equestionnaire equestionnaire) {
        this.setEquestionnaire(equestionnaire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Egroupe)) {
            return false;
        }
        return id != null && id.equals(((Egroupe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Egroupe{" +
            "id=" + getId() +
            ", titregroupe='" + getTitregroupe() + "'" +
            ", ordregroupe=" + getOrdregroupe() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
