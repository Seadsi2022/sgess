package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EcampagneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ecampagne.class);
        Ecampagne ecampagne1 = new Ecampagne();
        ecampagne1.setId(1L);
        Ecampagne ecampagne2 = new Ecampagne();
        ecampagne2.setId(ecampagne1.getId());
        assertThat(ecampagne1).isEqualTo(ecampagne2);
        ecampagne2.setId(2L);
        assertThat(ecampagne1).isNotEqualTo(ecampagne2);
        ecampagne1.setId(null);
        assertThat(ecampagne1).isNotEqualTo(ecampagne2);
    }
}
