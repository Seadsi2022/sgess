package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Eattributvariable.
 */
@Entity
@Table(name = "eattributvariable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "eattributvariable")
public class Eattributvariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "attrname")
    private String attrname;

    @Column(name = "attrvalue")
    private String attrvalue;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "eattributvariables", "eeventualitevariables", "egroupevariables", "etypevariable", "eunite" },
        allowSetters = true
    )
    private Evariable evariable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Eattributvariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttrname() {
        return this.attrname;
    }

    public Eattributvariable attrname(String attrname) {
        this.setAttrname(attrname);
        return this;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrvalue() {
        return this.attrvalue;
    }

    public Eattributvariable attrvalue(String attrvalue) {
        this.setAttrvalue(attrvalue);
        return this;
    }

    public void setAttrvalue(String attrvalue) {
        this.attrvalue = attrvalue;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Eattributvariable isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Evariable getEvariable() {
        return this.evariable;
    }

    public void setEvariable(Evariable evariable) {
        this.evariable = evariable;
    }

    public Eattributvariable evariable(Evariable evariable) {
        this.setEvariable(evariable);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Eattributvariable)) {
            return false;
        }
        return id != null && id.equals(((Eattributvariable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Eattributvariable{" +
            "id=" + getId() +
            ", attrname='" + getAttrname() + "'" +
            ", attrvalue='" + getAttrvalue() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
