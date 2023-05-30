import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Objects;

public class MazeGenMenu {

    private JFrame menuFrame;
    private JTextField mazeSizeField;
    private JTextField wallPercentageField;
    private JButton generateButton;
    private JButton showMazeButton;
    JCheckBox randomSGCheckBox;
    JPanel startGoalPanel;
    private JTextField startXField;
    private JTextField startYField;
    private JTextField goalXField;
    private JTextField goalYField;
    JSplitPane splitPane;
    boolean randomSG = true;


    /**
     * Constructor for the MazeGenMenu class
     * Initializes the components and adds them to the small subframe
     */
    public MazeGenMenu() {
        // Initialize components
        menuFrame = new JFrame("Maze Generator");
        mazeSizeField = new JTextField("10");
        wallPercentageField = new JTextField("30");
        generateButton = new JButton("Generate");
        showMazeButton = new JButton("Show Maze");
        randomSGCheckBox = new JCheckBox("Specify start and goal?");

        // Add components to frame
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Maze size: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel.add(mazeSizeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Wall percentage: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panel.add(wallPercentageField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(randomSGCheckBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(generateButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(showMazeButton, gbc);

        initStartGoalPanel();
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(startGoalPanel, BorderLayout.SOUTH);

        menuFrame.setContentPane(contentPane);

        // Set frame properties
        menuFrame.setSize(300, 350);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        menuFrame.setResizable(false);

    }

    /**
     * Show the menu window
     */
    void show() {
        // Show menu window
        menuFrame.setVisible(true);
    }

    /**
     * Initialize the optional start and goal panel
     */
    private void initStartGoalPanel() {
        // init start and goal panel

        startGoalPanel = new JPanel();
        startGoalPanel.setLayout(new GridLayout(0, 2));
        startGoalPanel.setBorder(BorderFactory.createTitledBorder("Start and Goal"));
        startGoalPanel.setVisible(false);
        startXField = new JTextField("0");
        startYField = new JTextField("0");
        goalXField = new JTextField("0");
        goalYField = new JTextField("0");
        startGoalPanel.add(new JLabel("Start X: "));
        startGoalPanel.add(startXField);
        startGoalPanel.add(new JLabel("Start Y: "));
        startGoalPanel.add(startYField);
        startGoalPanel.add(new JLabel("Goal X: "));
        startGoalPanel.add(goalXField);
        startGoalPanel.add(new JLabel("Goal Y: "));
        startGoalPanel.add(goalYField);
    }

    /**
     * Set the action listener for buttons
     *
     * @param listener the action listener
     */
    public void setActionListener(ActionListener listener) {
        generateButton.addActionListener(listener);
        randomSGCheckBox.addActionListener(listener);
        showMazeButton.addActionListener(listener);
    }

    /**
     * Generate the maze and save it to a file
     *
     * @throws IOException if the file cannot be written to
     */
    public void generateMaze() throws IOException {
        // Generate maze
        int mazeSize;
        int wallPercentage;
        int[][] startGoal = null;
        int startX;
        int startY;
        int goalX;
        int goalY;
        //Do a bunch of error checking
        try {
            mazeSize = Integer.parseInt(mazeSizeField.getText());
            wallPercentage = Integer.parseInt(wallPercentageField.getText());
            if (mazeSize < 3 || mazeSize > 64) {
                JOptionPane.showMessageDialog(null,
                        "Maze size must be between 3 and 64");
                return;
            }
            if (wallPercentage < 0 || wallPercentage > 100) {
                JOptionPane.showMessageDialog(null,
                        "Wall percentage must be between 0 and 100");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Maze size and wall percentage field must be valid integers!");
            return;
        }
//

        if (!randomSG) {
            try {
                startX = Integer.parseInt(startXField.getText());
                startY = Integer.parseInt(startYField.getText());
                goalX = Integer.parseInt(goalXField.getText());
                goalY = Integer.parseInt(goalYField.getText());
                if (startX < 0 || startX >= mazeSize || startY < 0 || startY >= mazeSize) {
                    JOptionPane.showMessageDialog(null,
                            "Start X and Y must be within maze size");
                    return;
                } else if (goalX < 0 || goalX >= mazeSize || goalY < 0 || goalY >= mazeSize) {
                    JOptionPane.showMessageDialog(null,
                            "Goal X and Y must be within maze size");
                    return;
                } else if (startX == goalX && startY == goalY) {
                    JOptionPane.showMessageDialog(null,
                            "Start and goal cannot be the same");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Start X, Start Y, Goal X and Goal Y field must be valid integers!");
                return;
            }
            startGoal = new int[][]{{startX, startY}, {goalX, goalY}};
        }

        MazeGenerator mazeGenerator = new MazeGenerator(mazeSize, wallPercentage, startGoal);
        mazeGenerator.generate();
        File fi = new File("newMaze.txt");
        FileWriter fileWriter = new FileWriter(fi);
        mazeGenerator.printMaze(fileWriter);

        JOptionPane.showMessageDialog(null,
                "Maze generated successfully\n"
                        + "File saved in path: " + fi.getAbsolutePath());

        readMaze();
    }

    /**
     * Shows a preview of the generated maze in a new text area window
     */
    public void readMaze() {
        // Read maze and output to JTextArea
        File fi = new File("newMaze.txt");
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fi));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (IOException e) {
            //show a popup
            JOptionPane.showMessageDialog(null,
                    "File not found, make sure you have generated a maze first");
        }

        JFrame frame = new JFrame("Maze Preview");
        JTextArea textArea = new JTextArea(sb.toString());

        int fontSize = 18 - (sb.length() / 500);
        textArea.setFont(new Font("Courier New", Font.BOLD, fontSize));
        textArea.setEditable(false);
        frame.add(textArea);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

/**
 * Self-contained Controller for the menu
 */
class Menu_Controller implements ActionListener {
    MazeGenMenu menu;

    /**
     * Constructor for the menu controller
     * @param menu the menu for MazeGen
     */
    public Menu_Controller(MazeGenMenu menu) {
        this.menu = menu;
        this.menu.show();
        this.menu.setActionListener(this);
    }

    /**
     * Process the action event for the menu
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Objects.equals(e.getActionCommand(), "Specify start and goal?")) {
            if (menu.randomSGCheckBox.isSelected()) {
                menu.randomSG = false;
                menu.startGoalPanel.setVisible(true);
            }
            else {
                menu.randomSG = true;
                menu.startGoalPanel.setVisible(false);
            }
        }
        else if (Objects.equals(e.getActionCommand(), "Generate")) {
            try {
                menu.generateMaze();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else if (Objects.equals(e.getActionCommand(), "Show Maze")) {
            menu.readMaze();
        }
    }
}
