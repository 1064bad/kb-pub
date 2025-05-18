package server.controller.render;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import server.controller.Model;

import static org.junit.jupiter.api.Assertions.*;

class HtmlRenderTest {
    static class TestClass {
        private final String str;

        public TestClass(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }
    }
    static class Test2 {
        public final TestClass test;

        public Test2(TestClass test) {
            this.test = test;
        }

        public TestClass getTest() {
            return test;
        }
    }

    @Test
    void getValue() {
        var model = new Model();
        var str = "hello dolly";
        model.set("testVar", new Test2(new TestClass(str)));
        var path = "testVar.test.str";
        Assertions.assertThat(new HtmlRender().getValueFromModel(model.getVariables(), path))
                .isEqualTo(str);
    }
}