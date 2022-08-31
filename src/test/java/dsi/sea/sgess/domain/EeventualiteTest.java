package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EeventualiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Eeventualite.class);
        Eeventualite eeventualite1 = new Eeventualite();
        eeventualite1.setId(1L);
        Eeventualite eeventualite2 = new Eeventualite();
        eeventualite2.setId(eeventualite1.getId());
        assertThat(eeventualite1).isEqualTo(eeventualite2);
        eeventualite2.setId(2L);
        assertThat(eeventualite1).isNotEqualTo(eeventualite2);
        eeventualite1.setId(null);
        assertThat(eeventualite1).isNotEqualTo(eeventualite2);
    }
}
