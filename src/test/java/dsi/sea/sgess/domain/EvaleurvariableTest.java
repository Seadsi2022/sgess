package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvaleurvariableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaleurvariable.class);
        Evaleurvariable evaleurvariable1 = new Evaleurvariable();
        evaleurvariable1.setId(1L);
        Evaleurvariable evaleurvariable2 = new Evaleurvariable();
        evaleurvariable2.setId(evaleurvariable1.getId());
        assertThat(evaleurvariable1).isEqualTo(evaleurvariable2);
        evaleurvariable2.setId(2L);
        assertThat(evaleurvariable1).isNotEqualTo(evaleurvariable2);
        evaleurvariable1.setId(null);
        assertThat(evaleurvariable1).isNotEqualTo(evaleurvariable2);
    }
}
