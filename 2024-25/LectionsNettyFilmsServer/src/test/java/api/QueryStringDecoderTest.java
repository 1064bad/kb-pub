package api;

import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class QueryStringDecoderTest {
    @Test
    void test() {
        var query = "/test/query?var1=qwerty&var2=qwe%20rty&var2=asd%20fgh";

        val decoder = new QueryStringDecoder(query);

        assertThat(decoder.path()).isEqualTo("/test/query");
        assertThat(decoder.parameters()).containsKey("var1");
        assertThat(decoder.parameters().get("var1"))
                .containsExactlyInAnyOrder("qwerty");
        assertThat(decoder.parameters()).containsKey("var2");
        assertThat(decoder.parameters().get("var2"))
                .containsExactlyInAnyOrder("qwe rty", "asd fgh");
    }
}
