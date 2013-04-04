package com.ij_mobile;

import imagej.data.Dataset;

import java.io.File;
import java.io.IOException;

import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.ImgPlus;
import net.imglib2.io.ImgIOException;

import com.imglibexamples.Example3a1;
import com.imglibexamples.Example3b;
import com.original.Convert;
import com.original.ImgWork;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MeasureActivity extends Activity{
	
	private static int RESULT_LOAD_IMAGE = 1;
	protected int crop, converted;
	@SuppressWarnings("rawtypes") ImgPlus img1;
	Dataset ds1;
	protected String picLoc, bmpLoc;
	Convert c;
	private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure_menu);

		Button buttonM1 = (Button) findViewById(R.id.buttonM1);
		final Button buttonLoadImage1 = (Button) findViewById(R.id.buttonSP1);
		final CheckBox cropBox = (CheckBox) findViewById(R.id.cropBox);
		final TextView textView1= (TextView) findViewById(R.id.textView1);
		
		buttonLoadImage1.setTag(0);
		buttonLoadImage1.setText("Select Picture");
		
		cropBox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((CheckBox) v).isChecked()){
					buttonLoadImage1.setText("Select Region");
					crop =1;
					buttonLoadImage1.setTag(1);

				}
				else{
					buttonLoadImage1.setText("Select Picture");
					crop =0;
					buttonLoadImage1.setTag(0);
				}
			}
		});
		
		buttonLoadImage1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {


				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				if(crop == 0) {
					System.out.println("Not Cropping!");
				} else {
					i.setType("image/*");
					i.putExtra("crop", "true");
					i.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
					i.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				}
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
		buttonM1.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				if(ds1 != null){
					try {
						img1 = ds1.getImgPlus();
						new Example3a1(img1.getImg());
						new Example3b(img1.getImg());
					} catch (ImgIOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IncompatibleTypeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					textView1.setText("Pease Select Image First!");
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			String picturePath;
			if(crop ==0){
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				
			}
			else{
                picturePath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                		+ File.separator + TEMP_PHOTO_FILE;
                System.out.println("path: "+picturePath);
			}
			c= new Convert();
			System.out.println(picturePath);
			System.out.println("Old Path1: " + picLoc);
			picLoc = picturePath;
			if (!picLoc.contains(".bmp")){
				System.out.println("Creating new BMP!");
				bmpLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + "test1.bmp";
				c.createBMP(picLoc, bmpLoc);
				converted = 1;
			}
			else{
				bmpLoc = picLoc;
				converted = 0;
			}
			File tempFile = getTempFile();
			if (tempFile.exists()) 
				tempFile.delete();
			
			File f = new File(bmpLoc);
			final String source = f.getAbsolutePath();
			System.out.println(source);
			try {
				System.out.println("Begin");
				final ImgWork imgw = new ImgWork();
				ds1 = imgw.createDs(source);
				System.out.println("im thinking success");
			} catch (ImgIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IncompatibleTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(converted == 1){
				File temp = new File(bmpLoc);
				temp.delete();
			}
		}
	}
}
