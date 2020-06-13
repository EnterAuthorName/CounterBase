import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MedianFilter {

    public static BufferedImage process(BufferedImage bufImg) throws IllegalArgumentException{
        BufferedImage original = bufImg;

        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage copy = new BufferedImage(width-2,height-2,BufferedImage.TYPE_INT_RGB);

        //The rgb for top, middle, bottom, going left to right in numbering for a 3x3 region
        int t0,t1,t2,m0,m1,m2,b0,b1,b2;
        int[] set = new int[9];

        //rgb for filtered pixel
        int filtered;
        for (int y = 1; y < height -1; y++){
            for(int x = 1; x < width-1;x++){
                t0 = original.getRGB(x-1,y-1) & 0xff;
                t1 = original.getRGB(x,y-1) & 0xff;
                t2 = original.getRGB(x+1,y-1) & 0xff;
                m0 = original.getRGB(x-1,y) & 0xff;
                m1 = original.getRGB(x,y) & 0xff;
                m2 = original.getRGB(x+1,y) & 0xff;
                b0 = original.getRGB(x-1,y+1) & 0xff;
                b1 = original.getRGB(x,y+1) & 0xff;
                b2 = original.getRGB(x+1,y+1) & 0xff;
                set[0]=t0;
                set[1]=t1;
                set[2]=t2;
                set[3]=m0;
                set[4]=m1;
                set[5]=m2;
                set[6]=b0;
                set[7]=b1;
                set[8]=b2;
                Arrays.sort(set);
                filtered = set[4];

                filtered = 0xff << 24 | filtered << 16 | filtered << 8| filtered;
                copy.setRGB(x-1,y-1,filtered);
            }
        }
        return copy;
    }

}
