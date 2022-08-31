package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Eeventualitevariable.
 */
@Entity
@Table(name = "eeventualitevariable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "eeventualitevariable")
public class Eeventualitevariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "eattributvariables", "eeventualitevariables", "egroupevariables", "etypevariable", "eunite" },
        allowSetters = true
    )
    private Evariable evariable;

    @ManyToOne
    @JsonIgnoreProperties(value = { "eeventualitevariables" }, allowSetters = true)
    private Eeventualite eeventualite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Eeventualitevariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evariable getEvariable() {
        return this.evariable;
    }

    public void setEvariable(Evariable evariable) {
        this.evariable = evariable;
    }

    public Eeventualitevariable evariable(Evariable evariable) {
        this.setEvariable(evariable);
        return this;
    }

    public Eeventualite getEeventualite() {
        return this.eeventualite;
    }

    public void setEeventualite(Eeventualite eeventualite) {
        this.eeventualite = eeventualite;
    }

    public Eeventualitevariable eeventualite(Eeventualite eeventualite) {
        this.setEeventualite(eeventualite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Eeventualitevariable)) {
            return false;
        }
        return id != null && id.equals(((Eeventualitevariable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Eeventualitevariable{" +
            "id=" + getId() +
            "}";
    }
}
