/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
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

package net.imglib2;

import net.imglib2.util.Util;

/**
 * Abstract implementation of {@link Cursor}. Java's {@link java.util.Iterator}
 * interface is implemented by mapping to abstract {@link #fwd()} and
 * {@link #get()}.
 *
 * <p>
 * For localization, default implementations are available that all build on the
 * abstract int variant. For particular cursors, this may be implemented more
 * efficiently saving at least one loop over <em>n</em>.
 *
 * <p>
 * This is identical to {@link AbstractCursor}, except that default
 * implementations build on the abstract int instead of long variant here.
 *
 * @param <T>
 * @author Stephan Preibisch
 * @author Stephan Saalfeld
 * @author Tobias Pietzsch
 */
public abstract class AbstractCursorInt< T > extends AbstractEuclideanSpace implements Cursor< T >
{
	/**
	 * used internally to forward all localize() versions to the (abstract)
	 * int[] version.
	 */
	final private int[] tmp;

	/**
	 * @param n number of dimensions in the {@link net.imglib2.img.Img}.
	 */
	public AbstractCursorInt( final int n )
	{
		super( n );
		tmp = new int[ n ];
	}

	@Override
	public void remove()
	{
		// NB: no action.
	}

	@Override
	public T next()
	{
		fwd();
		return get();
	}

	/**
	 * Highly recommended to override this with a more efficient version.
	 *
	 * @param steps
	 */
	@Override
	public void jumpFwd( final long steps )
	{
		for ( long j = 0; j < steps; ++j )
			fwd();
	}

	@Override
	public void localize( final float[] pos )
	{
		localize( this.tmp );
		for ( int d = 0; d < n; d++ )
			pos[ d ] = this.tmp[ d ];
	}

	@Override
	public void localize( final double[] pos )
	{
		localize( this.tmp );
		for ( int d = 0; d < n; d++ )
			pos[ d ] = this.tmp[ d ];
	}

	@Override
	public void localize( final long[] pos )
	{
		localize( this.tmp );
		for ( int d = 0; d < n; d++ )
			pos[ d ] = this.tmp[ d ];
	}

	@Override
	public float getFloatPosition( final int d )
	{
		return getIntPosition( d );
	}

	@Override
	public double getDoublePosition( final int d )
	{
		return getIntPosition( d );
	}

	@Override
	public long getLongPosition( final int d )
	{
		return getIntPosition( d );
	}

	@Override
	public String toString()
	{
		localize( tmp );
		return Util.printCoordinates( tmp ) + " = " + get();
	}

	@Override
	abstract public AbstractCursorInt< T > copy();

	@Override
	abstract public AbstractCursorInt< T > copyCursor();
}
