package com.ij_mobile;

import imagej.data.Dataset;

import java.io.File;
import java.io.IOException;

import com.modified.Statistics;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.ImgPlus;
import net.imglib2.io.ImgIOException;
import net.imglib2.ops.function.Function;
import net.imglib2.ops.function.real.StatCalculator;
import net.imglib2.ops.pointset.HyperVolumePointSet;
import net.imglib2.ops.pointset.PointSet;
import net.imglib2.type.numeric.real.DoubleType;

import com.original.Convert;
import com.original.ImgWork;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MeasureActivity extends Activity{

	private static int RESULT_LOAD_IMAGE = 1;
	protected int crop, converted, intDen, area;
	double mean;
	private String [] measurements;
	@SuppressWarnings("rawtypes") ImgPlus img1;
	Dataset ds1;
	protected String picLoc, bmpLoc;
	Convert c;
	private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measure_menu);
		measurements = new String[11];

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
					//System.out.println("Not Cropping!");
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
				if(bmpLoc != null){
					Statistics sts = new Statistics();
					final Function<long[], DoubleType> imgFunc = sts.imgFunc(ds1);
					PointSet region = new HyperVolumePointSet(ds1.getDims());
					@SuppressWarnings("rawtypes")
					StatCalculator stc = new StatCalculator(imgFunc, region);
					measurements[1] = stc.sampleStdDev() +"";
					//System.out.println(measurements[1] + " worked");
					measurements[2] = (int) stc.min()+"";
					measurements[3] = (int) stc.max()+"";
					mean = stc.arithmeticMean();
					intDen = (int)(mean*area);
					measurements[4] = mean+"";
					measurements[5] = stc.centroidXY().get1()+"";
					measurements[6] = stc.centroidXY().get2()+"";
					measurements[7] = stc.centerOfMassXY().get1()+"";
					measurements[8] = stc.centerOfMassXY().get2()+"";
					measurements[9] = intDen+"";
					measurements[10] = (int) stc.median() + "";
					measurements[7].indexOf(".");
					for(int i =1; i< 10; i++){
						int index = measurements[i].indexOf(".");
						if(index != -1 && index+4 < measurements[i].length())
							measurements[i] = measurements[i].substring(0, index+4);
					}
					//System.out.println(measurements[10] + " worked");
					buildTable();
				}
				else
					textView1.setText("Pease select an image first!");
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
				//System.out.println("path: "+picturePath);
			}
			c= new Convert();
			//System.out.println(picturePath);
			//System.out.println("Old Path1: " + picLoc);
			picLoc = picturePath;
			if (!picLoc.contains(".bmp")){
				//System.out.println("Creating new BMP!");
				bmpLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + "test1.bmp";
				c.createBMP(picLoc, bmpLoc);
				converted = 1;
			}
			else{
				bmpLoc = picLoc;
				converted = 0;
			}
			File tempFile = getTempFile();
			ImageView imageView = (ImageView) findViewById(R.id.imgView);
			Bitmap bmp = BitmapFactory.decodeFile(picLoc);
			area = bmp.getHeight() * bmp.getWidth();
			measurements[0] = area+"";
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 1500, 1500, false));
			bmp.recycle();
			if (tempFile.exists()) 
				tempFile.delete();

			File f = new File(bmpLoc);
			final String source = f.getAbsolutePath();
			//System.out.println(source);
			try {
				//System.out.println("Begin");
				@SuppressWarnings("rawtypes")
				final ImgWork imgw = new ImgWork();
				ds1 = imgw.createDs(source);
				//System.out.println("im thinking success");
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
	public void buildTable(){
		String [] str = {"Area: ", "StdDev: ", "Min: ", "Max: ", "Mean: ","Centeroid(X,Y): ", "Center of Mass(XM,YM): ", "Integrated Density: ", "Median: "};
		TableLayout tv=(TableLayout) findViewById(R.id.table);
		tv.removeAllViewsInLayout();
		int sc =0;
		for(int i=0;i<measurements.length; i++)
		{
			TableRow tr=new TableRow(MeasureActivity.this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			TextView b0=new TextView(MeasureActivity.this);
			/*b1.setPadding(10, 0, 0, 0);
			b1.setTextSize(15);
			String str1=json_data.getString("column2");*/
			b0.setText(str[sc]);
			b0.setTextColor(Color.BLACK);
			tr.addView(b0);
			if(i <5 || i>8){
				TextView b1=new TextView(MeasureActivity.this);
				b1.setText(measurements[i]+"");
				b1.setTextColor(Color.BLUE);
				//b3.setTextSize(15);
				tr.addView(b1);
			}
			else{
				TextView b1=new TextView(MeasureActivity.this);
				b1.setText(measurements[i]+", " + measurements[++i]);
				b1.setTextColor(Color.BLUE);
				//b3.setTextSize(15);
				tr.addView(b1);
			}


			tv.addView(tr);
			final View vline1 = new View(MeasureActivity.this);
			vline1.setLayoutParams(new                
					TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
			vline1.setBackgroundColor(Color.BLACK);
			tv.addView(vline1);  // add line below each row 
			sc++;

			/*{
			JSONObject json_data = jArray.getJSONObject(i);

			TextView b=new TextView(Yourclassname.this);
			String str=String.valueOf(json_data.getInt("column1"));
			b.setText(str);
			b.setTextColor(Color.RED);
			b.setTextSize(15);
			tr.addView(b);


			TextView b1=new TextView(Yourclassname.this);
			b1.setPadding(10, 0, 0, 0);
			b1.setTextSize(15);
			String str1=json_data.getString("column2");
			b1.setText(str1);
			b1.setTextColor(Color.WHITE);
			tr.addView(b1);

			TextView b2=new TextView(Yourclassname.this);
			b2.setPadding(10, 0, 0, 0);
			String str2=String.valueOf(json_data.getInt("column3"));
			b2.setText(str2);
			b2.setTextColor(Color.RED);
			b2.setTextSize(15);
			tr.addView(b2);

			tv.addView(tr);


			final View vline1 = new View(Yourclassname.this);
			vline1.setLayoutParams(new                
					TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
			vline1.setBackgroundColor(Color.WHITE);
			tv.addView(vline1);  // add line below each row   


		}*/

		}
	}
}

