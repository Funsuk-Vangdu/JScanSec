package forensics;

import java.net.*;
import java.util.*;
import java.io.*;

public class PortScanner implements ForensicAnalyzer {

    @Override
    public void analyze() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter target host (e.g., 127.0.0.1): ");
        String host = sc.nextLine();

        System.out.print("Enter start port: ");
        int startPort = sc.nextInt();

        System.out.print("Enter end port: ");
        int endPort = sc.nextInt();
        sc.nextLine();

        List<String> scanResults = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();

        for (int port = startPort; port <= endPort; port++) {
            Thread t = new PortCheckThread(host, port, scanResults);
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        System.out.print("Do you want to save the results to a file? (yes/no): ");
        String saveChoice = sc.nextLine();

        if (saveChoice.equalsIgnoreCase("yes")) {
            System.out.print("Enter file name: ");
            String filename = sc.nextLine().trim();
            saveToFile(filename, scanResults);
        }
    }

    public static void main(String[] args) {
        new PortScanner().analyze();
    }

    static String getServiceName(int port) {
        return switch (port) {
            case 20 -> "FTP Data";
            case 21 -> "FTP Control";
            case 22 -> "SSH";
            case 23 -> "Telnet";
            case 25 -> "SMTP";
            case 53 -> "DNS";
            case 80 -> "HTTP";
            case 110 -> "POP3";
            case 143 -> "IMAP";
            case 443 -> "HTTPS";
            case 3306 -> "MySQL";
            case 3389 -> "RDP";
            default -> "Unknown Service";
        };
    }

    static void saveToFile(String filename, List<String> results) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String line : results) {
                writer.write(line + "\n");
            }
            System.out.println("💾 Scan results saved to: " + filename);
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage());
        }
    }
}

class PortCheckThread extends Thread {
    private final String host;
    private final int port;
    private final List<String> scanResults;

    PortCheckThread(String host, int port, List<String> scanResults) {
        this.host = host;
        this.port = port;
        this.scanResults = scanResults;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 300);
            String result =
                    "Port " + port + " is open (" +
                    PortScanner.getServiceName(port) + ")";
            System.out.println(result);
            scanResults.add(result);
        } catch (Exception ignored) {
        }
    }
}
