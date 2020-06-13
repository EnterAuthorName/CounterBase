import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.Observable;

public class OTsu_thresh extends Observable {

    public BufferedImage process(BufferedImage img){
        //makes two image refences
        BufferedImage beforeSharpen = img;
        BufferedImage AfterSharpen = new BufferedImage(img.getColorModel(),img.copyData(null),img.isAlphaPremultiplied(), null);;
        //adds image before changes
        notifyImageUpdate(beforeSharpen);

        //apples process
        // Get raw image data
        Raster raster = AfterSharpen.getData();
        DataBuffer buffer = raster.getDataBuffer();

        DataBufferByte byteBuffer = (DataBufferByte) buffer;
        byte[] srcData = byteBuffer.getData(0);

        //check image color (witescale)
        if(img.getWidth()*img.getHeight()!=srcData.length){
            System.out.println("Unexpected image data size. Should be greyscale image");
            //return image with no changes
            return img;
        }

        byte[] dstData = new byte[srcData.length];

        // Create Otsu Thresholder
        OtsuThresholder thresholder = new OtsuThresholder();
        int threshold = thresholder.doThreshold(srcData, dstData);


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

    private class OtsuThresholder {
        private int histData[];
        private int maxLevelValue;
        private int threshold;

        public OtsuThresholder()
        {
            histData = new int[256];
        }

        public int[] getHistData()
        {
            return histData;
        }

        public int getMaxLevelValue()
        {
            return maxLevelValue;
        }

        public int getThreshold()
        {
            return threshold;
        }

        public int doThreshold(byte[] srcData, byte[] monoData)
        {
            int ptr;

            // Clear histogram data
            // Set all values to zero
            ptr = 0;
            while (ptr < histData.length) histData[ptr++] = 0;

            // Calculate histogram and find the level with the max value
            // Note: the max level value isn't required by the Otsu method
            ptr = 0;
            maxLevelValue = 0;
            while (ptr < srcData.length)
            {
                int h = 0xFF & srcData[ptr];
                histData[h] ++;
                if (histData[h] > maxLevelValue) maxLevelValue = histData[h];
                ptr ++;
            }

            // Total number of pixels
            int total = srcData.length;

            float sum = 0;
            for (int t=0 ; t<256 ; t++) sum += t * histData[t];

            float sumB = 0;
            int wB = 0;
            int wF = 0;

            float varMax = 0;
            threshold = 0;

            for (int t=0 ; t<256 ; t++)
            {
                wB += histData[t];					// Weight Background
                if (wB == 0) continue;

                wF = total - wB;						// Weight Foreground
                if (wF == 0) break;

                sumB += (float) (t * histData[t]);

                float mB = sumB / wB;				// Mean Background
                float mF = (sum - sumB) / wF;		// Mean Foreground

                // Calculate Between Class Variance
                float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

                // Check if new maximum found
                if (varBetween > varMax) {
                    varMax = varBetween;
                    threshold = t;
                }
            }

            // Apply threshold to create binary image
            if (monoData != null)
            {
                ptr = 0;
                while (ptr < srcData.length)
                {
                    monoData[ptr] = ((0xFF & srcData[ptr]) >= threshold) ? (byte) 255 : 0;
                    ptr ++;
                }
            }

            return threshold;
        }
    }
}
