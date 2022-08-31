package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EattributTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Eattribut.class);
        Eattribut eattribut1 = new Eattribut();
        eattribut1.setId(1L);
        Eattribut eattribut2 = new Eattribut();
        eattribut2.setId(eattribut1.getId());
        assertThat(eattribut1).isEqualTo(eattribut2);
        eattribut2.setId(2L);
        assertThat(eattribut1).isNotEqualTo(eattribut2);
        eattribut1.setId(null);
        assertThat(eattribut1).isNotEqualTo(eattribut2);
    }
}
