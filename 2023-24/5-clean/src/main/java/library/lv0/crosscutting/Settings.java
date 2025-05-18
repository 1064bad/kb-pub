package library.lv0.crosscutting;

public interface Settings {
    String EMAIL_SMTP_ADDRESS = "smtp.yandex.ru";
    int EMAIL_SMTP_PORT = 465;
    String EMAIL_ADDRESS = "busygin-k@kiszi.ru";

    String PG_CONN_STRING = "jdbc:postgresql://172.17.0.1/library";
//    String PG_CONN_STRING = "jdbc:postgresql://localhost/library";
    String PG_USERNAME = "user";
    String PG_PASSWORD = "secret";
}
