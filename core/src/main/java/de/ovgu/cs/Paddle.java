package de.ovgu.cs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle {

    public float x, y, width, height;

    public Paddle() {
        width = 100;
        height = 15;
        x = 400 - width / 2; // Ekranın ortasına koy
        y = 30;               // Ekranın altına yakın
    }

    public void draw(ShapeRenderer sr) {
        // Açık mavi renk
        sr.setColor(new Color(0.3f, 0.7f, 1f, 1f));
        sr.rect(x, y, width, height);

        // Üst parlak şerit (3D efekti)
        sr.setColor(new Color(0.6f, 0.9f, 1f, 1f));
        sr.rect(x, y + height - 4, width, 4);
    }
}