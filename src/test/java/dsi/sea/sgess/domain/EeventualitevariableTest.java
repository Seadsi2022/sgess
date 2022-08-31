package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EeventualitevariableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Eeventualitevariable.class);
        Eeventualitevariable eeventualitevariable1 = new Eeventualitevariable();
        eeventualitevariable1.setId(1L);
        Eeventualitevariable eeventualitevariable2 = new Eeventualitevariable();
        eeventualitevariable2.setId(eeventualitevariable1.getId());
        assertThat(eeventualitevariable1).isEqualTo(eeventualitevariable2);
        eeventualitevariable2.setId(2L);
        assertThat(eeventualitevariable1).isNotEqualTo(eeventualitevariable2);
        eeventualitevariable1.setId(null);
        assertThat(eeventualitevariable1).isNotEqualTo(eeventualitevariable2);
    }
}
