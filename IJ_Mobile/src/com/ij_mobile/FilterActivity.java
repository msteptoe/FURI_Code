package com.ij_mobile;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class FilterActivity extends Activity implements OnItemSelectedListener{

	private String color, colorP, picturePath, filename;
	private static int RESULT_LOAD_IMAGE = 1;
	private int width, height, channel;
	private Bitmap bmp, bmpFil, bmpGrayscale;
	private boolean bmpValid, grayValid;
	private float[] colorMatrix;
	private static final String TAG = "ScaleChannel";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_view);
		Button buttonLoadImage = (Button) findViewById(R.id.buttonLP);
		Button buttonApply = (Button) findViewById(R.id.buttonA);
		Button buttonSave = (Button) findViewById(R.id.buttonS);
		Spinner colors = (Spinner) findViewById(R.id.colors);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.colors, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		colors.setAdapter(adapter);
		colors.setOnItemSelectedListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			picturePath = extras.getString("loc");
			if(picturePath != null){
				File f = new File(picturePath);
				filename = f.getName();
				ImageView imageView = (ImageView) findViewById(R.id.imageView);
				bmp = BitmapFactory.decodeFile(picturePath);
				imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 1500, 1500, false));
			}
		}

		buttonLoadImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		buttonApply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(bmpValid || grayValid){
					/*if(!grayValid){
						bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
						Canvas cG = new Canvas(bmpGrayscale);
						Paint paintG = new Paint();
						ColorMatrix cm = new ColorMatrix();
						cm.setSaturation(0);
						ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
						paintG.setColorFilter(f);
						cG.drawBitmap(bmp, 0, 0, paintG);
						bmp.recycle();
						bmpValid = false;
						grayValid = true;
					}*/
					bmpFil = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
					Canvas c = new Canvas(bmpFil);
					Paint paint = new Paint();
					if(color.equals("Red")){
						float [] red = { 
								1, 0, 0, 0, 0, //red
								0, 0, 0, 0, 0, //green
								0, 0, 0, 0, 0, //blue
								0, 0, 0, 1, 0 //alpha  
						};
						channel = 1;
						colorMatrix = red;
					}
					else if(color.equals("Green")){
						float[] green = { 
								0, 0, 0, 0, 0, //red
								0, 1, 0, 0, 0, //green
								0, 0, 0, 0, 0, //blue
								0, 0, 0, 1, 0 //alpha  
						};
						channel = 2;
						colorMatrix = green;
					}
					else{
						float[] blue = { 
								0, 0, 0, 0, 0, //red
								0, 0, 0, 0, 0, //green
								0, 0, 1, 0, 0, //blue
								0, 0, 0, 1, 0 //alpha  
						};
						channel = 3;
						colorMatrix = blue;
					}
					colorP = color;
					ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
					paint.setColorFilter(filter);
					//c.drawBitmap(bmpGrayscale, 0, 0, paint);
					c.drawBitmap(bmp, 0, 0, paint);
					scaleChannel(bmpFil);
					ImageView imageView = (ImageView) findViewById(R.id.imageView);
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bmpFil, 1500, 1500, false));
					grayValid = true;
				}
				else{
					TextView textView= (TextView) findViewById(R.id.textView);
					textView.setText("Please select an image First!");
				}

			}
		});

		buttonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TextView textView= (TextView) findViewById(R.id.textView);
				if(grayValid){
					try {
						if(filename.contains("."))
							filename = filename.substring(0, filename.indexOf("."));
						File directory = new File(Environment.getExternalStorageDirectory()+ File.separator + "ImageJ" 
								+ File.separator+ "Filter" + File.separator);
						directory.mkdirs();
						File outputFile = new File(directory, filename+"_"+colorP+".png");
						FileOutputStream out = new FileOutputStream(outputFile);
						//System.out.println("Image Save: "+outputFile.getAbsolutePath());
						bmpFil.compress(Bitmap.CompressFormat.PNG, 100, out);
						textView.setText("Saved to: " + outputFile.getAbsolutePath());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(bmpValid){
					try {
						if(filename.contains("."))
							filename = filename.substring(0, filename.indexOf("."));
						File directory = new File(Environment.getExternalStorageDirectory()+ File.separator + "ImageJ" 
								+ File.separator+ "Filter" + File.separator);
						directory.mkdirs();
						File outputFile = new File(directory, filename+"_OG"+".png");
						FileOutputStream out = new FileOutputStream(outputFile);
						//System.out.println("Image Save: "+outputFile.getAbsolutePath());
						bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
						textView.setText("Saved to: " + outputFile.getAbsolutePath());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					textView.setText("Please apply a filter First!");
				}
				//filename = filename.substring(0, filename.indexOf(".jpg"));
				//System.out.println("Name: "+filename);
			}
		});
	}

	public void onItemSelected(AdapterView<?> parent, View view, 
			int pos, long id) {

		color = (String) parent.getItemAtPosition(pos);
		//System.out.println("Testing operation selection: " + color);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			File f = new File(picturePath);
			filename = f.getName();
			//System.out.println("PP: "+picturePath + "\n Name: " + filename);
			//System.out.println("Old Path1: " + picLoc1);
			ImageView imageView = (ImageView) findViewById(R.id.imageView);
			bmp = BitmapFactory.decodeFile(picturePath);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 1500, 1500, false));
			width = bmp.getWidth();
			height = bmp.getHeight();
			Log.v(TAG, "original Values begin");
			scaleChannel(bmp);
			Log.v(TAG, "original Values end");
			bmpValid = true;
			if(grayValid){
				//bmpGrayscale.recycle();
				bmpFil.recycle();
				grayValid = false;
			}
			//bmp.recycle();
		}
	}
	private int getBytes(int original) {
		String hexBytes = Integer.toHexString(original);
		//String hexBytes = Long.toHexString(original);
		Log.v(TAG, "HexBytes: " + hexBytes);
		int hex=0;
		switch (channel){
		case 1:
			hex = Integer.parseInt(hexBytes.substring(hexBytes.length()-6,hexBytes.length()-4),16);
			//Log.v(TAG, "Red Values: " + hex);
			break;
		case 2:
			hex = Integer.parseInt(hexBytes.substring(hexBytes.length()-2),16);
			//Log.v(TAG, "Green Values: " + hex);
			break;
		case 3:
			hex = Integer.parseInt(hexBytes.substring(hexBytes.length()-2),16);
			//Log.v(TAG, "Blue Values: " + hex);
			break;
		default:
			hex = Integer.parseInt(hexBytes.substring(hexBytes.length()-2),16);
			//Log.v(TAG, "Blue Values: " + hex);
			break;
		}
		
		return hex;
	}
	
	private void scaleChannel(Bitmap bmpSc) {
		int max = 0;
		int pix = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				//profile[j] += getLastBytes(bmpGrayscale.getPixel(j, i));
				//counts[j]++;
				//bmpGrayscale.setPixel(j, i, 0);
				pix = getBytes(bmpSc.getPixel(j, i));
				//Log.v(TAG, "Pixel=" + pix);
				if (max < pix)
					max = pix;
			}
		}
		System.out.println("Max Pixel Color is " + max);
	}	
}
