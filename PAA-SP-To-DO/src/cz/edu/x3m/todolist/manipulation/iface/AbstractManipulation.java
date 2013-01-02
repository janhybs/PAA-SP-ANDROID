package cz.edu.x3m.todolist.manipulation.iface;

import cz.edu.x3m.todolist.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public abstract class AbstractManipulation extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void attachButtonListeners() {
		Button cancelButton;
		Button confirmButton;

		cancelButton = (Button) findViewById(R.id.formCancel);
		confirmButton = (Button) findViewById(R.id.formConfirm);

		if (cancelButton != null)
			cancelButton.setOnClickListener(this);

		if (confirmButton != null)
			confirmButton.setOnClickListener(this);
	}

	protected String getFormTitle() {
		return ((EditText) findViewById(R.id.formTitle)).getText().toString();
	}

	protected String getFormDescription() {
		return ((EditText) findViewById(R.id.formDescription)).getText().toString();
	}

	protected Boolean getFormStatus() {
		CheckBox status = (CheckBox) findViewById(R.id.formStatus);
		return status == null ? null : status.isChecked();
	}

	protected void setFormTitle(String value) {
		((EditText) findViewById(R.id.formTitle)).setText(value);
	}

	protected void setFormDescription(String value) {
		((EditText) findViewById(R.id.formDescription)).setText(value);
	}

	protected void setFormStatus(Boolean value) {
		CheckBox status = (CheckBox) findViewById(R.id.formStatus);
		if (status != null)
			status.setChecked(value);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.formCancel:
			onCancelClicked();
			break;
		case R.id.formConfirm:
			onConfirmClicked();
			break;
		}
	}

	protected boolean isTitleFilled() {
		return getFormTitle().length() != 0;
	}

	protected void alertEmptyTitle() {
		((TextView) findViewById(R.id.formTitle)).requestFocus();
		Toast.makeText(this, R.string.please_fill_task, Toast.LENGTH_LONG).show();
	}

	protected void onCancelClicked() {
		finish();
	}

	protected boolean onConfirmClicked() {
		if (!isTitleFilled()) {
			alertEmptyTitle();
			return false;
		}
		return true;
	}
}
