package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EuniteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Eunite.class);
        Eunite eunite1 = new Eunite();
        eunite1.setId(1L);
        Eunite eunite2 = new Eunite();
        eunite2.setId(eunite1.getId());
        assertThat(eunite1).isEqualTo(eunite2);
        eunite2.setId(2L);
        assertThat(eunite1).isNotEqualTo(eunite2);
        eunite1.setId(null);
        assertThat(eunite1).isNotEqualTo(eunite2);
    }
}
