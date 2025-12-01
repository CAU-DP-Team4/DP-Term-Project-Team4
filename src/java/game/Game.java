package game;

import game.entities.*;
import game.entities.ghosts.Blinky;
import game.entities.ghosts.Ghost;
import game.ghostFactory.*;
import game.ghostStates.EatenMode;
import game.ghostStates.FrightenedMode;
import game.utils.CollisionDetector;
import game.utils.CsvReader;
import game.utils.EntityFactory;
import game.utils.KeyHandler;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Classe gérant le jeu en lui même
public class Game implements Observer {
    private Map<String, EntityFactory> entityFactoryMap = new HashMap<>();

    //Pour lister les différentes entités présentes sur la fenêtre
    private List<Entity> objects = new ArrayList();
    private List<Ghost> ghosts = new ArrayList();
    private static List<Wall> walls = new ArrayList();

    private static Pacman pacman;
    private static Blinky blinky;

    private static boolean firstInput = false;

    public Game(){
        //Initialisation du jeu

        //Chargement du fichier csv du niveau
        List<List<String>> data = null;
        try {
            data = new CsvReader().parseCsv(getClass().getClassLoader().getResource("level/level.csv").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        int cellsPerRow = data.get(0).size();
        int cellsPerColumn = data.size();
        int cellSize = 8;

        CollisionDetector collisionDetector = new CollisionDetector(this);

        initializeFactoryRegistry(collisionDetector);

        //Le niveau a une "grille", et pour chaque case du fichier csv, on affiche une entité parculière sur une case de la grille selon le caracère présent
        for(int xx = 0 ; xx < cellsPerRow ; xx++) {
            for(int yy = 0 ; yy < cellsPerColumn ; yy++) {
                String dataChar = data.get(yy).get(xx);

                // 맵에 등록된 문자가 있으면 해당 엔티티 생성
                if (entityFactoryMap.containsKey(dataChar)) {
                    Entity entity = entityFactoryMap.get(dataChar).create(xx * cellSize, yy * cellSize);
                    if (entity != null) objects.add(entity);
                }
            }
        }
    }

    // level.csv의 각 문자를 엔티티 생성 함수에 매핑하는 메서드
    private void initializeFactoryRegistry(CollisionDetector collisionDetector) {
        // 벽 등록
        entityFactoryMap.put("x", (x, y) -> {
            Wall wall = new Wall(x, y);
            walls.add(wall);
            return wall;
        });

        // 유령 집 벽 등록
        entityFactoryMap.put("-", (x, y) -> {
            GhostHouse gh = new GhostHouse(x, y);
            walls.add(gh);
            return gh;
        });

        // 팩맨 등록 (부수 효과 처리: Observer 등록, CollisionDetector 설정)
        entityFactoryMap.put("P", (x, y) -> {
            pacman = new Pacman(x, y);
            pacman.setCollisionDetector(collisionDetector);
            pacman.registerObserver(GameLauncher.getUIPanel());
            pacman.registerObserver(this);
            return pacman;
        });

        // 팩껌 등록
        entityFactoryMap.put(".", (x, y) -> new PacGum(x, y));

        // 슈퍼팩껌 등록
        entityFactoryMap.put("o", (x, y) -> new SuperPacGum(x, y));

        // 유령 등록 (부수 효과 처리: ghosts 리스트 추가, blinky 변수 할당)
        entityFactoryMap.put("b", (x, y) -> createGhost("b", x, y));
        entityFactoryMap.put("p", (x, y) -> createGhost("p", x, y));
        entityFactoryMap.put("i", (x, y) -> createGhost("i", x, y));
        entityFactoryMap.put("c", (x, y) -> createGhost("c", x, y));
    }

    // 유령 생성 헬퍼 메서드 (람다 식 내부가 너무 복잡해지는 것을 방지)
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
            ghosts.add(ghost); // ghosts 리스트에 별도 추가
            if (type.equals("b")) {
                blinky = (Blinky) ghost; // blinky 변수 할당
            }
            return ghost;
        }
        return null;
    }

    public static List<Wall> getWalls() {
        return walls;
    }

    public List<Entity> getEntities() {
        return objects;
    }

    //Mise à jour de toutes les entités
    public void update() {
        for (Entity o: objects) {
            if (!o.isDestroyed()) o.update();
        }
    }

    //Gestion des inputs
    public void input(KeyHandler k) {
        pacman.input(k);
    }

    //Rendu de toutes les entités
    public void render(Graphics2D g) {
        for (Entity o: objects) {
            if (!o.isDestroyed()) o.render(g);
        }
    }

    public static Pacman getPacman() {
        return pacman;
    }
    public static Blinky getBlinky() {
        return blinky;
    }

    //Le jeu est notifiée lorsque Pacman est en contact avec une PacGum, une SuperPacGum ou un fantôme
    @Override
    public void updatePacGumEaten(PacGum pg) {
        pg.destroy(); //La PacGum est détruite quand Pacman la mange
    }

    @Override
    public void updateSuperPacGumEaten(SuperPacGum spg) {
        spg.destroy(); //La SuperPacGum est détruite quand Pacman la mange
        for (Ghost gh : ghosts) {
            gh.getState().superPacGumEaten(); //S'il existe une transition particulière quand une SuperPacGum est mangée, l'état des fantômes change
        }
    }

    @Override
    public void updateGhostCollision(Ghost gh) {
        if (gh.getState() instanceof FrightenedMode) {
            gh.getState().eaten(); //S'il existe une transition particulière quand le fantôme est mangé, son état change en conséquence
        }else if (!(gh.getState() instanceof EatenMode)) {
            System.out.println("Game over !\nScore : " + GameLauncher.getUIPanel().getScore()); //Quand Pacman rentre en contact avec un Fantôme qui n'est ni effrayé, ni mangé, c'est game over !
            System.exit(0); //TODO
        }
    }

    public static void setFirstInput(boolean b) {
        firstInput = b;
    }

    public static boolean getFirstInput() {
        return firstInput;
    }
}
