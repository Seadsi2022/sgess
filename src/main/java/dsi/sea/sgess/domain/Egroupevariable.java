package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Egroupevariable.
 */
@Entity
@Table(name = "egroupevariable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "egroupevariable")
public class Egroupevariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ordrevariable")
    private Long ordrevariable;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "egroupevariable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "egroupevariable", "sstructure" }, allowSetters = true)
    private Set<Evaleurvariable> evaleurvariables = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "evaleurvariables", "suivant", "evariable", "egroupe" }, allowSetters = true)
    private Egroupevariable suivant;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "eattributvariables", "eeventualitevariables", "egroupevariables", "etypevariable", "eunite" },
        allowSetters = true
    )
    private Evariable evariable;

    @ManyToOne
    @JsonIgnoreProperties(value = { "egroupevariables", "equestionnaire" }, allowSetters = true)
    private Egroupe egroupe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Egroupevariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrdrevariable() {
        return this.ordrevariable;
    }

    public Egroupevariable ordrevariable(Long ordrevariable) {
        this.setOrdrevariable(ordrevariable);
        return this;
    }

    public void setOrdrevariable(Long ordrevariable) {
        this.ordrevariable = ordrevariable;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Egroupevariable isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Evaleurvariable> getEvaleurvariables() {
        return this.evaleurvariables;
    }

    public void setEvaleurvariables(Set<Evaleurvariable> evaleurvariables) {
        if (this.evaleurvariables != null) {
            this.evaleurvariables.forEach(i -> i.setEgroupevariable(null));
        }
        if (evaleurvariables != null) {
            evaleurvariables.forEach(i -> i.setEgroupevariable(this));
        }
        this.evaleurvariables = evaleurvariables;
    }

    public Egroupevariable evaleurvariables(Set<Evaleurvariable> evaleurvariables) {
        this.setEvaleurvariables(evaleurvariables);
        return this;
    }

    public Egroupevariable addEvaleurvariable(Evaleurvariable evaleurvariable) {
        this.evaleurvariables.add(evaleurvariable);
        evaleurvariable.setEgroupevariable(this);
        return this;
    }

    public Egroupevariable removeEvaleurvariable(Evaleurvariable evaleurvariable) {
        this.evaleurvariables.remove(evaleurvariable);
        evaleurvariable.setEgroupevariable(null);
        return this;
    }

    public Egroupevariable getSuivant() {
        return this.suivant;
    }

    public void setSuivant(Egroupevariable egroupevariable) {
        this.suivant = egroupevariable;
    }

    public Egroupevariable suivant(Egroupevariable egroupevariable) {
        this.setSuivant(egroupevariable);
        return this;
    }

    public Evariable getEvariable() {
        return this.evariable;
    }

    public void setEvariable(Evariable evariable) {
        this.evariable = evariable;
    }

    public Egroupevariable evariable(Evariable evariable) {
        this.setEvariable(evariable);
        return this;
    }

    public Egroupe getEgroupe() {
        return this.egroupe;
    }

    public void setEgroupe(Egroupe egroupe) {
        this.egroupe = egroupe;
    }

    public Egroupevariable egroupe(Egroupe egroupe) {
        this.setEgroupe(egroupe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Egroupevariable)) {
            return false;
        }
        return id != null && id.equals(((Egroupevariable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Egroupevariable{" +
            "id=" + getId() +
            ", ordrevariable=" + getOrdrevariable() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
