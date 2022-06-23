package juego;

import java.awt.*;
import entorno.*;

public class Comida {
    private Rectangle rec;
    // private Image imagen;

    public Comida(int x, int y) {
        this.rec = new Rectangle(x, y, 25, 25);
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.rec.x, this.rec.y, this.rec.width, this.rec.height, 0, Color.GREEN);
    }

    public int getX() {
        return this.rec.x;
    }

    public int getY() {
        return this.rec.y;
    }

    public Rectangle getRec() {
        return this.rec;
    }
}