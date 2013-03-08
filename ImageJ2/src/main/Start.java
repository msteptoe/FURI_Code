package main;

import java.io.File;
import java.util.Iterator;

import loci.formats.ImageWriter;
import loci.formats.out.JPEGWriter;


import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgPlus;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgSaver;
import net.imglib2.ops.img.ImageCombiner;
import net.imglib2.ops.operation.BinaryOperation;
import net.imglib2.ops.operation.real.binary.RealAdd;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.RealSum;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.io.ImgOpener;
import net.imglib2.type.NativeType;
import imagej.core.commands.calculator.ImageCalculator;
import imagej.core.commands.debug.MeasurementDemo;
import imagej.data.Dataset;
import imagej.data.DatasetService;
import imagej.data.DefaultDatasetService;
import imagej.io.IOService;
import imagej.service.ServiceHelper;
import imagej.ImageJ;

public class Start {
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, U, V> void main(String [] args){
		final Dataset dataset, dataset2, dataset3;
		ImgPlus img12 = null;
		Dataset output;
		ImageJ ij = new ImageJ();
		IOService io = ij.io();
		DefaultDatasetService dds = new DefaultDatasetService();
		DatasetService ds = ij.dataset();
		File f = new File("testing.bmp");
		File f2 = new File("testing2.bmp");
		final String source = f.getAbsolutePath();
		final String source2 = f2.getAbsolutePath();
		try {
			ImgWork imgw = new ImgWork();
			dataset = io.loadDataset(source);
			dataset2 = io.loadDataset(source2);
			dataset3 = imgw.createDs(source);
			System.out.println("info: "+ dataset.getTypeLabelLong());
			System.out.println("Source: " + source);
    		System.out.println(source);
    		System.out.println(imgw.createImgPlus(source));
    		ImgPlus img1 = imgw.createImgPlus(source);
    		System.out.println("serv:" + dataset.toString() + " mine:" + dataset3.toString());
    		ImgPlus img2 = imgw.createImgPlus(source2);
    		Img <T> imgt = imgw.createImg(source);
    		Start st = new Start();
    		
    		double avg = st.computeAverage( img1 );
    		System.out.println( "average Value: " + avg );
    		new Example3a1();
    		new Example3b();
    		
    		System.out.println("comparing (orig.orig, my.my): " + dataset.getImgPlus().size() + "." + dataset2.getImgPlus().size() + "," + img1.size() + "." + img2.size());
    		System.out.println(img1.size());
    		System.out.println("dataset imgplus size =" + dataset.getImgPlus().size());
    		System.out.println();
    		System.out.println("i'm thinking it worked");
    		final ImgSaver imageSaver = new ImgSaver();
    		
    		File f5 = new File("writetest1.jpg");
    		final ImgPlus img5 = dataset.getImgPlus();
    		imageSaver.saveImg(f5.getAbsolutePath(), img5);
    		
    		ImageCalc ic = new ImageCalc();
    		ic.setInput1(dataset);
    		ic.setInput2(dataset2);
    		ic.setDoubleOutput(false);
    		ic.setNewWindow(false);
    		ic.run();
    		output = ic.getInput1();
    		final ImgPlus img = output.getImgPlus();
    		/*@SuppressWarnings("unchecked")
    		Img<U> i1 = (Img<U>) dataset.getImgPlus();
    		@SuppressWarnings("unchecked")
    		Img<V> i2 = (Img<V>) dataset2.getImgPlus();
    		operator =new RealAdd<U,V,DoubleType>();
    		img = ImageCombiner.applyOp(operator, i1, i2, new ArrayImgFactory<DoubleType>(), new DoubleType());*/
    		File f3 = new File("donetest.jpg");
    		File f6 = new File("writetest2.jpg");
    		System.out.println(f3.getAbsolutePath());
    		imageSaver.saveImg(f3.getAbsolutePath(), img);
    		
    		final ImgPlus img6 = dataset2.getImgPlus();
    		imageSaver.saveImg(f6.getAbsolutePath(), img6);
    		
    		
    		System.out.println("comparing (orig.orig, my.my): " + dataset.getImgPlus().size() + "." + dataset2.getImgPlus().size() + "," + img1.size() + "." + img2.size());
    		ic.opTest(img1, img2);
    		File f12 = new File("alternative.png");
    		output = ds.create(img1);
    		img12 = output.getImgPlus();
    		imgw.saveImg(f12.getAbsolutePath(), img12);
    		System.out.println("Comparing op (orig, my): " + img.size() + "," + img1.size() + " " + img.getValidBits() + "," + img2.getValidBits());
    	
    		
    		
		} catch (ImgIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncompatibleTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  < T extends RealType< T > > double computeAverage( final Iterable< T > input )
	{
		// Count all values using the RealSum class.
		// It prevents numerical instabilities when adding up millions of pixels
		RealSum realSum = new RealSum();
		long count = 0;

		for ( final T type : input )
		{
			realSum.add( type.getRealDouble() );
			++count;
		}

		return realSum.getSum() / count;
	}
	
	public < T extends Comparable< T > & Type< T > > void computeMinMax(
			final Iterable< T > input, final T min, final T max )
		{
			// create a cursor for the image (the order does not matter)
			final Iterator< T > iterator = input.iterator();

			// initialize min and max with the first image value
			T type = iterator.next();

			min.set( type );
			max.set( type );

			// loop over the rest of the data and determine min and max value
			while ( iterator.hasNext() )
			{
				// we need this type more than once
				type = iterator.next();

				if ( type.compareTo( min ) < 0 )
					min.set( type );

				if ( type.compareTo( max ) > 0 )
					max.set( type );
			}
		}
}
