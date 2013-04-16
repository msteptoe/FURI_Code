FURI_Code
=========

ImageJ Mobile

IJ_Mobile is fully working android project. Features are limited to ImageCalculator, PlotProfile, and Measure.
ImageCalculator and Measure are based upon ImageJ2's code.Image I/O is handled by Bio-Formats and stbi. AChartEngine is used to plot data for PlotProfile.No native services from ImageJ2 available.
GraphActivity.java:	Graphs data sent from PlotActivity
ImageActivity.java:	Shows result of ImageCalcActivity
ImageCalcActivity.java:	Process/Image Calculator activity implemented here
ListActivity.java:	Displays a scrollable list of data points from PlotActivity
MainActivity.java:	Home screen to pick operation
MeasureActivity.java:	Analyze/Measure activity implemented here
PlotActivity.java:	Analyze/Plot Profile activity/function implemented here
ImageCalc.java:		Process/Image Calculator activity implemented here, same as found in ImageJ2 with exception of function to set operand string.
MyDatasetService.java:	May No longer be necessary. Same as defaultDatasetService except context is always set to null and no logging.
Statistics.java:	Same as statistics from ImgLib and ImageJ2, slight changes.
Convert.java:		stbi library load createBMP, uses JNI.
ImgWork.java:		Can create Img, ImgPlus, and Dataset objects since services are unavailable, as well as save images as using ImgSaver.


ijExtract contains java files extracted from ImageJ2 and ImgLib2 that do not contain java.awt class.

IJ_Test1 is an old working android project, same base as IJ_Mobile just before radical restructuring and writing.

ImageJ2 is a non-android project for desktop to test out different functions and work around classes to later put on mobile.

Outside sources can be found at links below:
AChartEngine: https://code.google.com/p/achartengine/
Bio-Formats: http://loci.wisc.edu/software/bio-formats
ImageJ2 and ImgLib2: http://developer.imagej.net/
stbi: http://nothings.org/

List of known imports:
import imagej.AbstractContextual;
import imagej.AbstractUIDetails;
import imagej.BasicDetails;
import imagej.Cancelable;
import imagej.Contextual;
import imagej.ImageJ;
import imagej.Instantiable;
import imagej.InstantiableException;
import imagej.MenuEntry;
import imagej.MenuPath;
import imagej.Previewable;
import imagej.Prioritized;
import imagej.Priority;
import imagej.UIDetails;
import imagej.ValidityProblem;
import imagej.command.Command;
import imagej.command.CommandInfo;
import imagej.command.CommandService;
import imagej.command.ContextCommand;
import imagej.data.CalibratedInterval;
import imagej.data.Data;
import imagej.data.Dataset;
import imagej.data.DatasetService;
import imagej.data.PositionableByAxis;
import imagej.data.display.ImageDisplay;
import imagej.data.event.*;
import imagej.data.overlay.Overlay;
import imagej.display.Display;
import imagej.display.event.*;
import imagej.event.EventDetails;
import imagej.event.EventHandler;
import imagej.event.EventService;
import imagej.event.EventSubscriber;
import imagej.event.ImageJEvent;
import imagej.input.Accelerator;
import imagej.input.InputModifiers;
import imagej.input.KeyCode;
import imagej.log.*;
import imagej.menu.MenuConstants;
import imagej.menu.MenuService;
import imagej.menu.ShadowMenu;
import imagej.menu.event.*;
import imagej.module.*;
import imagej.object.*;
import imagej.platform.Platform;
import imagej.plugin.*; sezpoz
import imagej.service.AbstractService;
import imagej.service.Service;
import imagej.service.ServiceHelper;
import imagej.service.ServiceIndex;
import imagej.tool.Tool;
import imagej.util.CheckSezpoz;
import imagej.util.ColorRGB;
import imagej.util.ClassUtils;
import imagej.util.ListUtils;
import imagej.util.Log;
import imagej.util.Manifest;
import imagej.util.MiscUtils;
import imagej.util.NumberUtils;
import imagej.util.POM;
import imagej.util.RealRect;
import imagej.util.StringMaker;
import imagej.widget.InputHarvester;
import imagej.widget.InputPanel;
import imagej.widget.InputWidget;
import imagej.widget.UIComponent;
import imagej.widget.WidgetModel;
import net.imglib2.AbstractInterval;
import net.imglib2.AbstractLocalizingInt;
import net.imglib2.AbstractLocalizingCursorInt;
import net.imglib2.Cursor;
import net.imglib2.ExtendedRandomAccessibleInterval;
import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.Positionable;
import net.imglib2.RealInterval;
import net.imglib2.RealPositionable;
import net.imglib2.converter.Converter;
import net.imglib2.display.ColorTable;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.ImgPlus;
import net.imglib2.img.NativeImgFactory;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.basictypeaccess.*;
import net.imglib2.meta.*;
import net.imglib2.ops.condition.*;
import net.imglib2.ops.function.Function;
import net.imglib2.ops.function.general.GeneralBinaryFunction;
import net.imglib2.ops.function.general.GeneralUnaryFunction;
import net.imglib2.ops.function.real.RealImageFunction;
import net.imglib2.ops.img.*;
import net.imglib2.ops.input.InputIteratorFactory;
import net.imglib2.ops.input.PointInputIteratorFactory;
import net.imglib2.ops.operation.BinaryOperation;
import net.imglib2.ops.operation.UnaryOperation;
import net.imglib2.ops.operation.complex.*;
import net.imglib2.ops.operation.real.*;
import net.imglib2.ops.operation.subset.*;
import net.imglib2.ops.parse.*;
import net.imglib2.ops.pointset.*;
import net.imglib2.ops.relation.*;
import net.imglib2.ops.util.*;
import net.imglib2.outofbounds.OutOfBounds;
import net.imglib2.outofbounds.OutOfBoundsBorder;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.roi.RegionOfInterest;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.*;
import net.imglib2.util.Intervals;
import net.imglib2.util.IntervalIndexer;
