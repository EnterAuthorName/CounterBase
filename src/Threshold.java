import java.awt.image.BufferedImage;

/**
 * Only for 8-bit grayscale images.
 * Houses static methods for computing thresholds and invoking threshold operation upon a BufferedImage
 */
public class Threshold {

    /**
     * Takes a BufferedImage and a threshold value to segment the image into a binary image of 0 and 255.
     * @param bufImg the BufferedImage to threshold
     * @param threshold the threshold value to split the image into binary groups
     * @return the thresholded BufferedImage
     */
    public static BufferedImage process(BufferedImage bufImg, int threshold){
        BufferedImage original = bufImg;
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage copy = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        int rgb;
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                rgb = original.getRGB(x,y) & 0xff;
                if(rgb <threshold){
                    rgb = 0;
                }
                else{
                    rgb = 255;
                }
                rgb = 0xff << 24 | rgb << 16 | rgb << 8 | rgb;
                copy.setRGB(x,y,rgb);
            }
        }
        return copy;
    }

    /**
     * Takes a BufferedImage and a threshold value to segment the image into a binary image of 0 and 1.
     * @param bufImg the BufferedImage to threshold
     * @param threshold the threshold value to split the image into binary groups
     * @return a binary thresholded image
     */
    public static BufferedImage processTrueBinary(BufferedImage bufImg, int threshold){
        BufferedImage original = bufImg;
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage copy = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        int rgb;
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                rgb = original.getRGB(x,y) & 0xff;
                if(rgb <threshold){
                    rgb = 0;
                }
                else{
                    rgb = 1;
                }
                rgb = 0xff << 24 | rgb << 16 | rgb << 8 | rgb;
                copy.setRGB(x,y,rgb);
            }
        }
        return copy;
    }

    /**
     * Auto-calculates the threshold of the histogram of the grayscale image via the Isodata Algorithm.
     * @param histo
     * @return -1 if the histogram is uniform or lacks a background and foreground element OR the mean
     */
    public static int calcThresholdIsodata(Histogram histo){
        int size = histo.get_numLevels();
        int mean0 = histo.get_mean();
        //statistic notation yeeeeahhhhhhhh
        int mean1,mew0,mew1,countLower, countUpper;
        do{
            countLower = Histogram.Count(histo,0,mean0);
            countUpper = Histogram.Count(histo,mean0+1,size-1);
            //If there's nothing in the foreground or background then we've been given something that can't be thresholded
            if(countLower == 0 || countUpper ==0){
                return -1;
            }
            mew0 = Histogram.Mean(histo,0,mean0);
            mew1 = Histogram.Mean(histo,mean0+1,size-1);
            mean1 = mean0;
            mean0 = mew0 + mew1;
            mean0 /=2;
            //System.out.println("Threshold mean moved from " + mean1 + " to "+ mean0);
        }
        while (mean0 != mean1);
        return mean0;
    }

}
