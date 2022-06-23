package juego;

import java.awt.*;
import entorno.*;

public class Serpiente {
    private Rectangle[] cuerpo;
    private String direccion;
    private int velocidad;
    
    public Serpiente(int x, int y) {
        this.cuerpo = new Rectangle[1];
        this.cuerpo[0] = new Rectangle(x, y, 25, 25);

        this.direccion = "";
        this.velocidad = 1;
    }

    public void dibujar(Entorno entorno) {
        for (int i = 1; i < this.cuerpo.length; i++) {
            if (i % 2 == 0) {  
                entorno.dibujarRectangulo(this.cuerpo[i].x, this.cuerpo[i].y, this.cuerpo[i].width, this.cuerpo[i].height, 0, Color.ORANGE);
            } else {
                entorno.dibujarRectangulo(this.cuerpo[i].x, this.cuerpo[i].y, this.cuerpo[i].width, this.cuerpo[i].height, 0, Color.YELLOW);
            }
        }

        entorno.dibujarRectangulo(this.cuerpo[0].x, this.cuerpo[0].y, this.cuerpo[0].width, this.cuerpo[0].height, 0, Color.RED);
    }

    public void mover() {
        for (int i = this.cuerpo.length - 1; i > 0; i--) {
            this.cuerpo[i].x = this.cuerpo[i - 1].x;
            this.cuerpo[i].y = this.cuerpo[i - 1].y;
        }

        switch (this.direccion) {
            case "arriba": this.cuerpo[0].y -= this.cuerpo[0].height * this.velocidad; break;
            case "abajo": this.cuerpo[0].y += this.cuerpo[0].height * this.velocidad; break;
            case "izquierda": this.cuerpo[0].x -= this.cuerpo[0].width * this.velocidad; break;
            case "derecha": this.cuerpo[0].x += this.cuerpo[0].width * this.velocidad; break;
        }
    }

    public void crecer() {
        Rectangle[] nuevoCuerpo = new Rectangle[this.cuerpo.length + 1];

        for (int i = 0; i < this.cuerpo.length; i++) {
            nuevoCuerpo[i] = this.cuerpo[i];
        }

        nuevoCuerpo[this.cuerpo.length] = new Rectangle(this.cuerpo[this.cuerpo.length - 1].x, this.cuerpo[this.cuerpo.length - 1].y, this.cuerpo[this.cuerpo.length - 1].width, this.cuerpo[this.cuerpo.length - 1].height);
        this.cuerpo = nuevoCuerpo;
    }

    public int getX() {
        return this.cuerpo[0].x;
    }

    public int getY() {
        return this.cuerpo[0].y;
    }

    public int getAncho() {
        return this.cuerpo[0].width;
    }

    public int getAlto() {
        return this.cuerpo[0].height;
    }

    public Rectangle getRec(int pos) {
        return this.cuerpo[pos];
    }

    public Rectangle[] getRecs() {
        return this.cuerpo;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public int getVelocidad() {
        return this.velocidad;
    }

    public int getLongitud() {
        return this.cuerpo.length;
    }

    public void setDireccion(String dir) {
        if (dir != "arriba" && dir != "abajo" && dir != "izquierda" && dir != "derecha") {
            throw new IllegalArgumentException("Direccion no valida");
        }

        this.direccion = dir;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
}