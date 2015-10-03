package com.davidguerrero.asteroids;

import java.util.List;
import java.util.Vector;

import com.davidguerrero.asteroids.Puntuacion.AfegirPuntuacio;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class listpuntuaciones extends ListActivity implements AfegirPuntuacio {


	private Vector<String> values = new Vector<String>();

	DatabaseHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.puntuacions);
		
		TextView tx = (TextView)findViewById(R.id.puntuacions);
		Button borrar = (Button)findViewById(R.id.borrarRecord);
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/ANDROID ROBOT.ttf");
		borrar.setTypeface(custom_font);
		tx.setTypeface(custom_font);

		db = new DatabaseHandler(this);
		// Reading all contacts
		Log.d("Reading: ", "Reading all contacts..");
		List<Puntuacion> puntuaciones = db.getAllContacts();

		for (int i = 0; i < puntuaciones.size(); ++i) {
			Puntuacion p = new Puntuacion();
			p = puntuaciones.get(i);
			String log = "Punts: " + p.getPuntos() + ", Nom: " + p.getNombre();
			values.add(log);
			// Writing Contacts to log
			Log.d("Name: ", log);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, values);
			setListAdapter(adapter);
		}

		final Button borrarRecords = (Button) findViewById(R.id.borrarRecord);
		borrarRecords.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				List<Puntuacion> puntuaciones = db.getAllContacts();
				for(Puntuacion p : puntuaciones){
					db.deletePuntuacion(p);
				}
				Intent refresh = new Intent(listpuntuaciones.this, listpuntuaciones.class);
				finish();
				startActivity(refresh);
			}
		});
	}

	@Override
	public void afegirPuntuacio(String punts) {
		db.addPuntuacion(new Puntuacion("default", punts));
		String log = "Punts: " + ", Nom: " + punts;
		values.add(log);
	}

	@Override
	public void afegirNom(String nom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPuntuacio() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getNom() {
		// TODO Auto-generated method stub

	}
}
