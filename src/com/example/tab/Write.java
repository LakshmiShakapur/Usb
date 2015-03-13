package com.example.tab;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.hardware.usb.UsbDeviceConnection;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

public class Write extends Activity {
	EditText et;
	String secretKey = "";
	Readadapter try_adap;
	File myFile;
	String data = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);

		setupUI(findViewById(R.id.writePage));

		try_adap = new Readadapter(this);
		try_adap.open();

		et = (EditText) findViewById(R.id.edit);
		Button write = (Button) findViewById(R.id.write);
		write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					data = et.getText().toString();
					myFile = new File(getStoragepath(), "root.txt");

					if (!myFile.exists()) {
						myFile.createNewFile();

						if (!myFile.exists()) {
							Toast.makeText(getBaseContext(),
									"Text File Not Created !",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getBaseContext(),
									"Text File Created Successfully !",
									Toast.LENGTH_SHORT).show();
							if (!data.equals("") && data.length()>0) {
								fileWrite();
							}
							else{
								Toast.makeText(getBaseContext(),
										"Please enter Some Text!",
										Toast.LENGTH_SHORT).show();
							}
						}

					} else if (!data.equals("") && data.length()>0) {
						fileWrite();
					}
					else
					{
						Toast.makeText(getBaseContext(),
								"Please enter Some Text!",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void fileWrite() {

		try {
			FileOutputStream out = new FileOutputStream(myFile);
			BufferedOutputStream bout = new BufferedOutputStream(out);

			String key = "";
			try {
				key = AES.setencryptedkey(secretKey);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Log.d("", "32 bit key is : " + key);

			byte[] cipher = null;
			try {
				
				cipher = AES.Encrypt(data.getBytes("UTF-8"), key);
				
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}
			String base64Text = Base64.encodeToString(cipher, Base64.DEFAULT);

			Log.d("", "base64 encrypted text " + base64Text);

			bout.write(base64Text.getBytes("UTF-8"));
			bout.flush();
			bout.close();
			out.close();
			Toast.makeText(getBaseContext(),
					"File Writing Operation Completed !", Toast.LENGTH_SHORT)
					.show();

		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}
	}

	public String getStoragepath() {
		String path = null;
		File storageDir = new File("/mnt/");
		if (storageDir.isDirectory()) {
			String[] dirList = storageDir.list();
			for (int i = 0; i < dirList.length; i++) {
				if (dirList[i].contains("uhost")) {
					path = "/mnt/" + dirList[i];
					Log.d("msg", path);
				}
			}
			
		}
		return path;
	}

	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {
			view.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(Write.this);
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
	protected void onResume() {
		et.setText("");
		try {
			Cursor c = try_adap.fetch_retdb(1);
			c.moveToFirst();
			secretKey = c.getString(c
					.getColumnIndexOrThrow(Readadapter.KEY_Key));

		} catch (Exception e) {
			e.printStackTrace();
			secretKey = "";
		}
		if (secretKey.equals("") && secretKey.length() <= 0) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					Write.this);
			alertDialog.setCancelable(false);
			alertDialog.setTitle("Message");
			alertDialog.setMessage("Please Set Encryption Key First ! ");

			alertDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							TabHost tabhost = (TabHost) getParent()
									.findViewById(android.R.id.tabhost);
							tabhost.setCurrentTab(3);
						}
					});
			alertDialog.show();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try_adap.close();
	}

}
