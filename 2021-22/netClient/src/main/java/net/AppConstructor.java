package net;

import net.core.Core;
import net.transport.persist.socket.PersistSocketTransport;
import net.transport.session.channel.SessionChannelTransport;
import net.transport.session.netty.NettySessionTransport;
import net.transport.session.socket.SessionSocketTransport;
import net.transport.session.serialization.SessionSerializeTransport;
import net.ui.ConsoleConnector;
import net.ui.ConsoleUI;

public class AppConstructor {
    public static Core construct() {
        return new Core(
                new ConsoleUI(new ConsoleConnector()),
                new NettySessionTransport());
    }
}
