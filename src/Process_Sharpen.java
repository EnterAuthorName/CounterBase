import java.awt.image.BufferedImage;
import java.util.Observable;

/*
One of the image processes that can happen.
It sharpens the image
 */
public class Process_Sharpen extends Observable {
    public void Process(BufferedImage img){
        //add the image at start of process
        notifyImageUpdate(img);

        //implment the sharpening process

        //add image at the end of the process
        notifyImageUpdate(img);
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
