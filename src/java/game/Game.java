package game;

import game.entities.*;
import game.entities.ghosts.Blinky;
import game.entities.ghosts.Ghost;
import game.gameStates.GameState;
import game.gameStates.MenuState;
import game.gameStates.PlayState;
import game.ghostFactory.*;
import game.ghostStates.EatenMode;
import game.ghostStates.FrightenedMode;
import game.mode.ClassicModeStrategy;
import game.mode.ClassicModeStrategy;
import game.mode.ModeStrategy;
import game.mode.ModeStrategyFactory;
import game.utils.CollisionDetector;
import game.utils.CsvReader;
import game.utils.EntityFactory;
import game.utils.KeyHandler;
import game.GameMode;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Classe gérant le jeu en lui même
public class Game {
    private GameState currentState;

    private static Pacman pacman;
    private static Blinky blinky;
    private static List<Wall> walls = new ArrayList<>();

    private static boolean firstInput = false;

    
    private ModeStrategy modeStrategy;
    private GameMode gameMode;

    public Game(){
        // 초기 상태를 메뉴로 설정
        this.currentState = new MenuState(this);
        // 기본 게임 모드는 클래식 모드로 설정 (기본 팩맨게임 모드)
        this.gameMode = GameMode.CLASSIC;
        this.modeStrategy = new ClassicModeStrategy();
    }


    // --- State 위임 메서드 ---
    public void setState(GameState state) {
        this.currentState = state;
        this.currentState.init();
    }

    // 현재 상태(PlayState)의 엔티티 리스트를 반환 (CollisionDetector용)
    public List<Entity> getEntities() {
        if (currentState instanceof PlayState) {
            return ((PlayState) currentState).getEntities();
        }
        return new ArrayList<>();
    }

    public void update() {
        currentState.update();
    }

    public void render(Graphics2D g) {
        currentState.render(g);
    }

    public void input(KeyHandler k) {
        currentState.input(k);
    }

    public static List<Wall> getWalls() {
        return walls;
    }

    public static Pacman getPacman() {
        return pacman;
    }

    public static void setPacman(Pacman p) {
        pacman = p;
    }

    public static Blinky getBlinky() {
        return blinky;
    }

    public static void setBlinky(Blinky b) {
        blinky = b;
    }

    public static void setFirstInput(boolean b) {
        firstInput = b;
    }

    public static boolean getFirstInput() {
        return firstInput;
    }

    public ModeStrategy getModeStrategy() {
        return modeStrategy;
    }

    public GameMode getGameMode() { 
        return gameMode; 
    }

    public void setGameMode(GameMode mode) { 
        if (this.gameMode == mode) return;
        this.gameMode = mode; 
        this.modeStrategy = ModeStrategyFactory.getStrategy(mode);
    }
    
}
