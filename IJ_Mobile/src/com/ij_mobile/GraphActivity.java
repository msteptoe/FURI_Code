package com.ij_mobile;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class GraphActivity extends Activity{
	
	private GraphicalView mChart;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private XYSeries mCurrentSeries;

    private XYSeriesRenderer mCurrentRenderer;
    
    private double [] gray;
    
    //String title;

    private void initChart() {
        mCurrentSeries = new XYSeries("Pixel Data");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mCurrentRenderer.setColor(Color.BLACK);
        mCurrentRenderer.setLineWidth(2);
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mRenderer.setXTitle("Distance (pixels)");
        mRenderer.setYTitle("Gray Value");
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setAxesColor(Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        for(int i=0; i< mRenderer.getScalesCount(); i++){
        	mRenderer.setYLabelsColor(i, Color.BLACK);
        }
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setMarginsColor(Color.WHITE);
        mRenderer.setGridColor(Color.GRAY);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setShowLegend(false);
        mRenderer.setShowGrid(true);
        mRenderer.setAxisTitleTextSize(28);
        mRenderer.setLabelsTextSize(24);
        mRenderer.setPointSize(5f);
    }

    private void addData() {
    	
    	for(int i =0; i<gray.length; i++){
    		mCurrentSeries.add(i+1, gray[i]);
    	}
    }
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot_view);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    gray = extras.getDoubleArray("gray");
		    //title = extras.getString("title");
		}
	}
	
	 protected void onResume() {
	        super.onResume();
	        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
	        if (mChart == null) {
	            initChart();
	            addData();
	            //mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer, 0.3f);
	            mChart = ChartFactory.getLineChartView(this, mDataset, mRenderer);
	            layout.addView(mChart);
	        } else {
	            mChart.repaint();
	        }
	    }
	
}
