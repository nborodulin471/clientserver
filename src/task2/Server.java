package task2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {
    private ServerSocketChannel serverSocketChannel;

    public Server(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", port));
        } catch (IOException e) {
            System.out.println("Не удалось объявить сервер");
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Старт сервера");
        while (true) {
            try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) {
                        break;
                    }
                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента: " + "'" + msg + "'" +
                            " Убираю пробелы:");
                    socketChannel.write(ByteBuffer.wrap(("Ответ сервера: " + deleteSpaces(msg))
                            .getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException e) {
                System.out.println("Не удалось запустить сервер");
                e.printStackTrace();
                break;
            } finally {
                System.out.println("Сервер закрыт");
            }
        }
    }

    private String deleteSpaces(String s) {
        return s.replaceAll(" ", "");
    }
}

