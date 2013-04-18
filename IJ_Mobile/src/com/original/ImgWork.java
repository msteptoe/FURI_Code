/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2012 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
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

package com.original;

import imagej.data.Dataset;
import imagej.data.DefaultDataset;
import imagej.ui.DialogPrompt.Result;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.ImgPlus;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgOpener;
import net.imglib2.io.ImgSaver;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import java.io.File;

import net.imglib2.img.Img;

public final class ImgWork <T extends RealType<T> & NativeType<T>> {

	public ImgPlus<T> createImgPlus(String source) throws ImgIOException, IncompatibleTypeException{
		final ImgOpener imageOpener = new ImgOpener();
		final ImgPlus<T> imgPlus = imageOpener.openImg(source);
		return imgPlus;
	}
	
	public Img<T> createImg(String source) throws ImgIOException, IncompatibleTypeException{
		final ImgOpener imageOpener = new ImgOpener();
		final Img<T> img = imageOpener.openImg(source);
		return img;
	}

	public Dataset createDs(String source) throws ImgIOException, IncompatibleTypeException{
		final ImgOpener imageOpener = new ImgOpener();
		final ImgPlus<T> imgPlus = imageOpener.openImg(source);
		final Dataset dataset = new DefaultDataset(null, imgPlus);
		return dataset;
	}

	public void saveImg(String source, ImgPlus imp) {
		@SuppressWarnings("rawtypes")
		File outputFile = new File(source);
		boolean overwrite = true;
		Result result = null;

		// TODO prompts the user if the file is dirty or being saved to a new
		// location. Could remove the isDirty check to always overwrite the current
		// file
		if (outputFile.exists() &&
			(!outputFile.getAbsolutePath().equals(
				imp.getSource())))
		{
			/*result =
				uiService.showDialog("\"" + outputFile.getName() +
					"\" already exists. Do you want to replace it?", "Save [IJ2]",
					DialogPrompt.MessageType.WARNING_MESSAGE,
					DialogPrompt.OptionType.YES_NO_OPTION);
			overwrite = result == DialogPrompt.Result.YES_OPTION;*/
			overwrite = true;
		}

		if (overwrite) {
			final ImgSaver imageSaver = new ImgSaver();
			boolean saveImage = true;
			try {
				//imageSaver.addStatusListener(new StatusDispatcher(statusService));

				if (imageSaver.isCompressible(imp)) {
					/*result =
					uiService.showDialog("Your image contains axes other than XYZCT.\n"
						+ "When saving, these may be compressed to the "
						+ "Channel axis (or the save process may simply fail).\n"
						+ "Would you like to continue?", "Save [IJ2]",
						DialogPrompt.MessageType.WARNING_MESSAGE,
						DialogPrompt.OptionType.YES_NO_OPTION);*/
				}
				//saveImage = result == DialogPrompt.Result.YES_OPTION;
				
				saveImage = true;
				imageSaver.saveImg(outputFile.getAbsolutePath(), imp);
				//eventService.publish(new FileSavedEvent(img.getSource()));
			}
			catch (final ImgIOException e) {
				/*log.error(e);
				uiService.showDialog(e.getMessage(), "IJ2: Save Error",
					DialogPrompt.MessageType.ERROR_MESSAGE);*/
				System.out.println("Save Error");
				e.printStackTrace();
				return;
			}
			catch (final IncompatibleTypeException e) {
				/*log.error(e);
				uiService.showDialog(e.getMessage(), "IJ2: Save Error",
					DialogPrompt.MessageType.ERROR_MESSAGE);*/
				System.out.println("Save Error");
				e.printStackTrace();
				return;
			}

			if (saveImage) {
				/*dataset.setName(outputFile.getName());
				dataset.setDirty(false);

				display.setName(outputFile.getName());*/
				imp.setName(outputFile.getName());
				// NB - removed when display became ItemIO.Both.
				//  Restore later if necessary.
				//display.update();
			}
		}
	}
}
