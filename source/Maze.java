/**
 * Maze class represents the Maze that the Maze Bot will traverse
 */
public class Maze {

    private boolean isWall;
    private boolean isSpace;
    private boolean isGoal;
    private boolean isStart;
    private static int[][] goalState;
    private static int[][] startState;

    /**
     * the default constructor of maze with no parameters
     */
    public Maze() {
        this.isSpace = false;
        this.isWall = false;
        this.isStart = false;
        this.isGoal = false;
    }

    /**
     * setWall is a function to set a given index of the maze as a wall
     * @param wall boolean value to be set
     */
    public void setWall(boolean wall) {
        isWall = wall;
    }

    /**
     * setSpace is a function to set a given index of the maze as an empty space
     * @param space boolean value to be set
     */
    public void setSpace(boolean space) {
        isSpace = space;
    }

    /**
     * setGoal is a function to set a given index of the maze as a Goal
     * @param goal boolean value to be set
     */
    public void setGoal(boolean goal) {
        isGoal = goal;
    }

    /**
     * setStart is a function to set a given index of the maze as the starting point
     * @param start boolean value to be set
     */
    public void setStart(boolean start) {
        isStart = start;
    }

    /**
     * setGoalState is a function to store the index of the goal state in a variable
     * @param goalState the 2d array index of the goal state in the maze
     */
    public static void setGoalState(int[][] goalState) {
        Maze.goalState = goalState;
    }

    /**
     * setStartState is a function to store the index of the start state in a variable
     * @param startState the 2d array index of the start state in the maze
     */
    public static void setStartState(int[][] startState) {
        Maze.startState = startState;
    }

    /**
     * getGoalState is a getter function of the index of the goal state
     * @return the 2d array index of the goal state
     */
    public static int[][] getGoalState() {
        return goalState;
    }

    /**
     * getStartState is a getter function of the index of the start state
     * @return the 2d array index of the start state
     */
    public static int[][] getStartState() {
        return startState;
    }

    /**
     * isWall is a getter function to return the boolean value of the isWall property
     * @return the boolean value of the isWall property of the object
     */
    public boolean isWall() {
        return isWall;
    }

    /**
     * isSpace is a getter function to return the boolean value of the isSpace property
     * @return the boolean value of the isSpace property of the object
     */
    public boolean isSpace() {
        return isSpace;
    }

    /**
     * isGoal is a getter function to return the boolean value of the isGoal property
     * @return the boolean value of the isGoal property of the object
     */
    public boolean isGoal() {
        return isGoal;
    }

    /**
     * isStart is a getter function to return the boolean value of the isStart property
     * @return the boolean value of the isStart property of the object
     */
    public boolean isStart() {
        return isStart;
    }

}
