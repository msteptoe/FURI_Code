package com.ij_mobile;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.original.Convert;
import com.original.ImgWork;
import com.original.ImageCalc;
import com.imglibexamples.Example3a1;
import com.imglibexamples.Example3b;

import java.io.File;

import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.ImgPlus;
import net.imglib2.io.ImgIOException;

public class MainActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	protected String picLoc1, picLoc2, bmpLoc1, bmpLoc2;
	protected int picLoc =0;
	Convert c, d;
	
	@SuppressWarnings("rawtypes") ImgPlus img1;
	@SuppressWarnings("rawtypes") ImgPlus img2;

	@SuppressWarnings("rawtypes")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bmpLoc1 = null;
		bmpLoc2 = null;
		Button buttonLoadImage1 = (Button) findViewById(R.id.buttonLP1);
		Button buttonLoadImage2 = (Button) findViewById(R.id.buttonLP2);
		Button buttonImp1 = (Button) findViewById(R.id.buttonIMP1);
		Button buttonImp2 = (Button) findViewById(R.id.buttonIMP2);
		Button buttonM1 = (Button) findViewById(R.id.buttonM1);
		Button buttonM2 = (Button) findViewById(R.id.buttonM2);
		Button buttonCalc = (Button) findViewById(R.id.buttonCALC);
		final ImgWork imgw = new ImgWork();
		final ImageCalc ic = new ImageCalc();
		buttonLoadImage1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				picLoc =1;
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		buttonLoadImage2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				picLoc =2;
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});

		buttonImp1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if(bmpLoc1 !=null){
					File f = new File(bmpLoc1);
					final String source1 = f.getAbsolutePath();
					System.out.println(source1);
					try {
						System.out.println("Begin");
						img1 = imgw.createImgPlus(source1);
						System.out.println("im thinking success");
					} catch (ImgIOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IncompatibleTypeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					System.out.println("Please select image 1!");
			}
		});
		buttonImp2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if(bmpLoc2 !=null){
					File f2 = new File(bmpLoc2);
					final String source2 = f2.getAbsolutePath();
					try {
						System.out.println("Begin");
						img2 = imgw.createImgPlus(source2);
						System.out.println("im thinking success");
					} catch (ImgIOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IncompatibleTypeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					System.out.println("No Image Selected!");
			}
		});
		buttonM1.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) {
				try {
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
		});
		buttonM2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		buttonCalc.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				File f12 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator +"AdditionTest.png");
				ic.opTest(img1, img2);
				imgw.saveImg(f12.getAbsolutePath(), img1);
				System.out.println("Saved!");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			c= new Convert();
			System.out.println(picturePath);
			if(picLoc == 1){
				System.out.println("Old Path1: " + picLoc1);
				picLoc1 = picturePath;
				if (!picLoc1.contains(".bmp")){
					System.out.println("Creating new BMP!");
					bmpLoc1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + "test1.bmp";
					c.createBMP(picLoc1, bmpLoc1);
				}
				else
					bmpLoc1 = picLoc1;
			}
			else{
				System.out.println("Old Path2: " + picLoc2);
				picLoc2 = picturePath;
				if(!picLoc2.contains(".bmp")){
					System.out.println("Creating new BMP!");
					bmpLoc2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + "test2.bmp";
					c.createBMP(picLoc2, bmpLoc2);
				}
				else
					bmpLoc2 = picLoc2;
			}
		}
	}
}
