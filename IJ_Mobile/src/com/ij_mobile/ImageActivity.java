package com.ij_mobile;

import java.io.File;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageActivity extends Activity {
	
	private String op, f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
	
	
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    op = extras.getString("result");
		    f = extras.getString("loc");
		}
	
		TextView textView1= (TextView) findViewById(R.id.textView1);
		//TextView textView2= (TextView) findViewById(R.id.textView2);
		textView1.setText("Result of " + op + " operation!");
		//textView2.setText("Location: " + f);
		
		ImageView imageView = (ImageView) findViewById(R.id.imgView);
        imageView.setImageBitmap(BitmapFactory.decodeFile(f));
	}

}
