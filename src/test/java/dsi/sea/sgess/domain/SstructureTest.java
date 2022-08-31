package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SstructureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sstructure.class);
        Sstructure sstructure1 = new Sstructure();
        sstructure1.setId(1L);
        Sstructure sstructure2 = new Sstructure();
        sstructure2.setId(sstructure1.getId());
        assertThat(sstructure1).isEqualTo(sstructure2);
        sstructure2.setId(2L);
        assertThat(sstructure1).isNotEqualTo(sstructure2);
        sstructure1.setId(null);
        assertThat(sstructure1).isNotEqualTo(sstructure2);
    }
}
