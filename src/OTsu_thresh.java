import java.awt.image.BufferedImage;
import java.util.Observable;

public class OTsu_thresh extends Observable {

    public BufferedImage process(BufferedImage img){
        //makes two image refences
        BufferedImage beforeSharpen = img;
        BufferedImage AfterSharpen = new BufferedImage(img.getColorModel(),img.copyData(null),img.isAlphaPremultiplied(), null);;
        //adds image before changes
        notifyImageUpdate(beforeSharpen);


        //adds image after changes and returns
        notifyImageUpdate(AfterSharpen);
        return AfterSharpen;
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
