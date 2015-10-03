package com.davidguerrero.asteroids;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.test.MoreAsserts;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

public class VistaJuego extends SurfaceView {

	public interface GameEventListener {

		/**
		 * Called when a collision happened.
		 */
		public void onGameOver();

	}

	private GameEventListener gameListener;
	private View view;

	private boolean scoreUpdate = false;

	public void setGameListener(GameEventListener listener) {
		this.gameListener = listener;
	}

	Vibrator v;

	private Grafic nave;
	private Grafic navePeque;
	private int giroNave;
	private double aceleracionNave;

	private boolean finAcelerar = true;

	private double result = 0;

	private Grafic misil;
	private static int PASO_VELOCIDAD_MISIL = 12;
	private static final int PASO_GIRO_NAVE = 5;
	private static final float PASO_ACELERACION_NAVE = 0.5f;
	private Drawable drawableMisil;

	private Drawable drawableExplossio;
	private Drawable drawableAsteroidemig;
	private Drawable drawableAsteroidePetit;
	private Drawable drawableAsteroideNivel;
	private Drawable drawableNave;
	private Drawable drawableNaveVidas;
	private Grafic asteroidemig;
	private Grafic asteroidePetit;

	private Vector<Grafic> Asteroides;
	private int numAsteroides = 4;
	private int level;
	private int numVidas = 4;
	private int novaVida;
	private boolean estoyMuerto = false;
	private boolean noColision = false;
	private boolean ahoraPintaVidas = true;
	private int nuevaColision = 0;
	private Grafic asteroideNivel;

	private ThreadJoc thread;
	private static int PERIODO_PROCESO = 20;
	private long ultimoProceso = 0;

	private int ang = 0;
	private Vector<Grafic> Misiles = new Vector<Grafic>();
	private Vector<Grafic> vidas = new Vector<Grafic>();

	private static int puntuacion;

	private Context context;

