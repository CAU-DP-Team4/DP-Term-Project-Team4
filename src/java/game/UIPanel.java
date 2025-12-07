package game;

import game.entities.PacGum;
import game.entities.SuperPacGum;
import game.entities.ghosts.Ghost;
import game.ghostStates.FrightenedMode;

import javax.swing.*;
import java.awt.*;

//Panneau de l'interface utilisateur
public class UIPanel extends JPanel implements Observer {
    public static int width;
    public static int height;

    private int score = 0;
    private JLabel scoreLabel;
    private JLabel modeLabel;

    public UIPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(20.0F));
        scoreLabel.setForeground(Color.white);
        this.add(scoreLabel, BorderLayout.WEST);

        // 확장모드 UI 설정
        modeLabel = new JLabel("");
        modeLabel.setFont(scoreLabel.getFont().deriveFont(20.0F));
        modeLabel.setForeground(Color.white);
        this.add(modeLabel, BorderLayout.EAST);
    }

    public void updateLives(int lives) {
        this.modeLabel.setText("Lives: " + lives);
    }

    public void updateScore(int incrScore) {
        this.score += incrScore;
        this.scoreLabel.setText("Score: " + score);
    }

    // 게임 시작 또는 재시작 시 점수 초기화
    public void resetScore() {
        this.score = 0;
        this.scoreLabel.setText("Score: " + score);
    }

    public int getScore() {
        return score;
    }

    //L'interface est notifiée lorsque Pacman est en contact avec une PacGum, une SuperPacGum ou un fantôme, et on met à jour le score affiché en conséquence
    @Override
    public void updatePacGumEaten(PacGum pg) {
        updateScore(10);
    }

    @Override
    public void updateSuperPacGumEaten(SuperPacGum spg) {
        updateScore(100);
    }

    @Override
    public void updateGhostCollision(Ghost gh) {
        if (gh.getState() instanceof FrightenedMode) { //Dans le cas où Pacman est en contact avec un fantôme on ne met à jour le score que lorsque ce dernier est en mode "frightened"
            int scoreMultiplier = gh.getScoreMultiplier();
            updateScore(500 * scoreMultiplier);
        }
    }
}
