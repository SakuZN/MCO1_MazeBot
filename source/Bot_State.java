import java.util.*;

public class Bot_State {

    private Maze[][] maze;
    private int[][] state;
    private int  x;
    private int  y;
    private int costSoFar;
    private int pathCost;
    private int heuristicCost;
    private Bot_State parent;
    private final PriorityQueue<Bot_State> FRONTIER = new PriorityQueue<>(new priorityState());
    private final HashSet<Bot_State> EXPLORED = new HashSet<>();

    /**
     * The public constructor of the state class
     * Used to initialize the maze
     * @param maze the maze to be traversed
     */
    public Bot_State(Maze[][] maze) {
        this.maze = maze;
    }

    /**
     * Private constructor used only to create new successor states
     * Each state contains important information about the state
     * @param state the current location of the state in the maze
     * @param pathCost the cost of moving to this state
     * @param heuristicCost the heuristic cost of this state to the goal state
     * @param parent the parent of this state / previous state
     */
    private Bot_State(int[][] state, int pathCost, int heuristicCost, Bot_State parent) {
        this.state = state;
        this.x = state[0][0];
        this.y = state[0][1];
        this.pathCost = pathCost;
        this.heuristicCost = heuristicCost;
        this.costSoFar = pathCost + heuristicCost;
        this.parent = parent;
    }

