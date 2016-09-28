package dicegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
//the class extends JFrame.

public class DiceGame extends JFrame {

    //this are all the variables.
    private final MyCanvas canvas = new MyCanvas();
    private final JButton btnRollPlayer;

    private final JButton btnAddScore;
    private final JButton btnReRoll;
    private final JButton btnNewGame;

    int[] selected = new int[5];
    int[] selectedC = new int[5];
    int[] computerSelectStore = new int[5];
    int[] userSelectStore = new int[5];
    int scorePlayerChoice = 0;
    int scorePlayer = 0;
    int scoreComputer = 0;
    int btnWidth = 150;
    int btnHeight = 60;
    int rolls = -1;
    int rollsLeft = 4;
    int win = 101;
    int bgHeight;
    int bgWidth;
    int playerWins = 0;
    int computerWins = 0;
    int ifAiReply;

    // int rollsC = 0;
    int rollsLeftC = 3;
    int computerReRoll = 0;
    boolean enableSelection = true;
    boolean nn = true;

    private final JLabel[] lblPlayerDice = new JLabel[5];
    private final JLabel[] lblComputerDice = new JLabel[5];
    private JPanel playerDices = new JPanel();
    private JPanel computerDices = new JPanel();

    JLabel lblmessage = new JLabel();
    String message = "";

    JLabel lblPlayerScore = new JLabel(Integer.toString(scorePlayer));
    JLabel lblComputerScore = new JLabel(Integer.toString(scoreComputer));
    JLabel lblPlayerWins = new JLabel();
    JLabel lblComputerWins = new JLabel();
    JLabel lblReRollLeft = new JLabel();
    JLabel lblComputerReRollLeft = new JLabel();
    JLabel lblWinningScore = new JLabel();
    JPanel backgroundPanel = new JPanel();
    JPanel scorebox = new JPanel(new FlowLayout());
    JPanel bottomButtons = new JPanel(new FlowLayout());

    Border thickBorder;
    Dice dicePlayer = new Dice();
    Dice diceComputer = new Dice();
    int sel = 0;

    DiceGame() {
        //this is the look and feel, it changes the look of the software to system look.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println(e);
        }
        try { //surrounded with try and catch, just incase the file is not found.
            loadScore(); //this loads the score from a text file, see the placeholder. 
        } catch (Exception e) {
            System.out.println("File not found" + e);
        }
        setLayout(new BorderLayout()); //setting the the layout to BorderLayout.

        setResizable(true); //allows the user to resize the frame.
        setPreferredSize(new Dimension(900, 700)); //setting the preffered size.
        this.setMinimumSize(new Dimension(900, 700)); // and the minimum size.

        canvas.setLayout(new GridBagLayout()); //canvas is a panel with a background image. 
        GridBagConstraints c = new GridBagConstraints(); // using GridBagLayout positioning.

