package com.davidguerrero.asteroids;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davidguerrero.asteroids.GameOverDialog.GameOverDialogListener;
import com.davidguerrero.asteroids.VistaJuego.GameEventListener;
import com.davidguerrero.asteroids.VistaJuego.ThreadJoc;

public class Juego extends Activity implements GameOverDialogListener {

	private VistaJuego vistaJuego;
	private static TextView marcador;
	private ThreadJoc gameLoopThread = null;
	private LinearLayout pausarBotons;
	private ImageButton buttonderecho;
	private ImageButton buttonizquierdo;
	private ImageButton buttonboost;
	private ImageButton buttonshoot;

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b;
			if (msg.what == 1) {
				b = msg.getData();
				String scoreText = Integer.toString(b.getInt("k1"));
				marcador.setText(scoreText);
			}
			super.handleMessage(msg);
		}
	};

	public static TextView getMarcador() {
		return marcador;
	}

	public static void setMarcador(TextView marcador) {
		Juego.marcador = marcador;
	}

	public static GameOverDialogListener getGameListener() {
		return gameListener;
	}

	public static void setGameListener(GameOverDialogListener gameListener) {
		Juego.gameListener = gameListener;
	}

	private static GameOverDialogListener gameListener = new GameOverDialogListener() {

		@Override
		public void salir() {
			// TODO Auto-generated method stub

		}

		@Override
		public void afegirPunts() {
			// TODO Auto-generated method stub

		}

		@Override
		public void actualizaScore(int score) {
			String scoreText = Integer.toString(score);
			Juego.getMarcador().setText(scoreText);
		}
	};

	@Override
	public void onBackPressed() {
		if (gameLoopThread.getPausar()) {
			gameLoopThread.reanudar();
			pausarBotons.setVisibility(View.INVISIBLE);
			buttonboost.setEnabled(true);
			buttonderecho.setEnabled(true);
			buttonizquierdo.setEnabled(true);
			buttonshoot.setEnabled(true);
		} else {
			buttonboost.setEnabled(false);
			buttonshoot.setEnabled(false);
			buttonderecho.setEnabled(false);
			buttonizquierdo.setEnabled(false);
			pausarBotons.setVisibility(View.VISIBLE);
			gameLoopThread.pausar();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);

		vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
		marcador = (TextView) findViewById(R.id.score);
		TextView score = (TextView) findViewById(R.id.scoreTitle);
		pausarBotons = (LinearLayout) findViewById(R.id.layoutBotons);
		gameLoopThread = vistaJuego.getThread();
		gameLoopThread.setHandler(handler);
		pausarBotons.setVisibility(View.INVISIBLE);
		
		Button volver = (Button)findViewById(R.id.Tornar);
		Button salir = (Button)findViewById(R.id.Sortir);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ANDROID ROBOT.ttf");
		marcador.setTypeface(custom_font);
		volver.setTypeface(custom_font);
		salir.setTypeface(custom_font);
		score.setTypeface(custom_font);
		
		vistaJuego.setGameListener(new GameEventListener() {

			@Override
			public void onGameOver() {
				vistaJuego.pausar();
				mostrarGameOver();
			}
		});

		buttonderecho = (ImageButton) findViewById(R.id.der);
		buttonderecho.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vistaJuego.giroIzquierda();
			}
		});
		buttonizquierdo = (ImageButton) findViewById(R.id.izq);
		buttonizquierdo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				VistaJuego juego = (VistaJuego) findViewById(R.id.VistaJuego);
				juego.giroDerecha();
			}
		});
		buttonboost = (ImageButton) findViewById(R.id.bo);
		buttonboost.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				VistaJuego juego = (VistaJuego) findViewById(R.id.VistaJuego);
				juego.acelera();
			}
		});
		buttonshoot = (ImageButton) findViewById(R.id.sho);
		buttonshoot.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				VistaJuego juego = (VistaJuego) findViewById(R.id.VistaJuego);
				juego.dispara();
			}
		});
		final Button tornar = (Button) findViewById(R.id.Tornar);
		tornar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gameLoopThread.reanudar();
				pausarBotons.setVisibility(View.INVISIBLE);
				buttonboost.setEnabled(true);
				buttonderecho.setEnabled(true);
				buttonizquierdo.setEnabled(true);
				buttonshoot.setEnabled(true);
			}
		});
		final Button sortir = (Button) findViewById(R.id.Sortir);
		sortir.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gameLoopThread.detener();
				finish();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		vistaJuego.pausar();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	public void mostrarGameOver() {
		GameOverDialog dialog = new GameOverDialog();
		gameLoopThread.detener();
		dialog.show(getFragmentManager(), "gameover");
	}

	/*
	 * @Override public void jugarDeNuevo() { vistaJuego.restart(); }
	 */

	@Override
	public void salir() {
		finish();
	}

	@Override
	public void afegirPunts() {
		Intent intent = new Intent(Juego.this, DadesPuntuacio.class);
		finish();
		startActivity(intent);
	}

	@Override
	public void actualizaScore(int score) {
		String scoreText = Integer.toString(score);
		marcador.setText(scoreText);
	}
}
