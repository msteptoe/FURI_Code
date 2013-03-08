package com.original;

import imagej.data.Dataset;
import imagej.data.DatasetService;
import imagej.event.EventService;
import imagej.event.StatusService;
import imagej.io.IOService;
import imagej.io.StatusDispatcher;
import imagej.io.event.FileSavedEvent;
import imagej.module.ModuleService;
import imagej.plugin.Parameter;
import imagej.plugin.Plugin;
import imagej.service.AbstractService;
import imagej.service.Service;
import imagej.ui.DialogPrompt;
import imagej.ui.DialogPrompt.Result;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.ImgPlus;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgOpener;
import net.imglib2.io.ImgSaver;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import java.io.File;

import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.ImgPlus;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.io.ImgIOException;
import net.imglib2.io.ImgOpener;
import net.imglib2.meta.AxisType;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

public final class ImgWork <T extends RealType<T> & NativeType<T>> {
	
	private Dataset dataset;
	
	public ImgPlus<T> createImgPlus(String source) throws ImgIOException, IncompatibleTypeException{
		final ImgOpener imageOpener = new ImgOpener();
		final ImgPlus<T> imgPlus = imageOpener.openImg(source);
		return imgPlus;
	}
	
	public Dataset buildDataset(ImgPlus img){
		AxisType [] ar = new AxisType [img.numDimensions()];
		for (int i =0; i < img.numDimensions(); i++){
			ar[i] = img.axis(i);
		}
		//dataset.setAxes(ar);
		dataset.setImgPlus(img);
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
