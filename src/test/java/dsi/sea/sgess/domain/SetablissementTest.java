package dsi.sea.sgess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dsi.sea.sgess.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SetablissementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Setablissement.class);
        Setablissement setablissement1 = new Setablissement();
        setablissement1.setId(1L);
        Setablissement setablissement2 = new Setablissement();
        setablissement2.setId(setablissement1.getId());
        assertThat(setablissement1).isEqualTo(setablissement2);
        setablissement2.setId(2L);
        assertThat(setablissement1).isNotEqualTo(setablissement2);
        setablissement1.setId(null);
        assertThat(setablissement1).isNotEqualTo(setablissement2);
    }
}
