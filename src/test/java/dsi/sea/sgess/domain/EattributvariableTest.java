package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EattributvariableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Eattributvariable.class);
        Eattributvariable eattributvariable1 = new Eattributvariable();
        eattributvariable1.setId(1L);
        Eattributvariable eattributvariable2 = new Eattributvariable();
        eattributvariable2.setId(eattributvariable1.getId());
        assertThat(eattributvariable1).isEqualTo(eattributvariable2);
        eattributvariable2.setId(2L);
        assertThat(eattributvariable1).isNotEqualTo(eattributvariable2);
        eattributvariable1.setId(null);
        assertThat(eattributvariable1).isNotEqualTo(eattributvariable2);
    }
}
