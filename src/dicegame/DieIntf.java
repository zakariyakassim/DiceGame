package dicegame;

import javax.swing.ImageIcon;

public interface DieIntf {

    public void setValue(int ref);

    public void setImage();

    public int getValue();

    public String getImageSource();

    public ImageIcon getImageIcon();

}
