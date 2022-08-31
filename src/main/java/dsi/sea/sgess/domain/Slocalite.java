package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Slocalite.
 */
@Entity
@Table(name = "slocalite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "slocalite")
public class Slocalite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "codelocalite")
    private Long codelocalite;

    @Column(name = "nomlocalite")
    private String nomlocalite;

    @OneToMany(mappedBy = "slocalite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "evaleurvariables", "parent", "scodes", "slocalite" }, allowSetters = true)
    private Set<Sstructure> sstructures = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sstructures", "parent", "natureLocalite", "typeLocalite" }, allowSetters = true)
    private Slocalite parent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "slocalites", "equestionnaires", "parent", "scode", "sstructures" }, allowSetters = true)
    private Scodevaleur natureLocalite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "slocalites", "equestionnaires", "parent", "scode", "sstructures" }, allowSetters = true)
    private Scodevaleur typeLocalite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Slocalite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodelocalite() {
        return this.codelocalite;
    }

    public Slocalite codelocalite(Long codelocalite) {
        this.setCodelocalite(codelocalite);
        return this;
    }

    public void setCodelocalite(Long codelocalite) {
        this.codelocalite = codelocalite;
    }

    public String getNomlocalite() {
        return this.nomlocalite;
    }

    public Slocalite nomlocalite(String nomlocalite) {
        this.setNomlocalite(nomlocalite);
        return this;
    }

    public void setNomlocalite(String nomlocalite) {
        this.nomlocalite = nomlocalite;
    }

    public Set<Sstructure> getSstructures() {
        return this.sstructures;
    }

    public void setSstructures(Set<Sstructure> sstructures) {
        if (this.sstructures != null) {
            this.sstructures.forEach(i -> i.setSlocalite(null));
        }
        if (sstructures != null) {
            sstructures.forEach(i -> i.setSlocalite(this));
        }
        this.sstructures = sstructures;
    }

    public Slocalite sstructures(Set<Sstructure> sstructures) {
        this.setSstructures(sstructures);
        return this;
    }

    public Slocalite addSstructure(Sstructure sstructure) {
        this.sstructures.add(sstructure);
        sstructure.setSlocalite(this);
        return this;
    }

    public Slocalite removeSstructure(Sstructure sstructure) {
        this.sstructures.remove(sstructure);
        sstructure.setSlocalite(null);
        return this;
    }

    public Slocalite getParent() {
        return this.parent;
    }

    public void setParent(Slocalite slocalite) {
        this.parent = slocalite;
    }

    public Slocalite parent(Slocalite slocalite) {
        this.setParent(slocalite);
        return this;
    }

    public Scodevaleur getNatureLocalite() {
        return this.natureLocalite;
    }

    public void setNatureLocalite(Scodevaleur scodevaleur) {
        this.natureLocalite = scodevaleur;
    }

    public Slocalite natureLocalite(Scodevaleur scodevaleur) {
        this.setNatureLocalite(scodevaleur);
        return this;
    }

    public Scodevaleur getTypeLocalite() {
        return this.typeLocalite;
    }

    public void setTypeLocalite(Scodevaleur scodevaleur) {
        this.typeLocalite = scodevaleur;
    }

    public Slocalite typeLocalite(Scodevaleur scodevaleur) {
        this.setTypeLocalite(scodevaleur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Slocalite)) {
            return false;
        }
        return id != null && id.equals(((Slocalite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Slocalite{" +
            "id=" + getId() +
            ", codelocalite=" + getCodelocalite() +
            ", nomlocalite='" + getNomlocalite() + "'" +
            "}";
    }
}
