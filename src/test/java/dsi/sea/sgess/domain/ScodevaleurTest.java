package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScodevaleurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scodevaleur.class);
        Scodevaleur scodevaleur1 = new Scodevaleur();
        scodevaleur1.setId(1L);
        Scodevaleur scodevaleur2 = new Scodevaleur();
        scodevaleur2.setId(scodevaleur1.getId());
        assertThat(scodevaleur1).isEqualTo(scodevaleur2);
        scodevaleur2.setId(2L);
        assertThat(scodevaleur1).isNotEqualTo(scodevaleur2);
        scodevaleur1.setId(null);
        assertThat(scodevaleur1).isNotEqualTo(scodevaleur2);
    }
}
