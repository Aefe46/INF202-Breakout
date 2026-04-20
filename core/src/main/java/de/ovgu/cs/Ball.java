package de.ovgu.cs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball {

    public float x, y;
    public float speedX, speedY;
    public float radius;

    public Ball() {
        radius = 10;
        x = 400;        // Ekran ortası
        y = 300;        // Ekran ortası
        speedX = 220;   // Saniyede 220 pixel sağa
        speedY = -220;  // Saniyede 220 pixel aşağı
    }

    public void update(float delta) {
        x += speedX * delta; // Her frame konumu güncelle
        y += speedY * delta;
    }

    public void draw(ShapeRenderer sr) {
        // Dış halo (parlama efekti)
        sr.setColor(new Color(1f, 0.9f, 0.3f, 0.3f));
        sr.circle(x, y, radius + 5);

        // Topun ana rengi (sarı)
        sr.setColor(new Color(1f, 0.9f, 0.2f, 1f));
        sr.circle(x, y, radius);

        // Küçük parlak nokta
        sr.setColor(Color.WHITE);
        sr.circle(x - 3, y + 3, 3);
    }
}