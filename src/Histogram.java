import java.awt.image.BufferedImage;

/**
 * Only for 8-bit grayscale images
 * Represents the RGB histogram of a BufferedImage. Provides static methods that are useful for Histograms e.g. calculating means and counting
 * the number of items between two bounds.
 */
public class Histogram {

    private int[] _bins;
    //numLevels is the number of intensity levels of the histogram
    private int _mean, _count, _numLevels;

    /**
     * Initialises the histogram based on the BufferedImage
     * @param bufImg
     */
    public Histogram(BufferedImage bufImg){
        BufferedImage original = bufImg;

        _bins = new int[256];
        _numLevels = 256;
        //Sets all bins to 0
        for(int i=0;i<256;i++){
            _bins[i]=0;
        }
        int width = original.getWidth();
        int height = original.getHeight();
        int rgb;
        //Updates the frequency of the bins as we go through the image getting the rgb value
        for(int y=0;y<height;y++){
            for(int x=0;x<width;x++){
                rgb = original.getRGB(x,y) & 0xff;
                _bins[rgb] = _bins[rgb]+1;
            }
        }

        _count = 0;
        int sum = 0;
        //Gets the count and sum of the histogram then calculates the mean
        for(int i=0;i<256;i++){
            _count += _bins[i];
            sum += _bins[i] * i;
        }
        _mean = sum / _count;
        //System.out.println("Mean is " + _mean + "\n" + "n=" + _count);

    }

    /**
     * Calculates the number of items in the histogram between the lower and upper bounds (inclusive);
     * @param h the histogram
     * @param lower the lower bound
     * @param upper the upper bound
     * @throws IllegalArgumentException if lower > upper
     *                                  if lower or upper <0
     * @return the number of items inside the bounds
     */
    public static int Count(Histogram h,int lower, int upper) throws IllegalArgumentException{
        if(lower < 0 || upper < 0){
            throw new IllegalArgumentException("Bounds cannot be negative");
        }
        if(lower > upper){
            throw new IllegalArgumentException("The lower bound cannot be bigger than the upper bound");
        }
        int[] bins = h.get_bins();
        int inside = 0;
        int binValue;
        for(int i =0; i <bins.length;i++){
            binValue = bins[i];
            if(binValue >= lower && binValue <= upper) {
                inside++;
            }
        }
        return inside;
    }

    /**
     * Calculates the mean of the histogram between the lower and upper bounds(inclusive)
     * @param h the histogram
     * @param lower the lower bound
     * @param upper the upper bound
     * @throws IllegalArgumentException if lower > upper
     *                                  if lower or upper <0
     * @return the mean of the histogram inside the bounds
     */
    public static int Mean(Histogram h, int lower, int upper) throws IllegalArgumentException{
        if(lower < 0 || upper < 0){
            throw new IllegalArgumentException("Bounds cannot be negative");
        }
        if(lower > upper){
            throw new IllegalArgumentException("The lower bound cannot be bigger than the upper bound");
        }
        int[] bins = h.get_bins();
        int size = 0;
        int sum = 0;
        for(int i = lower; i <= upper; i++){
            size += bins[i];
            sum += i*bins[i];
        }
        return sum/size;
    }

    public int get_mean(){
        return _mean;
    }

    /**
     * Gets the total number of items inside the bins
     * @return the total number of items inside the bins
     */
    public int get_count(){
        return _count;
    }

    public int[] get_bins(){
        return _bins;
    }

    /**
     * Gets the number of intensity levels
     * @return the number of intensity levels
     */
    public int get_numLevels(){
        return _numLevels;
    }

}
