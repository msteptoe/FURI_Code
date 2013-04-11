package com.ij_mobile;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ListActivity extends Activity{

	private double [] gray;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			gray = extras.getDoubleArray("gray");
		}
		buildTable();
	}
	public void buildTable(){
		TableLayout tv=(TableLayout) findViewById(R.id.table);
		tv.removeAllViewsInLayout();
		for(int i=0;i<gray.length+1; i++)
		{
			TableRow tr=new TableRow(ListActivity.this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));

			TextView b0=new TextView(ListActivity.this);
			TextView b1=new TextView(ListActivity.this);
			/*b1.setPadding(10, 0, 0, 0);
			b1.setTextSize(15);
			String str1=json_data.getString("column2");*/
			if(i == 0){
				b0.setText("X");
				b0.setTextColor(Color.BLACK);
				b0.setBackgroundColor(Color.GRAY);
				b0.setTextAppearance(this, android.R.style.TextAppearance_Large);
				b1.setText("Y");
				b1.setTextColor(Color.BLACK);
				b1.setBackgroundColor(Color.GRAY);
				b1.setTextAppearance(this, android.R.style.TextAppearance_Large);
			}
			else{
				b0.setText(i+"");
				b0.setTextColor(Color.BLACK);
				b0.setTextSize(15);
				String g = gray[i-1] +"";
				if(g.length() > g.lastIndexOf(".")+5)
					b1.setText(g.substring(0, g.lastIndexOf(".")+5));
				else
					b1.setText(g);
				b1.setTextColor(Color.BLACK);
				b1.setTextSize(15);
			}
			//b3.setTextSize(15);
			b0.setPadding(20, 0, 10, 0);
			b1.setPadding(10, 0, 0, 0);
			tr.addView(b0);
			tr.addView(b1);
			tv.addView(tr);
			final View vline1 = new View(ListActivity.this);
			vline1.setLayoutParams(new                
					TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
			vline1.setBackgroundColor(Color.BLACK);
			tv.addView(vline1);  // add line below each row 
		}
	}
}
