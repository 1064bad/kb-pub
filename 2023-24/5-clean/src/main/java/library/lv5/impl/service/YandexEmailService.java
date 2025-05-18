package library.lv5.impl.service;

import library.lv0.crosscutting.Settings;
import library.lv2.spi.service.Email;
import library.lv2.spi.service.EmailService;
import library.lv2.spi.service.ServiceAppException;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class YandexEmailService implements EmailService {
    private final Mailer mailer;

    public YandexEmailService() {
        try {
            mailer = MailerBuilder
                    .withSMTPServer(
                            Settings.EMAIL_SMTP_ADDRESS,
                            Settings.EMAIL_SMTP_PORT,
                            Settings.EMAIL_ADDRESS,
                            System.getenv("CLEAN_YMAIL_TOKEN"))
                    .withTransportStrategy(TransportStrategy.SMTPS)
                    .buildMailer();
            mailer.testConnection();
        }
        catch (Exception e) {
            throw new ServiceAppException(e);
        }
    }

    @Override
    public void send(Email email) {
        mailer.sendMail(
                EmailBuilder.startingBlank()
                        .from(Settings.EMAIL_ADDRESS)
                        .to(email.getReceiverAddress())
                        .withSubject("new book added!")
                        .withPlainText(email.getBody())
                        .buildEmail());
    }
}
