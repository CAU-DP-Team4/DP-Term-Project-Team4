package game.gameStates;

import game.Game;
import game.GameLauncher;
import game.GameplayPanel;
import game.rmi.ScoreManager;
import game.utils.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class GameOverState implements GameState {
    private Game game;
    private boolean scoreSaved = false; // 점수 저장 여부 플래그

    public GameOverState(Game game) {
        this.game = game;
    }

    @Override
    public void init() {
        // 점수 저장 로직 실행
        saveScoreProcess();
    }

    private void saveScoreProcess() {
        int finalScore = GameLauncher.getUIPanel().getScore();

        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(null,
                    "GAME OVER!\nYour Score: " + finalScore + "\nEnter your name:",
                    "Register High Score",
                    JOptionPane.PLAIN_MESSAGE);

            if (name != null && !name.trim().isEmpty()) {
                try {
                    // RMI Leaderboard에 점수 등록
                    ScoreManager.getInstance().addScore(name, finalScore);
                    scoreSaved = true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to connect to Leaderboard server.");
                }
            }
        });
    }

    @Override
    public void update() {}

    @Override
    public void input(KeyHandler k) {
        if (k.k_up.isPressed) game.setState(new PlayState(game)); // 재시작
    }

    @Override
    public void render(Graphics2D g) {
        int finalScore = GameLauncher.getUIPanel().getScore();

        // 배경 (반투명 검정색으로 덮어씌우기 효과)
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameplayPanel.width, GameplayPanel.height);

        // 텍스트 설정
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String gameOverText = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(gameOverText);
        g.drawString(gameOverText, (GameplayPanel.width - textWidth) / 2, 200);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreText = "Score: " + finalScore;
        textWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (GameplayPanel.width - textWidth) / 2, 240);

        // 점수 저장 완료 메시지
        if (scoreSaved) {
            g.setColor(Color.GREEN);
            String savedText = "Score Saved to Leaderboard!";
            textWidth = g.getFontMetrics().stringWidth(savedText);
            g.drawString(savedText, (GameplayPanel.width - textWidth) / 2, 280);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        String restartText = "Press 'UP' or 'Z' to Restart";
        textWidth = g.getFontMetrics().stringWidth(restartText);
        g.drawString(restartText, (GameplayPanel.width - textWidth) / 2, 350);
    }
}