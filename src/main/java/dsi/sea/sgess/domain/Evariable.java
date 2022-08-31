package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Evariable.
 */
@Entity
@Table(name = "evariable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "evariable")
public class Evariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nomvariable")
    private String nomvariable;

    @Column(name = "descvariable")
    private String descvariable;

    @Column(name = "champ")
    private String champ;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "evariable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "evariable" }, allowSetters = true)
    private Set<Eattributvariable> eattributvariables = new HashSet<>();

    @OneToMany(mappedBy = "evariable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "evariable", "eeventualite" }, allowSetters = true)
    private Set<Eeventualitevariable> eeventualitevariables = new HashSet<>();

    @OneToMany(mappedBy = "evariable")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "evaleurvariables", "suivant", "evariable", "egroupe" }, allowSetters = true)
    private Set<Egroupevariable> egroupevariables = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "evariables" }, allowSetters = true)
    private Etypevariable etypevariable;

    @ManyToOne
    @JsonIgnoreProperties(value = { "evariables", "unitebase" }, allowSetters = true)
    private Eunite eunite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomvariable() {
        return this.nomvariable;
    }

    public Evariable nomvariable(String nomvariable) {
        this.setNomvariable(nomvariable);
        return this;
    }

    public void setNomvariable(String nomvariable) {
        this.nomvariable = nomvariable;
    }

    public String getDescvariable() {
        return this.descvariable;
    }

    public Evariable descvariable(String descvariable) {
        this.setDescvariable(descvariable);
        return this;
    }

    public void setDescvariable(String descvariable) {
        this.descvariable = descvariable;
    }

    public String getChamp() {
        return this.champ;
    }

    public Evariable champ(String champ) {
        this.setChamp(champ);
        return this;
    }

    public void setChamp(String champ) {
        this.champ = champ;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Evariable isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Eattributvariable> getEattributvariables() {
        return this.eattributvariables;
    }

    public void setEattributvariables(Set<Eattributvariable> eattributvariables) {
        if (this.eattributvariables != null) {
            this.eattributvariables.forEach(i -> i.setEvariable(null));
        }
        if (eattributvariables != null) {
            eattributvariables.forEach(i -> i.setEvariable(this));
        }
        this.eattributvariables = eattributvariables;
    }

    public Evariable eattributvariables(Set<Eattributvariable> eattributvariables) {
        this.setEattributvariables(eattributvariables);
        return this;
    }

    public Evariable addEattributvariable(Eattributvariable eattributvariable) {
        this.eattributvariables.add(eattributvariable);
        eattributvariable.setEvariable(this);
        return this;
    }

    public Evariable removeEattributvariable(Eattributvariable eattributvariable) {
        this.eattributvariables.remove(eattributvariable);
        eattributvariable.setEvariable(null);
        return this;
    }

    public Set<Eeventualitevariable> getEeventualitevariables() {
        return this.eeventualitevariables;
    }

    public void setEeventualitevariables(Set<Eeventualitevariable> eeventualitevariables) {
        if (this.eeventualitevariables != null) {
            this.eeventualitevariables.forEach(i -> i.setEvariable(null));
        }
        if (eeventualitevariables != null) {
            eeventualitevariables.forEach(i -> i.setEvariable(this));
        }
        this.eeventualitevariables = eeventualitevariables;
    }

    public Evariable eeventualitevariables(Set<Eeventualitevariable> eeventualitevariables) {
        this.setEeventualitevariables(eeventualitevariables);
        return this;
    }

    public Evariable addEeventualitevariable(Eeventualitevariable eeventualitevariable) {
        this.eeventualitevariables.add(eeventualitevariable);
        eeventualitevariable.setEvariable(this);
        return this;
    }

    public Evariable removeEeventualitevariable(Eeventualitevariable eeventualitevariable) {
        this.eeventualitevariables.remove(eeventualitevariable);
        eeventualitevariable.setEvariable(null);
        return this;
    }

    public Set<Egroupevariable> getEgroupevariables() {
        return this.egroupevariables;
    }

    public void setEgroupevariables(Set<Egroupevariable> egroupevariables) {
        if (this.egroupevariables != null) {
            this.egroupevariables.forEach(i -> i.setEvariable(null));
        }
        if (egroupevariables != null) {
            egroupevariables.forEach(i -> i.setEvariable(this));
        }
        this.egroupevariables = egroupevariables;
    }

    public Evariable egroupevariables(Set<Egroupevariable> egroupevariables) {
        this.setEgroupevariables(egroupevariables);
        return this;
    }

    public Evariable addEgroupevariable(Egroupevariable egroupevariable) {
        this.egroupevariables.add(egroupevariable);
        egroupevariable.setEvariable(this);
        return this;
    }

    public Evariable removeEgroupevariable(Egroupevariable egroupevariable) {
        this.egroupevariables.remove(egroupevariable);
        egroupevariable.setEvariable(null);
        return this;
    }

    public Etypevariable getEtypevariable() {
        return this.etypevariable;
    }

    public void setEtypevariable(Etypevariable etypevariable) {
        this.etypevariable = etypevariable;
    }

    public Evariable etypevariable(Etypevariable etypevariable) {
        this.setEtypevariable(etypevariable);
        return this;
    }

    public Eunite getEunite() {
        return this.eunite;
    }

    public void setEunite(Eunite eunite) {
        this.eunite = eunite;
    }

    public Evariable eunite(Eunite eunite) {
        this.setEunite(eunite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evariable)) {
            return false;
        }
        return id != null && id.equals(((Evariable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evariable{" +
            "id=" + getId() +
            ", nomvariable='" + getNomvariable() + "'" +
            ", descvariable='" + getDescvariable() + "'" +
            ", champ='" + getChamp() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
