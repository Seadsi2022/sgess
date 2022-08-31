package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EquestionnaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equestionnaire.class);
        Equestionnaire equestionnaire1 = new Equestionnaire();
        equestionnaire1.setId(1L);
        Equestionnaire equestionnaire2 = new Equestionnaire();
        equestionnaire2.setId(equestionnaire1.getId());
        assertThat(equestionnaire1).isEqualTo(equestionnaire2);
        equestionnaire2.setId(2L);
        assertThat(equestionnaire1).isNotEqualTo(equestionnaire2);
        equestionnaire1.setId(null);
        assertThat(equestionnaire1).isNotEqualTo(equestionnaire2);
    }
}
