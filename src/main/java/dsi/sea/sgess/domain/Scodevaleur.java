package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Scodevaleur.
 */
@Entity
@Table(name = "scodevaleur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "scodevaleur")
public class Scodevaleur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "codevaleur_lib")
    private String codevaleurLib;

    @OneToMany(mappedBy = "typeLocalite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "sstructures", "parent", "natureLocalite", "typeLocalite" }, allowSetters = true)
    private Set<Slocalite> slocalites = new HashSet<>();

    @OneToMany(mappedBy = "typestructure")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "egroupes", "parent", "ecampagne", "typestructure" }, allowSetters = true)
    private Set<Equestionnaire> equestionnaires = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "slocalites", "equestionnaires", "parent", "scode", "sstructures" }, allowSetters = true)
    private Scodevaleur parent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "scodevaleurs" }, allowSetters = true)
    private Scode scode;

    @ManyToMany(mappedBy = "scodes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "evaleurvariables", "parent", "scodes", "slocalite" }, allowSetters = true)
    private Set<Sstructure> sstructures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Scodevaleur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodevaleurLib() {
        return this.codevaleurLib;
    }

    public Scodevaleur codevaleurLib(String codevaleurLib) {
        this.setCodevaleurLib(codevaleurLib);
        return this;
    }

    public void setCodevaleurLib(String codevaleurLib) {
        this.codevaleurLib = codevaleurLib;
    }

    public Set<Slocalite> getSlocalites() {
        return this.slocalites;
    }

    public void setSlocalites(Set<Slocalite> slocalites) {
        if (this.slocalites != null) {
            this.slocalites.forEach(i -> i.setTypeLocalite(null));
        }
        if (slocalites != null) {
            slocalites.forEach(i -> i.setTypeLocalite(this));
        }
        this.slocalites = slocalites;
    }

    public Scodevaleur slocalites(Set<Slocalite> slocalites) {
        this.setSlocalites(slocalites);
        return this;
    }

    public Scodevaleur addSlocalite(Slocalite slocalite) {
        this.slocalites.add(slocalite);
        slocalite.setTypeLocalite(this);
        return this;
    }

    public Scodevaleur removeSlocalite(Slocalite slocalite) {
        this.slocalites.remove(slocalite);
        slocalite.setTypeLocalite(null);
        return this;
    }

    public Set<Equestionnaire> getEquestionnaires() {
        return this.equestionnaires;
    }

    public void setEquestionnaires(Set<Equestionnaire> equestionnaires) {
        if (this.equestionnaires != null) {
            this.equestionnaires.forEach(i -> i.setTypestructure(null));
        }
        if (equestionnaires != null) {
            equestionnaires.forEach(i -> i.setTypestructure(this));
        }
        this.equestionnaires = equestionnaires;
    }

    public Scodevaleur equestionnaires(Set<Equestionnaire> equestionnaires) {
        this.setEquestionnaires(equestionnaires);
        return this;
    }

    public Scodevaleur addEquestionnaire(Equestionnaire equestionnaire) {
        this.equestionnaires.add(equestionnaire);
        equestionnaire.setTypestructure(this);
        return this;
    }

    public Scodevaleur removeEquestionnaire(Equestionnaire equestionnaire) {
        this.equestionnaires.remove(equestionnaire);
        equestionnaire.setTypestructure(null);
        return this;
    }

    public Scodevaleur getParent() {
        return this.parent;
    }

    public void setParent(Scodevaleur scodevaleur) {
        this.parent = scodevaleur;
    }

    public Scodevaleur parent(Scodevaleur scodevaleur) {
        this.setParent(scodevaleur);
        return this;
    }

    public Scode getScode() {
        return this.scode;
    }

    public void setScode(Scode scode) {
        this.scode = scode;
    }

    public Scodevaleur scode(Scode scode) {
        this.setScode(scode);
        return this;
    }

    public Set<Sstructure> getSstructures() {
        return this.sstructures;
    }

    public void setSstructures(Set<Sstructure> sstructures) {
        if (this.sstructures != null) {
            this.sstructures.forEach(i -> i.removeScode(this));
        }
        if (sstructures != null) {
            sstructures.forEach(i -> i.addScode(this));
        }
        this.sstructures = sstructures;
    }

    public Scodevaleur sstructures(Set<Sstructure> sstructures) {
        this.setSstructures(sstructures);
        return this;
    }

    public Scodevaleur addSstructure(Sstructure sstructure) {
        this.sstructures.add(sstructure);
        sstructure.getScodes().add(this);
        return this;
    }

    public Scodevaleur removeSstructure(Sstructure sstructure) {
        this.sstructures.remove(sstructure);
        sstructure.getScodes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Scodevaleur)) {
            return false;
        }
        return id != null && id.equals(((Scodevaleur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Scodevaleur{" +
            "id=" + getId() +
            ", codevaleurLib='" + getCodevaleurLib() + "'" +
            "}";
    }
}
