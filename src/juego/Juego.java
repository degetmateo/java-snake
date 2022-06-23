package juego;

import java.awt.*;
import entorno.*;
import media.*;

import javax.sound.sampled.Clip;

public class Juego extends InterfaceJuego {
	private Entorno entorno;
	private String estado;

	// private Image imagenInicio;
	// private Image imagenJuego;
	// private Image imagenDerrota;
	// private Image imagenVictoria;

	private Clip sonidoCrash;
	private Clip sonidoComer;
	
	private Serpiente serpiente;
	private int esperaMovimiento;
	private int restoDificultad;
	private int puntos;
	private Comida comida;

	public Juego() {
		this.entorno = new Entorno(this, "Juego de la Serpiente - PUNTOS: 0", 400, 400);
		this.estado = "inicio";

		this.sonidoCrash = Sonido.cargarSonido("crash.wav");
		this.sonidoComer = Sonido.cargarSonido("comer.wav");

		this.serpiente = new Serpiente(25 + (25 / 2), 25 + (25 / 2));
		this.esperaMovimiento = 60;
		this.restoDificultad = 10;
		this.puntos = 0;

		this.generarComida();
		
		// this.imagenInicio = Herramientas.cargarImagen("img/imagen-inicio.png");
		// this.imagenJuego = Herramientas.cargarImagen("img/imagen-juego.png");
		// this.imagenDerrota = Herramientas.cargarImagen("img/imagen-derrota.png");
		// this.imagenVictoria = Herramientas.cargarImagen("img/imagen-victoria.png");

		this.entorno.iniciar();
	}

	public void tick() {
		// Dependiendo del valor de la variable de instancia estado, se ejecutará un método diferente.
		// Cada método dibuja y ejecuta la lógica de una pantalla distinta.
		switch (this.estado) {
			case "inicio": this.dibujarInicio(); break;
			case "juego": this.dibujarJuego(); break;
			case "pausa": this.dibujarPausa(); break;
			case "derrota": this.dibujarDerrota(); break;
			case "victoria": this.dibujarVictoria(); break;
		}
	}

	public void dibujarInicio() {
		// this.entorno.dibujarImagen(this.imagenInicio, this.entorno.ancho() / 2, this.entorno.alto() / 2, 0, 1);

		this.entorno.cambiarFont("Arial", 16, Color.WHITE);
		this.entorno.escribirTexto("Presione ENTER para comenzar", 20, 30);
		this.entorno.escribirTexto("Durante el juego, presione ENTER para pausarlo", 20, 60);

		if (this.entorno.sePresiono(this.entorno.TECLA_ENTER)) {
			this.estado = "juego";
		}
	}

	public void dibujarPausa() {
		this.entorno.cambiarFont("Arial", 16, Color.WHITE);
		this.entorno.escribirTexto("El juego está pausado", 20, 30);
		this.entorno.escribirTexto("Presione ENTER para continuar", 20, 60);

		if (this.entorno.sePresiono(this.entorno.TECLA_ENTER)) {
			this.estado = "juego";
		}
	}

	public void dibujarJuego() {
		// this.entorno.dibujarImagen(this.imagenJuego, 400, 300, 0, 1);

		if (this.entorno.sePresiono(this.entorno.TECLA_ENTER)) {
			this.estado = "pausa";
		}

		this.serpiente.dibujar(this.entorno);
		this.comida.dibujar(this.entorno);

		this.controlarSerpiente();

		if (this.esperaMovimiento == 0) {
			this.serpiente.mover();
			this.esperaMovimiento = 60 - this.restoDificultad;
	
			if (this.existeColision(this.serpiente.getRec(0), this.comida.getRec())) {
				this.serpiente.crecer();
				this.generarComida();
				this.puntos++;
	
				if (this.restoDificultad < 50) {
					this.restoDificultad += 2;
				}

				Sonido.ejecutarSonido(this.sonidoComer);
			}

			if (this.existeColisionBorde(this.serpiente.getRec(0))) {
				this.estado = "derrota";
				Sonido.ejecutarSonido(this.sonidoCrash);
			}
	
			for (int i = 2; i < this.serpiente.getLongitud(); i++) {
				if (this.existeColision(this.serpiente.getRec(0), this.serpiente.getRec(i))) {
					this.estado = "derrota";
					Sonido.ejecutarSonido(this.sonidoCrash);
				}
			}
		}

		this.esperaMovimiento--;
		this.entorno.setTitle("Juego de la Serpiente - PUNTOS: " + this.puntos);
	}

