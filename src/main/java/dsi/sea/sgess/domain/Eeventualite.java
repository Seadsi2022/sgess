package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Eeventualite.
 */
@Entity
@Table(name = "eeventualite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "eeventualite")
public class Eeventualite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "eventualitevalue")
    private String eventualitevalue;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "eeventualite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "evariable", "eeventualite" }, allowSetters = true)
    private Set<Eeventualitevariable> eeventualitevariables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Eeventualite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventualitevalue() {
        return this.eventualitevalue;
    }

    public Eeventualite eventualitevalue(String eventualitevalue) {
        this.setEventualitevalue(eventualitevalue);
        return this;
    }

    public void setEventualitevalue(String eventualitevalue) {
        this.eventualitevalue = eventualitevalue;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Eeventualite isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Eeventualitevariable> getEeventualitevariables() {
        return this.eeventualitevariables;
    }

    public void setEeventualitevariables(Set<Eeventualitevariable> eeventualitevariables) {
        if (this.eeventualitevariables != null) {
            this.eeventualitevariables.forEach(i -> i.setEeventualite(null));
        }
        if (eeventualitevariables != null) {
            eeventualitevariables.forEach(i -> i.setEeventualite(this));
        }
        this.eeventualitevariables = eeventualitevariables;
    }

    public Eeventualite eeventualitevariables(Set<Eeventualitevariable> eeventualitevariables) {
        this.setEeventualitevariables(eeventualitevariables);
        return this;
    }

    public Eeventualite addEeventualitevariable(Eeventualitevariable eeventualitevariable) {
        this.eeventualitevariables.add(eeventualitevariable);
        eeventualitevariable.setEeventualite(this);
        return this;
    }

    public Eeventualite removeEeventualitevariable(Eeventualitevariable eeventualitevariable) {
        this.eeventualitevariables.remove(eeventualitevariable);
        eeventualitevariable.setEeventualite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Eeventualite)) {
            return false;
        }
        return id != null && id.equals(((Eeventualite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Eeventualite{" +
            "id=" + getId() +
            ", eventualitevalue='" + getEventualitevalue() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
