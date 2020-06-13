import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Observable;

/*
One of the image processes that can happen.
It sharpens the image
 */
public class Process_Sharpen extends Observable {
    public void Process(BufferedImage img) throws InterruptedException {
        //makes two image refences
        BufferedImage beforeSharpen = img;
        BufferedImage AfterSharpen = new BufferedImage(img.getColorModel(),img.copyData(null),img.isAlphaPremultiplied(), null);;
        //adds image before changes
        notifyImageUpdate(beforeSharpen);

        //starts image sharping
        Kernel kernel= new Kernel(3,3,new float[]{
                -1, -1, -1,
                -1, 9, -1,
                -1, -1, -1
        });
        BufferedImageOp op = new ConvolveOp(kernel);
        AfterSharpen = op.filter(AfterSharpen,null);

        //add image at the end of the process
        Thread.sleep(1000);
        notifyImageUpdate(AfterSharpen);
    }

    /**
     * Notifies any observers that the processor has updated a BufferedImage
     * @param bufImage
     */
    private void notifyImageUpdate(BufferedImage bufImage){
        setChanged();
        notifyObservers(bufImage);
    }
}