	public void dibujarDerrota() {
		// this.entorno.dibujarImagen(this.imagenDerrota, 400, 300, 0, 1);

		this.entorno.cambiarFont("Arial", 16, Color.WHITE);
		this.entorno.escribirTexto("Derrota", 20, 30);
		this.entorno.escribirTexto("Tu serpiente ha obtenido una longitud de " + this.puntos, 20, 60);
		this.entorno.escribirTexto("Presione ENTER para volver a jugar", 20, 90);

		if (this.entorno.sePresiono(this.entorno.TECLA_ENTER)) {
			this.reiniciar();
			this.estado = "juego";
		}
	}

	public void dibujarVictoria() {
		// this.entorno.dibujarImagen(this.imagenVictoria, 400, 300, 0, 1);

		if (this.entorno.sePresiono(this.entorno.TECLA_ENTER)) {
			this.reiniciar();
			this.estado = "juego";
		}
	}

	// Obtener un numero entero al azar en un rango
	public int getRandom(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}

	// Método que comprueba si hay una colisión entre dos rectangulos.
	public boolean existeColision(Rectangle a, Rectangle b) {
		return a.x + a.width / 2 > b.x - b.width / 2 &&
			   a.x - a.width / 2 < b.x + b.width / 2 &&
			   a.y + a.height / 2 > b.y - b.height / 2 &&
			   a.y - a.height / 2 < b.y + b.height / 2;
	}

	// Método que comprueba si hay una colisión entre un rectangulo y el borde de la pantalla.
	public boolean existeColisionBorde(Rectangle rec) {
		return rec.x - rec.width / 2 < 0 || rec.x + rec.width / 2 > this.entorno.ancho() ||
			   rec.y - rec.height / 2 < 0 || rec.y + rec.height / 2 > this.entorno.alto();
	}

	public void escucharSonido(String nombre) {

	}

	public void reiniciar() {
		this.serpiente = new Serpiente(this.serpiente.getAncho() / 2, this.serpiente.getAlto() / 2);
		this.esperaMovimiento = 60;
		this.restoDificultad = 0;
		this.puntos = 0;
		this.generarComida();
	}

	public void controlarSerpiente() {
		String dir = "";

		if (this.serpiente.getLongitud() > 1) {
			dir = this.serpiente.getDireccion();
		}

		if ((this.entorno.sePresiono(this.entorno.TECLA_ARRIBA) || this.entorno.sePresiono('w')) && dir != "abajo") {
			this.serpiente.setDireccion("arriba");
		}

		if ((this.entorno.sePresiono(this.entorno.TECLA_ABAJO) || this.entorno.sePresiono('s')) && dir != "arriba") {
			this.serpiente.setDireccion("abajo");
		}

		if ((this.entorno.sePresiono(this.entorno.TECLA_IZQUIERDA) || this.entorno.sePresiono('a')) && dir != "derecha") {
			this.serpiente.setDireccion("izquierda");
		}

		if ((this.entorno.sePresiono(this.entorno.TECLA_DERECHA) || this.entorno.sePresiono('d')) && dir != "izquierda") {
			this.serpiente.setDireccion("derecha");
		}
	}

	public void generarComida() {
		int rangoX = this.entorno.ancho() / 25;
		int rangoY = this.entorno.alto() / 25;

		int x = (int) (this.getRandom(1, rangoX) * 25) + 25 / 2;
		int y = (int) (this.getRandom(1, rangoY) * 25) + 25 / 2;

		this.comida = new Comida(x, y);

		if (this.existeColisionBorde(this.comida.getRec())) {
			this.generarComida();
			return;
		}

		for (int i = 0; i < this.serpiente.getLongitud(); i++) {
			if (this.existeColision(this.serpiente.getRec(i), this.comida.getRec())) {
				this.generarComida();
				return;
			}
		}
	}
}