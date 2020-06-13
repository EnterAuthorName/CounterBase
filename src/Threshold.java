import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Observable;

public class Threshold extends Observable {
    public BufferedImage Process(BufferedImage img, int threshold) throws InterruptedException {
        //check threshold
        if(threshold>255 || threshold<0){
            System.out.println("Threshold is out of range");
            return img;
        }

        //makes two image refences
        BufferedImage beforeSharpen = img;
        BufferedImage AfterSharpen = new BufferedImage(img.getColorModel(),img.copyData(null),img.isAlphaPremultiplied(), null);;
        //adds image before changes
        notifyImageUpdate(beforeSharpen);

        //starts image processing
        //for each pixel
        for(int row=0; row<img.getWidth();row++){
            for(int cols=0; cols<img.getHeight();cols++){
                Color c = new Color(img.getRGB(cols,row));
                double Luminance = intensity(c);

                //sets white or black depending on if its meets.
                if(Luminance >= threshold){
                    AfterSharpen.setRGB(row, cols, Color.WHITE.getRGB());
                }
                else {
                    AfterSharpen.setRGB(row, cols, Color.BLACK.getRGB());
                }
            }
        }

        //add image at the end of the process
        Thread.sleep(1000);
        notifyImageUpdate(AfterSharpen);

        return AfterSharpen;
    }

    public static double intensity(Color c){
        int r=c.getRed(),
                g=c.getGreen(),
                b=c.getBlue();
        if(r==g && r==b){
            return r;
        }
        return 0.299*r+0.587*g+0.114*b;
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