package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtypechampTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etypechamp.class);
        Etypechamp etypechamp1 = new Etypechamp();
        etypechamp1.setId(1L);
        Etypechamp etypechamp2 = new Etypechamp();
        etypechamp2.setId(etypechamp1.getId());
        assertThat(etypechamp1).isEqualTo(etypechamp2);
        etypechamp2.setId(2L);
        assertThat(etypechamp1).isNotEqualTo(etypechamp2);
        etypechamp1.setId(null);
        assertThat(etypechamp1).isNotEqualTo(etypechamp2);
    }
}
