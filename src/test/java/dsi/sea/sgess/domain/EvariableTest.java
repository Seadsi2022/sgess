package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvariableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evariable.class);
        Evariable evariable1 = new Evariable();
        evariable1.setId(1L);
        Evariable evariable2 = new Evariable();
        evariable2.setId(evariable1.getId());
        assertThat(evariable1).isEqualTo(evariable2);
        evariable2.setId(2L);
        assertThat(evariable1).isNotEqualTo(evariable2);
        evariable1.setId(null);
        assertThat(evariable1).isNotEqualTo(evariable2);
    }
}
