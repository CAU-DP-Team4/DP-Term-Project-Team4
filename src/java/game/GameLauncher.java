package game;

import game.rmi.ScoreManager;

import javax.swing.*;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//Point d'entrée de l'application
public class GameLauncher {
    private static UIPanel uiPanel;

    public static void main(String[] args) {
        // --- [RMI Server Setup Start] ---
        try {
            // 1. RMI 레지스트리 생성
            Registry registry = LocateRegistry.createRegistry(54321);

            // 2. 서비스 객체 생성 (Singleton)
            ScoreManager scoreManager = ScoreManager.getInstance();

            // 3. "Leaderboard"라는 이름으로 서비스 바인딩
            registry.rebind("Leaderboard", scoreManager);

            System.out.println("RMI Server is ready.");
        } catch (Exception e) {
            System.err.println("RMI Server failed: " + e.toString());
            e.printStackTrace();
        }
        // --- [RMI Server Setup End] ---

        JFrame window = new JFrame();
        window.setTitle("Pacman");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel gameWindow = new JPanel();

        //Création de la "zone de jeu"
        try {
            gameWindow.add(new GameplayPanel(448,496));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Création de l'UI (pour afficher le score)
        uiPanel = new UIPanel(256,496);
        gameWindow.add(uiPanel);

        window.setContentPane(gameWindow);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static UIPanel getUIPanel() {
        return uiPanel;
    }
}
