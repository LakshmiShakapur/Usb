package com.example.tab;

import java.io.File;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.textservice.TextInfo;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Read extends Activity {
	TextView txtInfo;
	String secretKey = "";
	Readadapter try_adap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.read);

		try_adap = new Readadapter(this);
		try_adap.open();

		txtInfo = (TextView) findViewById(R.id.text);
		Button b = (Button) findViewById(R.id.read);
		
		txtInfo.setMovementMethod(new ScrollingMovementMethod());
		
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					File myFile = new File(getStoragepath(), "root.txt");

					if (myFile.exists()) {
						String data = "";
						String key = AES.setencryptedkey(secretKey);

						Log.d("", "32 bit key is : " + key);
						Scanner readfile = new Scanner(myFile);

						while (readfile.hasNextLine()) {
							data = data + readfile.nextLine();
							Log.d("cipher text", "" + data);
						}

						readfile.close();

						byte[] base64data = Base64.decode(
								data.getBytes("UTF-8"), Base64.DEFAULT);
						byte[] decryptedText = AES.Decrypt(base64data, key);
						
						if(data.length()<=0)
						{
							Toast.makeText(getBaseContext(), "File is empty!",
									Toast.LENGTH_SHORT).show(); 
						}else{
							txtInfo.setText(new String(decryptedText, "UTF-8"));
						}
						

					}
					else
					{
						Toast.makeText(getBaseContext(), "No file Directory Found!",
								Toast.LENGTH_SHORT).show(); 
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getBaseContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	public String getStoragepath() {
		String path = null;
		Log.d("","external storage  directory : "+Environment.getExternalStorageDirectory());
		File storageDir = new File("/mnt/");
		if (storageDir.isDirectory()) {
			String[] dirList = storageDir.list();
			for (int i = 0; i < dirList.length; i++) {
				if (dirList[i].contains("uhost")) { //uhost is the directory created by usb in my tab .it may vary different phones/tabs.
					path = "/mnt/" + dirList[i];
					Log.d("msg", path);
				}
			}
		}
		return path;
	}


	@Override
	protected void onResume() {
		super.onResume();
		txtInfo.setText("");
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
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(Read.this);
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

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try_adap.close();
	}
}
