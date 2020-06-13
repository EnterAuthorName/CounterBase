import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
/**
 * Counts the number of cells in an image
 */
public class Counter {

    public static void main(String[] args){
        try{
            if (args.length !=1){
                System.err.println("Counter requires one image input");
                return;
            }
            File input = new File(args[0]);
            BufferedImage _bufImage = ImageIO.read(input);

            //setup Gui
            GUI gui = new GUI();

            //Create the auxiliary classes
            Processor processor = new Processor();
            Process_Sharpen sharpen = new Process_Sharpen();
            //add all observers
            processor.addObserver(gui);
            sharpen.addObserver(gui);

            //do processes
            //sharpen
            sharpen.Process(_bufImage);
        }
        catch (Exception e){
            System.out.println("[EXCEPTION] An error has occurred: " + e);
            e.printStackTrace();
        }
    }

}
