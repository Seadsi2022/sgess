package dsi.sea.sgess.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Scode.
 */
@Entity
@Table(name = "scode")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "scode")
public class Scode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code_lib")
    private String codeLib;

    @OneToMany(mappedBy = "scode")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "slocalites", "equestionnaires", "parent", "scode", "sstructures" }, allowSetters = true)
    private Set<Scodevaleur> scodevaleurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Scode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeLib() {
        return this.codeLib;
    }

    public Scode codeLib(String codeLib) {
        this.setCodeLib(codeLib);
        return this;
    }

    public void setCodeLib(String codeLib) {
        this.codeLib = codeLib;
    }

    public Set<Scodevaleur> getScodevaleurs() {
        return this.scodevaleurs;
    }

    public void setScodevaleurs(Set<Scodevaleur> scodevaleurs) {
        if (this.scodevaleurs != null) {
            this.scodevaleurs.forEach(i -> i.setScode(null));
        }
        if (scodevaleurs != null) {
            scodevaleurs.forEach(i -> i.setScode(this));
        }
        this.scodevaleurs = scodevaleurs;
    }

    public Scode scodevaleurs(Set<Scodevaleur> scodevaleurs) {
        this.setScodevaleurs(scodevaleurs);
        return this;
    }

    public Scode addScodevaleur(Scodevaleur scodevaleur) {
        this.scodevaleurs.add(scodevaleur);
        scodevaleur.setScode(this);
        return this;
    }

    public Scode removeScodevaleur(Scodevaleur scodevaleur) {
        this.scodevaleurs.remove(scodevaleur);
        scodevaleur.setScode(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Scode)) {
            return false;
        }
        return id != null && id.equals(((Scode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Scode{" +
            "id=" + getId() +
            ", codeLib='" + getCodeLib() + "'" +
            "}";
    }
}
