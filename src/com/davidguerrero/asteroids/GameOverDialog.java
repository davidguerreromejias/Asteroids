package com.davidguerrero.asteroids;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class GameOverDialog extends DialogFragment {

	public interface GameOverDialogListener {
		public void afegirPunts();
		
		public void actualizaScore(int score);

		public void salir();
	}

	private GameOverDialogListener listener;

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			listener = (GameOverDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Build the dialog and set up the button click handlers
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.gameover)
				.setPositiveButton(R.string.guardapuntuacio,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Send the positive button event back to the
								// host activity
								listener.afegirPunts();
							}
						})
				.setNegativeButton(R.string.sortir,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Send the negative button event back to the
								// host activity
								listener.salir();
							}
						});
		return builder.create();
	}

}
