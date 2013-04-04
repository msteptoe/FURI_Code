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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ImageCalcActivity extends Activity implements OnItemSelectedListener{
	
	private static int RESULT_LOAD_IMAGE = 1;
	protected String picLoc1, picLoc2, bmpLoc1, bmpLoc2, op;
	protected int picLoc =0;
	Convert c, d;
	
	@SuppressWarnings("rawtypes") ImgPlus img1;
	@SuppressWarnings("rawtypes") ImgPlus img2;
	@SuppressWarnings("rawtypes") ImgPlus output;
	Dataset ds1, ds2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_calculator);
		
		bmpLoc1 = null;
		bmpLoc2 = null;
		
		Button buttonLoadImage1 = (Button) findViewById(R.id.buttonLP1);
		Button buttonLoadImage2 = (Button) findViewById(R.id.buttonLP2);
		Button buttonImp1 = (Button) findViewById(R.id.buttonIMP1);
		Button buttonImp2 = (Button) findViewById(R.id.buttonIMP2);
		Button buttonCalc = (Button) findViewById(R.id.buttonCALC);
		Spinner operations = (Spinner) findViewById(R.id.operations);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.operations, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		operations.setAdapter(adapter);
		operations.setOnItemSelectedListener(this);
		
		@SuppressWarnings("rawtypes")final ImgWork imgw = new ImgWork();
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

		buttonImp1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if(bmpLoc1 !=null){
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
				}
				else
					System.out.println("No Image Selected!");
			}
		});
		
		buttonCalc.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				//img1 = ds1.getImgPlus();
				//img2 = ds2.getImgPlus();
				File f12 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + op +".png");
				//ic.opTest(img1, img2);
				@SuppressWarnings("rawtypes")
				ImageCalc ic = new ImageCalc();
	    		ic.setInput1(ds1);
	    		ic.setInput2(ds2);
	    		ic.setDoubleOutput(false);
	    		ic.setNewWindow(false);
	    		if(op != null)
	    			ic.setOpName(op);
	    		ic.run();
				imgw.saveImg(f12.getAbsolutePath(), ic.getInput1().getImgPlus());
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
        // An item was selected. You can retrieve the selected item using
        op = (String) parent.getItemAtPosition(pos);
        System.out.println("Testing operation selection: " + op);
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
