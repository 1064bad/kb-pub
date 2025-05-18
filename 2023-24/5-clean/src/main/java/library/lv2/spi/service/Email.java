package library.lv2.spi.service;

import lombok.Data;

@Data
public class Email {
    private final String receiverAddress;
    private final String body;
}
