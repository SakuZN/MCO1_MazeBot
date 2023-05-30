import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MazeGUI_Controller implements ActionListener {

    private final List<Bot_State> path;
    private final List<Bot_State> explored;

    /* GUIs */
    private final MazeGUI mazeGUI;

    /**
     * Constructor for the Controller class
     * @param mazeGUI the GUI
     * @param bot_AStar The bot state
     */
    public MazeGUI_Controller(MazeGUI mazeGUI, Bot_State bot_AStar) {
        this.mazeGUI = mazeGUI;
        this.path = bot_AStar.getBestPath();
        this.explored = bot_AStar.getFullPath();
        mazeGUI.setActionListener(this);
    }

    /**
     * Calls the moveBot method in the MazeGUI class
     */
    public void moveBot() {
        mazeGUI.moveBot(path);
    }

    /**
     * Calls the resetMaze method in the MazeGUI class
     */
    public void resetMaze() {
        mazeGUI.resetMaze(explored, path);
    }

    /**
     * Calls the algorithm method in the MazeGUI class
     */
    public void algorithm() {
        mazeGUI.algorithm(explored);
    }

    /**
     * Calls the restartMaze method in the MazeGUI class
     * @throws FileNotFoundException if the file is not found
     */
    public void restartMaze() throws IOException {
        mazeGUI.restartMaze();
    }

    /**
     * Calls the generateRandomMaze method in the MazeGUI class
     * @throws IOException if the file is not found
     */
    public void generateRandomMaze() throws IOException {
        mazeGUI.generateRandomMaze();
    }


    /**
     * Action listener for the buttons
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Move Bot")) {
            if(mazeGUI.getTimer().isRunning())
                return;

            resetMaze();
            moveBot();
        }
        else if (e.getActionCommand().equals("Reset")) {
            if(mazeGUI.getTimer().isRunning())
                return;

            resetMaze();
        }
        else if (e.getActionCommand().equals("A* Search")) {
            if(mazeGUI.getTimer().isRunning())
                return;

            resetMaze();
            algorithm();
        }
        else if (e.getActionCommand().equals("Generate Random Maze")) {
            if(mazeGUI.getTimer().isRunning())
                return;
            try {
                generateRandomMaze();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (e.getActionCommand().equals("Pick New Maze")) {
            if(mazeGUI.getTimer().isRunning())
                return;
            try {
                restartMaze();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
