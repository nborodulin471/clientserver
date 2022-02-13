package task1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client implements Runnable{
    private SocketChannel socketChannel;

    public Client(String host, int port) {
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
        } catch (IOException e) {
            System.out.println("Клиенту не удалось подключиться");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Старт клиента");
        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            String msg;
            System.out.println("Для окончания введите 'конец'");
            while (true) {
                System.out.println("Введите сообщение:");
                msg = scanner.nextLine();
                if ("конец".equals(msg)) break;
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (IOException e) {
            System.out.println("Клиенту не удалось подключиться");
            e.printStackTrace();
        } finally {
            System.out.println("Клиент закрыт");
            try {
                socketChannel.close();
            } catch (IOException e) {
                System.out.println("Клиент не смог разорвать соединение");
                e.printStackTrace();
            }
        }
    }
}


