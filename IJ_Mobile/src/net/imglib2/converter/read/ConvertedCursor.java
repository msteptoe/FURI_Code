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

package net.imglib2.converter.read;

import net.imglib2.Cursor;
import net.imglib2.converter.AbstractConvertedCursor;
import net.imglib2.converter.Converter;
import net.imglib2.type.Type;

/**
 * TODO
 *
 */
public class ConvertedCursor< A, B extends Type< B > > extends AbstractConvertedCursor< A, B >
{
	final protected Converter< A, B > converter;
	final protected B converted;

	/**
	 * Creates a copy of b for conversion that can be accessed through {@link #get()}.
	 * 
	 * @param source
	 * @param converter
	 * @param b
	 */
	public ConvertedCursor( final Cursor< A > source, final Converter< A, B > converter, final B b )
	{
		super( source );
		this.converter = converter;
		this.converted = b.copy();
	}

	@Override
	public B get()
	{
		converter.convert( source.get(), converted );
		return converted;
	}

	@Override
	public ConvertedCursor< A, B > copy()
	{
		return new ConvertedCursor< A, B >( ( Cursor< A > ) source.copy(), converter, converted );
	}
}
