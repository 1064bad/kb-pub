package app.server.net;

import app.IO;
import app.transport.Transport;
import app.transport.message.ErrorResponse;
import app.transport.message.Message;

public class UnimplementedHandler extends Handler {
    public UnimplementedHandler(Transport transport, IO io) {
        super(transport, io);
    }

    @Override
    public void handle(Message message) {
        try {
            transport.send(new ErrorResponse(
                    STR."no handler for message type \{message.getClass().getSimpleName()}"));
        } finally {
            transport.disconnect();
        }
    }
}
