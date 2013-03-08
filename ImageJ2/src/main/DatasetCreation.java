package main;

import imagej.command.ContextCommand;
import imagej.data.Dataset;
import imagej.io.IOService;
import imagej.io.plugins.OpenImage;
import imagej.log.LogService;
import imagej.menu.MenuConstants;
import imagej.module.ItemIO;
import imagej.plugin.Menu;
import imagej.plugin.Parameter;
import imagej.plugin.Plugin;
import imagej.ui.DialogPrompt;
import imagej.ui.UIService;

import java.io.File;

import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.io.ImgIOException;

public class DatasetCreation {

	private LogService log;

	private IOService ioService;

	private UIService uiService;

	private File inputFile;

	private Dataset dataset;
	
	public void run() {
		File f = new File("testing2.bmp");
		OpenImage im = new OpenImage();
		final String source = f.getAbsolutePath();
		im.setInputFile(f);
		im.run();
		/*try {
			dataset = ioService.loadDataset(source);
		}
		catch (final ImgIOException e) {
			log.error(e);
			uiService.showDialog(e.getMessage(),
				DialogPrompt.MessageType.ERROR_MESSAGE);
		}
		catch (final IncompatibleTypeException e) {
			log.error(e);
			uiService.showDialog(e.getMessage(),
				DialogPrompt.MessageType.ERROR_MESSAGE);
		}*/
	}
}
