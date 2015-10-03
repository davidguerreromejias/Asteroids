package com.davidguerrero.asteroids;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tx = (TextView)findViewById(R.id.textView1);
		Button b1 = (Button)findViewById(R.id.Button01);
		Button b2 = (Button)findViewById(R.id.instruccions);
		Button b3 = (Button)findViewById(R.id.Button04);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ANDROID ROBOT.ttf");
		tx.setTypeface(custom_font);
		b1.setTypeface(custom_font);
		b2.setTypeface(custom_font);
		b3.setTypeface(custom_font);
	}

	public void lanzarPuntuaciones(View view) {
		Intent i = new Intent(this, listpuntuaciones.class);
		startActivity(i);
	}

	public void lanzarJuego(View view) {
		Intent i = new Intent(this, Juego.class);
		startActivity(i);
	}

	public void lanzarInstruccions(View view) {
		Intent i = new Intent(this, Instruccions.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Button04:
			lanzarPuntuaciones(null);
			break;
		case R.id.Button01:
			lanzarJuego(null);
			break;
		case R.id.instruccions:
			lanzarInstruccions(null);
			break;
		}
		return true;
	}
}
