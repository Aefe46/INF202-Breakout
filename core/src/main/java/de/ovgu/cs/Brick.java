package de.ovgu.cs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Brick {

    public float x, y, width, height;
    public boolean isAlive;
    public Color color; // Her tuğlanın rengi

    public Brick(float x, float y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        width = 80;
        height = 25;
        isAlive = true;
    }

    public void draw(ShapeRenderer sr) {
        if (!isAlive) return;

        // Tuğlanın ana rengini çiz
        sr.setColor(color);
        sr.rect(x, y, width, height);

        // Tuğlanın kenar çizgisi (daha koyu renk)
        sr.setColor(color.r * 0.6f, color.g * 0.6f, color.b * 0.6f, 1f);
        sr.rect(x, y, width, 2);          // alt kenar
        sr.rect(x, y + height - 2, width, 2); // üst kenar
        sr.rect(x, y, 2, height);          // sol kenar
        sr.rect(x + width - 2, y, 2, height); // sağ kenar
    }
}