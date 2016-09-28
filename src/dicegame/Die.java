package dicegame;

import javax.swing.ImageIcon;

public class Die implements DieIntf {

    int value; 
    String imageSource;
    ImageIcon icon;
  
   
    @Override
    public void setValue(int ref) {
        value = ref;
    }
 
    @Override
    public void setImage() { //setting the image by the value
        if (value == 0) { 
            imageSource = "images/nothing.png";
        }
          else if (value == 1) {
            imageSource = "images/face1.png"; 
        } else if (value == 2) {
            imageSource = "images/face2.png";
        } else if (value == 3) {
            imageSource = "images/face3.png";
        } else if (value == 4) {
            imageSource = "images/face4.png";
        } else if (value == 5) {
            imageSource = "images/face5.png";
        } else if (value == 6) {
            imageSource = "images/face6.png";
        }
        icon = new ImageIcon(imageSource);
    }
   
    @Override
    public int getValue() {
        
        return value;
    }

    @Override
    public String getImageSource() {
        return imageSource;
    }
    
    @Override
    public ImageIcon getImageIcon() {
        return icon;
    }

}

