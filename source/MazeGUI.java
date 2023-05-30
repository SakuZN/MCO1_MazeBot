import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MazeGUI extends JFrame {
    private final Maze[][] maze;
    private JLabel[][] label;
    private JTextArea textArea;

    /**
     * delayTimer is used to delay the animation of the bot moving through the maze
     */
    private Timer delayTimer = new Timer(100, null);

    /**
     * delay is the delay between each step of the bot moving through the maze
     */
    private int delay = 300;
    // private JLabel bot;

    /**
     * isOptimalPath is a variable to check if user has made the bot move
     */
    private boolean isOptimalPath = false;

    /**
     * isALLPath is a variable to check if user has made the bot search for a path
     */
    private boolean isALLPath = false;

    /**
     * thereIsPath is a variable to check if there is a path from start to goal
     */
    private boolean thereIsPath = false;

    /**
     * haveDoneSearch is a variable to check if the bot has searched for a path at least once
     */
    private boolean haveDoneSearch = false;

    private JButton btnStart;
    private JButton btnReset;
    private JButton btnAlgorithm;
    private JButton btnGenerate;
    private JButton btnNewMaze;
    private final ImageIcon bot = new ImageIcon("bot.png");
    private final Image botImage = bot.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

    /**
     * constructor for MazeGUI with the 2d array maze as parameter
     *
     * @param maze is the 2d array of Maze objects
     */
    public MazeGUI(Maze[][] maze) {
        super("MazeBot");

        this.maze = maze;
        this.setLayout(new BorderLayout());
        this.setSize(1200, 800);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        layoutComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * layoutComponents is a function designing the GUI and laying out its components
     */
    public void layoutComponents() {
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(maze.length, maze[0].length));

        JPanel panelSouth = new JPanel();
        panelSouth.setBackground(Color.pink);

        btnStart = new JButton("Move Bot");
        btnReset = new JButton("Reset");
        btnAlgorithm = new JButton("A* Search");
        btnGenerate = new JButton("Generate Random Maze");
        btnNewMaze = new JButton("Pick New Maze");
        label = new JLabel[maze.length][maze[0].length];

        bot.setImage(botImage);

        /* spawns MAZE */
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                label[i][j] = new JLabel();
                label[i][j].setHorizontalAlignment(JLabel.CENTER);
                label[i][j].setPreferredSize(new Dimension(100, 100));

                if (maze[i][j].isWall()) {                      // wall
                    label[i][j].setBackground(Color.BLACK);
                } else if (maze[i][j].isSpace()) {               // space
                    label[i][j].setBackground(Color.WHITE);
                } else if (maze[i][j].isStart()) {               // starting
                    label[i][j].setBackground(Color.YELLOW);
                    label[i][j].setText("Start");
                    label[i][j].setIcon(bot);
                } else if (maze[i][j].isGoal()) {                // goal
                    label[i][j].setBackground(Color.GREEN);
                    label[i][j].setText("Goal");
                }

                label[i][j].setOpaque(true);
                label[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                panelCenter.add(label[i][j], BorderLayout.CENTER);

            }

        }

        //Add all functional buttons to the bottom panel

        this.add(panelCenter, BorderLayout.CENTER);
        panelSouth.add(btnStart);
        panelSouth.add(btnReset);
        panelSouth.add(btnAlgorithm);
        panelSouth.add(btnGenerate);
        panelSouth.add(btnNewMaze);

        this.add(panelSouth, BorderLayout.SOUTH);

        //Adding text area to show states explored information
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        add(scrollPane, BorderLayout.EAST);
        textArea.append("States Explored: " + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * setActionListener is a function that sets the action listener for the buttons
     *
     * @param listener the action listener
     */
    public void setActionListener(ActionListener listener) {

        btnStart.addActionListener(listener);
        btnReset.addActionListener(listener);
        btnAlgorithm.addActionListener(listener);
        btnGenerate.addActionListener(listener);
        btnNewMaze.addActionListener(listener);
    }

    /**
     * Move the bot to the goal only if a path is found
     *
     * @param path the path to the goal
     */
    public void moveBot(List<Bot_State> path) {
        if (delayTimer.isRunning())
            return;

        if (!thereIsPath && !haveDoneSearch) {
            showMessage("No Path found. Please initiate my search algorithm.");
            return;
        } else if (haveDoneSearch && !thereIsPath && path == null) {
            showMessage("Search done but I cannot reach the goal!");
            return;
        }

        if (!showDelay())
            return;

        isOptimalPath = true;

        final int[] index = {0};
        delayTimer = new Timer(delay, e -> {
            if (index[0] < path.size()) {
                Bot_State currBotState = path.get(index[0]);
                int x = currBotState.getX();
                int y = currBotState.getY();
                int cost = currBotState.getPathCost();
                int heuristic = currBotState.getHeuristicCost();
                int totalCost = currBotState.getCostSoFar();
                String stateInfo = String.format("""
                                State #%d - (%d, %d)
                                Cost so far: %d | Heuristic: %d | Total cost: %d

                                """,
                        index[0], x, y, cost, heuristic, totalCost);
                label[x][y].setIcon(bot);
                label[x][y].setToolTipText(stateInfo);
                if (index[0] < path.size() - 1)
                    label[x][y].setBackground(Color.YELLOW);
                textArea.append(stateInfo);
                textArea.setCaretPosition(textArea.getDocument().getLength());
                index[0]++;
            } else {
                showMessage("Optimal steps to the goal: " + (path.size() - 1));
                ((Timer) e.getSource()).stop();
            }
        });
        delayTimer.start();

    }

    /**
     * This method will reset the maze to its original state
     *
     * @param explored the list of states explored
     * @param path     the list of states in the optimal path
     */
    public void resetMaze(List<Bot_State> explored, List<Bot_State> path) {
        if (isOptimalPath) {
            reinstateMaze(path);
            isOptimalPath = false;
        } else if (isALLPath) {
            reinstateMaze(explored);
            isALLPath = false;
        }
    }

    /**
     * This method will show the A* search algorithm in action
     *
     * @param explored the list of states explored
     */
    public void algorithm(List<Bot_State> explored) {
        if (delayTimer.isRunning())
            return;
        if (!showDelay())
            return;
        isALLPath = true;

        final int[] index = {0};
        delayTimer = new Timer(delay, e -> {
            if (index[0] < explored.size()) {
                Bot_State currBotState = explored.get(index[0]);
                int x = currBotState.getX();
                int y = currBotState.getY();
                int cost = currBotState.getPathCost();
                int heuristic = currBotState.getHeuristicCost();
                int totalCost = currBotState.getCostSoFar();
                String stateInfo = String.format("""
                                State #%d - (%d, %d)
                                Cost so far: %d | Heuristic: %d | Total cost: %d

                                """,
                        index[0], x, y, cost, heuristic, totalCost);
                label[x][y].setBackground(Color.green);
                label[x][y].setToolTipText(stateInfo);
                label[x][y].setText(String.format("#%d", index[0]));
                textArea.append(stateInfo);
                textArea.setCaretPosition(textArea.getDocument().getLength());
                if (Arrays.deepEquals(currBotState.getState(), Maze.getGoalState()))
                    showOptimalPath(currBotState, explored.size() - 1);
                index[0]++;
            } else {
                if (!thereIsPath)
                    showMessage("No path found");
                ((Timer) e.getSource()).stop();
            }
        });
        delayTimer.start();
        haveDoneSearch = true;
    }

    /**
     * If a path is found, show the optimal path from goal to start
     *
     * @param explored the goal state to be traced back to start
     */
    private void showOptimalPath(Bot_State explored, int exploredSize) {
        List<Bot_State> goalToStart = new ArrayList<>();

        while (explored != null) {
            goalToStart.add(explored);
            explored = explored.getParent();
        }

        final int[] index = {0};
        delayTimer = new Timer(delay, e -> {
            if (index[0] < goalToStart.size()) {
                Bot_State currBotState = goalToStart.get(index[0]);
                int x = currBotState.getX();
                int y = currBotState.getY();

                if (maze[x][y].isGoal())
                    label[x][y].setBackground(Color.red);
                else
                    label[x][y].setBackground(Color.yellow);

                index[0]++;
            } else {
                showMessage("Optimal path found!\nIt took " + exploredSize + " steps to find the goal.");
                ((Timer) e.getSource()).stop();
            }
        });
        delayTimer.start();
        thereIsPath = true;

    }

    /**
     * Reinstate the maze to its original state
     *
     * @param explored the list of explored states
     */

    private void reinstateMaze(List<Bot_State> explored) {
        //clear the maze in reverse order of explored
        final int[] index = {explored.size() - 1};
        delay = 0;
        delayTimer = new Timer(delay, e -> {
            if (index[0] >= 0) {
                Bot_State currBotState = explored.get(index[0]);
                int x = currBotState.getX();
                int y = currBotState.getY();
                label[x][y].setIcon(null);
                label[x][y].setToolTipText(null);
                label[x][y].setText(null);
                if (maze[x][y].isWall()) {
                    label[x][y].setBackground(Color.BLACK);
                } else if (maze[x][y].isSpace()) {
                    label[x][y].setBackground(Color.WHITE);
                } else if (maze[x][y].isStart()) {
                    label[x][y].setBackground(Color.YELLOW);
                    label[x][y].setText("Start");
                    label[x][y].setIcon(bot);
                } else if (maze[x][y].isGoal()) {
                    label[x][y].setBackground(Color.GREEN);
                    label[x][y].setText("Goal");
                }
                index[0]--;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        delayTimer.start();
        textArea.setText("States Explored: " + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Fully restarts the maze by disposing the current frame and launching a new one
     *
     */
    public void restartMaze() throws IOException {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to restart?", "Restart", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            MazeBot.launchGUI();
        }

    }

    /**
     * Let the user generate  random maze
     */
    public void generateRandomMaze(){

        MazeGenMenu mazeGenMenu = new MazeGenMenu();
        Menu_Controller menu_controller = new Menu_Controller(mazeGenMenu);
    }

    /**
     * This method will return the delay timer
     *
     * @return the delay timer
     */
    public Timer getTimer() {
        return delayTimer;
    }

    /**
     * Reusable method to show a message
     *
     * @param message the message to be shown
     */
    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Show a dialog to get the delay of search or movement animation
     */
    private boolean showDelay() {
        boolean delayWasSet = true;
        String delayString = JOptionPane.showInputDialog("Enter delay in milliseconds");

        //check if user cancel the dialog
        if (delayString == null)
            return !delayWasSet;
        try {
            delay = Integer.parseInt(delayString);
        } catch (NumberFormatException e) {
            showMessage("Invalid delay, defaulting to 100ms");
            delay = 100;
        }
        return delayWasSet;
    }

}
