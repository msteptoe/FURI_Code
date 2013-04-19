/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2012 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package com.ij_mobile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
	private boolean grayValid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_profile);
		Button buttonSP = (Button) findViewById(R.id.buttonSP);
		Button buttonS = (Button) findViewById(R.id.buttonS);
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
					textView0.setText("Please select an image first!");
			}
		});
		
		buttonL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(bmp != null){ 
					if(!grayValid){
						bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
						Canvas c = new Canvas(bmpGrayscale);
						Paint paint = new Paint();
						ColorMatrix cm = new ColorMatrix();
						cm.setSaturation(0);
						ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
						paint.setColorFilter(f);
						c.drawBitmap(bmp, 0, 0, paint);
						grayValid = true;
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
					textView0.setText("Please select an image first!");
			}
		});
		
		buttonS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(bmp != null){ 
					if(!grayValid){
						bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
						Canvas c = new Canvas(bmpGrayscale);
						Paint paint = new Paint();
						ColorMatrix cm = new ColorMatrix();
						cm.setSaturation(0);
						ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
						paint.setColorFilter(f);
						c.drawBitmap(bmp, 0, 0, paint);
						grayValid = true;
						gray = getColumnAverageProfile();
					}
					String format = "yyyyMMdd_HHmmss";
					SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
					File directory = new File(Environment.getExternalStorageDirectory()+ File.separator + "ImageJ" + File.separator+ "PlotProfile" + File.separator);
					directory.mkdirs();
					File outputFile = new File(directory, "Results_" +sdf.format(System.currentTimeMillis()) + ".txt"); 
					//System.out.println("Saving to: "+outputFile);
					FileWriter out = null;
					try {
						String tab = "\t\t";
						out = new FileWriter(outputFile);
						out.write("X"+ tab + "Y\n");
						for(int i=0; i<gray.length; i++)
							out.write(i+1 + tab + gray[i] + "\n");
						if (out != null) {
						       out.close();
						       textView0.setText("Location: " + outputFile);
						     }
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				else
					textView0.setText("Please select an image first!");
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
			if(grayValid){
				bmpGrayscale.recycle();
				grayValid = false;
			}
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
