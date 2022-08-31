package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EgroupevariableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Egroupevariable.class);
        Egroupevariable egroupevariable1 = new Egroupevariable();
        egroupevariable1.setId(1L);
        Egroupevariable egroupevariable2 = new Egroupevariable();
        egroupevariable2.setId(egroupevariable1.getId());
        assertThat(egroupevariable1).isEqualTo(egroupevariable2);
        egroupevariable2.setId(2L);
        assertThat(egroupevariable1).isNotEqualTo(egroupevariable2);
        egroupevariable1.setId(null);
        assertThat(egroupevariable1).isNotEqualTo(egroupevariable2);
    }
}
