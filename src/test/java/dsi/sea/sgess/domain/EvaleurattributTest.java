package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvaleurattributTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaleurattribut.class);
        Evaleurattribut evaleurattribut1 = new Evaleurattribut();
        evaleurattribut1.setId(1L);
        Evaleurattribut evaleurattribut2 = new Evaleurattribut();
        evaleurattribut2.setId(evaleurattribut1.getId());
        assertThat(evaleurattribut1).isEqualTo(evaleurattribut2);
        evaleurattribut2.setId(2L);
        assertThat(evaleurattribut1).isNotEqualTo(evaleurattribut2);
        evaleurattribut1.setId(null);
        assertThat(evaleurattribut1).isNotEqualTo(evaleurattribut2);
    }
}
