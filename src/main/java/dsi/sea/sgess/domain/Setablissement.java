package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Setablissement.
 */
@Entity
@Table(name = "setablissement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "setablissement")
public class Setablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "codeadministratif")
    private String codeadministratif;

    @JsonIgnoreProperties(value = { "evaleurvariables", "parent", "scodes", "slocalite" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Sstructure sstructure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Setablissement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeadministratif() {
        return this.codeadministratif;
    }

    public Setablissement codeadministratif(String codeadministratif) {
        this.setCodeadministratif(codeadministratif);
        return this;
    }

    public void setCodeadministratif(String codeadministratif) {
        this.codeadministratif = codeadministratif;
    }

    public Sstructure getSstructure() {
        return this.sstructure;
    }

    public void setSstructure(Sstructure sstructure) {
        this.sstructure = sstructure;
    }

    public Setablissement sstructure(Sstructure sstructure) {
        this.setSstructure(sstructure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Setablissement)) {
            return false;
        }
        return id != null && id.equals(((Setablissement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Setablissement{" +
            "id=" + getId() +
            ", codeadministratif='" + getCodeadministratif() + "'" +
            "}";
    }
}
