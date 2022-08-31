package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sstructure.
 */
@Entity
@Table(name = "sstructure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sstructure")
public class Sstructure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nomstructure")
    private String nomstructure;

    @Column(name = "siglestructure")
    private String siglestructure;

    @Column(name = "telstructure")
    private String telstructure;

    @Column(name = "bpstructure")
    private String bpstructure;

    @Column(name = "emailstructure")
    private String emailstructure;

    @Column(name = "profondeur")
    private Long profondeur;

    @OneToMany(mappedBy = "sstructure")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "egroupevariable", "sstructure" }, allowSetters = true)
    private Set<Evaleurvariable> evaleurvariables = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "evaleurvariables", "parent", "scodes", "slocalite" }, allowSetters = true)
    private Sstructure parent;

    @ManyToMany
    @JoinTable(
        name = "rel_sstructure__scode",
        joinColumns = @JoinColumn(name = "sstructure_id"),
        inverseJoinColumns = @JoinColumn(name = "scode_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "slocalites", "equestionnaires", "parent", "scode", "sstructures" }, allowSetters = true)
    private Set<Scodevaleur> scodes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sstructures", "parent", "natureLocalite", "typeLocalite" }, allowSetters = true)
    private Slocalite slocalite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sstructure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomstructure() {
        return this.nomstructure;
    }

    public Sstructure nomstructure(String nomstructure) {
        this.setNomstructure(nomstructure);
        return this;
    }

    public void setNomstructure(String nomstructure) {
        this.nomstructure = nomstructure;
    }

    public String getSiglestructure() {
        return this.siglestructure;
    }

    public Sstructure siglestructure(String siglestructure) {
        this.setSiglestructure(siglestructure);
        return this;
    }

    public void setSiglestructure(String siglestructure) {
        this.siglestructure = siglestructure;
    }

    public String getTelstructure() {
        return this.telstructure;
    }

    public Sstructure telstructure(String telstructure) {
        this.setTelstructure(telstructure);
        return this;
    }

    public void setTelstructure(String telstructure) {
        this.telstructure = telstructure;
    }

    public String getBpstructure() {
        return this.bpstructure;
    }

    public Sstructure bpstructure(String bpstructure) {
        this.setBpstructure(bpstructure);
        return this;
    }

    public void setBpstructure(String bpstructure) {
        this.bpstructure = bpstructure;
    }

    public String getEmailstructure() {
        return this.emailstructure;
    }

    public Sstructure emailstructure(String emailstructure) {
        this.setEmailstructure(emailstructure);
        return this;
    }

    public void setEmailstructure(String emailstructure) {
        this.emailstructure = emailstructure;
    }

    public Long getProfondeur() {
        return this.profondeur;
    }

    public Sstructure profondeur(Long profondeur) {
        this.setProfondeur(profondeur);
        return this;
    }

    public void setProfondeur(Long profondeur) {
        this.profondeur = profondeur;
    }

    public Set<Evaleurvariable> getEvaleurvariables() {
        return this.evaleurvariables;
    }

    public void setEvaleurvariables(Set<Evaleurvariable> evaleurvariables) {
        if (this.evaleurvariables != null) {
            this.evaleurvariables.forEach(i -> i.setSstructure(null));
        }
        if (evaleurvariables != null) {
            evaleurvariables.forEach(i -> i.setSstructure(this));
        }
        this.evaleurvariables = evaleurvariables;
    }

    public Sstructure evaleurvariables(Set<Evaleurvariable> evaleurvariables) {
        this.setEvaleurvariables(evaleurvariables);
        return this;
    }

    public Sstructure addEvaleurvariable(Evaleurvariable evaleurvariable) {
        this.evaleurvariables.add(evaleurvariable);
        evaleurvariable.setSstructure(this);
        return this;
    }

    public Sstructure removeEvaleurvariable(Evaleurvariable evaleurvariable) {
        this.evaleurvariables.remove(evaleurvariable);
        evaleurvariable.setSstructure(null);
        return this;
    }

    public Sstructure getParent() {
        return this.parent;
    }

    public void setParent(Sstructure sstructure) {
        this.parent = sstructure;
    }

    public Sstructure parent(Sstructure sstructure) {
        this.setParent(sstructure);
        return this;
    }

    public Set<Scodevaleur> getScodes() {
        return this.scodes;
    }

    public void setScodes(Set<Scodevaleur> scodevaleurs) {
        this.scodes = scodevaleurs;
    }

    public Sstructure scodes(Set<Scodevaleur> scodevaleurs) {
        this.setScodes(scodevaleurs);
        return this;
    }

    public Sstructure addScode(Scodevaleur scodevaleur) {
        this.scodes.add(scodevaleur);
        scodevaleur.getSstructures().add(this);
        return this;
    }

    public Sstructure removeScode(Scodevaleur scodevaleur) {
        this.scodes.remove(scodevaleur);
        scodevaleur.getSstructures().remove(this);
        return this;
    }

    public Slocalite getSlocalite() {
        return this.slocalite;
    }

    public void setSlocalite(Slocalite slocalite) {
        this.slocalite = slocalite;
    }

    public Sstructure slocalite(Slocalite slocalite) {
        this.setSlocalite(slocalite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sstructure)) {
            return false;
        }
        return id != null && id.equals(((Sstructure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sstructure{" +
            "id=" + getId() +
            ", nomstructure='" + getNomstructure() + "'" +
            ", siglestructure='" + getSiglestructure() + "'" +
            ", telstructure='" + getTelstructure() + "'" +
            ", bpstructure='" + getBpstructure() + "'" +
            ", emailstructure='" + getEmailstructure() + "'" +
            ", profondeur=" + getProfondeur() +
            "}";
    }
}