    @Override
    /**
     * Override the equals method to compare the x and y coordinates of each state
     * @param obj the object to compare
     * @return true if the x and y coordinates of each state are equal
     */
    public boolean equals(Object obj) {
        if (obj instanceof Bot_State other) {
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }

    @Override
    /**
     * Override the hashcode method to return the hashcode of the x and y coordinates of each state
     */
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * The comparator function for the priority queue which compares the cost of each state
     * and returns the state with the lowest cost
     */
    static class priorityState implements Comparator<Bot_State> {
        @Override
        public int compare(Bot_State s1, Bot_State s2) {
            int start1 = Bot_State.manhattanDistance(s1.state, Maze.getStartState());
            int start2 = Bot_State.manhattanDistance(s2.state, Maze.getStartState());

            //Implementation that improves the bot's search performance
            //if (s1.costSoFar == s2.costSoFar)
                //return start2 - start1;

            return s1.costSoFar - s2.costSoFar;
        }
    }

    /**
     * A* algorithm that precalculates the total cost of each state and adds it to the priority queue
     * @param maze The maze, which is a 2D array of Maze objects
     * @return the optimal path from the start state to the goal state
     */
    private List<Bot_State> AStarAlgorithm(Maze[][] maze){
        int[][] goalState = Maze.getGoalState();
        int[][] startState = Maze.getStartState();
        int initialHeuristic = manhattanDistance(startState, goalState);
        Bot_State start = new Bot_State(startState, 0, initialHeuristic, null);
        FRONTIER.clear();
        EXPLORED.clear();

        //Add the start state to the priority queue/FRONTIER
        FRONTIER.add(start);

        // While the frontier is not empty, keep searching
        while (!FRONTIER.isEmpty()) {
            // Get the state with the lowest cost
            Bot_State current = FRONTIER.poll();
            // If the current state is the goal state, return the path
            if (isEnd(current, goalState)) {
                return getPath(current);
            }
            // If the current state is not in the explored states, add it to explored states
            EXPLORED.add(current);
            // Get the next possible states
            List<Bot_State> nextBotStates = getNextStates(maze, current);
            // Add the next possible states to the frontier
            for (Bot_State successors : nextBotStates)
                // If the next possible state is not in the explored states, add to frontier
                if (!EXPLORED.contains(successors))
                    FRONTIER.add(successors);
        }
        // If no path is found, return null
        return null;
    }

    /**
     * A* algorithm that precalculates the total cost of each state and adds it to the priority queue
     * @param maze The maze, which is a 2d array of Maze objects
     * @return the full path / states the bot went to before finding the optimal path
     */
    private List<Bot_State> AStarStateExplored(Maze[][] maze){
        int[][] goalState = Maze.getGoalState();
        int[][] startState = Maze.getStartState();
        List<Bot_State> explored = new ArrayList<>();
        int initialHeuristic = manhattanDistance(startState, goalState);
        Bot_State start = new Bot_State(startState, 0, initialHeuristic, null);
        FRONTIER.clear();
        EXPLORED.clear();

        //Add the start state to the priority queue/FRONTIER
        FRONTIER.add(start);

        //While the frontier is not empty do:
        while (!FRONTIER.isEmpty()) {
            Bot_State current = FRONTIER.poll(); //Get the state with the lowest cost
            if (isEnd(current, goalState)) { //If current state is the goal state, return the explored states
                explored.add(current);
                return explored;
            }
            //If the current state is not in the explored states, add it to explored states to be returned
            if (!EXPLORED.contains(current))
                explored.add(current);

            EXPLORED.add(current); //Add the current state to the explored states
            List<Bot_State> nextBotStates = getNextStates(maze, current); //Get the next possible states
            for (Bot_State successors : nextBotStates) //Add the next possible states to the frontier
                if (!EXPLORED.contains(successors)) //If the next possible state is not in the explored states, add
                    FRONTIER.add(successors);

        }
        // Return all explored state, even if a path is not found
        return explored;
    }

    /**
     * Simply checks if the current state is the goal state
     * @param s the current state
     * @param goalState the goal state
     * @return true if the current state is the goal state
     */
    private boolean isEnd(Bot_State s, int[][] goalState){
        return Arrays.deepEquals(s.state, goalState);
    }

    /**
     * Helper function to get the successors states from the current state of the bot
     * @param maze The maze, which is a 2D array of Maze objects
     * @param current The current state of the bot
     * @return a list of the next possible states
     */
    private List<Bot_State> getNextStates(Maze[][] maze, Bot_State current) {
        List<Bot_State> nextBotStates = new ArrayList<>();

        //List of all possible moves
        int[] xMoves = {1, -1, 0, 0};
        int[] yMoves = {0, 0, 1, -1};

        //Checks for N.E.W.S moves
        for (int i = 0; i < 4; i++) {
            int x = current.x + xMoves[i];
            int y = current.y + yMoves[i];
            int[][] posState = {{x, y}};

            //If the next possible state is valid, add it to the list of next possible states
            if (x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && !maze[x][y].isWall()) {
                int newCost = current.pathCost + 1;
                int heuristicCost = manhattanDistance(posState, Maze.getGoalState());
                Bot_State nextBotState = new Bot_State(posState, newCost, heuristicCost, current);
                nextBotStates.add(nextBotState);
            }
        }
        return nextBotStates;
    }

    /**
     * Helper function that traces the path from the goal state to the start state
     * @param current the current state
     * @return the optimal path from the start state to the goal state
     */
    private List<Bot_State> getPath(Bot_State current) {
        List<Bot_State> path = new ArrayList<>();
        //While state's parent is not null, add the state to the path and set the current state to the parent
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        //Reverse the path so that it is from start to goal
        Collections.reverse(path);
        return path;
    }


    /**
     * Admissible heuristic function that calculates how close the next state is to the goal state
     * @param nextState the next state
     * @param goalState the goal state
     * @return the manhattan distance between the next state and the goal state
     */

    private static int manhattanDistance(int[][] nextState, int[][] goalState) {

        int distance = 0;
        distance += Math.abs(nextState[0][0] - goalState[0][0]);
        distance += Math.abs(nextState[0][1] - goalState[0][1]);
        return distance;
    }

    /**
     * Gets the list of optimal states from the A* algorithm
     * @return the list of optimal states from start to goal
     */
    public List<Bot_State> getBestPath() {
        return AStarAlgorithm(maze);
    }

    /**
     * Gets the list of all explored states from the A* algorithm
     * @return the list of all explored states from start to goal
     */
    public List<Bot_State> getFullPath() {
        return AStarStateExplored(maze);
    }

    /**
     * Gets the state's x and y coordinates
     * @return the state's x and y coordinates
     */
    public int[][] getState() {
        return state;
    }

    /**
     * getter method to return the x index
     * @return the x index
     */
    public int getX() {
        return x;
    }

    /**
     * getter function to return the y index
     * @return the y index
     */
    public int getY() {
        return y;
    }


    /**
     * Gets the move cost of the state
     * @return the move cost of the state
     */
    public int getPathCost() {
        return pathCost;
    }

    /**
     * Gets the total cost of the state
     * @return the total cost of the state
     */
    public int getCostSoFar() {
        return costSoFar;
    }

    /**
     * Gets the heuristic cost of the state
     * @return the heuristic cost of the state
     */
    public int getHeuristicCost() {
        return heuristicCost;
    }

    /**
     * Gets the parent state of the current state
     * @return the parent state of the current state
     */
    public Bot_State getParent() {
        return parent;
    }
}