package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Evaleurattribut.
 */
@Entity
@Table(name = "evaleurattribut")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "evaleurattribut")
public class Evaleurattribut implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valeur")
    private String valeur;

    @Column(name = "valeurdisplayname")
    private String valeurdisplayname;

    @OneToMany(mappedBy = "evaleurattribut")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "evaleurattribut", "etypechamps" }, allowSetters = true)
    private Set<Eattribut> eattributs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evaleurattribut id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return this.valeur;
    }

    public Evaleurattribut valeur(String valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getValeurdisplayname() {
        return this.valeurdisplayname;
    }

    public Evaleurattribut valeurdisplayname(String valeurdisplayname) {
        this.setValeurdisplayname(valeurdisplayname);
        return this;
    }

    public void setValeurdisplayname(String valeurdisplayname) {
        this.valeurdisplayname = valeurdisplayname;
    }

    public Set<Eattribut> getEattributs() {
        return this.eattributs;
    }

    public void setEattributs(Set<Eattribut> eattributs) {
        if (this.eattributs != null) {
            this.eattributs.forEach(i -> i.setEvaleurattribut(null));
        }
        if (eattributs != null) {
            eattributs.forEach(i -> i.setEvaleurattribut(this));
        }
        this.eattributs = eattributs;
    }

    public Evaleurattribut eattributs(Set<Eattribut> eattributs) {
        this.setEattributs(eattributs);
        return this;
    }

    public Evaleurattribut addEattribut(Eattribut eattribut) {
        this.eattributs.add(eattribut);
        eattribut.setEvaleurattribut(this);
        return this;
    }

    public Evaleurattribut removeEattribut(Eattribut eattribut) {
        this.eattributs.remove(eattribut);
        eattribut.setEvaleurattribut(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evaleurattribut)) {
            return false;
        }
        return id != null && id.equals(((Evaleurattribut) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evaleurattribut{" +
            "id=" + getId() +
            ", valeur='" + getValeur() + "'" +
            ", valeurdisplayname='" + getValeurdisplayname() + "'" +
            "}";
    }
}
