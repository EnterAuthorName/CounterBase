import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * An auxiliary class for Counter
 * A simple GUI to be used in conjunction with the Processor class for digital image processing.
 * The GUI displays the image in the centre, allows back and forth viewing of gallery images and the saving (.png forced) of the current gallery image.
 */
public class GUI implements Observer {

    //The container for the GUI
    private JFrame _frame = null;
    //Houses the label that controls the image to be displayed
    private JLabel _labelImage = null;
    //Displays the index of the current image in the gallery in format CURRENT-IMAGE-INDEX/NUMBER OF ITEMS INSIDE GALLERY
    private JTextField _counter = null;
    //Current directory that the program started in
    private File _currentDir = new File(System.getProperty("user.dir"));
    private ArrayList<ImageIcon> _gallery = null;
    //Gallery size is -1 of the size of the gallery so that it starts from 0
    private int _galleryIndex,_gallerySize;
    private static final String TITLE = "Counter";
    private static final int WIDTH = 800, HEIGHT = 400;

    /**
     * Creates and displays a GUI for viewing the results of image processing.
     */
    public GUI(){
        _frame = new JFrame(TITLE);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        //Padding
        panel.setBorder(new EmptyBorder(50,50,50,50));
        _gallery = new ArrayList<>(1);
        _galleryIndex = 0;

        JPanel panelImage = new JPanel();
        //Sets the size of panel(the size of the GUI since nothing there initially).
        panelImage.setPreferredSize(new Dimension(WIDTH,HEIGHT));

        //Creates the menubar
        JMenu menu = new JMenu("File");
        JMenuBar menuBar = new JMenuBar();
        //JMenuItem i1 = new JMenuItem("Save");
        JMenuItem i2 = new JMenuItem("Save As...");
        i2.addActionListener(new SaveAs());
        //JMenuItem i3 = new JMenuItem("Save All");
        //menu.add(i1);
        menu.add(i2);
        //menu.add(i3);
        menuBar.add(menu);
        _frame.setJMenuBar(menuBar);

        //Creates the label for displaying images
        ImageIcon icon = new ImageIcon();
        _labelImage = new JLabel();
        _labelImage.setIcon(icon);
        panelImage.add(_labelImage,BorderLayout.CENTER);
        panel.add(panelImage,BorderLayout.CENTER);

        //Creates button for going backwards in the gallery
        JPanel panelButton = new JPanel();
        panel.add(BorderLayout.SOUTH, panelButton);
        final JButton buttonBack = new JButton("<");
        panelButton.add(buttonBack);
        buttonBack.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("[Button] 'Back' has been pressed");
                updateGallery(0);
            }
        });
        //For whatever reason only one button can have a key listener
        //Change this to keybinding later
        buttonBack.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        updateGallery(0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        updateGallery(1);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        //Creates counter for gallery
        _counter = new JTextField("-/-");
        _counter.setEditable(false);
        _counter.setPreferredSize(new Dimension(100,30));
        _counter.setHorizontalAlignment(SwingConstants.CENTER);
        panelButton.add(_counter);

        //Creates button for going forwards in the gallery
        final JButton buttonNext = new JButton(">");
        panelButton.add(buttonNext);
        buttonNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ////System.out.println("[Button] 'Next' has been pressed");
                updateGallery(1);
            }
        });

        //Finally add the panels to the frame, pack it and display.
        _frame.add(panel,BorderLayout.CENTER);
        _frame.pack();
        _frame.setVisible(true);
    }

    /**
     * Handles the Save As dialog menu and functionality for images but will force .png extension.
     */
    class SaveAs implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(_gallery.size() == 0){
                return;
            }
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(_currentDir);
            // Demonstrate "Save" dialog:
            int rVal = fc.showSaveDialog(GUI.this._frame);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                String name = fc.getSelectedFile().getName();
                String dir = fc.getCurrentDirectory().toString();
                Image img = _gallery.get(_galleryIndex).getImage();
                BufferedImage bufImg = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
                Graphics2D grap = bufImg.createGraphics();
                grap.drawImage(img,0,0,null);
                grap.dispose();
                String filename = dir + "\\" + name;
                if(!filename.endsWith("png")){
                    filename += ".png";
                }
                File output = new File(filename);

                try {
                    //Doesn't work with jpg. I'm not sure why
                    ImageIO.write(bufImg, "png", output);
                }
                catch (IOException ie){
                    ie.printStackTrace();
                    output.delete();
                }
            }
        }
    }

    /**
     * Updates the galley to either the previous or next image in the gallery if applicable.
     * @param forward 0 for backwards, 1 for forwards, anything else to invalidate the current
     */
    private void updateGallery(int forward){
        //Gallery can't be empty
        if(_gallery.size() !=0){
            if(forward == 0){
                if(_galleryIndex > 0) {
                    _galleryIndex--;
                }
                else{
                    _galleryIndex = _gallerySize;
                }
            }
            else if (forward == 1){
                if(_galleryIndex >= _gallerySize) {
                    _galleryIndex = 0;
                }
                else {
                    _galleryIndex++;
                }
            }
            //System.out.println("[Gallery] Changed to index " + _galleryIndex);
            _labelImage.setIcon(_gallery.get(_galleryIndex));
            _counter.setText(_galleryIndex + "/" +_gallerySize);
            _frame.invalidate();
        }
    }

    @Override
    /**
     * Updates the GUI's gallery when whatever GUI observes updates
     * Currently assumed the only updates are BufferedImages. This will most definitely cause problems if it isn't a BufferedImage.
     */
    public void update(Observable o, Object arg) {
        //System.out.println("[GUI] " + o.toString() + " has updated");
        _gallery.add(new ImageIcon((BufferedImage)arg));
        _gallerySize = _gallery.size()-1;
        updateGallery(2);
    }
}
