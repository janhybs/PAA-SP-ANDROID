package cz.edu.x3m.todolist.utils;


import cz.edu.x3m.todolist.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



public class InfoActivity extends Activity {


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.info_layout);

		TextView devEmail, devName;

		devEmail = (TextView) findViewById (R.id.devEmail);
		devName = (TextView) findViewById (R.id.devName);
		
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick (View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/html");
				intent.putExtra(Intent.EXTRA_EMAIL, "x3mSpeedy@gmail.com");
				intent.putExtra(Intent.EXTRA_SUBJECT, "PAA-SP");

				startActivity(Intent.createChooser(intent, getResources ().getString (R.string.send_email)));
			}
		};
		
		devEmail.setOnClickListener (listener);
		devName.setOnClickListener (listener);
	}
}
