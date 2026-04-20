package de.ovgu.cs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    private ShapeRenderer sr;
    private SpriteBatch batch;
    private BitmapFont font;

    private Paddle paddle;
    private Ball ball;
    private Brick[] bricks;

    private int lives = 3;
    private int score = 0;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean paused = false; // YENİ: Pause durumu

    private static final Color[] ROW_COLORS = {
        new Color(1f, 0.3f, 0.3f, 1f),
        new Color(1f, 0.6f, 0.1f, 1f),
        new Color(1f, 1f, 0.2f, 1f),
        new Color(0.3f, 1f, 0.4f, 1f),
        new Color(0.3f, 0.7f, 1f, 1f),
    };

    @Override
    public void create() {
        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        setupGame();
    }

    private void setupGame() {
        paddle = new Paddle();
        ball = new Ball();
        lives = 3;
        score = 0;
        gameOver = false;
        gameWon = false;
        paused = false;

        bricks = new Brick[25];
        int index = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                float x = 45 + col * 142;
                float y = 530 - row * 50;
                bricks[index] = new Brick(x, y, ROW_COLORS[row]);
                index++;
            }
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // GAME OVER ekrani
        if (gameOver) {
            batch.begin();
            font.setColor(Color.RED);
            font.draw(batch, "GAME OVER!", 270, 350);
            font.setColor(Color.WHITE);
            font.draw(batch, "Score: " + score, 330, 300);
            font.draw(batch, "LEERTASTE = Neustart", 220, 250);
            batch.end();
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) setupGame();
            return;
        }

        // KAZANDIN ekrani
        if (gameWon) {
            batch.begin();
            font.setColor(Color.YELLOW);
            font.draw(batch, "GEWONNEN!", 290, 380);
            font.setColor(Color.WHITE);
            font.draw(batch, "Score: " + score, 330, 320);
            font.draw(batch, "LEERTASTE = Neustart", 220, 260);
            batch.end();
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) setupGame();
            return;
        }

        // P tusu: pause ac/kapat
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }

        // PAUSE ekrani
        if (paused) {
            drawGame();
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(new Color(0f, 0f, 0f, 0.55f));
            sr.rect(0, 0, 800, 600);
            sr.end();
            batch.begin();
            font.getData().setScale(2f);
            font.setColor(Color.WHITE);
            font.draw(batch, "PAUSIERT", 300, 340);
            font.getData().setScale(1.4f);
            font.setColor(new Color(0.7f, 0.7f, 0.7f, 1f));
            font.draw(batch, "P = Weiterspielen", 265, 285);
            font.getData().setScale(2f);
            batch.end();
            return;
        }

        // === OYUN GUNCELLEME ===
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  paddle.x -= 320 * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) paddle.x += 320 * delta;
        if (paddle.x < 0) paddle.x = 0;
        if (paddle.x + paddle.width > 800) paddle.x = 800 - paddle.width;

        ball.update(delta);

        if (ball.x - ball.radius < 0 || ball.x + ball.radius > 800) ball.speedX *= -1;
        if (ball.y + ball.radius > 600) ball.speedY *= -1;

        if (ball.y - ball.radius < paddle.y + paddle.height
                && ball.y - ball.radius > paddle.y
                && ball.x > paddle.x
                && ball.x < paddle.x + paddle.width
                && ball.speedY < 0) {
            ball.speedY *= -1;
            float hitPos = (ball.x - paddle.x) / paddle.width;
            ball.speedX = (hitPos - 0.5f) * 600;
        }

        if (ball.y < 0) {
            lives--;
            if (lives <= 0) {
                gameOver = true;
            } else {
                ball.x = 400; ball.y = 300;
                ball.speedX = 220; ball.speedY = -220;
            }
        }

        boolean allDestroyed = true;
        for (Brick brick : bricks) {
            if (!brick.isAlive) continue;
            allDestroyed = false;
            if (ball.x + ball.radius > brick.x
                    && ball.x - ball.radius < brick.x + brick.width
                    && ball.y + ball.radius > brick.y
                    && ball.y - ball.radius < brick.y + brick.height) {
                brick.isAlive = false;
                ball.speedY *= -1;
                score += 10;
            }
        }
        if (allDestroyed) gameWon = true;

        drawGame();
    }

    private void drawGame() {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0.3f, 0.3f, 0.5f, 1f));
        sr.rect(0, 570, 800, 2);
        for (Brick brick : bricks) brick.draw(sr);
        paddle.draw(sr);
        ball.draw(sr);
        for (int i = 0; i < lives; i++) {
            sr.setColor(Color.RED);
            sr.circle(20 + i * 30, 580, 8);
        }
        sr.end();

        batch.begin();
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        font.draw(batch, "Score: " + score, 10, 615);
        font.draw(batch, "Leben: ", 620, 615);
        font.getData().setScale(2f);
        batch.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
        batch.dispose();
        font.dispose();
    }
}