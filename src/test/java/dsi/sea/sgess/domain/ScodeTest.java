package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scode.class);
        Scode scode1 = new Scode();
        scode1.setId(1L);
        Scode scode2 = new Scode();
        scode2.setId(scode1.getId());
        assertThat(scode1).isEqualTo(scode2);
        scode2.setId(2L);
        assertThat(scode1).isNotEqualTo(scode2);
        scode1.setId(null);
        assertThat(scode1).isNotEqualTo(scode2);
    }
}
