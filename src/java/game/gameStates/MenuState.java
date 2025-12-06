package game.gameStates;

import game.Game;
import game.GameMode;
import game.GameplayPanel;
import game.mode.ModeStrategyFactory;
import game.utils.KeyHandler;
import java.awt.*;

public class MenuState implements GameState {
    private Game game;
    private int modeIndex = 0;
    private GameMode[] modes = GameMode.values();

    public MenuState(Game game) { 
        this.game = game; 
    }
    

    @Override public void init() {}
    @Override public void update() {}

    @Override
    public void input(KeyHandler k) {
        if (k.k_left.isPressed) {
            modeIndex = (modeIndex - 1 + modes.length) % modes.length;
            GameMode chosen = modes[modeIndex];
            game.setGameMode(chosen);
        }
        if (k.k_right.isPressed) {
            modeIndex = (modeIndex + 1) % modes.length;
            GameMode chosen = modes[modeIndex];
            game.setGameMode(chosen);
        }

        // 엔터키 대신 편의상 UP 키로 시작 (KeyHandler 수정 없이 사용 가능)
        if (k.k_up.isPressed) game.setState(new PlayState(game));
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameplayPanel.width, GameplayPanel.height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("PRESS 'UP' TO START", 20, 240);
        g.drawString("Mode: " + modes[modeIndex].name(), 20, 280);
    }
}