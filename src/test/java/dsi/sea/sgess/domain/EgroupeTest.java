package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EgroupeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Egroupe.class);
        Egroupe egroupe1 = new Egroupe();
        egroupe1.setId(1L);
        Egroupe egroupe2 = new Egroupe();
        egroupe2.setId(egroupe1.getId());
        assertThat(egroupe1).isEqualTo(egroupe2);
        egroupe2.setId(2L);
        assertThat(egroupe1).isNotEqualTo(egroupe2);
        egroupe1.setId(null);
        assertThat(egroupe1).isNotEqualTo(egroupe2);
    }
}
