package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Equestionnaire.
 */
@Entity
@Table(name = "equestionnaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "equestionnaire")
public class Equestionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "objetquest")
    private String objetquest;

    @Column(name = "descriptionquest")
    private String descriptionquest;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "equestionnaire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "egroupevariables", "equestionnaire" }, allowSetters = true)
    private Set<Egroupe> egroupes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "egroupes", "parent", "ecampagne", "typestructure" }, allowSetters = true)
    private Equestionnaire parent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "equestionnaires" }, allowSetters = true)
    private Ecampagne ecampagne;

    @ManyToOne
    @JsonIgnoreProperties(value = { "slocalites", "equestionnaires", "parent", "scode", "sstructures" }, allowSetters = true)
    private Scodevaleur typestructure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Equestionnaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjetquest() {
        return this.objetquest;
    }

    public Equestionnaire objetquest(String objetquest) {
        this.setObjetquest(objetquest);
        return this;
    }

    public void setObjetquest(String objetquest) {
        this.objetquest = objetquest;
    }

    public String getDescriptionquest() {
        return this.descriptionquest;
    }

    public Equestionnaire descriptionquest(String descriptionquest) {
        this.setDescriptionquest(descriptionquest);
        return this;
    }

    public void setDescriptionquest(String descriptionquest) {
        this.descriptionquest = descriptionquest;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Equestionnaire isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Egroupe> getEgroupes() {
        return this.egroupes;
    }

    public void setEgroupes(Set<Egroupe> egroupes) {
        if (this.egroupes != null) {
            this.egroupes.forEach(i -> i.setEquestionnaire(null));
        }
        if (egroupes != null) {
            egroupes.forEach(i -> i.setEquestionnaire(this));
        }
        this.egroupes = egroupes;
    }

    public Equestionnaire egroupes(Set<Egroupe> egroupes) {
        this.setEgroupes(egroupes);
        return this;
    }

    public Equestionnaire addEgroupe(Egroupe egroupe) {
        this.egroupes.add(egroupe);
        egroupe.setEquestionnaire(this);
        return this;
    }

    public Equestionnaire removeEgroupe(Egroupe egroupe) {
        this.egroupes.remove(egroupe);
        egroupe.setEquestionnaire(null);
        return this;
    }

    public Equestionnaire getParent() {
        return this.parent;
    }

    public void setParent(Equestionnaire equestionnaire) {
        this.parent = equestionnaire;
    }

    public Equestionnaire parent(Equestionnaire equestionnaire) {
        this.setParent(equestionnaire);
        return this;
    }

    public Ecampagne getEcampagne() {
        return this.ecampagne;
    }

    public void setEcampagne(Ecampagne ecampagne) {
        this.ecampagne = ecampagne;
    }

    public Equestionnaire ecampagne(Ecampagne ecampagne) {
        this.setEcampagne(ecampagne);
        return this;
    }

    public Scodevaleur getTypestructure() {
        return this.typestructure;
    }

    public void setTypestructure(Scodevaleur scodevaleur) {
        this.typestructure = scodevaleur;
    }

    public Equestionnaire typestructure(Scodevaleur scodevaleur) {
        this.setTypestructure(scodevaleur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equestionnaire)) {
            return false;
        }
        return id != null && id.equals(((Equestionnaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equestionnaire{" +
            "id=" + getId() +
            ", objetquest='" + getObjetquest() + "'" +
            ", descriptionquest='" + getDescriptionquest() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
