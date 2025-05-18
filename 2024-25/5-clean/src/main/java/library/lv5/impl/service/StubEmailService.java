package library.lv5.impl.service;

import library.lv2.spi.service.Email;
import library.lv2.spi.service.EmailService;
import library.lv2.spi.service.ServiceAppException;
import library.lv4.controller.console.IO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StubEmailService implements EmailService {
    private final IO io;

    @Override
    public void send(Email email) {
        throw new ServiceAppException("email error");

//        io.println("email to " + email.getReceiverAddress() + " send: ");
//        io.println(email.getBody());
//        io.println("======= end of email body");
    }
}
