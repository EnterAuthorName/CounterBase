import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class RegionLabeling {

    private static BufferedImage _image;
    private static int _width, _height;


    public static BufferedImage labelRegions(BufferedImage bufImg){
        System.out.println("Labelling Regions...");
        BufferedImage original = bufImg;
        _width = original.getWidth();
        _height = original.getHeight();
        _image = new BufferedImage(_width,_height,BufferedImage.TYPE_INT_RGB);
        _image.setData(original.getData());
        int label = 2;
        int RGB;
        for(int y=0;y<_height;y++){
            for(int x=0;x<_width;x++){
                RGB = _image.getRGB(x,y) & 0xff;
                if( RGB == 1){
                    FloodFill(x,y,label);
                    label++;
                }
            }
        }
        System.out.println("Finished labelling. There are " + (label-2) + " regions");
        return _image;
    }

    private static void FloodFill(int x, int y, int label){
        Queue<Point> queue =new LinkedList<>();
        queue.add(new Point(x,y));
        int xPos, yPos, RGB;
        Point p;
        while(!queue.isEmpty()){
            p = queue.remove();
            xPos = p.x;
            yPos = p.y;
            //many things map to 1 if &0xff. Need to match sure that it is only 1 and not anything else
            if(isInBounds(xPos,yPos) && (_image.getRGB(xPos,yPos)&0xff) == 1 && (_image.getRGB(xPos,yPos)&0xff00) == 1&& (_image.getRGB(xPos,yPos)&0xff0000) == 1){
                //RGB = 0xff << 24 | label<<16 | label<<8 | label;
                //RGB = label;
                RGB = 0xff444444;
                _image.setRGB(xPos,yPos,RGB);
                if(isInBounds(xPos+1,yPos)&& (_image.getRGB(xPos+1,yPos)&0xff) == 1&& (_image.getRGB(xPos+1,yPos)&0xff00) == 1&& (_image.getRGB(xPos+1,yPos)&0xff0000) == 1) {
                    queue.add(new Point(xPos + 1, yPos));
                }
                if(isInBounds(xPos,yPos+1)&& (_image.getRGB(xPos,yPos+1)&0xff) == 1&& (_image.getRGB(xPos,yPos+1)&0xff00) == 1&& (_image.getRGB(xPos,yPos+1)&0xff0000) == 1) {
                    queue.add(new Point(xPos, yPos + 1));
                }
                if(isInBounds(xPos,yPos-1)&& (_image.getRGB(xPos,yPos-1)&0xff) == 1&& (_image.getRGB(xPos,yPos-1)&0xff00) == 1&& (_image.getRGB(xPos,yPos-1)&0xff0000) == 1) {
                    queue.add(new Point(xPos, yPos - 1));
                }
                if(isInBounds(xPos-1,yPos)&& (_image.getRGB(xPos-1,yPos)&0xff) == 1&& (_image.getRGB(xPos-1,yPos)&0xff00) == 1&& (_image.getRGB(xPos-1,yPos)&0xff0000) == 1) {
                    queue.add(new Point(xPos - 1, yPos));
                }
            }
        }
    }

    private static boolean isInBounds(int x, int y){
        if(x>=0 && x < _width && y >= 0 && y < _height) {
            return true;
        }
        else {
            return false;
        }
    }

}
