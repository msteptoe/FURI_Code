package com.ij_mobile;

import java.io.File;

import imagej.data.Dataset;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.ImgPlus;
import net.imglib2.io.ImgIOException;

import com.modified.ImageCalc;
import com.original.Convert;
import com.original.ImgWork;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ImageCalcActivity extends Activity implements OnItemSelectedListener{

	private static int RESULT_LOAD_IMAGE = 1;
	protected String picLoc1, picLoc2, bmpLoc1, bmpLoc2, op, opImg1, opImg2;
	protected int picLoc =0, converted1, converted2;
	Convert c, d;

	@SuppressWarnings("rawtypes") ImgPlus img1;
	@SuppressWarnings("rawtypes") ImgPlus img2;
	@SuppressWarnings("rawtypes") ImgPlus output;
	Dataset ds1, ds2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_calculator);
		@SuppressWarnings("rawtypes")
		final ImgWork imgw = new ImgWork();
		Button buttonLoadImage1 = (Button) findViewById(R.id.buttonLP1);
		Button buttonLoadImage2 = (Button) findViewById(R.id.buttonLP2);
		Button buttonCalc = (Button) findViewById(R.id.buttonCALC);
		Spinner operations = (Spinner) findViewById(R.id.operations);
		Spinner images1 = (Spinner) findViewById(R.id.item1);
		Spinner images2 = (Spinner) findViewById(R.id.item2);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.operations, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		operations.setAdapter(adapter);
		operations.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
				R.array.images, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		images1.setAdapter(adapter2);
		images1.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
				R.array.images, android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		images2.setAdapter(adapter3);
		images2.setOnItemSelectedListener(this);
		images2.setTag("images2");
		images1.setTag("images1");
		operations.setTag("operations");

		@SuppressWarnings({ "rawtypes", "unused" }) final ImageCalc ic = new ImageCalc();

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

		buttonCalc.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				@SuppressWarnings("rawtypes")
				ImageCalc ic = new ImageCalc();
				TextView textView1= (TextView) findViewById(R.id.textView1);
				System.out.println("opImg1: " + opImg1 + " opImg2: " + opImg2 + " op: " + op);
				if(opImg1 != null && opImg1.contains("Image 1")){
					if(bmpLoc1 !=null)
						ic.setInput1(ds1);
					else{
						textView1.setText("Please select image 1!");
						System.out.println("opImg1 ds1 null");
						return;
					}
				}
				else{
					if(bmpLoc2 != null)
						ic.setInput1(ds2);
					else{
						textView1.setText("Please select image 2!");
						System.out.println("opImg1 ds2 null");
						return;
					}
				}
				if(opImg2.contains("Image 1")){
					if(bmpLoc1 != null)
						ic.setInput2(ds1);
					else{
						textView1.setText("Please select image 1!");
						System.out.println("opImg2 ds1 null");
						return;
					}
				}
				else{
					if(bmpLoc2 != null)
						ic.setInput2(ds2);
					else{
						textView1.setText("Please select image 2!");
						System.out.println("opImg2 ds2 null");
						return;
					}
				}
				File f12 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + op +".png");
				if(op != null)
					ic.setOpName(op);
				ic.run();
				imgw.saveImg(f12.getAbsolutePath(), ic.getOutput().getImgPlus());
				System.out.println("Saved!");
				Intent intentImageView = new Intent(ImageCalcActivity.this, ImageActivity.class);
				intentImageView.putExtra("result", op);
				intentImageView.putExtra("loc", f12.getAbsolutePath());
				startActivity(intentImageView);
			}
		});
	}
	public void onItemSelected(AdapterView<?> parent, View view, 
			int pos, long id) {
		if(parent.getTag() == "operations"){
			op = (String) parent.getItemAtPosition(pos);
			System.out.println("Testing operation selection: " + op);
		}
		else if(parent.getTag() == "images1"){
			opImg1 =(String) parent.getItemAtPosition(pos);
			System.out.println("Img1 selection: " + opImg1);
		}
		else{
			opImg2 =(String) parent.getItemAtPosition(pos);
			System.out.println("Img2 selection: " + opImg2);
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			@SuppressWarnings("rawtypes")
			final ImgWork imgw = new ImgWork();
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
					converted1 = 1;
				}
				else{
					bmpLoc1 = picLoc1;
					converted1 = 0;
				}
				File f = new File(bmpLoc1);
				final String source1 = f.getAbsolutePath();
				System.out.println(source1);
				try {
					System.out.println("Begin");
					//img1 = imgw.createImgPlus(source1);
					ds1 = imgw.createDs(source1);
					System.out.println("im thinking success");
				} catch (ImgIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IncompatibleTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(converted1 ==1)
					f.delete();
				ImageView imageView = (ImageView) findViewById(R.id.imgView1);
				Bitmap bmp = BitmapFactory.decodeFile(picLoc1);
				imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 1500, 1500, false));
				bmp.recycle();
			}
			else{
				System.out.println("Old Path2: " + picLoc2);
				picLoc2 = picturePath;
				if(!picLoc2.contains(".bmp")){
					System.out.println("Creating new BMP!");
					bmpLoc2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + "test2.bmp";
					c.createBMP(picLoc2, bmpLoc2);
					converted2 = 1;
				}
				else{
					bmpLoc2 = picLoc2;
					converted2 = 0;
				}
				File f2 = new File(bmpLoc2);
				final String source2 = f2.getAbsolutePath();
				try {
					System.out.println("Begin");
					//img2 = imgw.createImgPlus(source2);
					ds2 = imgw.createDs(source2);
					System.out.println("im thinking success");
				} catch (ImgIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IncompatibleTypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(converted2 ==1)
					f2.delete();
				ImageView imageView = (ImageView) findViewById(R.id.imgView2);
				Bitmap bmp = BitmapFactory.decodeFile(picLoc2);
				imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 1500, 1500, false));
				bmp.recycle();
			}
		}
	}
}
