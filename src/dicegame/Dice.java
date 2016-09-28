package dicegame;

import java.util.Random;
import javax.swing.ImageIcon;


public class Dice {

    Die die = new Die();
    int roll = 0;
    

    public void init() {
        die.setValue(0);
        die.setImage();
    }

    public void setImages(int ref) { //gets the value and sets the image
        die.setValue(ref);
        die.setImage();
    }

//    public void setValue(int ref) {
//        die.setValue(ref);
//    }

    public void Roll() {
        roll = (new Random().nextInt(6)) + 1; //random value
        die.setValue(roll); //sets the value
        die.setImage(); // sets the image
    }

    public String getSource() {
        return die.getImageSource(); //returning the die image source from the die class
    }

    public int getValue() {
        return die.getValue(); //returning the value from the die class
    }

    public ImageIcon getImageIcon() {
        return die.getImageIcon(); // returning the image icon from the die class
    }

}