	public VistaJuego(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		thread = new ThreadJoc();
		puntuacion = 0;
		level = 1;

		Drawable drawableAsteroide;
		view = this;

		v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

		drawableNave = context.getResources().getDrawable(
				R.drawable.rocket);
		nave = new Grafic(this, drawableNave, 0);

		drawableMisil = context.getResources().getDrawable(R.drawable.laser_rojo);

		drawableExplossio = context.getResources().getDrawable(
				R.drawable.explosion);
		drawableAsteroidemig = context.getResources().getDrawable(
				R.drawable.asteroidemig);
		drawableAsteroidePetit = context.getResources().getDrawable(
				R.drawable.asteroidpetit);
		drawableAsteroide = context.getResources().getDrawable(
				R.drawable.asteroidef);
		drawableAsteroideNivel = context.getResources().getDrawable(
				R.drawable.asteroidef);
		drawableNaveVidas = context.getResources().getDrawable(
				R.drawable.rocket_peque);
		Asteroides = new Vector<Grafic>();
		Grafic asteroide;
		for (int i = 0; i < numAsteroides; i++) {
			asteroide = new Grafic(this, drawableAsteroide, 1);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setIncY(Math.random() * 4 - 2);

			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroide);
		}

	}

	public void restart() {
		init();
	}

	public void giroDerecha() {
		ang -= 20;
		nave.setAngulo(ang);
	}

	public void giroIzquierda() {
		ang += 20;
		nave.setAngulo(ang);
	}

	public void acelera() {
		aceleracionNave = 10;
		finAcelerar = false;
	}

	public void dispara() {
		activaMisil();
	}

	private void destruyeAsteroide(int j, int i) {
		subdividirAsteroide(j);
		Asteroides.remove(j);
	}

	private void subdividirAsteroide(int j) {
		double x = Asteroides.get(j).getPosX();
		double y = Asteroides.get(j).getPosY();
		if (Asteroides.get(j).getTipus() == 1) {
			for (int k = 0; k < 4; ++k) {
				asteroidemig = new Grafic(this, drawableAsteroidemig, 2);
				asteroidemig.setPosX(x + Math.random() / 4);
				asteroidemig.setPosY(y + Math.random() / 4);
				asteroidemig.setIncX(Math.random() * 4 - 2);
				asteroidemig.setIncY(Math.random() * 4 - 2);

				asteroidemig.setAngulo((int) (Math.random() * 360));
				asteroidemig.setRotacion((int) (Math.random() * 8 - 4));
				Asteroides.add(asteroidemig);
			}
		}
		if (Asteroides.get(j).getTipus() == 2) {
			for (int k = 0; k < 2; ++k) {
				asteroidePetit = new Grafic(this, drawableAsteroidePetit, 3);
				asteroidePetit.setPosX(x + Math.random() / 4);
				asteroidePetit.setPosY(y + Math.random() / 4);
				asteroidePetit.setIncX(Math.random() * 4 - 2);
				asteroidePetit.setIncY(Math.random() * 4 - 2);

				asteroidePetit.setAngulo((int) (Math.random() * 360));
				asteroidePetit.setRotacion((int) (Math.random() * 8 - 4));
				Asteroides.add(asteroidePetit);
			}
		}
	}

	private void activaMisil() {
		misil = new Grafic(this, drawableMisil, 0);

		misil.setPosX(nave.getPosX() + nave.getAncho() / 2 - misil.getAncho()
				/ 2);
		misil.setPosY(nave.getPosY() + nave.getAlto() / 2 - misil.getAlto() / 2);
		misil.setAngulo(nave.getAngulo());
		misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo()))
				* PASO_VELOCIDAD_MISIL);
		misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo()))
				* PASO_VELOCIDAD_MISIL);
		misil.settiempoMisil(Math.min(
				this.getWidth() / Math.abs(misil.getIncX()), this.getHeight()
						/ Math.abs(misil.getIncY())) - 2);

		Misiles.add(misil);

	}

	private void pintaVidas() {
		for (int i = 0; i <= numVidas; ++i) {
			navePeque = new Grafic(this, drawableNaveVidas, 0);
			navePeque.setPosX(view.getWidth() - (navePeque.getAncho() * i));
			navePeque.setPosY(0);
			navePeque.setIncX(0);
			navePeque.setIncY(0);
			vidas.add(navePeque);
		}
	}

	public class ThreadJoc extends Thread {

		private boolean pausa;
		private boolean corriendo;
		private Handler handler;

		@Override
		public void run() {
			corriendo = true;
			while (corriendo) {
				actualitzacio();
				try {
					Thread.sleep(PERIODO_PROCESO);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				synchronized (this) {
					while (pausa) {
					}
				}
			}
		}

		public void pausar() {
			pausa = true;
		}

		public void reanudar() {
			pausa = false;
		}

		public void detener() {
			corriendo = false;
			pausa = false;
			if (pausa)
				reanudar();
		}

		public boolean getPausar() {
			return pausa;
		}

		public void setHandler(Handler handler) {
			this.handler = handler;
		}

		public void sendMessage(Bundle b) {
			Message msg = handler.obtainMessage();
			msg.what = 1;
			msg.setData(b);
			handler.sendMessage(msg);
		}

	}

	protected synchronized void actualitzacio() {
		long ahora = System.currentTimeMillis();
		if (ultimoProceso + PERIODO_PROCESO > ahora)
			return;
		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora;

		if (ahoraPintaVidas) {
			pintaVidas();
			ahoraPintaVidas = false;
		}
		if (finAcelerar) {
			nave.setIncX(0);
			nave.setIncY(0);
			result = 0;
		} else if (!finAcelerar) {
			++result;
			nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
			double nIncX = nave.getIncX() + aceleracionNave
					* Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
			double nIncY = nave.getIncY() + aceleracionNave
					* Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
			if (Math.hypot(nIncX, nIncY) <= Grafic.getMaxVelocidad()) {
				nave.setIncX(nIncX);
				nave.setIncY(nIncY);
			} else {
				nave.setIncX(0);
				nave.setIncY(0);
			}
			if (result == 40)
				finAcelerar = true;
		} else if (result <= 0.0) {
			finAcelerar = true;
		}
		for (int i = 0; i < Asteroides.size() && !noColision; ++i) {
			if (nave.verificaColision(Asteroides.elementAt(i))) {
				double posX = nave.getPosX();
				double posY = nave.getPosY();
				int angul = nave.getAngulo();
				int rotacio = nave.getRotacion();
				colisiona(posX, posY,angul,rotacio);
			}
		}

		for (int m = 0; m < Misiles.size(); m++) {
			Misiles.elementAt(m).incrementaPos(retardo);
			Misiles.elementAt(m).settiempoMisil(
					Misiles.elementAt(m).gettiempoMisil() - retardo);
			if (Misiles.elementAt(m).gettiempoMisil() >= 0) {
				for (int i = 0, fin = 0; fin == 0 && i < Asteroides.size(); i++)
					if (Misiles.elementAt(m).verificaColision(
							Asteroides.elementAt(i))) {
						scoreUpdate = true;
						if (Asteroides.get(i).getTipus() == 3)
							puntuacion += 500;
						else if (Asteroides.get(i).getTipus() == 2)
							puntuacion += 200;
						else if (Asteroides.get(i).getTipus() == 1)
							puntuacion += 100;

						destruyeAsteroide(i, m);
						if (Misiles.size() > 0)
							Misiles.remove(m);
						if (Misiles.size() == 0)
							m--;
						fin = 9;

					}
			} else {
				Misiles.remove(m);
				if (Misiles.size() > 0)
					m--;
			}

		}

		if (Asteroides.isEmpty() && level == 1) {
			pintaAsteroidesNivell2();
		}

		if (Asteroides.isEmpty() && level == 2) {
			pintaAsteroidesNivell3();
		}

		if (Asteroides.isEmpty() && level == 3) {
			pintaAsteroidesNivell4();
		}

		if (Asteroides.isEmpty() && level == 4) {
			pintaAsteroidesNivell5();
		}

		if (scoreUpdate) {
			scoreUpdate = false;
			Bundle b = new Bundle();
			b.putInt("k1", puntuacion);
			thread.sendMessage(b);
		}

		if (estoyMuerto) {
			double x, y;
			++novaVida;
			if(novaVida<30)finAcelerar = true;
			if(novaVida == 30){
				x = nave.getPosX();
				y = nave.getPosY();
				int angulo = nave.getAngulo();
				int rota = nave.getRotacion();
				finTiempoMuerto(x, y, angulo, rota);
			}
			if (novaVida == 150) {
				novaVida = 0;
				estoyMuerto = false;
			}
		}

		if (noColision) {
			++nuevaColision;
			if (nuevaColision == 70)
				noColision = false;
		}

		if (!finAcelerar)
			nave.incrementaPos(retardo);
		for (Grafic asteroide : Asteroides) {
			asteroide.incrementaPos(retardo);
		}
	}

	private void pintaAsteroidesNivell5() {
		++level;
		for (int i = 0; i < 12; i++) {
			asteroideNivel = new Grafic(this, drawableAsteroideNivel, 1);
			asteroideNivel.setIncX(Math.random() * 8);
			asteroideNivel.setIncY(Math.random() * 8);

			asteroideNivel.setAngulo((int) (Math.random() * 360));
			asteroideNivel.setRotacion((int) (Math.random() * 8 - 4));

			Asteroides.add(asteroideNivel);
		}
		for (Grafic asteroideNivel : Asteroides) {
			do {
				asteroideNivel.setPosX(Math.random()
						* (view.getWidth() - asteroideNivel.getAncho()));
				asteroideNivel.setPosY(Math.random()
						* (view.getHeight() - asteroideNivel.getAlto()));
			} while (asteroideNivel.distancia(nave) < (view.getWidth() + view
					.getHeight()) / 5);
		}
	}

	private void pintaAsteroidesNivell4() {
		++level;
		for (int i = 0; i < 10; i++) {
			asteroideNivel = new Grafic(this, drawableAsteroideNivel, 1);
			asteroideNivel.setIncX(Math.random() * 6);
			asteroideNivel.setIncY(Math.random() * 6);

			asteroideNivel.setAngulo((int) (Math.random() * 360));
			asteroideNivel.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroideNivel);
		}
		for (Grafic asteroideNivel : Asteroides) {
			do {
				asteroideNivel.setPosX(Math.random()
						* (view.getWidth() - asteroideNivel.getAncho()));
				asteroideNivel.setPosY(Math.random()
						* (view.getHeight() - asteroideNivel.getAlto()));
			} while (asteroideNivel.distancia(nave) < (view.getWidth() + view
					.getHeight()) / 5);
		}
	}

	private void pintaAsteroidesNivell3() {
		++level;
		for (int i = 0; i < 8; i++) {
			asteroideNivel = new Grafic(this, drawableAsteroideNivel, 1);
			asteroideNivel.setIncX(Math.random() * 4);
			asteroideNivel.setIncY(Math.random() * 4);

			asteroideNivel.setAngulo((int) (Math.random() * 360));
			asteroideNivel.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroideNivel);
		}
		for (Grafic asteroideNivel : Asteroides) {
			do {
				asteroideNivel.setPosX(Math.random()
						* (view.getWidth() - asteroideNivel.getAncho()));
				asteroideNivel.setPosY(Math.random()
						* (view.getHeight() - asteroideNivel.getAlto()));
			} while (asteroideNivel.distancia(nave) < (view.getWidth() + view
					.getHeight()) / 5);
		}
	}

	private void pintaAsteroidesNivell2() {
		++level;
		for (int i = 0; i < 6; i++) {
			asteroideNivel = new Grafic(this, drawableAsteroideNivel, 1);
			asteroideNivel.setIncX(Math.random() * 4 - 1);
			asteroideNivel.setIncY(Math.random() * 4 - 1);

			asteroideNivel.setAngulo((int) (Math.random() * 360));
			asteroideNivel.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroideNivel);
		}
		for (Grafic asteroideNivel : Asteroides) {
			do {
				asteroideNivel.setPosX(Math.random()
						* (view.getWidth() - asteroideNivel.getAncho()));
				asteroideNivel.setPosY(Math.random()
						* (view.getHeight() - asteroideNivel.getAlto()));
			} while (asteroideNivel.distancia(nave) < (view.getWidth() + view
					.getHeight()) / 5);
		}
	}

	private void colisiona(double posX, double posY, int angul, int rotacio) {
		nave = new Grafic(this, drawableExplossio, 0);
		nave.setPosX(posX);
		nave.setPosY(posY);
		nave.setAngulo(angul);
		nave.setRotacion(rotacio);
		ang = angul;
		if (gameListener != null && numVidas == 0) {
			v.vibrate(500);
			gameListener.onGameOver();
		} else {
			novaVida = 0;
			ang = 0;
			ahoraPintaVidas = true;
			v.vibrate(500);
			estoyMuerto = true;
			noColision = true;
			nuevaColision = 0;
			vidas.clear();
		}
		--numVidas;
	}

	private void finTiempoMuerto(double posX, double posY, int angul, int rota) {
		nave = new Grafic(this, drawableNave, 0);
		nave.setPosX(posX);
		nave.setPosY(posY);
		nave.setAngulo(angul);
		nave.setRotacion(rota);
	}

	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter,
			int alto_anter) {
		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

		nave.setPosX(ancho / 2 - nave.getAncho() / 2);
		nave.setPosY(alto / 2 - nave.getAlto() / 2);

		for (Grafic asteroide : Asteroides) {
			do {
				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			} while (asteroide.distancia(nave) < (ancho + alto) / 5);
		}

		ultimoProceso = System.currentTimeMillis();
		thread.start();
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		nave.dibuixaGrafic(canvas);

		for (Grafic asteroide : Asteroides) {
			asteroide.dibuixaGrafic(canvas);
		}

		for (Grafic misil : Misiles) {
			misil.dibuixaGrafic(canvas);
		}

		for (Grafic navePeque : vidas) {
			navePeque.dibuixaGrafic(canvas);
		}
	}

	public ThreadJoc getThread() {
		return thread;
	}

	public void pausar() {
		getThread().pausar();
	}

	public static int getPuntuacion() {
		return puntuacion;
	}

	public static void setPuntuacion(int puntuacion) {
		VistaJuego.puntuacion = puntuacion;
	}

}