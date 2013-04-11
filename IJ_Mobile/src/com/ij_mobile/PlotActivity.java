package com.ij_mobile;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PlotActivity extends Activity{

	private static int RESULT_LOAD_IMAGE = 1;
	private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";
	private int width, height;
	private double[] gray;
	private Bitmap bmp, bmpGrayscale;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_profile);
		Button buttonSP = (Button) findViewById(R.id.buttonSP);
		Button buttonP = (Button) findViewById(R.id.buttonP);
		Button buttonL = (Button) findViewById(R.id.buttonL);
		final TextView textView0 = (TextView) findViewById(R.id.textView0);
		buttonSP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				i.setType("image/*");
				i.putExtra("crop", "true");
				i.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
				i.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		buttonP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(bmp != null){ 
					if(bmpGrayscale == null){
						bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
						Canvas c = new Canvas(bmpGrayscale);
						Paint paint = new Paint();
						ColorMatrix cm = new ColorMatrix();
						cm.setSaturation(0);
						ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
						paint.setColorFilter(f);
						c.drawBitmap(bmp, 0, 0, paint); 
						/*bmp.recycle();
						ImageView imageView = (ImageView) findViewById(R.id.imgView);
						if(height > 1500 || width > 1500)
							imageView.setImageBitmap(Bitmap.createScaledBitmap(bmpGrayscale, 1500, 1500, false));
						else
							imageView.setImageBitmap(bmpGrayscale);*/
						gray = getColumnAverageProfile();
					}
					Intent intentGraphView = new Intent(PlotActivity.this, GraphActivity.class);
					intentGraphView.putExtra("gray", gray);
					startActivity(intentGraphView);
				}
				else
					textView0.setText("Pease select an image first!");
			}
		});
		
		buttonL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(bmp != null){ 
					if(bmpGrayscale == null){
						bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
						Canvas c = new Canvas(bmpGrayscale);
						Paint paint = new Paint();
						ColorMatrix cm = new ColorMatrix();
						cm.setSaturation(0);
						ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
						paint.setColorFilter(f);
						c.drawBitmap(bmp, 0, 0, paint); 
						/*bmp.recycle();
						ImageView imageView = (ImageView) findViewById(R.id.imgView);
						if(height > 1500 || width > 1500)
							imageView.setImageBitmap(Bitmap.createScaledBitmap(bmpGrayscale, 1500, 1500, false));
						else
							imageView.setImageBitmap(bmpGrayscale);*/
						gray = getColumnAverageProfile();
					}
					Intent intentListView = new Intent(PlotActivity.this, ListActivity.class);
					intentListView.putExtra("gray", gray);
					startActivity(intentListView);
				}
				else
					textView0.setText("Pease select an image first!");
			}
		});

	}
	private Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}

	private File getTempFile() {

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),TEMP_PHOTO_FILE);
			try {
				file.createNewFile();
			} catch (IOException e) {}

			return file;
		} else {

			return null;
		}
	}

	public int getLastBytes(long original) {
		String hexBytes = Long.toHexString(original);
		return Integer.parseInt(hexBytes.substring(hexBytes.length()-2),16);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			String picLoc= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
					+ File.separator + TEMP_PHOTO_FILE;
			//System.out.println("path: "+picLoc);
			File tempFile = getTempFile();
			ImageView imageView = (ImageView) findViewById(R.id.imgView);
			bmp = BitmapFactory.decodeFile(picLoc);
			height = bmp.getHeight();
			width = bmp.getWidth();
			
			/*Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			title = new File(picturePath).getName();
			System.out.println("Testing Title: " + title);*/
			
			
			if(height > 1500 || width > 1500)
				imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 1500, 1500, false));
			else
				imageView.setImageBitmap(bmp);
			if (tempFile.exists()) 
				tempFile.delete();
			if(bmpGrayscale !=null)
				bmpGrayscale.recycle();
		}
	}
	double[] getColumnAverageProfile() {
		double[] profile = new double[width];
		int[] counts = new int[width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				profile[j] += getLastBytes(bmpGrayscale.getPixel(j, i));
				counts[j]++;
			}
		}
		for (int i = 0; i < width; i++)
			profile[i] /= counts[i];
		return profile;
	}	
}
