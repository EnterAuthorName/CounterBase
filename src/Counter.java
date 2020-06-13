import javax.imageio.ImageIO;
import java.awt.*;
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

            //setup buffereed image
            // this is easier to do at the start and keep updated rather than having to do each process
            //RGBA, Height, width
            int[][][] rgba_buffer = new int[3][_bufImage.getHeight()][_bufImage.getWidth()];
            rgba_buffer = getBuffer(rgba_buffer, _bufImage);

            //Create the auxiliary classes
            Processor processor = new Processor();
            Process_Sharpen sharpen = new Process_Sharpen();
            //add all observers
            processor.addObserver(gui);
            sharpen.addObserver(gui);

            //do processes
            //sharpen
            sharpen.Process(_bufImage,rgba_buffer);
            processor.Process(_bufImage);
        }
        catch (Exception e){
            System.out.println("[EXCEPTION] An error has occurred: " + e);
            e.printStackTrace();
        }
    }

    public static int[][][] getBuffer(int[][][] x, BufferedImage img){
        //for each pixel
        for(int row=0;row>img.getHeight();row++){
            for(int col=0;col>img.getWidth();col++){
                //get pixel color
                Color c= new Color(img.getRGB(col,row));
                x[0][row][col]=c.getRed();
                x[1][row][col]=c.getGreen();
                x[2][row][col]=c.getBlue();
            }
        }
        return x;
    }

}
