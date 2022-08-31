package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ecampagne.
 */
@Entity
@Table(name = "ecampagne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ecampagne")
public class Ecampagne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "objetcampagne")
    private String objetcampagne;

    @Column(name = "description")
    private String description;

    @Column(name = "debutcampagne")
    private Instant debutcampagne;

    @Column(name = "fincampagne")
    private Instant fincampagne;

    @Column(name = "debutreelcamp")
    private Instant debutreelcamp;

    @Column(name = "finreelcamp")
    private Instant finreelcamp;

    @Column(name = "isopen")
    private Boolean isopen;

    @OneToMany(mappedBy = "ecampagne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "egroupes", "parent", "ecampagne", "typestructure" }, allowSetters = true)
    private Set<Equestionnaire> equestionnaires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ecampagne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjetcampagne() {
        return this.objetcampagne;
    }

    public Ecampagne objetcampagne(String objetcampagne) {
        this.setObjetcampagne(objetcampagne);
        return this;
    }

    public void setObjetcampagne(String objetcampagne) {
        this.objetcampagne = objetcampagne;
    }

    public String getDescription() {
        return this.description;
    }

    public Ecampagne description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDebutcampagne() {
        return this.debutcampagne;
    }

    public Ecampagne debutcampagne(Instant debutcampagne) {
        this.setDebutcampagne(debutcampagne);
        return this;
    }

    public void setDebutcampagne(Instant debutcampagne) {
        this.debutcampagne = debutcampagne;
    }

    public Instant getFincampagne() {
        return this.fincampagne;
    }

    public Ecampagne fincampagne(Instant fincampagne) {
        this.setFincampagne(fincampagne);
        return this;
    }

    public void setFincampagne(Instant fincampagne) {
        this.fincampagne = fincampagne;
    }

    public Instant getDebutreelcamp() {
        return this.debutreelcamp;
    }

    public Ecampagne debutreelcamp(Instant debutreelcamp) {
        this.setDebutreelcamp(debutreelcamp);
        return this;
    }

    public void setDebutreelcamp(Instant debutreelcamp) {
        this.debutreelcamp = debutreelcamp;
    }

    public Instant getFinreelcamp() {
        return this.finreelcamp;
    }

    public Ecampagne finreelcamp(Instant finreelcamp) {
        this.setFinreelcamp(finreelcamp);
        return this;
    }

    public void setFinreelcamp(Instant finreelcamp) {
        this.finreelcamp = finreelcamp;
    }

    public Boolean getIsopen() {
        return this.isopen;
    }

    public Ecampagne isopen(Boolean isopen) {
        this.setIsopen(isopen);
        return this;
    }

    public void setIsopen(Boolean isopen) {
        this.isopen = isopen;
    }

    public Set<Equestionnaire> getEquestionnaires() {
        return this.equestionnaires;
    }

    public void setEquestionnaires(Set<Equestionnaire> equestionnaires) {
        if (this.equestionnaires != null) {
            this.equestionnaires.forEach(i -> i.setEcampagne(null));
        }
        if (equestionnaires != null) {
            equestionnaires.forEach(i -> i.setEcampagne(this));
        }
        this.equestionnaires = equestionnaires;
    }

    public Ecampagne equestionnaires(Set<Equestionnaire> equestionnaires) {
        this.setEquestionnaires(equestionnaires);
        return this;
    }

    public Ecampagne addEquestionnaire(Equestionnaire equestionnaire) {
        this.equestionnaires.add(equestionnaire);
        equestionnaire.setEcampagne(this);
        return this;
    }

    public Ecampagne removeEquestionnaire(Equestionnaire equestionnaire) {
        this.equestionnaires.remove(equestionnaire);
        equestionnaire.setEcampagne(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ecampagne)) {
            return false;
        }
        return id != null && id.equals(((Ecampagne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ecampagne{" +
            "id=" + getId() +
            ", objetcampagne='" + getObjetcampagne() + "'" +
            ", description='" + getDescription() + "'" +
            ", debutcampagne='" + getDebutcampagne() + "'" +
            ", fincampagne='" + getFincampagne() + "'" +
            ", debutreelcamp='" + getDebutreelcamp() + "'" +
            ", finreelcamp='" + getFinreelcamp() + "'" +
            ", isopen='" + getIsopen() + "'" +
            "}";
    }
}
