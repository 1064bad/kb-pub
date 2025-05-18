package kb.tmp2.orm.config;

import kb.tmp2.orm.domain.Author;
import kb.tmp2.orm.domain.Award;
import kb.tmp2.orm.domain.Book;
import kb.tmp2.orm.domain.Commission;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.function.Consumer;
import java.util.function.Function;

public class Db {
    private static final SessionFactory sessionFactory;

    static {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .loadProperties("hibernate.properties")
                        .build();
        sessionFactory = new MetadataSources(registry)
                .addAnnotatedClasses(
                        Author.class,
                        Award.class,
                        Book.class,
                        Commission.class
                )
                .buildMetadata()
                .buildSessionFactory();
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static void inTransaction(Consumer<Session> action) {
        sessionFactory.inTransaction(action);
    }

    public static <T> T fromTransaction(Function<Session, T> action) {
        return sessionFactory.fromTransaction(action);
    }
}
