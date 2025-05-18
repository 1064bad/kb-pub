package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

@Slf4j
public class ObjectMapperTest {
    @RequiredArgsConstructor
    @Getter
    private static class MarshallingClass {
        private final int x;
        private final String y;
        private final List<String> list;
        private final Set<MarshallingClass> objects;
    }

    private static MarshallingClass createObject() {
        return new MarshallingClass(
                11,
                "qwerty asddfg",
                List.of("qwe", "asd", "zxc"),
                Set.of(
                        new MarshallingClass(
                                2,
                                "asd",
                                List.of("zs", "xc", "cv"),
                                Set.of(new MarshallingClass(3, "yui", null, null))),
                        new MarshallingClass(
                                4,
                                "ghj ghj ghj",
                                List.of("55", "66", "77"),
                                Set.of())
                )
        );
    }

    @Test
    void testJson() {
        var mapper = new ObjectMapper();
        var obj = createObject();
        val arr = new String[1];
        Assertions.assertThatNoException()
                .isThrownBy(() -> arr[0] = mapper.writeValueAsString(obj));

        log.info(arr[0]);
    }

    @Test
    void testXml() {
        var mapper = new XmlMapper();
        var obj = createObject();
        val arr = new String[1];
        Assertions.assertThatNoException()
                .isThrownBy(() -> arr[0] = mapper.writeValueAsString(obj));

        log.info(arr[0]);
    }
}
