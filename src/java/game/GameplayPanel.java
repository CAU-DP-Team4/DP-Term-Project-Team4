package game;

import game.utils.KeyHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

//Panneau de la "zone de jeu"
public class GameplayPanel extends JPanel implements Runnable {
    public static int width;
    public static int height;
    private Thread thread;
    private boolean running = false;

    private BufferedImage img;
    private Graphics2D g;
    private Image backgroundImage;

    private KeyHandler key;

    private Game game;

    public GameplayPanel(int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();
        // 이미지 경로 로딩 방식
        backgroundImage = ImageIO.read(getClass().getResource("/img/background.png"));
    }

    @Override
    public void addNotify() {
        super.addNotify();

        if (thread == null) {
            thread = new Thread(this, "GameThread");
            thread.start();
        }
    }

    //initialisation du jeu
    public void init() {
        running = true;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) img.getGraphics();

        key = new KeyHandler(this);

        game = new Game();
    }

    //mise à jour du jeu
    public void update() {
        game.update();
    }

    //gestion des inputs
    public void input(KeyHandler key) {
        game.input(key);
    }

    //버퍼 이미지(img)에 게임 요소들을 그립니다.
    public void render() {
        if (g != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, null);
            game.render(g);
        }
    }

    // 직접 그리는 대신 Swing에게 "다시 그려달라"고 요청만 합니다.
    public void draw() {
        repaint();
    }

    // Swing이 적절한 타이밍(EDT 스레드)에 호출하여 화면에 실제로 그리는 메서드입니다.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 이전 잔상을 지우기 위해 필수
        if (img != null) {
            // render()에서 미리 그려둔 버퍼 이미지를 화면에 복사
            g.drawImage(img, 0, 0, width, height, null);
        }
        // 맥OS 애니메이션 부드러움을 위해 그래픽 동기화
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void run() {
        init();

        //Pour faire en sorte que le jeu tourne à 60FPS (tutoriel consulté : https://www.youtube.com/watch?v=LhUN3EKZiio)
        final double GAME_HERTZ = 60.0;
        final double TBU = 1000000000 / GAME_HERTZ; //Time before update

        final int MUBR = 5; // Must update before render

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime;

        final double TARGET_FPS = 60.0;
        final double TTBR = 1000000000 / TARGET_FPS; //Total time before render

        int frameCount = 0;
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);
        int oldFrameCount = 0;

        while (running) {
            double now = System.nanoTime();
            int updateCount = 0;
            while ((now - lastUpdateTime) > TBU && (updateCount < MUBR)) {
                input(key);
                update();
                lastUpdateTime += TBU;
                updateCount++;
            }

            if (now - lastUpdateTime > TBU) {
                lastUpdateTime = now - TBU;
            }

            render(); // 버퍼에 그림
            draw();   // 화면 갱신 요청 (repaint -> paintComponent)

            lastRenderTime = now;
            frameCount++;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime) {
                if (frameCount != oldFrameCount) {
                    //System.out.println("FPS : " + frameCount);
                    oldFrameCount = frameCount;
                }
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while ((now - lastRenderTime < TTBR) && (now - lastUpdateTime < TBU)) {
                Thread.yield();

                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    System.err.println("ERROR yielding thread");
                }

                now = System.nanoTime();
            }
        }
    }
}