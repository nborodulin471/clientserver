package task2;

public class Main {

    public static void main(String[] args) {
        Thread server = new Thread(new Server(8081));
        server.start();

        Thread client = new Thread(new Client("127.0.0.1", 8081));
        client.start();

        try {
            client.join();
            server.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
