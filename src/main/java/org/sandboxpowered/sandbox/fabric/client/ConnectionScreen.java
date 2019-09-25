package org.sandboxpowered.sandbox.fabric.client;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.server.network.packet.HandshakeC2SPacket;
import net.minecraft.server.network.packet.LoginHelloC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UncaughtExceptionLogger;
import org.sandboxpowered.sandbox.fabric.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionScreen extends Screen {
    private static final AtomicInteger CONNECTOR_THREADS_COUNT = new AtomicInteger(0);
    private final Screen parent;
    private ClientConnection connection;
    private boolean connectingCancelled;
    private Text status = new TranslatableText("connect.connecting");

    public ConnectionScreen(Text text_1, Screen parent) {
        super(text_1);
        this.parent = parent;
    }

    private void connect(final String string_1, final int int_1) {
        Log.info("Connecting to {}, {}", string_1, int_1);
        Thread thread_1 = new Thread("Server Connector #" + CONNECTOR_THREADS_COUNT.incrementAndGet()) {
            public void run() {
                InetAddress inetAddress_1 = null;

                try {
                    if (ConnectionScreen.this.connectingCancelled) {
                        return;
                    }

                    inetAddress_1 = InetAddress.getByName(string_1);
                    ConnectionScreen.this.connection = ClientConnection.connect(inetAddress_1, int_1, ConnectionScreen.this.minecraft.options.shouldUseNativeTransport());
                    ConnectionScreen.this.connection.setPacketListener(new ClientLoginNetworkHandler(ConnectionScreen.this.connection, ConnectionScreen.this.minecraft, ConnectionScreen.this.parent, ConnectionScreen.this::setStatus));
                    ConnectionScreen.this.connection.send(new HandshakeC2SPacket(string_1, int_1, NetworkState.LOGIN));
                    ConnectionScreen.this.connection.send(new LoginHelloC2SPacket(ConnectionScreen.this.minecraft.getSession().getProfile()));
                } catch (UnknownHostException var4) {
                    if (ConnectionScreen.this.connectingCancelled) {
                        return;
                    }

                    Log.error("Couldn't connect to server", var4);
                    ConnectionScreen.this.minecraft.execute(() -> {
                        ConnectionScreen.this.minecraft.openScreen(new DisconnectedScreen(ConnectionScreen.this.parent, "connect.failed", new TranslatableText("disconnect.genericReason", new Object[]{"Unknown host"})));
                    });
                } catch (Exception var5) {
                    if (ConnectionScreen.this.connectingCancelled) {
                        return;
                    }

                    Log.error("Couldn't connect to server", var5);
                    String string_1x = inetAddress_1 == null ? var5.toString() : var5.toString().replaceAll(inetAddress_1 + ":" + int_1, "");
                    ConnectionScreen.this.minecraft.execute(() -> {
                        ConnectionScreen.this.minecraft.openScreen(new DisconnectedScreen(ConnectionScreen.this.parent, "connect.failed", new TranslatableText("disconnect.genericReason", new Object[]{string_1x})));
                    });
                }

            }
        };
        thread_1.setUncaughtExceptionHandler(new UncaughtExceptionLogger(Log.LOG));
        thread_1.start();
    }

    public ConnectionScreen setStatus(Text status) {
        this.status = status;
        return this;
    }
}