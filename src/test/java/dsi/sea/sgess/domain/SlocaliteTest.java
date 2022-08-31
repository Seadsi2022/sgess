package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlocaliteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Slocalite.class);
        Slocalite slocalite1 = new Slocalite();
        slocalite1.setId(1L);
        Slocalite slocalite2 = new Slocalite();
        slocalite2.setId(slocalite1.getId());
        assertThat(slocalite1).isEqualTo(slocalite2);
        slocalite2.setId(2L);
        assertThat(slocalite1).isNotEqualTo(slocalite2);
        slocalite1.setId(null);
        assertThat(slocalite1).isNotEqualTo(slocalite2);
    }
}
