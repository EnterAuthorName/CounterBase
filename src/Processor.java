import java.awt.image.BufferedImage;
import java.util.Observable;

/**
 * An auxiliary class for Counter
 * Takes an image and applies operations to the image.
 */
public class Processor extends Observable{

    /**
     * Takes the given BufferedImage and begins processing it.
     * @param image
     */
    public void Process(BufferedImage image){
        try {
            System.out.println("[Processor] Processing Image...");

            //Example of processing an image
//            Thread.sleep(1000);
//            System.out.println("[Processor] Changing image");
//            BufferedImage _bufImage = image;
//            //Always have this after some operation is complete.
//            notifyImageUpdate(_bufImage);
//
//            Thread.sleep(2000);
//
//            //Example of making a new image
//            BufferedImage bi = new BufferedImage(_bufImage.getColorModel(),_bufImage.copyData(null),_bufImage.isAlphaPremultiplied(), null);
//
//            int width = _bufImage.getWidth();
//            int height = _bufImage.getHeight();
//            for(int i=0; i < height/3;i++){
//                for(int j = 0; j < width; j++){
//                    bi.setRGB(j,i,111);
//                }
//            }
//            notifyImageUpdate(bi);

        }
        catch (Exception e){
            System.out.println("[EXCEPTION] An error has occurred: " + e);
            e.printStackTrace();
        }

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
