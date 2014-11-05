
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.awt.AWTException;

import java.awt.Dimension;

import java.awt.Rectangle;

import java.awt.Robot;

import java.io.File;

import java.io.IOException;

 

public class capture extends Thread{
int i=0;
    @Override
public void run() {

try {

Robot robot = new Robot();

 
while(true){

// Capture screen from the top left in 200 by 200 pixel size.
//

BufferedImage bufferedImage = robot.createScreenCapture(
new Rectangle(new Dimension(1024,768)));

 

//

// The captured image will the writen into a file called

// screenshot.png
//

File imageFile = new File("screenshot"+ (i++) +".jpg");

ImageIO.write(bufferedImage, "jpg", imageFile);
}
} catch (AWTException e) {

e.printStackTrace();

} catch (IOException e) {

e.printStackTrace();

}

}

}