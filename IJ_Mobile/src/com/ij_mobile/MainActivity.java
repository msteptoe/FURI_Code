package com.ij_mobile;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button buttonMV = (Button) findViewById(R.id.buttonMV);
		Button buttonICV = (Button) findViewById(R.id.buttonICV);
		Button buttonPP = (Button) findViewById(R.id.buttonPP);
		buttonMV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intentMeasure = new Intent(MainActivity.this, MeasureActivity.class);
				startActivity(intentMeasure);
			}
		});
		buttonICV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intentCalc = new Intent(MainActivity.this, ImageCalcActivity.class);
				startActivity(intentCalc);
			}
		});
		buttonPP.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intentPlot = new Intent(MainActivity.this, PlotActivity.class);
				startActivity(intentPlot);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
}
