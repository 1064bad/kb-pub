package library.lv5.impl.service;

import library.lv0.crosscutting.di.Dependency;
import library.lv2.spi.service.Email;
import library.lv2.spi.service.EmailService;
import library.lv4.controller.console.IO;
import lombok.RequiredArgsConstructor;

@Dependency
@RequiredArgsConstructor
public class StubEmailService implements EmailService {
    private final IO io;

    @Override
    public void send(Email email) {
        io.println("email to " + email.getReceiverAddress() + " send: ");
        io.println(email.getBody());
        io.println("======= end od email body");
    }
}
