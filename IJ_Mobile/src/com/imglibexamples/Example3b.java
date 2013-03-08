package com.imglibexamples;


import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgOpener;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import com.util.RealSum;

/**
 * Perform a generic computation of average intensity
 *
 * @author Stephan Preibisch & Stephan Saalfeld
 *
 */
public class Example3b
{
	public < T extends RealType< T > & NativeType< T > > Example3b(Img <T> img) throws
		ImgIOException, IncompatibleTypeException
	{
	
		// compute average of the image
		double avg = computeAverage( img );
		System.out.println( "average Value: " + avg );
	}

	/**
	 * Compute the average intensity for an {@link Iterable}.
	 *
	 * @param input - the input data
	 * @return - the average as double
	 */
	public < T extends RealType< T > > double computeAverage( final Iterable< T > input )
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

	public static void main( String[] args ) throws ImgIOException, IncompatibleTypeException
	{

		// run the example
		//new Example3b();
	}
}
