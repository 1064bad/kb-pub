package kb.tmp2.orm;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import kb.tmp2.orm.config.di.DependencyProcessor;
import kb.tmp2.orm.controller.BookHttpService;

public class App {
    public static void main(String[] args) throws InterruptedException {
        var dp = new DependencyProcessor("kb.tmp2.orm");

        Server.builder()
                .http(9999)

                .service("/hello",
                        (ctx, req) -> HttpResponse.of("Hello!"))

                .service("/book", dp.getDependency(BookHttpService.class))

                .build()
                .start()
                .join();
    }
}
