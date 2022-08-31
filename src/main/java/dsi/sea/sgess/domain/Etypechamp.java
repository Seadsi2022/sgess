package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etypechamp.
 */
@Entity
@Table(name = "etypechamp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "etypechamp")
public class Etypechamp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "displayname")
    private String displayname;

    @ManyToMany
    @JoinTable(
        name = "rel_etypechamp__eattribut",
        joinColumns = @JoinColumn(name = "etypechamp_id"),
        inverseJoinColumns = @JoinColumn(name = "eattribut_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaleurattribut", "etypechamps" }, allowSetters = true)
    private Set<Eattribut> eattributs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etypechamp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Etypechamp name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public Etypechamp displayname(String displayname) {
        this.setDisplayname(displayname);
        return this;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Set<Eattribut> getEattributs() {
        return this.eattributs;
    }

    public void setEattributs(Set<Eattribut> eattributs) {
        this.eattributs = eattributs;
    }

    public Etypechamp eattributs(Set<Eattribut> eattributs) {
        this.setEattributs(eattributs);
        return this;
    }

    public Etypechamp addEattribut(Eattribut eattribut) {
        this.eattributs.add(eattribut);
        eattribut.getEtypechamps().add(this);
        return this;
    }

    public Etypechamp removeEattribut(Eattribut eattribut) {
        this.eattributs.remove(eattribut);
        eattribut.getEtypechamps().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etypechamp)) {
            return false;
        }
        return id != null && id.equals(((Etypechamp) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etypechamp{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", displayname='" + getDisplayname() + "'" +
            "}";
    }
}
