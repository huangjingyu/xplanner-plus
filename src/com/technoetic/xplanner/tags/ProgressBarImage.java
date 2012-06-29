package com.technoetic.xplanner.tags;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.technoetic.xplanner.format.DecimalFormat;

public class ProgressBarImage extends BufferedImage {
    private double value;
    private double maxValue;
    private Color valueColor = Color.BLUE;
    private Color maxValueColor = Color.RED;
    private DecimalFormat format;
   private double captionValue;

   public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame("Test");
        jFrame.setSize(800, 700);
        ProgressBarImage image = new ProgressBarImage(100, 20, 12.9, Color.BLUE, 16.8, Color.RED, new DecimalFormat(Locale.getDefault(), ""));
        FileOutputStream fos = new FileOutputStream( "c:\\temp\\pic.jpeg" );
        // BufferedOutputStream bos = new BufferedOutputStream( fos );
        image.encodeJPEG( fos, (float) 1.0 );
        fos.flush();
        fos.close();

        jFrame.getContentPane().add(new JLabel(new ImageIcon("c:\\temp\\pic.jpeg")));
        jFrame.pack();
        jFrame.show();
    }

   public static class Model {
      public int width;
      public int height;
      public double value;
      public Color valueColor;
      public double maxValue;
      public Color maxValueColor;
      public DecimalFormat format;
      private double captionValue;

      public Model(int width, int height, double value, Color valueColor, double maxValue, Color maxValueColor, DecimalFormat format, double captionValue) {
         this.width = width;
         this.height = height;
         this.value = value;
         this.valueColor = valueColor;
         this.maxValue = maxValue;
         this.maxValueColor = maxValueColor;
         this.format = format;
         this.captionValue = captionValue;
      }
   }

    public ProgressBarImage(int width,
                            int height,
                            double value,
                            Color valueColor,
                            double maxValue,
                            Color maxValueColor,
                            DecimalFormat format) {
        this(new Model(width, height, value, valueColor, maxValue, maxValueColor, format, value));
    }

    public ProgressBarImage(Model model) {
        super(model.width, model.height /*model.height*/, BufferedImage.TYPE_INT_RGB);
        this.value = model.value;
        this.valueColor = model.valueColor;
        this.maxValue = model.maxValue;
        this.maxValueColor = model.maxValueColor;
        this.format = model.format;
       this.captionValue = model.captionValue;
        init();
    }

    private void init() {
        Graphics2D drawGraphics = createGraphics();
        Graphics2D textGraphics = getTextGraphics();

        int rightMarginInPixels = 0; //getValueTextWidth(textGraphics, maxValue);
        int valueInPixels = getPixelsForValue();

        int textHeight = textGraphics.getFontMetrics().getHeight() - 2;

        drawRectangle(drawGraphics, 0, 0, getWidth(), getHeight(), Color.WHITE);
        drawBar(drawGraphics, 0, valueInPixels, getHeight(), valueColor);
        if (valueInPixels < getWidth())
            drawBar(drawGraphics, valueInPixels, getWidth()-valueInPixels-rightMarginInPixels, getHeight(), maxValueColor);

        drawValue(textGraphics, valueInPixels, getHeight() - 2);
//         if (valueInPixels < getWidth())
//             drawValue(textGraphics, getWidth()-rightMarginInPixels, getHeight() - 2, maxValue, Color.BLACK);
    }

    private void drawValue(Graphics2D g, int x, int y) {
        int textWidth = getValueTextWidth(g);
        int xText = x;
        Color bgColor = maxValueColor;
        if (textWidth < x) {
            xText = x - textWidth;
            bgColor = valueColor;
        }
//        g.drawString(text, xText, y);
        drawValue(g, xText, y, getTextColor(bgColor));
    }

    private void drawValue(Graphics2D g, int x, int y, Color color) {
        g.setColor(color);
        Font oldFont = g.getFont();
//        if (!color.equals(Color.BLACK)) {
//            g.setFont(g.getFont().deriveFont(Font.BOLD));
//        }
        TextLayout tl = new TextLayout(getCaption(), g.getFont(), g.getFontRenderContext());
        tl.draw(g, x, y);
        g.setFont(oldFont);
    }

   private String getCaption()
   {
      return format.format(captionValue);
   }

   private int getValueTextWidth(Graphics2D g) {
      return SwingUtilities.computeStringWidth(g.getFontMetrics(), getCaption());
    }

    private Graphics2D getTextGraphics() {
        Graphics2D g = createGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, getFontSize()));
//        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setPaintMode();
//        g.setXORMode(Color.BLACK);
        return g;
    }

    private int getFontSize() {
        return (int) Math.round(getHeight()*0.91);
    }

    private Color getTextColor(Color bgColor) {
        Color color = null;
        if (bgColor.equals(Color.RED) || bgColor.equals(Color.BLUE))
            color = Color.WHITE;
        else
            color = Color.BLACK;
        return color;
    }

    private void drawBar(Graphics2D g, int x, int width, int height, Color color) {
        drawRectangle(g, x, 0 , width, height, color);
    }

    private void drawRectangle(Graphics2D g, int x, int y, int width, int height, Color color) {
        g.setColor(color);
        g.fillRect(x,y, width, height);
    }

    private int getPixelsForValue() {
        return (int)Math.round((getWidth() / maxValue) * value);
    }

    public void encodeJPEG( OutputStream outputStream, float outputQuality )
       throws java.io.IOException {
       int outputWidth  = this.getWidth( null );
       if ( outputWidth < 1 )
          throw new IllegalArgumentException( "output image width " + outputWidth + " is out of range" );
       int outputHeight = this.getHeight( null );
       if ( outputHeight < 1 )
          throw new IllegalArgumentException( "output image height " + outputHeight + " is out of range" );

       // Get a buffered image from the image.
       BufferedImage bi = new BufferedImage( outputWidth, outputHeight,
          BufferedImage.TYPE_INT_RGB );
       Graphics2D biContext = bi.createGraphics();
       biContext.drawImage( this, 0, 0, null );
       // Note that additional drawing such as watermarks or logos can be placed here.

       // com.sun.image.codec.jpeg package is included in sun and ibm sdk 1.3
       JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( outputStream );
       // The default quality is 0.75.
       JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam( bi );
       jep.setQuality( outputQuality, true );
       encoder.encode( bi, jep );
       // encoder.encode( bi );
       outputStream.flush();
    } // encodeImage

}
