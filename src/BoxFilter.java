import java.awt.image.BufferedImage;

/**
 * For 8 bit gray scale images
 * Houses the static method for Box Blur Filter operation using the 3x3 matrix [1,1,1][1,1,1][1,1,1]
 */
public class BoxFilter {

    /**
     * Takes a BufferedImage and applies the box filter on it
     * @param bufImg
     * @return A blurred BufferedImage
     */
    public static BufferedImage process(BufferedImage bufImg) {
        BufferedImage original = bufImg;
        BufferedImage copy = new BufferedImage(original.getColorModel(),original.copyData(null),original.isAlphaPremultiplied(), null);

        int width = copy.getWidth();
        int height = copy.getHeight();
        //The rgb for top, middle, bottom, going left to right in numbering for a 3x3 region
        int t0,t1,t2,m0,m1,m2,b0,b1,b2;

        //rgb for filtered pixel
        int filtered;
        //RGB is 4 bytes.
        int max = (int)Math.pow(2,32)-1;
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

                filtered = t0 + t1 + t2
                        + m0  + m1 + m2
                        + b0 + b1 + b2;
                //System.out.println("[Box] " + t0 +","+t1 +","+t2 +","+ m0 +","+m1 +","+m2 +","+ b0 +","+b1 +","+b2);
                filtered /=9;
                filtered = 0xff << 24 | filtered << 16 | filtered << 8| filtered;
                //System.out.println("[Box] Operation on " + x + "," + y + " yields RGB " + filtered);
                //System.out.println("[Box] Operation on " + x + "," + y + " filtered to RGB " + filtered);
                copy.setRGB(x,y,filtered);
            }
        }
        return copy;
    }

}
