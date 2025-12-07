package game.gameStates;

import game.Game;
import game.GameLauncher;
import game.GameplayPanel;
import game.Observer;
import game.entities.*;
import game.entities.ghosts.*;
import game.ghostFactory.*;
import game.ghostStates.EatenMode;
import game.ghostStates.FrightenedMode;
import game.mode.ModeStrategy;
import game.mode.ModeStrategyFactory;
import game.utils.CollisionDetector;
import game.utils.CsvReader;
import game.utils.EntityFactory; // 사용자가 정의한 인터페이스라고 가정
import game.utils.KeyHandler;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayState implements GameState, Observer {

    private Game game;
    private ModeStrategy modeStrategy;
    private List<Entity> objects = new ArrayList<>();
    private List<Ghost> ghosts = new ArrayList<>();

    // 사용자가 구현한 Factory Registry 맵
    private Map<String, EntityFactory> entityFactoryMap = new HashMap<>();

    public PlayState(Game game) {
        this.game = game;
        init();
    }

    @Override
    public void init() {
        // 게임 시작 또는 재시작 시 점수 초기화
        GameLauncher.getUIPanel().resetScore();

        objects.clear();
        ghosts.clear();
        Game.getWalls().clear(); // Game의 static 벽 리스트 초기화

        // 레벨 데이터 로딩
        List<List<String>> data = null;
        try {
            data = new CsvReader().parseCsv(getClass().getClassLoader().getResource("level/level.csv").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        int cellSize = 8;
        int cellsPerRow = data.get(0).size();
        int cellsPerColumn = data.size();

        CollisionDetector collisionDetector = new CollisionDetector(game);

        // 팩토리 초기화 (사용자 코드 로직 이동)
        initializeFactoryRegistry(collisionDetector);
        
        this.modeStrategy = game.getModeStrategy();
        this.modeStrategy.onStart(game, this);

        // 맵 파싱 및 엔티티 생성
        for(int xx = 0 ; xx < cellsPerRow ; xx++) {
            for(int yy = 0 ; yy < cellsPerColumn ; yy++) {
                String dataChar = data.get(yy).get(xx);

                if (entityFactoryMap.containsKey(dataChar)) {
                    Entity entity = entityFactoryMap.get(dataChar).create(xx * cellSize, yy * cellSize);
                    if (entity != null) {
                        objects.add(entity);

                        // ghost나 pacman처럼 MovingEntity인 경우 시작 위치 설정
                        if(entity instanceof MovingEntity) {
                            ((MovingEntity) entity).setStartPosition(xx * cellSize, yy * cellSize);
                        }
                        
                    }
                }
            }
        }
    }

    // 사용자 코드의 initializeFactoryRegistry를 PlayState에 맞게 수정
    private void initializeFactoryRegistry(CollisionDetector collisionDetector) {
        // 벽 등록: Game의 static 리스트에 추가
        entityFactoryMap.put("x", (x, y) -> {
            Wall wall = new Wall(x, y);
            Game.getWalls().add(wall);
            return wall;
        });

        // 유령 집 등록
        entityFactoryMap.put("-", (x, y) -> {
            GhostHouse gh = new GhostHouse(x, y);
            Game.getWalls().add(gh);
            return gh;
        });

        // 팩맨 등록: Game의 static 변수 설정 및 Observer 등록
        entityFactoryMap.put("P", (x, y) -> {
            Pacman p = new Pacman(x, y);
            p.setCollisionDetector(collisionDetector);
            p.registerObserver(GameLauncher.getUIPanel());
            p.registerObserver(this); // PlayState가 Observer 역할 수행
            Game.setPacman(p); // Game의 static 변수 업데이트
            return p;
        });

        // 팩껌 등록
        entityFactoryMap.put(".", (x, y) -> new PacGum(x, y));

        // 슈퍼팩껌 등록
        entityFactoryMap.put("o", (x, y) -> new SuperPacGum(x, y));

        // 유령 등록
        entityFactoryMap.put("b", (x, y) -> createGhost("b", x, y));
        entityFactoryMap.put("p", (x, y) -> createGhost("p", x, y));
        entityFactoryMap.put("i", (x, y) -> createGhost("i", x, y));
        entityFactoryMap.put("c", (x, y) -> createGhost("c", x, y));
    }

    private Entity createGhost(String type, int x, int y) {
        AbstractGhostFactory factory = null;
        switch (type) {
            case "b": factory = new BlinkyFactory(); break;
            case "p": factory = new PinkyFactory(); break;
            case "i": factory = new InkyFactory(); break;
            case "c": factory = new ClydeFactory(); break;
        }

        if (factory != null) {
            Ghost ghost = factory.makeGhost(x, y);
            ghosts.add(ghost);
            if (type.equals("b")) {
                Game.setBlinky((Blinky) ghost); // Game의 static 변수 업데이트
            }
            return ghost;
        }
        return null;
    }

    @Override
    public void update() {
        for (Entity o: objects) {
            if (!o.isDestroyed()) o.update();
        }
    }

    @Override
    public void render(Graphics2D g) {
        for (Entity o: objects) {
            if (!o.isDestroyed()) o.render(g);
        }
    }

    @Override
    public void input(KeyHandler k) {
        Game.getPacman().input(k);
    }

    // --- Observer 구현 (Game.java에서 이동) ---

    @Override
    public void updatePacGumEaten(PacGum pg) {
        pg.destroy();
    }

    @Override
    public void updateSuperPacGumEaten(SuperPacGum spg) {
        spg.destroy();
        for (Ghost gh : ghosts) {
            gh.getState().superPacGumEaten();
        }
    }

    @Override
    public void updateGhostCollision(Ghost gh) {
        if (gh.getState() instanceof FrightenedMode) {
            gh.getState().eaten();
        } else if (!(gh.getState() instanceof EatenMode)) {
            if(game.getModeStrategy() != null) {
                game.getModeStrategy().onCollision(game, this, gh);
            }else{
                // 게임모드 미설정 시, Game Over으로 상태전환
                game.setState(new GameOverState(game));
            }

            
        }
        
    }

    // CollisionDetector 등에서 엔티티 리스트가 필요할 때 호출
    public List<Entity> getEntities() {
        return objects;
    }

    public void resetPositions() {
        // Pacman
        if (Game.getPacman() != null) {
            Game.getPacman().resetToStart();
        }

        // Ghost들
        for (Ghost g : ghosts) {
            if (g != null) {
                g.resetToStart();
                // 유령을 집 모드로 초기화해줘야 나올 수 있음.
                g.switchHouseMode();
            }
        }

        // 플레이가 재시작되면 첫 입력 기다리도록 재설정
        Game.setFirstInput(false);
    }

    public void updateModeUI(int lives) {
        GameLauncher.getUIPanel().updateLives(lives);
    }
}