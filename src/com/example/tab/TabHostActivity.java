package com.example.tab;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabHostActivity extends TabActivity {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tab_host);
		Resources ressources = getResources();
		TabHost tabHost = getTabHost();
		
		

		// Home tab
		
		Intent intentHome = new Intent().setClass(this, Home.class);
		TabSpec tabSpecHome = tabHost
				.newTabSpec("Home")
				.setIndicator("", ressources.getDrawable(R.drawable.homeicon))
				.setContent(intentHome);
		
		// Read tab
		Intent intentRead = new Intent().setClass(this, Read.class);
		TabSpec tabSpecRead = tabHost.newTabSpec("Read")
				.setIndicator("", ressources.getDrawable(R.drawable.readicon))
				.setContent(intentRead);
        
		// Write tab
		Intent intentWrite = new Intent().setClass(this, Write.class);
		TabSpec tabSpecWrite = tabHost
				.newTabSpec("Write")
				.setIndicator("", ressources.getDrawable(R.drawable.writeicon))
				.setContent(intentWrite);
		
		// Setting tab
		Intent intentSetting = new Intent().setClass(this, Setting.class);
		TabSpec tabSpecSetting = tabHost
				.newTabSpec("Setting")
				.setIndicator("", ressources.getDrawable(R.drawable.setting))
				.setContent(intentSetting);
        
        
		// add all tabs
		tabHost.addTab(tabSpecHome);
		tabHost.addTab(tabSpecRead);
		tabHost.addTab(tabSpecWrite);
		tabHost.addTab(tabSpecSetting);
		
		// set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}


}