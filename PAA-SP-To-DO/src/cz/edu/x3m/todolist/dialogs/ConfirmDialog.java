package cz.edu.x3m.todolist.dialogs;

import cz.edu.x3m.todolist.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;



public class ConfirmDialog {

	
	public static void showConfirmDialog (Context context, int title, OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder (context);
		
		builder.setTitle (title);
		builder.setPositiveButton (R.string.dialog_yes, listener);
		builder.setNegativeButton (R.string.dialog_no, new DialogInterface.OnClickListener () {
			public void onClick (DialogInterface dialog, int id) {
				dialog.cancel ();
			}
		});
		builder.setCancelable (false);
		builder.create ().show ();
	}
}
