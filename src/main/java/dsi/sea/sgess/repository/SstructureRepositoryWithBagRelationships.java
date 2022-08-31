package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Sstructure;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SstructureRepositoryWithBagRelationships {
    Optional<Sstructure> fetchBagRelationships(Optional<Sstructure> sstructure);

    List<Sstructure> fetchBagRelationships(List<Sstructure> sstructures);

    Page<Sstructure> fetchBagRelationships(Page<Sstructure> sstructures);
}
