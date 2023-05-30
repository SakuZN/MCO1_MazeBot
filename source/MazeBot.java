/**
 * @Author Balderosa, Caasi, Marcellana, Noche
 */

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

/**
 * MazeBot class is a driver class to run the program
 */
public class MazeBot {

    public static void main(String[] args) throws IOException {

        launchGUI();
    }

    /**
     * Simply launches the GUI
     */
    public static void launchGUI() throws IOException {
        // if maze.txt does not exist, generate a default maze found in the specs
        if (!mazeExist())
            autoGenMaze();

        Maze[][] maze = selectFile();
        MazeGUI mazeGUI = new MazeGUI(maze);
        Bot_State Astar = new Bot_State(maze);
        MazeGUI_Controller mazeGUIController = new MazeGUI_Controller(mazeGUI, Astar);
    }

    /**
     * selectFile is a function to select a text file to be read assuming file format is valid
     * @return a 2d array of Maze objects
     */
    private static Maze[][] selectFile()  {
        char [][] array = new char[0][];
        int size = 0;
        int goalCount = 0;
        int startCount = 0;
        int invalidCharCount = 0;
        boolean validMaze = false;
        // loop until a valid maze.txt file is selected
        while (!validMaze) {

            JFileChooser fileChooser = new JFileChooser();

            //Set default directory to this class's directory
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            //Set file filter to only show text files
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "Text Files";
                }
            });

            //Show file chooser dialog
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                array = null;
                try {
                    Scanner sc = new Scanner(fileChooser.getSelectedFile());

                    size = sc.nextInt();

                    sc.nextLine();

                    // initialize the array with the size input
                    array = new char[size][size];

                    // store the characters from the file input into the array
                    for (int i = 0; i < size; i++) {
                        String line = sc.nextLine();
                        line = line.trim();
                        for (int j = 0; j < size; j++) {
                            if (line.length() != size) {
                                invalidCharCount++;
                                continue;
                            }
                            array[i][j] = line.charAt(j);
                            if (array[i][j] == 'G') {
                                goalCount++;
                            } else if (array[i][j] == 'S') {
                                startCount++;
                            } else if (array[i][j] != '.' && array[i][j] != '#') {
                                invalidCharCount++;
                            }
                        }
                    }
                    if (goalCount == 1 && startCount == 1 && invalidCharCount == 0 && !sc.hasNext())
                        validMaze = true;
                    else {
                        JOptionPane.showMessageDialog(
                                null, "Invalid maze",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        goalCount = 0;
                        startCount = 0;
                        invalidCharCount = 0;
                    }
                    sc.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null, "Error reading file",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    goalCount = 0;
                    startCount = 0;
                    invalidCharCount = 0;
                }
            }
            else {
                System.exit(0);
            }
        }


        // create, instantiate, and setup the 2d array of Maze objects
        Maze[][] maze = new Maze[size][size];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = new Maze();
                // set the maze according to the characters in the array
                switch (array[i][j]) {
                    // set the space and wall boolean values
                    case '.' -> maze[i][j].setSpace(true);
                    case '#' -> maze[i][j].setWall(true);

                    // set the goal and start boolean values
                    case 'G' -> {
                        maze[i][j].setGoal(true);
                        Maze.setGoalState(new int[][]{{i, j}});
                    }
                    case 'S' -> {
                        maze[i][j].setStart(true);
                        Maze.setStartState(new int[][]{{i, j}});
                    }
                }
            }
        }
        return maze;
    }

    /**
     * Function to check if maze.txt exists in the current directory
     * @return true if maze.txt exists, false otherwise
     */
    private static boolean mazeExist() {
        String fileName = "maze.txt";
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * Function to automatically generate a maze.txt file seen in the default maze setup in MCO1 Specifications
     * @throws IOException if file cannot be created
     */
    private static void autoGenMaze() throws IOException {
        // TODO Auto-generated method stub
        //Choose default file default as the same as mazebot
        int size = 5;
        char[][] defMaze = {
                {'.', '.', '.', '.', 'G'},
                {'.', '#', '#', '#', '#'},
                {'.', '.', '.', '#', 'S'},
                {'.', '#', '.', '#', '.'},
                {'.', '#', '.', '.', '.'}};

        File file = new File("maze.txt");
        FileWriter fw = new FileWriter(file);

        fw.write(size + "\n");
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                fw.write(defMaze[i][j]);
            }
            fw.write("\n");
        }
        fw.close();
    }
}
