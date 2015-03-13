package com.example.tab;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

public class Setting extends Activity {
	Button btnOk, btnCancel;
	EditText key;
	String value = "";
	Readadapter try_adap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		setupUI(findViewById(R.id.settingPage));

		try_adap = new Readadapter(this);
		try_adap.open();

		btnOk = (Button) findViewById(R.id.ok);
		btnCancel = (Button) findViewById(R.id.cancel);
		key = (EditText) findViewById(R.id.keyValue);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				value = key.getText().toString();

				if (value.length() <= 0) {
					Toast.makeText(getBaseContext(),
							"Please provide valid encryption key ",
							Toast.LENGTH_SHORT).show();
				} else {

					try {
						Cursor c = try_adap.fetch_retdb(1);
						if (c.getCount() == 0) {
							long inserted = try_adap.insertData(value);
							if (inserted > 0) {
								Toast.makeText(getApplicationContext(),
										"Encryption Key Saved", 100).show();
								Log.d("", "sectetekey " + value);
								key.setText("");

								TabHost tabhost = (TabHost) getParent()
										.findViewById(android.R.id.tabhost);
								tabhost.setCurrentTab(0);
							} else {
								Toast.makeText(getApplicationContext(),
										"Encryption Key not Saved", 100).show();
							}
						} else {

							boolean inserted = try_adap
									.updateDataUser(1, value);
							if (inserted) {
								Toast.makeText(getApplicationContext(),
										"Encryption Key updated", 100).show();
							
								key.setText("");

								TabHost tabhost = (TabHost) getParent()
										.findViewById(android.R.id.tabhost);
								tabhost.setCurrentTab(0);
							} else {
								Toast.makeText(getApplicationContext(),
										"Encryption Key not updated", 100)
										.show();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TabHost tabhost = (TabHost) getParent().findViewById(
						android.R.id.tabhost);
				tabhost.setCurrentTab(0);

			}
		});
	}

	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(Setting.this);

					return false;
				}

			});
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try_adap.close();
	}

}
