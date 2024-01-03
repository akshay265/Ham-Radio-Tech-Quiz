import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class QuizGUI extends JFrame implements ActionListener {
    private static JButton aBtn, bBtn, cBtn, dBtn, nextBtn, quitBtn;
    private static JLabel totalLbl, rightLbl, wrongLbl; // Displays the stats of questions answered.
    private static JTextArea textDisplay; // Area where question (and its answer) is displayed.
    private boolean isQuestionDisplayed; // Used to toggle whether the answer should be shown.

    public QuizGUI() throws IOException {
        super("Ham Radio Quiz");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,800);
        Toolkit theKit = getToolkit();
        Dimension screenSize = theKit.getScreenSize();
        Rectangle thisWindow = getBounds();
        int x = (screenSize.width - thisWindow.width) / 2;
        int y = (screenSize.height - thisWindow.height) / 2;
        setBounds(x, y, thisWindow.width, thisWindow.height);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.lightGray);
        setResizable(true);

        PageInitializer.init();

        JPanel topPanel = new JPanel();
        totalLbl = new JLabel("Total Attempted: " + PageInitializer.getTotalAttempts());
        rightLbl = new JLabel("   Num Right: " + PageInitializer.getNumCorrect());
        wrongLbl = new JLabel("   Num Wrong: " + PageInitializer.getNumWrong());
        // used to space stats from buttons
        JLabel spacerLbl = new JLabel("                                    ");
        nextBtn = new JButton("Next");
        quitBtn = new JButton("Quit");
        nextBtn.addActionListener(this);
        quitBtn.addActionListener(this);
        topPanel.add(totalLbl);
        topPanel.add(rightLbl);
        topPanel.add(wrongLbl);
        topPanel.add(spacerLbl);
        topPanel.add(nextBtn);
        topPanel.add(quitBtn);

        textDisplay = new JTextArea();
        textDisplay.setMargin( new Insets(10,10,10,10));
        textDisplay.setText(PageInitializer.qToString());
        isQuestionDisplayed = true;
        textDisplay.setEditable(false);
        textDisplay.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        textDisplay.setLineWrap(true);
        textDisplay.setWrapStyleWord(true);

        JPanel bottomPanel = new JPanel();
        JLabel ansLbl = new JLabel("Answer: ");
        aBtn = new JButton("A");
        bBtn = new JButton("B");
        cBtn = new JButton("C");
        dBtn = new JButton("D");
        aBtn.addActionListener(this);
        bBtn.addActionListener(this);
        cBtn.addActionListener(this);
        dBtn.addActionListener(this);
        bottomPanel.add(ansLbl);
        bottomPanel.add(aBtn);
        bottomPanel.add(bBtn);
        bottomPanel.add(cBtn);
        bottomPanel.add(dBtn);


        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(textDisplay, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }


    public static void main(String[] args) {
        try {
            new QuizGUI();
        } catch (Exception e) {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                    e.getMessage(),
                    "Java Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == quitBtn) {
            System.exit(1);
        }

        if (isQuestionDisplayed) { // Question page -- All buttons work
            char userAns = (source == aBtn) ? 'A' :
                           (source == bBtn) ? 'B' :
                           (source == cBtn) ? 'C' :
                           (source == dBtn) ? 'D' : 'X';
            char correctAns = PageInitializer.correctAns();

            if (userAns == correctAns) {
                PageInitializer.rightAns();
            } else {
                PageInitializer.wrongAns();
            }

            textDisplay.append("\n\n Correct Answer is:  " + correctAns);
            textDisplay.append("\n Your Answer was:  " + userAns);
            isQuestionDisplayed = false;

        } else { // Answer page -- Only next and quit buttons should work
            if (source == nextBtn) {
                PageInitializer.nextQ();
                textDisplay.setText(PageInitializer.qToString());
                totalLbl.setText("Total Attempted: " + PageInitializer.getTotalAttempts());
                rightLbl.setText("   Num Right: " + PageInitializer.getNumCorrect());
                wrongLbl.setText("   Num Wrong: " + PageInitializer.getNumWrong());
                isQuestionDisplayed = true;
            }
        }
    }
}
