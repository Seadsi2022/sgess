package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Eattribut.
 */
@Entity
@Table(name = "eattribut")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "eattribut")
public class Eattribut implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "attrname")
    private String attrname;

    @Column(name = "attrdisplayname")
    private String attrdisplayname;

    @ManyToOne
    @JsonIgnoreProperties(value = { "eattributs" }, allowSetters = true)
    private Evaleurattribut evaleurattribut;

    @ManyToMany(mappedBy = "eattributs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "eattributs" }, allowSetters = true)
    private Set<Etypechamp> etypechamps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Eattribut id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttrname() {
        return this.attrname;
    }

    public Eattribut attrname(String attrname) {
        this.setAttrname(attrname);
        return this;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrdisplayname() {
        return this.attrdisplayname;
    }

    public Eattribut attrdisplayname(String attrdisplayname) {
        this.setAttrdisplayname(attrdisplayname);
        return this;
    }

    public void setAttrdisplayname(String attrdisplayname) {
        this.attrdisplayname = attrdisplayname;
    }

    public Evaleurattribut getEvaleurattribut() {
        return this.evaleurattribut;
    }

    public void setEvaleurattribut(Evaleurattribut evaleurattribut) {
        this.evaleurattribut = evaleurattribut;
    }

    public Eattribut evaleurattribut(Evaleurattribut evaleurattribut) {
        this.setEvaleurattribut(evaleurattribut);
        return this;
    }

    public Set<Etypechamp> getEtypechamps() {
        return this.etypechamps;
    }

    public void setEtypechamps(Set<Etypechamp> etypechamps) {
        if (this.etypechamps != null) {
            this.etypechamps.forEach(i -> i.removeEattribut(this));
        }
        if (etypechamps != null) {
            etypechamps.forEach(i -> i.addEattribut(this));
        }
        this.etypechamps = etypechamps;
    }

    public Eattribut etypechamps(Set<Etypechamp> etypechamps) {
        this.setEtypechamps(etypechamps);
        return this;
    }

    public Eattribut addEtypechamp(Etypechamp etypechamp) {
        this.etypechamps.add(etypechamp);
        etypechamp.getEattributs().add(this);
        return this;
    }

    public Eattribut removeEtypechamp(Etypechamp etypechamp) {
        this.etypechamps.remove(etypechamp);
        etypechamp.getEattributs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Eattribut)) {
            return false;
        }
        return id != null && id.equals(((Eattribut) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Eattribut{" +
            "id=" + getId() +
            ", attrname='" + getAttrname() + "'" +
            ", attrdisplayname='" + getAttrdisplayname() + "'" +
            "}";
    }
}
