package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtypevariableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etypevariable.class);
        Etypevariable etypevariable1 = new Etypevariable();
        etypevariable1.setId(1L);
        Etypevariable etypevariable2 = new Etypevariable();
        etypevariable2.setId(etypevariable1.getId());
        assertThat(etypevariable1).isEqualTo(etypevariable2);
        etypevariable2.setId(2L);
        assertThat(etypevariable1).isNotEqualTo(etypevariable2);
        etypevariable1.setId(null);
        assertThat(etypevariable1).isNotEqualTo(etypevariable2);
    }
}
