package com.davidguerrero.asteroids;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DadesPuntuacio extends Activity {

	private DatabaseHandler db;
	private Puntuacion puntuacion;
	private String scoreText;
	EditText et1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dades_puntuacio);
		TextView marcador = (TextView) findViewById(R.id.result);
		TextView punt = (TextView) findViewById(R.id.puntuacio);
		TextView nom = (TextView) findViewById(R.id.nom);
		et1 = (EditText) findViewById(R.id.entradaNombre);
		int score = VistaJuego.getPuntuacion();
		scoreText = Integer.toString(score);
		marcador.setText(scoreText);
		db = new DatabaseHandler(this);
		puntuacion = new Puntuacion();
		Button guardar = (Button)findViewById(R.id.guardar);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ANDROID ROBOT.ttf");
		marcador.setTypeface(custom_font);
		guardar.setTypeface(custom_font);
		punt.setTypeface(custom_font);
		nom.setTypeface(custom_font);
		et1.setTypeface(custom_font);

		final Button guardarSortir = (Button) findViewById(R.id.guardar);
		guardarSortir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String nom;
				nom = et1.getText().toString();
				puntuacion.setNombre(nom);
				puntuacion.setPuntos(scoreText);
				db.addPuntuacion(puntuacion);

				Intent refresh = new Intent(DadesPuntuacio.this, MainActivity.class);
				startActivity(refresh);
				DadesPuntuacio.this.finish();
			}
		});

	}
}