        bgHeight = getHeight(); //setting a default height and width for the buttons to make my game look good.
        bgWidth = getWidth();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { // window resized.
                bgHeight = getHeight(); //getting the height and the width of the frame so i can set it to the background image.
                bgWidth = getWidth();
            }
        });
        addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) { //window state is changed.
                bgHeight = getHeight();
                bgWidth = getWidth();
            }
        });
        addWindowListener(new WindowAdapter() { // window closing event with a little yes no option massage.
            @Override
            public void windowClosing(WindowEvent e) {
                new Message(20, lblmessage, "Closing").startCountdownFromNow();
                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to close it? ", "", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) { //if yess
                    
                    try {
                        saveScore(); // save the score to a text file.
                    } catch (Exception ex) {
                        Logger.getLogger(DiceGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0); // exit.
                } else {
                    new Message(20, lblmessage, "Closing Canceled").startCountdownFromNow();

                }
            }

            @Override
            public void windowOpened(WindowEvent e) {
                ifAiReply = JOptionPane.showConfirmDialog(null, "Artificial Intelligence? ", "", JOptionPane.YES_NO_OPTION);
                if (ifAiReply == JOptionPane.YES_OPTION) {
                    setTitle("ARTIFICIAL INTELLIGENCE DICE GAME");
                    new Message(4, lblmessage, "Artificial Intelligence Mode").startCountdownFromNow();
                } else {
                    setTitle("RANDOM STRATEGY DICE GAME");
                    new Message(4, lblmessage, "Random Strategy Mode").startCountdownFromNow();
                }
            }
        });

        playerDices.setOpaque(false);
        computerDices.setOpaque(false);

        for (int i = 0; i < lblPlayerDice.length; i++) {
            lblPlayerDice[i] = new JLabel();
            lblComputerDice[i] = new JLabel();
            selected[i] = 0;
            userSelectStore[i] = 0;
        }

        thickBorder = new LineBorder(new Color(173, 47, 48), 4);
        btnRollPlayer = new JButton("ROLL DICE");
        btnRollPlayer.setBorder(thickBorder);
        btnRollPlayer.setOpaque(false);
        btnRollPlayer.setContentAreaFilled(false);
        btnRollPlayer.setForeground(new Color(173, 47, 48));
        btnRollPlayer.setFont(new Font("Arial", Font.BOLD, 20));
        btnRollPlayer.setPreferredSize(new Dimension(btnWidth, btnHeight));
        btnRollPlayer.setMinimumSize(new Dimension(btnWidth, btnHeight));
        btnRollPlayer.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                thickBorder = new LineBorder(new Color(112, 87, 93), 4);
                btnRollPlayer.setBorder(thickBorder);
                btnRollPlayer.setForeground(new Color(112, 87, 93));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                thickBorder = new LineBorder(new Color(173, 47, 48), 4);
                btnRollPlayer.setBorder(thickBorder);
                btnRollPlayer.setForeground(new Color(173, 47, 48));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                //playerDices.removeAll();

                if (nn) { // nn = true

                    RollTheDice(); //this happens unless the user clicked on reroll.

                } else { //if nn is not true. that means the user is rerolling
                    for (int i = 0; i < lblPlayerDice.length; i++) {
                        dicePlayer.Roll(); //rolls all the dices with loop.
                        playerDices.add(lblPlayerDice[i]); //adds the label to the player dices panel.
                        if (selected[i] == 1) { //select is array of int, if the user wants to keep the third die then select[2] = 1(starts from 0)
                            dicePlayer.setImages(userSelectStore[i]);
                        }
                        lblPlayerDice[i].setIcon(dicePlayer.getImageIcon()); //sets the icon.
                        userSelectStore[i] = dicePlayer.getValue(); //stores the values.

                    }
                    
                    //when done rerolling then it sets it back to true for the next roll.
                    nn = true;
                     new Message(6, lblmessage, "Player ReRolled").startCountdownFromNow();

                }

                enableSelection = false; // the selection is disabled
                selection();
                lblPlayerDice[0].setBorder(BorderFactory.createEmptyBorder());
                lblPlayerDice[1].setBorder(BorderFactory.createEmptyBorder());
                lblPlayerDice[2].setBorder(BorderFactory.createEmptyBorder());
                lblPlayerDice[3].setBorder(BorderFactory.createEmptyBorder());
                lblPlayerDice[4].setBorder(BorderFactory.createEmptyBorder());

                btnAddScore.setVisible(true);
                btnRollPlayer.setVisible(false);
                if (rolls < rollsLeft) {
                    btnReRoll.setVisible(true);
                }

                if (rollsLeft == 1) {
                    addScore();
                }

                playerDices.revalidate();
                playerDices.repaint();

            }
        });

        btnNewGame = new JButton("NEW GAME");
        btnNewGame.setBorder(thickBorder);
        btnNewGame.setOpaque(false);
        btnNewGame.setContentAreaFilled(false);
        btnNewGame.setForeground(new Color(173, 47, 48));
        btnNewGame.setFont(new Font("Arial", Font.BOLD, 20));
        btnNewGame.setPreferredSize(new Dimension(btnWidth, btnHeight));
        btnNewGame.setMinimumSize(new Dimension(btnWidth, btnHeight));
        btnNewGame.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                thickBorder = new LineBorder(new Color(112, 87, 93), 4);
                btnNewGame.setBorder(thickBorder);
                btnNewGame.setForeground(new Color(112, 87, 93));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                thickBorder = new LineBorder(new Color(173, 47, 48), 4);
                btnNewGame.setBorder(thickBorder);
                btnNewGame.setForeground(new Color(173, 47, 48));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                new Message(10, lblmessage, "New Game").startCountdownFromNow();
                dicePlayer.init();
                dicePlayer.setImages(0);    //everything back to 0.
                diceComputer.init();
                diceComputer.setImages(0);
                lblPlayerScore.setText(Integer.toString(0));
                lblComputerScore.setText(Integer.toString(0));
                scorePlayerChoice = 0;
                scorePlayer = 0;
                scoreComputer = 0;
                rolls = -1;
                rollsLeft = 4;
                rollsLeftC = 3;
                btnReRoll.setVisible(false);
                btnRollPlayer.setVisible(true);
                btnAddScore.setVisible(false);
                lblComputerReRollLeft.setText("COMPUTER REROLL: " + Integer.toString(rollsLeftC));
                lblReRollLeft.setText("ReRoll left: " + Integer.toString(rollsLeft - 1));

                for (int i = 0; i < lblPlayerDice.length; i++) {
                    selected[i] = 0;
                    userSelectStore[i] = 0;

                }

                for (int i = 0; i < lblPlayerDice.length; i++) {
                    dicePlayer.init();

                    lblPlayerDice[i].setIcon(dicePlayer.getImageIcon());
                    playerDices.add(lblPlayerDice[i]);
                }

                for (int i = 0; i < lblComputerDice.length; i++) {
                    diceComputer.init();
                    lblComputerDice[i].setIcon(diceComputer.getImageIcon());
                    computerDices.add(lblComputerDice[i]);
                }
                ifAiReply = JOptionPane.showConfirmDialog(null, "Artificial Intelligence? ", "", JOptionPane.YES_NO_OPTION);
                if (ifAiReply == JOptionPane.YES_OPTION) {
                    setTitle("ARTIFICIAL INTELLIGENCE DICE GAME");
                    new Message(4, lblmessage, "Artificial Intelligence Mode").startCountdownFromNow();
                } else {
                    setTitle("RANDOM STRATEGY DICE GAME");
                    new Message(4, lblmessage, "Random Strategy Mode").startCountdownFromNow();
                }
                repaint();

            }
        });

        btnAddScore = new JButton("ADD SCORE");
        btnAddScore.setBorder(thickBorder);
        btnAddScore.setOpaque(false);
        btnAddScore.setContentAreaFilled(false);
        btnAddScore.setForeground(new Color(173, 47, 48));
        btnAddScore.setFont(new Font("Arial", Font.BOLD, 20));
        btnAddScore.setPreferredSize(new Dimension(btnWidth, btnHeight));
        btnAddScore.setMinimumSize(new Dimension(btnWidth, btnHeight));
        btnAddScore.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                thickBorder = new LineBorder(new Color(112, 87, 93), 4);
                btnAddScore.setBorder(thickBorder);
                btnAddScore.setForeground(new Color(112, 87, 93));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                thickBorder = new LineBorder(new Color(173, 47, 48), 4);
                btnAddScore.setBorder(thickBorder);
                btnAddScore.setForeground(new Color(173, 47, 48));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                addScore();  //adds the scores.

            }

        });

        btnReRoll = new JButton("ReRoll");
        btnReRoll.setBorder(thickBorder);
        btnReRoll.setOpaque(false);
        btnReRoll.setContentAreaFilled(false);
        btnReRoll.setForeground(new Color(173, 47, 48));
        btnReRoll.setFont(new Font("Arial", Font.BOLD, 20));
        btnReRoll.setPreferredSize(new Dimension(btnWidth, btnHeight));
        btnReRoll.setMinimumSize(new Dimension(btnWidth, btnHeight));
        btnReRoll.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                thickBorder = new LineBorder(new Color(112, 87, 93), 4);
                btnReRoll.setBorder(thickBorder);
                btnReRoll.setForeground(new Color(112, 87, 93));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                thickBorder = new LineBorder(new Color(173, 47, 48), 4);
                btnReRoll.setBorder(thickBorder);
                btnReRoll.setForeground(new Color(173, 47, 48));
            }

            @Override
            public void mouseClicked(MouseEvent e) { //btn reroll cliked
                 new Message(6, lblmessage, "Player ReRolling").startCountdownFromNow();
                           
                rollsLeft--; // take one from rerolls left
                lblReRollLeft.setText("ReRoll left: " + Integer.toString(rollsLeft - 1)); //setting the text to the label.
                playerDices.removeAll();
                if (rolls < rollsLeft) {
                    btnReRoll.setVisible(true);

                } else {

                    btnReRoll.setVisible(false);
                }
                rolls++;

                btnRollPlayer.setVisible(true);
                for (int i = 0; i < lblPlayerDice.length; i++) {

                    playerDices.add(lblPlayerDice[i]);

                }
                nn = false;

                btnReRoll.setVisible(false);
                btnAddScore.setVisible(false);
                enableSelection = true;
                selection();

                repaint();

            }
        });

        lblPlayerScore.setForeground(new Color(173, 47, 48));
        lblPlayerScore.setFont(new Font("Arial", Font.BOLD, 60));

        lblComputerScore.setForeground(new Color(173, 47, 48));
        lblComputerScore.setFont(new Font("Arial", Font.BOLD, 60));

        lblWinningScore.setForeground(new Color(255, 255, 255));
        lblWinningScore.setPreferredSize(new Dimension(100, btnHeight));
        lblWinningScore.setFont(new Font("Arial", Font.BOLD, 12));
        lblWinningScore.setText("WINNING SCORE: " + Integer.toString(win));
        lblWinningScore.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblWinningScore.setForeground(new Color(112, 87, 93));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblWinningScore.setForeground(new Color(255, 255, 255));
            }

            @Override
            public void mouseClicked(MouseEvent e) { //input dialog pops up so the user can enter the winning score.
                win = Integer.parseInt(JOptionPane.showInputDialog("Enter"));
                lblWinningScore.setText("WINNING SCORE: " + Integer.toString(win));

            }
        });

        lblPlayerWins.setForeground(new Color(255, 255, 255));
        lblPlayerWins.setPreferredSize(new Dimension(100, btnHeight));
        lblPlayerWins.setFont(new Font("Arial", Font.BOLD, 12));
        lblPlayerWins.setText("PLAYER WINS: " + Integer.toString(playerWins));

        lblComputerWins.setForeground(new Color(255, 255, 255));
        lblComputerWins.setPreferredSize(new Dimension(100, btnHeight));
        lblComputerWins.setFont(new Font("Arial", Font.BOLD, 12));
        lblComputerWins.setText("COMPUTER WINS: " + Integer.toString(computerWins));

        lblReRollLeft.setForeground(new Color(255, 255, 255));
        lblReRollLeft.setPreferredSize(new Dimension(100, btnHeight));
        lblReRollLeft.setFont(new Font("Arial", Font.BOLD, 12));
        lblReRollLeft.setText("PlAYER REROLL: " + Integer.toString(rollsLeft - 1));

        lblComputerReRollLeft.setForeground(new Color(255, 255, 255));
        lblComputerReRollLeft.setPreferredSize(new Dimension(100, btnHeight));
        lblComputerReRollLeft.setFont(new Font("Arial", Font.BOLD, 12));
        lblComputerReRollLeft.setText("COMPUTER REROLL: " + Integer.toString(rollsLeftC));

        lblmessage.setForeground(new Color(255, 255, 255));
        lblmessage.setPreferredSize(new Dimension(100, btnHeight));
        lblmessage.setFont(new Font("Arial", Font.BOLD, 20));

        for (int i = 0; i < lblPlayerDice.length; i++) {
            dicePlayer.init();
            lblPlayerDice[i].setIcon(dicePlayer.getImageIcon());
            playerDices.add(lblPlayerDice[i]);
        }

        for (int i = 0; i < lblComputerDice.length; i++) {
            diceComputer.init();
            lblComputerDice[i].setIcon(diceComputer.getImageIcon());
            computerDices.add(lblComputerDice[i]);
        }

        JPanel ReRollButton = new JPanel();
        ReRollButton.setOpaque(false);

        ReRollButton.add(btnReRoll);
        bottomButtons.add(btnRollPlayer);
        bottomButtons.add(btnAddScore);

        scorebox.setBorder(BorderFactory.createTitledBorder(thickBorder, "SCORE BOX", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
        scorebox.setOpaque(false);
        scorebox.setLayout(new BoxLayout(scorebox, BoxLayout.Y_AXIS));
        scorebox.add(lblWinningScore);
        scorebox.add(lblPlayerWins);
        scorebox.add(lblComputerWins);
        scorebox.add(lblReRollLeft);
        scorebox.add(lblComputerReRollLeft);

        add(canvas);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridwidth = 2;
        c.weighty = 1.0;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        canvas.add(btnNewGame, c);

       
       

        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        canvas.add(scorebox, c);
        
        c.anchor = GridBagConstraints.ABOVE_BASELINE;
        c.insets = new Insets(-50, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        canvas.add(lblmessage, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(100, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        canvas.add(computerDices, c);

        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(100, 0, 0, 50);
        c.gridx = 0;
        c.gridy = 1;
        canvas.add(lblComputerScore, c);

        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0, 0, 40, 0);
        c.gridx = 0;
        c.gridy = 2;
        canvas.add(playerDices, c);

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(70, 50, 100, 0);
        c.gridx = 0;
        c.gridy = 2;
        canvas.add(lblPlayerScore, c);

        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.insets = new Insets(0, 100, 10, 0);

        c.gridx = 0;
        c.gridy = 3;
        canvas.add(btnReRoll, c);

        c.anchor = GridBagConstraints.PAGE_END;
        c.insets = new Insets(0, 0, 10, 0);
        c.gridx = 0;
        c.gridy = 3;

        canvas.add(btnRollPlayer, c);

        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(0, 100, 10, 100);
        c.gridx = 0;
        c.gridy = 3;

        canvas.add(btnAddScore, c);

        new Message(4, lblmessage, "Initialising").startCountdownFromNow();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        pack();

    }

    public void RollTheDice() {
        Thread player = new Thread(new Runnable() { //this is the thread for the player.

            @Override
            public void run() {

                for (int i = 0; i < lblPlayerDice.length; i++) {
                    dicePlayer.Roll();
                    lblPlayerDice[i].setIcon(dicePlayer.getImageIcon());
                    playerDices.add(lblPlayerDice[i]);
                    userSelectStore[i] = dicePlayer.getValue();

                }

            }

        });

        player.start();

        Thread computer = new Thread(new Runnable() { // this is the thread for the computer

            @Override
            public void run() {
                int computerRollNumber = 0;
                int difference = 0;
                for (int i = 0; i < computerSelectStore.length; i++) {
                    diceComputer.Roll();
                    lblComputerDice[i].setIcon(diceComputer.getImageIcon());
                    computerDices.add(lblComputerDice[i]);
                    //  scoreComputer = scoreComputer + diceComputer.getValue();
                    computerSelectStore[i] = diceComputer.getValue();
                    computerRollNumber = computerRollNumber + diceComputer.getValue();
                    System.out.println(computerSelectStore[i]);

                }

                if (rollsLeftC > 0) { //checks if rerolls left for computer

                    if (ifAiReply == JOptionPane.YES_OPTION) { //checks if user selected AI

                        System.out.println("");

                        //  computerReRoll = new Random().nextInt(2); // random 1 and 0.
                        //System.out.println("");
                        //System.out.println(computerReRoll);
                        if (scoreComputer < scorePlayer) {  //checks if computer total score lower than user total score.
                            difference = scorePlayer - scoreComputer; //finds the difference the 2 total scores
                            System.out.println("computerRollNumber: " + computerRollNumber);
                            System.out.println("difference x 5: " + difference * 5);

                            if (computerRollNumber < difference * 5) {

                                // if (computerReRoll == 1) { //if 1 then computer decided to reroll.
                                System.out.println("computer is rerolling AI");
                             //   new Message(6, lblmessage, "Computer ReRolling").startCountdownFromNow();
                                new Message(6, lblmessage, "Computer ReRolled").startCountdownFromNow();

//                        for (int i = 0; i < selectedC.length; i++) {
//                            selectedC[i] = new Random().nextInt(2);
//                            System.out.println("selectedC = " + selectedC[i]);
//                        }
                                for (int i = 0; i < selectedC.length; i++) { //checks all the dice values
                                    if (computerSelectStore[i] > 4) { // if the die value higher than 4 
                                        selectedC[i] = 1; // 1 is keep
                                    } else { //else if not higher than 4
                                        selectedC[i] = 0; // 0 means reroll
                                    }
                                    System.out.println("selectedC = " + selectedC[i]);
                                }

                                for (int i = 0; i < selectedC.length; i++) {
                                    diceComputer.Roll(); //it rerolls the first die

                                    computerDices.add(lblComputerDice[i]); //addes to the computer dices panel

                                    if (selectedC[i] == 1) { //it checks if not meant to be rerolled
                                        diceComputer.setImages(computerSelectStore[i]); //if not then sets it back to the way it was
                                    }
                                    computerSelectStore[i] = diceComputer.getValue();//stores in an array
                                    lblComputerDice[i].setIcon(diceComputer.getImageIcon()); // sets the icon
                                    System.out.println(computerSelectStore[i]);

                                }
                                rollsLeftC--; //take 1 away from the reroll
                                lblComputerReRollLeft.setText("COMPUTER REROLL: " + Integer.toString(rollsLeftC)); //setting the text

                                System.out.println("Computer rerolls left: " + rollsLeftC);

                            }
                        }
                    } else {
                        computerReRoll = new Random().nextInt(2); // random 1 and 0. 

                        System.out.println("");
                        System.out.println("computerReRoll: " + computerReRoll);
                        if (computerReRoll == 1) { //if 1 then computer decided to reroll.

                            System.out.println("");

                            System.out.println("computer is rerolling random");
                          //  new Message(6, lblmessage, "Computer ReRolling").startCountdownFromNow();
                            new Message(6, lblmessage, "Computer ReRolled").startCountdownFromNow();

                            for (int i = 0; i < selectedC.length; i++) {
                                selectedC[i] = new Random().nextInt(2); //random reroll for each dice
                                System.out.println("selectedC = " + selectedC[i]);
                            }

                            for (int i = 0; i < selectedC.length; i++) {
                                diceComputer.Roll();//it rerolls the first die

                                computerDices.add(lblComputerDice[i]);
                                //  scoreComputer = scoreComputer + diceComputer.getValue();

                                if (selectedC[i] == 1) {//it checks if not meant to be rerolled
                                    diceComputer.setImages(computerSelectStore[i]);//if not then sets it back to the way it was
                                }
                                computerSelectStore[i] = diceComputer.getValue(); //stores in an array
                                lblComputerDice[i].setIcon(diceComputer.getImageIcon()); // sets the icon
                                System.out.println(computerSelectStore[i]);

                            }
                            rollsLeftC--; //take 1 away from the reroll
                            lblComputerReRollLeft.setText("COMPUTER REROLL: " + Integer.toString(rollsLeftC)); //setting the text

                            System.out.println("Computer rerolls left: " + rollsLeftC);

                        } else {

                            System.out.println("not decided to reroll");
                        }

                    }

                }

            }

        });

        computer.start();

    }

    public void selection() {

        if (enableSelection) {

            lblPlayerDice[0].addMouseListener(new java.awt.event.MouseAdapter() {
                boolean ss = true;

                @Override
                public void mouseEntered(MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[0].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                    }
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[0].setBorder(BorderFactory.createEmptyBorder());
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selected[0] == 0) {
                        lblPlayerDice[0].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                        ss = false;
                        selected[0] = 1;
                    } else if (selected[0] == 1) {
                        lblPlayerDice[0].setBorder(BorderFactory.createEmptyBorder());
                        selected[0] = 0;
                        ss = true;
                    }
                }
            });

            lblPlayerDice[1].addMouseListener(new java.awt.event.MouseAdapter() {
                boolean ss = true;

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[1].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[1].setBorder(BorderFactory.createEmptyBorder());
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selected[1] == 0) {
                        lblPlayerDice[1].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                        ss = false;
                        selected[1] = 1;
                    } else if (selected[1] == 1) {
                        lblPlayerDice[1].setBorder(BorderFactory.createEmptyBorder());
                        selected[1] = 0;
                        ss = true;
                    }
                }
            });

            lblPlayerDice[2].addMouseListener(new java.awt.event.MouseAdapter() {
                boolean ss = true;

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[2].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[2].setBorder(BorderFactory.createEmptyBorder());
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selected[2] == 0) {
                        lblPlayerDice[2].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                        ss = false;
                        selected[2] = 1;
                    } else if (selected[2] == 1) {
                        lblPlayerDice[2].setBorder(BorderFactory.createEmptyBorder());
                        selected[2] = 0;
                        ss = true;
                    }
                }
            });

            lblPlayerDice[3].addMouseListener(new java.awt.event.MouseAdapter() {
                boolean ss = true;

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[3].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[3].setBorder(BorderFactory.createEmptyBorder());
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selected[3] == 0) {
                        lblPlayerDice[3].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                        ss = false;
                        selected[3] = 1;
                    } else if (selected[3] == 1) {
                        lblPlayerDice[3].setBorder(BorderFactory.createEmptyBorder());
                        selected[3] = 0;
                        ss = true;
                    }
                }
            });

            lblPlayerDice[4].addMouseListener(new java.awt.event.MouseAdapter() {
                boolean ss = true;

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[4].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (ss) {
                        lblPlayerDice[4].setBorder(BorderFactory.createEmptyBorder());
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selected[4] == 0) {
                        lblPlayerDice[4].setBorder(BorderFactory.createTitledBorder(thickBorder, "KEEP", TitledBorder.CENTER, TitledBorder.TOP, new Font("arial", Font.BOLD, 12), new Color(173, 47, 48)));
                        ss = false;
                        selected[4] = 1;
                    } else if (selected[4] == 1) {
                        lblPlayerDice[4].setBorder(BorderFactory.createEmptyBorder());
                        selected[4] = 0;
                        ss = true;
                    }
                }
            });

        } else {

            for (int i = 0; i < lblPlayerDice.length; i++) {
                for (MouseListener al : lblPlayerDice[i].getMouseListeners()) {
                    lblPlayerDice[i].removeMouseListener(al);
                }
            }
              
        }

    }

    public void addScore() {
        for (int i = 0; i < userSelectStore.length; i++) {
            scorePlayer = scorePlayer + userSelectStore[i];
            scoreComputer = scoreComputer + computerSelectStore[i];
        }

        lblPlayerScore.setText(Integer.toString(scorePlayer));
        lblComputerScore.setText(Integer.toString(scoreComputer));
        btnAddScore.setVisible(false);
        btnRollPlayer.setVisible(true);
        btnReRoll.setVisible(false);

        if ((scorePlayer >= win) && (scoreComputer < win)) {

            JOptionPane.showMessageDialog(null, "YOU WIN! :)");
            new Message(10, lblmessage, "Player Wins").startCountdownFromNow();
            playerWins++;
            lblPlayerWins.setText("PLAYER WINS: " + Integer.toString(playerWins));
            for (int i = 0; i < lblPlayerDice.length; i++) {
                selected[i] = 0;
                userSelectStore[i] = 0;

            }
            newGame();
        }
        if ((scoreComputer >= win) && (scorePlayer < win)) {
            JOptionPane.showMessageDialog(null, "YOU LOST! :(");
            new Message(10, lblmessage, "Computer Wins").startCountdownFromNow();
            computerWins++;
            lblComputerWins.setText("COMPUTER WINS: " + Integer.toString(computerWins));
            for (int i = 0; i < lblPlayerDice.length; i++) {
                selected[i] = 0;
                userSelectStore[i] = 0;

            }
            newGame();
        }
        if ((scorePlayer >= win) && (scoreComputer >= win)) {
            if (scorePlayer > scoreComputer) {
                JOptionPane.showMessageDialog(null, "YOU WIN! :)");
                new Message(3, lblmessage, "Player Wins").startCountdownFromNow();
                playerWins++;
                lblPlayerWins.setText("PLAYER WINS: " + Integer.toString(playerWins));
                for (int i = 0; i < lblPlayerDice.length; i++) {
                    selected[i] = 0;
                    userSelectStore[i] = 0;

                }
            }
            if (scoreComputer > scorePlayer) {
                JOptionPane.showMessageDialog(null, "YOU LOST! :(");
                new Message(10, lblmessage, "Computer Wins").startCountdownFromNow();
                computerWins++;
                lblComputerWins.setText("COMPUTER WINS: " + Integer.toString(computerWins));
                for (int i = 0; i < lblPlayerDice.length; i++) {
                    selected[i] = 0;
                    userSelectStore[i] = 0;

                }
            }
            newGame();
        }
        enableSelection = false;
        selection();
        lblPlayerDice[0].setBorder(BorderFactory.createEmptyBorder());
        lblPlayerDice[1].setBorder(BorderFactory.createEmptyBorder());
        lblPlayerDice[2].setBorder(BorderFactory.createEmptyBorder());
        lblPlayerDice[3].setBorder(BorderFactory.createEmptyBorder());
        lblPlayerDice[4].setBorder(BorderFactory.createEmptyBorder());

    }

    public void saveScore() throws Exception {
        FileOutputStream fostream = new FileOutputStream("score.txt");
        OutputStreamWriter oswriter = new OutputStreamWriter(fostream);
        BufferedWriter bwriter = new BufferedWriter(oswriter);

        bwriter.write(Integer.toString(playerWins));
        bwriter.newLine();
        bwriter.write(Integer.toString(computerWins));
        bwriter.newLine();

        bwriter.close();
        oswriter.close();
        fostream.close();
    }

    public void loadScore() throws Exception {
        Scanner sc = new Scanner(new File("score.txt"));
        playerWins = sc.nextInt();
        computerWins = sc.nextInt();
        lblPlayerWins.setText("PLAYER WINS: " + Integer.toString(playerWins));
        lblComputerWins.setText("COMPUTER WINS: " + Integer.toString(computerWins));
    }

    public void newGame() {
        dicePlayer.init();
        dicePlayer.setImages(0);    //everything back to 0.
        diceComputer.init();
        diceComputer.setImages(0);
        lblPlayerScore.setText(Integer.toString(0));
        lblComputerScore.setText(Integer.toString(0));
        scorePlayerChoice = 0;
        scorePlayer = 0;
        scoreComputer = 0;
        rolls = -1;
        rollsLeft = 4;
        rollsLeftC = 3;
        btnReRoll.setVisible(false);
        btnRollPlayer.setVisible(true);
        btnAddScore.setVisible(false);
        lblComputerReRollLeft.setText("COMPUTER REROLL: " + Integer.toString(rollsLeftC));
        lblReRollLeft.setText("ReRoll left: " + Integer.toString(rollsLeft - 1));

        for (int i = 0; i < lblPlayerDice.length; i++) {
            selected[i] = 0;
            userSelectStore[i] = 0;

        }

        for (int i = 0; i < lblPlayerDice.length; i++) {
            dicePlayer.init();

            lblPlayerDice[i].setIcon(dicePlayer.getImageIcon());
            playerDices.add(lblPlayerDice[i]);
        }

        for (int i = 0; i < lblComputerDice.length; i++) {
            diceComputer.init();
            lblComputerDice[i].setIcon(diceComputer.getImageIcon());
            computerDices.add(lblComputerDice[i]);
        }
    }

    public static void main(String[] args) throws IOException {
        DiceGame game = new DiceGame();
    }

    private class MyCanvas extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Image bg = new ImageIcon("background.jpg").getImage();
            g2.drawImage(bg, 0, 0, bgWidth, bgHeight, this);

        }
    }
}
