package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Eunite.
 */
@Entity
@Table(name = "eunite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "eunite")
public class Eunite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nomunite")
    private String nomunite;

    @Column(name = "symboleunite")
    private String symboleunite;

    @Column(name = "facteur")
    private Long facteur;

    @OneToMany(mappedBy = "eunite")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "eattributvariables", "eeventualitevariables", "egroupevariables", "etypevariable", "eunite" },
        allowSetters = true
    )
    private Set<Evariable> evariables = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "evariables", "unitebase" }, allowSetters = true)
    private Eunite unitebase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Eunite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomunite() {
        return this.nomunite;
    }

    public Eunite nomunite(String nomunite) {
        this.setNomunite(nomunite);
        return this;
    }

    public void setNomunite(String nomunite) {
        this.nomunite = nomunite;
    }

    public String getSymboleunite() {
        return this.symboleunite;
    }

    public Eunite symboleunite(String symboleunite) {
        this.setSymboleunite(symboleunite);
        return this;
    }

    public void setSymboleunite(String symboleunite) {
        this.symboleunite = symboleunite;
    }

    public Long getFacteur() {
        return this.facteur;
    }

    public Eunite facteur(Long facteur) {
        this.setFacteur(facteur);
        return this;
    }

    public void setFacteur(Long facteur) {
        this.facteur = facteur;
    }

    public Set<Evariable> getEvariables() {
        return this.evariables;
    }

    public void setEvariables(Set<Evariable> evariables) {
        if (this.evariables != null) {
            this.evariables.forEach(i -> i.setEunite(null));
        }
        if (evariables != null) {
            evariables.forEach(i -> i.setEunite(this));
        }
        this.evariables = evariables;
    }

    public Eunite evariables(Set<Evariable> evariables) {
        this.setEvariables(evariables);
        return this;
    }

    public Eunite addEvariable(Evariable evariable) {
        this.evariables.add(evariable);
        evariable.setEunite(this);
        return this;
    }

    public Eunite removeEvariable(Evariable evariable) {
        this.evariables.remove(evariable);
        evariable.setEunite(null);
        return this;
    }

    public Eunite getUnitebase() {
        return this.unitebase;
    }

    public void setUnitebase(Eunite eunite) {
        this.unitebase = eunite;
    }

    public Eunite unitebase(Eunite eunite) {
        this.setUnitebase(eunite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Eunite)) {
            return false;
        }
        return id != null && id.equals(((Eunite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Eunite{" +
            "id=" + getId() +
            ", nomunite='" + getNomunite() + "'" +
            ", symboleunite='" + getSymboleunite() + "'" +
            ", facteur=" + getFacteur() +
            "}";
    }
}
