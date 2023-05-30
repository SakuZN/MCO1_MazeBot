import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MazeGenerator {
    private static final char S = 'S';  // Start
    private static final char G = 'G';  // Goal
    private static final char DOT = '.';  // Movable Space
    private static final char HASH = '#';  // Wall
    private final char[][] maze;
    private final int[][] startGoal;
    private final int size;
    private final int hashChance;

    /**
     * Constructor for the MazeGenerator class
     * @param size the size of the maze
     * @param hashChance the chance of a space being a wall
     */

    public MazeGenerator(int size, int hashChance) {
        this.size = size;
        maze = new char[size][size];
        this.hashChance = hashChance;
        this.startGoal = null;
    }

    public MazeGenerator(int size, int hashChance, int[][] startGoal) {
        this.size = size;
        maze = new char[size][size];
        this.hashChance = hashChance;
        this.startGoal = startGoal;
    }

    /**
     * Generate a random maze
     */
    public void generate() {
        Random random = new Random();
        if (startGoal == null && size > 3) {
            int randomSX = random.nextInt(0, size);
            int randomSY = random.nextInt(0, size);
            maze[randomSX][randomSY] = S;
            int randomGX = random.nextInt(0, size);
            int randomGY = random.nextInt(0, size);
            while (tooClose(randomSX, randomSY, randomGX, randomGY)) {
                randomGX = random.nextInt(0, size);
                randomGY = random.nextInt(0, size);
            }

            maze[randomGX][randomGY] = G;
        }
        else {
            int startX = startGoal[0][0];
            int startY = startGoal[0][1];
            int goalX = startGoal[1][0];
            int goalY = startGoal[1][1];
            maze[startX][startY] = S;
            maze[goalX][goalY] = G;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int randomNum = random.nextInt(100);
                if (randomNum < hashChance && maze[i][j] != S && maze[i][j] != G)
                        maze[i][j] = HASH;
                else if (maze[i][j] != S && maze[i][j] != G)
                    maze[i][j] = DOT;
            }
        }
    }

    /**
     * Print the maze to a file
     *
     * @param fileWriter the file to print to
     * @throws IOException if file cannot be written to
     */
    public void printMaze(FileWriter fileWriter) throws IOException {
        fileWriter.write((size) + "\n");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fileWriter.write(maze[i][j]);
            }
            fileWriter.write("\n");
        }
        fileWriter.close();
    }

    /**
     * Check if the start and goal are too close
     * @param sx the start x
     * @param sy the start y
     * @param gx the goal x
     * @param gy the goal y
     * @return whether the start and goal are too close
     */
    private static boolean tooClose(int sx, int sy, int gx, int gy) {
        return Math.abs(sx - gx) < 3 && Math.abs(sy - gy) < 3;
    }


    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter size of maze: ");
        int size = scan.nextInt();
        while (size < 2) {
            System.out.println("Size must be greater than 1. Enter size of maze: ");
            size = scan.nextInt();
        }
        MazeGenerator mazeGenerator = new MazeGenerator(size, 30);

        // output to absolute path of mazegen class


        File file = new File("mazeGen.txt");
        FileWriter fileWriter = new FileWriter(file);
        mazeGenerator.generate();
        mazeGenerator.printMaze(fileWriter);
    }
}
