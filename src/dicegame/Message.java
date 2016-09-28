
package dicegame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;



public class Message {

    private JLabel label;
    private int waitSeconds;
    private String text;

    public Message(int waitSeconds, JLabel label, String txt) {
        this.label = label;
        this.waitSeconds = waitSeconds;
        this.text = txt;
    }
    
    public void startCountdownFromNow() {
        label.setText(text);
        Timer timer = new Timer(waitSeconds * 1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("");
            }
        });
        timer.start();
    }
}