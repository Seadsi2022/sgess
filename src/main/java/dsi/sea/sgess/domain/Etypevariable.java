package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etypevariable.
 */
@Entity
@Table(name = "etypevariable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "etypevariable")
public class Etypevariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nomtypevar")
    private String nomtypevar;

    @Column(name = "desctypevariable")
    private String desctypevariable;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "etypevariable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "eattributvariables", "eeventualitevariables", "egroupevariables", "etypevariable", "eunite" },
        allowSetters = true
    )
    private Set<Evariable> evariables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etypevariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomtypevar() {
        return this.nomtypevar;
    }

    public Etypevariable nomtypevar(String nomtypevar) {
        this.setNomtypevar(nomtypevar);
        return this;
    }

    public void setNomtypevar(String nomtypevar) {
        this.nomtypevar = nomtypevar;
    }

    public String getDesctypevariable() {
        return this.desctypevariable;
    }

    public Etypevariable desctypevariable(String desctypevariable) {
        this.setDesctypevariable(desctypevariable);
        return this;
    }

    public void setDesctypevariable(String desctypevariable) {
        this.desctypevariable = desctypevariable;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Etypevariable isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Evariable> getEvariables() {
        return this.evariables;
    }

    public void setEvariables(Set<Evariable> evariables) {
        if (this.evariables != null) {
            this.evariables.forEach(i -> i.setEtypevariable(null));
        }
        if (evariables != null) {
            evariables.forEach(i -> i.setEtypevariable(this));
        }
        this.evariables = evariables;
    }

    public Etypevariable evariables(Set<Evariable> evariables) {
        this.setEvariables(evariables);
        return this;
    }

    public Etypevariable addEvariable(Evariable evariable) {
        this.evariables.add(evariable);
        evariable.setEtypevariable(this);
        return this;
    }

    public Etypevariable removeEvariable(Evariable evariable) {
        this.evariables.remove(evariable);
        evariable.setEtypevariable(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etypevariable)) {
            return false;
        }
        return id != null && id.equals(((Etypevariable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etypevariable{" +
            "id=" + getId() +
            ", nomtypevar='" + getNomtypevar() + "'" +
            ", desctypevariable='" + getDesctypevariable() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
