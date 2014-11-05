import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.*;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;


public class convert extends Thread {

public static final String OUTPUT_FILE_FLV = "outputFile.flv";

private IContainer outContainer;
private IStream outStream;
private IStreamCoder outStreamCoder;

private long timeStamp = 0;
private Robot robot;
private Toolkit toolkit;
private Rectangle screenBounds;

/**
* @param args
*/

    @Override
public void run(){
	
//	encodeImage(takeSingleSnapshot());}
	try{
		robot = new Robot();
		}catch (AWTException e){
		System.out.println(e.getMessage());
		}
		toolkit = Toolkit.getDefaultToolkit();
		screenBounds = new Rectangle(toolkit.getScreenSize());

		setupStreams();


int index = 0;
while(true){

	encodeImage(takeSingleSnapshot());
 
try{
Thread.sleep(333);
} catch(InterruptedException e)
{
e.printStackTrace(System.out);
}
index ++;
}
}


public convert(){
}

public void setupStreams(){
outContainer = IContainer.make();

int retval = outContainer.open(OUTPUT_FILE_FLV, IContainer.Type.WRITE, null);
if (retval <0)
throw new RuntimeException("could not open output file");

outStream = outContainer.addNewStream(0);
outStreamCoder = outStream.getStreamCoder();

ICodec codec = ICodec.guessEncodingCodec(null, null, OUTPUT_FILE_FLV, null, ICodec.Type.CODEC_TYPE_VIDEO);

outStreamCoder.setNumPicturesInGroupOfPictures(30);
outStreamCoder.setCodec(codec);

outStreamCoder.setBitRate(25000);
outStreamCoder.setBitRateTolerance(9000);

int width = toolkit.getScreenSize().width;
int height = toolkit.getScreenSize().height;

outStreamCoder.setPixelType(IPixelFormat.Type.YUV420P);
outStreamCoder.setHeight(height);
outStreamCoder.setWidth(width);
outStreamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
outStreamCoder.setGlobalQuality(0);

IRational frameRate = IRational.make(3,1);
outStreamCoder.setFrameRate(frameRate);
outStreamCoder.setTimeBase(IRational.make(frameRate.getDenominator(), frameRate.getNumerator()));
frameRate = null;

retval = outStreamCoder.open();
if (retval <0)
throw new RuntimeException("could not open input decoder");
retval = outContainer.writeHeader();
if (retval <0)
throw new RuntimeException("could not write file header");
}

public void encodeImage(BufferedImage originalImage){
ByteArrayOutputStream byteConvert = new ByteArrayOutputStream();
BufferedImage jpegImage = null;
try{
ImageIO.write(originalImage, "jpeg", byteConvert);


byte[] imageData = byteConvert.toByteArray();
InputStream imageData2 = new ByteArrayInputStream(imageData);
jpegImage = ImageIO.read(imageData2);
} catch(Exception e){
e.printStackTrace(System.out);
}
IPacket packet = IPacket.make();

IConverter converter = null;
try{
converter = ConverterFactory.createConverter(jpegImage, IPixelFormat.Type.YUV420P);
} catch(UnsupportedOperationException e){
System.out.println(e.getMessage());
e.printStackTrace(System.out);
}

IVideoPicture outFrame = converter.toPicture(jpegImage, timeStamp);
timeStamp += 333000;

outFrame.setQuality(0);
int retval = outStreamCoder.encodeVideo(packet, outFrame, 0);
if (retval < 0)
throw new RuntimeException("could not encode video");
if (packet.isComplete()){
retval = outContainer.writePacket(packet);
if (retval < 0)
throw new RuntimeException("could not save packet to container");
}
}

public void closeStreams(){
int retval = outContainer.writeTrailer();
if (retval < 0)
throw new RuntimeException("Could not write trailer to output file");
}

public BufferedImage takeSingleSnapshot(){
return robot.createScreenCapture(this.screenBounds);
}

}