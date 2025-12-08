package client;

import game.rmi.ScoreService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class LeaderboardViewer {
    public static void main(String[] args) {
        try {
            // 1. RMI 레지스트리 조회
            Registry registry = LocateRegistry.getRegistry("localhost", 54321);

            // 2. 원격 객체(Proxy) 조회 ("Leaderboard" 이름으로 찾음)
            ScoreService stub = (ScoreService) registry.lookup("Leaderboard");

            System.out.println("=== PAC-MAN Global Leaderboard (via RMI) ===");

            while (true) {
                System.out.println("\nfetching data...");

                // 3. 원격 메서드 호출
                List<String> scores = stub.getTopScores();

                if (scores.isEmpty()) {
                    System.out.println("No records yet.");
                } else {
                    for (String s : scores) {
                        System.out.println(s);
                    }
                }

                System.out.println("--------------------------------------------");
                System.out.println("Press Enter to refresh (or type 'q' to quit):");

                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if ("q".equalsIgnoreCase(input)) break;
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            System.out.println("\nEnsure the Pac-Man Game is running first!");
        }
    }
}