package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Etypechamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EtypechampRepositoryWithBagRelationships {
    Optional<Etypechamp> fetchBagRelationships(Optional<Etypechamp> etypechamp);

    List<Etypechamp> fetchBagRelationships(List<Etypechamp> etypechamps);

    Page<Etypechamp> fetchBagRelationships(Page<Etypechamp> etypechamps);
}
