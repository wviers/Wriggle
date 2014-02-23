import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


//A* Graph Search
//I add the heads states to the Frontier queue first, adding states for each of the worms starting  
//with 0.  States will be ordered in the frontier using the heuristic that gives highest priority to 
//states that move the blue worm closer to the goal.  The distance is measured by the MahattanDistance
//equation.  For states that move a non blue worm, the priority considers whether or not that move is 
//towards or away from the goal.  States that move non blue worms away from the goal are preferred.  
//States are popped off of the frontier queue onto the evalQueue which in turn adds new states to the 
//frontier.  Each time a state is added to the eval queue it is also added to a hashmap representing
//the explored set.  This continues until the state on the eval queue is the goal state.
public class ASGS
{
    char[][] board;
    
    //The states to be evaluated
    ArrayList<State> frontierStates = new ArrayList<State>();
    
    //The first state is the current state being evaluated
    ArrayList<State> evalStates = new ArrayList<State>();
    
    //Hashmap of explored states
    HashMap<String, State> exploredStates = new HashMap<String, State>();
    
    int currentState = -1;
    int newState = 1;
    int numWorms;
    long startTime, endTime;
    
    //Each index contains a int array that contains {headRow, headCol, tailRow, tailCol}
    ArrayList<int[]> wormPositions = new ArrayList<int[]>();
    
    
	public ASGS(char[][] passedBoard, int worms) throws IOException
	{
		board = passedBoard;
		numWorms = worms;
		String key = "";
		
	    //Setup inital state
	    State initialState = new State(0, board, currentState, 0, 0, 0, 0);
	    frontierStates.add(initialState);
	    
    	//generate key for initial state
	    key = buildKey(initialState);
    	exploredStates.put(key, initialState);
	      
	    //Finds the head and tail coordinates of all of the worms
	    getWormPosistions(frontierStates.get(0).board);
		
		
    	char[][] tempBoard = new char[board.length][board[0].length];
    	Boolean goalFound = false, stateChanged = false;
		State tempState;
    	
    	//Measure runtime of algorithm
		startTime = System.nanoTime();
    	
    	//State id's used for debugging purposes
		currentState++;
		newState = 1;
		stateChanged = false;
		
    	while(goalFound == false)
    	{	
    		if(stateChanged == true)
    			currentState++;
      		stateChanged = false;
    		
           	//pop frontier for eval
   			if(!frontierStates.isEmpty())
   			{
   				evalStates.add(0, frontierStates.remove(0));
   				key = buildKey(evalStates.get(0));
   		    	exploredStates.put(key, evalStates.get(0));
   			}
   			
            //Get worm position in the new state
    		getWormPosistions(evalStates.get(0).board);
    		
    		for(int h = 0; h < numWorms; h++)
    		{
	   			//above head is empty
	    		if(wormPositions.get(h)[0] > 0 && evalStates.get(0).board[wormPositions.get(h)[0] - 1][wormPositions.get(h)[1]] == 'e')
	    		{
	    			tempBoard = moveWorm(h, 1, 'U');
	   				tempState = new State(newState, tempBoard, evalStates.get(0), h, 0, wormPositions.get(h)[1], wormPositions.get(h)[0] - 1);
	   				newState++;
	    			stateChanged = true;
	    			heuristic(tempState, h, 0);
	    		}
	    		//below head is empty
	   			if(wormPositions.get(h)[0] < board.length - 1 && evalStates.get(0).board[wormPositions.get(h)[0] + 1][wormPositions.get(h)[1]] == 'e')
	    		{
	    			tempBoard = moveWorm(h, 1, 'D');
	    			tempState = new State(newState, tempBoard, evalStates.get(0), h, 0, wormPositions.get(h)[1], wormPositions.get(h)[0] + 1);
	   				newState++;
	   				stateChanged = true;
	   				heuristic(tempState, h, 1);
	    		}
	    		//left of head is empty
	    		if(wormPositions.get(h)[1] > 0 && evalStates.get(0).board[wormPositions.get(h)[0]][wormPositions.get(h)[1] - 1] == 'e')
	  			{
	    			tempBoard = moveWorm(h, 1, 'L');
	    			tempState = new State(newState, tempBoard, evalStates.get(0), h, 0, wormPositions.get(h)[1] - 1, wormPositions.get(h)[0]);
	    			newState++;
	    			stateChanged = true;
	   				heuristic(tempState, h, 0);
	   			}
	    		//right of head is empty
	    		if(wormPositions.get(h)[1] < board[0].length - 1 && evalStates.get(0).board[wormPositions.get(h)[0]][wormPositions.get(h)[1] + 1] == 'e')
	    		{
	   				tempBoard = moveWorm(h, 1, 'R');
	   				tempState = new State(newState, tempBoard, evalStates.get(0), h, 0, wormPositions.get(h)[1] + 1, wormPositions.get(h)[0]);
	    			newState++;
	    			stateChanged = true;
	   				heuristic(tempState, h, 1);
	    		}
	   			//above tail is empty
	    		if(wormPositions.get(h)[2] > 0 && evalStates.get(0).board[wormPositions.get(h)[2] - 1][wormPositions.get(h)[3]] == 'e')
	    		{
	    			tempBoard = moveWorm(h, 0, 'U');
	   				tempState = new State(newState, tempBoard, evalStates.get(0), h, 1, wormPositions.get(h)[3], wormPositions.get(h)[2] - 1);
	  				newState++;
	   				stateChanged = true;
	   				heuristic(tempState, h, 0);
	    		}
	    		//below tail is empty
	    		if(wormPositions.get(h)[2] < board.length - 1 && evalStates.get(0).board[wormPositions.get(h)[2] + 1][wormPositions.get(h)[3]] == 'e')
	   			{
	   				tempBoard = moveWorm(h, 0, 'D');
	    			tempState = new State(newState, tempBoard, evalStates.get(0), h, 1, wormPositions.get(h)[3], wormPositions.get(h)[2] + 1);
	    			newState++;
	    			stateChanged = true;
	   				heuristic(tempState, h, 1);
	   			}
	   			//left of tail is empty
	    		if(wormPositions.get(h)[3] > 0 && evalStates.get(0).board[wormPositions.get(h)[2]][wormPositions.get(h)[3] - 1] == 'e')
	    		{
	    			tempBoard = moveWorm(h, 0, 'L');
	   				tempState = new State(newState, tempBoard, evalStates.get(0), h, 1, wormPositions.get(h)[3] - 1, wormPositions.get(h)[2]);
	   				newState++;
	   				stateChanged = true;
	   				heuristic(tempState, h, 0);
	    		}
	    		//right of tail is empty
	    		if(wormPositions.get(h)[3] < board[0].length - 1 && evalStates.get(0).board[wormPositions.get(h)[2]][wormPositions.get(h)[3] + 1] == 'e')
	   			{
	   				tempBoard = moveWorm(h, 0, 'R');
	    			tempState = new State(newState, tempBoard, evalStates.get(0), h, 1, wormPositions.get(h)[3] + 1, wormPositions.get(h)[2]);
	    			newState++;
	    			stateChanged = true;
	   				heuristic(tempState, h, 1);
	    		}
	    		
	        	getWormPosistions(evalStates.get(0).board);
	        	
	           	//check eval for GS 
	           	if((wormPositions.get(0)[0] == board.length - 1 && wormPositions.get(0)[1] == board[0].length - 1) || (wormPositions.get(0)[2] == board.length - 1 && wormPositions.get(0)[3] == board[0].length - 1))
	           	{
	           		goalFound = true;
	            	endTime = System.nanoTime();
	            }  
    		}
    	} 	
    	
    	
		File output = new File("./output.txt");
    	BufferedWriter out = new BufferedWriter(new FileWriter(output));
    	
    	State currentState = new State(evalStates.get(0));
    	ArrayList<State> solutionPath = new ArrayList<State>();
    	solutionPath.add(currentState);
    	
    	//Find the solution path
    	while(currentState.parent.id != 0)
    	{
    		currentState = new State(currentState.parent);
    		solutionPath.add(0, currentState);
    	}
    	
    	
    	//Print the solution path to the output file
    	for(int i = 0; i < solutionPath.size(); i++)
    	{
    		out.write(String.valueOf(solutionPath.get(i).solutionStep[0]));
    		out.write(" ");
    		out.write(String.valueOf(solutionPath.get(i).solutionStep[1]));
    		out.write(" ");
    		out.write(String.valueOf(solutionPath.get(i).solutionStep[2]));
    		out.write(" ");
    		out.write(String.valueOf(solutionPath.get(i).solutionStep[3])); 
    		out.newLine();
    	}
    	
    	
    	board = evalStates.get(0).board;
        for(int i = 0; i < board.length; i++)
        {
        	for(int j = 0; j < board[0].length; j++)
        	{
        		out.write(board[i][j]);
        		out.write(" ");
        	}
        	out.newLine();
        }
    	
    	
    	//Time in milliseconds
    	out.write(String.valueOf((endTime - startTime) / 1000000));
		out.newLine();
		
    	//NUmber of steps in solution
    	out.write(String.valueOf(solutionPath.size()));
		out.newLine();
    	
    	out.close();
	}
	
	
	//Moves the passed in worms, passed in body part (1 for head, 0 for tail), in the passed in direction (U, D, L, R)
    public char[][] moveWorm(int wormNumber, int head, char direction)
    {
    	char[][] tempBoard = new char[board.length][board[0].length];
    	boolean moveMade = false;
    	
    	//Copy current board
    	for(int i = 0; i < board.length; i++)
        {
             for(int j = 0; j < board[0].length; j++)
            	 tempBoard[i][j] = evalStates.get(0).board[i][j];
        }
    	 
    	
    	//Makes the changes at the tail then uses the follow to head methods to make the changes there 
    	if(head == 0)
    	{
    		for(int i = 0; i < board.length; i++)
    		{
    			for(int j = 0; j < board[0].length; j++)
    			{
    				if(moveMade == false)
    				{
    					if(tempBoard[i][j] == (char) (wormNumber + 48) && direction == 'U')
    					{
    						tempBoard[i - 1][j] = (char) (wormNumber + 48);
    						tempBoard[i][j] = '^';
    						tempBoard = followBodyToTail(i, j, tempBoard, direction);
    						moveMade = true;
    					}
    					if(tempBoard[i][j] == (char) (wormNumber + 48) && direction == 'D')
    					{
    						tempBoard[i + 1][j] = (char) (wormNumber + 48);
    						tempBoard[i][j] = 'v';
    						tempBoard = followBodyToTail(i, j, tempBoard, direction);
    						moveMade = true;
    					}
    					if(tempBoard[i][j] == (char) (wormNumber + 48) && direction == 'L')
    					{
    						tempBoard[i][j - 1] = (char) (wormNumber + 48);
    						tempBoard[i][j] = '<';
    						tempBoard = followBodyToTail(i, j, tempBoard, direction);
    						moveMade = true;
    					}
    					if(tempBoard[i][j] == (char) (wormNumber + 48) && direction == 'R')
    					{
    						tempBoard[i][j + 1] = (char) (wormNumber + 48);
    						tempBoard[i][j] = '>';
    						tempBoard = followBodyToTail(i, j, tempBoard, direction);
    						moveMade = true;
    					}
    				}
    			}
    		}
    	}
    	else //Move was made by head
    	{
    		for(int i = 0; i < board.length; i++)
    		{
    			for(int j = 0; j < board[0].length; j++)
    			{
    				if(tempBoard[i][j] == (char) (wormNumber + 48) && moveMade == false)
    				{
    					tempBoard[i][j] = 'e';
    					tempBoard = findAndMoveHead(i, j, tempBoard, wormNumber, direction);
    					moveMade = true;
    				}
    			}
    		}
    	}  	
    	return tempBoard;
    }
    
    
    //This function is called when the move originates in the head and handles the shifting of the worm,
    //by following the arrows from the tail of the worm that is currently moving.
    public char[][] findAndMoveHead(int row, int col, char[][] tempBoard, int wormNumber, char direction)
    {
		Boolean firstLink = true, finished = false;
    	
    	while(finished == false)
    	{
    		//Follows the arrows to the tail and moves it, stops when is finds a letter indicating a tail
    		if(row > 0 && tempBoard[row - 1][col] == 'v')
    		{		
    			row--;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[row][col + 1] == '<')
    		{
    			col++;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[row + 1][col] == '^')
    		{
    			row++;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}		
    		}
    		else if(col > 0 && tempBoard[row][col - 1] == '>')
    		{
    			col--;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    		}
    		else if(row > 0 && tempBoard[row - 1][col] == 'D')
    		{
    			row--;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    			else
    				tempBoard[row][col] = 'v';
    			finished = true;
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[row][col + 1] == 'L')
    		{
    			col++;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    			else
    				tempBoard[row][col] = '<';
    			finished = true;
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[row + 1][col] == 'U')
    		{
    			row++;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    			else
    				tempBoard[row][col] = '^';
    			finished = true;
    		}
    		else if(col > 0 && tempBoard[row][col - 1] == 'R')
    		{
    			col--;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    			else
    				tempBoard[row][col] = '>';
    			finished = true;
    		}
    	}
    	
    	//Makes the final change at the tail
    	if(direction == 'D')
    		tempBoard[row + 1][col] = 'U';
    	else if(direction == 'U')
    		tempBoard[row - 1][col] = 'D';
    	else if(direction == 'L')
    		tempBoard[row][col - 1] = 'R';
    	else
    		tempBoard[row][col + 1] = 'L';
    	
    	return tempBoard;
    	
    }
    
    
    //This function is called when the move originates from the tail of the worm it performs the
    //changes to move the tail forward. The head is found using the passed in tail coordinates.
    public char[][] followBodyToTail(int row, int col, char[][] tempBoard, char direction)
    {
    	//If capitol E is ever inserted then ERROR
    	char directionToLastMove = 'E';
		boolean finished = false;
		boolean firstMove = true;
    	
    	while(finished == false)
    	{
    		//Follows the arrows to the tail and moves it
    		if(row > 0 && tempBoard[row - 1][col] == 'v')
    		{
    			row--;
    			directionToLastMove = 'D';
    			firstMove = false;
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[row][col + 1] == '<')
    		{
    			col++;
    			directionToLastMove = 'L';
    			firstMove = false;
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[row + 1][col] == '^')
    		{
    			row++;
    			directionToLastMove = 'U';    
    			firstMove = false;
    		}
    		else if(col > 0 && tempBoard[row][col - 1] == '>')
    		{
    			col--;
    			directionToLastMove = 'R';
    			firstMove = false;
    		}
    		else if(row > 0 && tempBoard[row - 1][col] == 'D')
    		{
    			if(firstMove == true)
    				tempBoard[row][col] = direction;
    			else
    				tempBoard[row][col] = directionToLastMove;
    			row--;
    			tempBoard[row][col] = 'e';
    			finished = true;
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[row][col + 1] == 'L')
    		{
    			if(firstMove == true)
    				tempBoard[row][col] = direction;
    			else
    				tempBoard[row][col] = directionToLastMove;
    			col++;
    			tempBoard[row][col] = 'e';
    			finished = true;
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[row + 1][col] == 'U')
    		{
    			if(firstMove == true)
    				tempBoard[row][col] = direction;
    			else
    				tempBoard[row][col] = directionToLastMove;
    			row++;
    			tempBoard[row][col] = 'e';
    			finished = true;
    		}
    		else if(col > 0 && tempBoard[row][col - 1] == 'R')
    		{
    			if(firstMove == true)
    				tempBoard[row][col] = direction;
    			else
    				tempBoard[row][col] = directionToLastMove;
    			col--;
    			tempBoard[row][col] = 'e';
    			finished = true;
    		}   		
    	}  	
    	return tempBoard;
    }
    

    //Finds the position each of the worms and saves it coordinates in the wormPositions list
    public void getWormPosistions(char[][] currentBoard)
    {
        int[] tempWorm = new int[4];
        int[] coords = new int[2];
        
        wormPositions.clear();
        
        for(int h = 0; h < numWorms; h++)
        {
        	for(int i = 0; i < currentBoard.length; i++)
        	{
            	for(int j = 0; j < currentBoard[0].length; j++)
            	{	
      	      		if(currentBoard[i][j] == (char) (h + 48))
      	      		{
      	                tempWorm = new int[4];
      	      			tempWorm[2] = i;
      	      			tempWorm[3] = j;
      	      			coords = getHeadCoords(i, j, currentBoard);
      	      			tempWorm[0] = coords[0];
      	      			tempWorm[1] = coords[1];
      	                wormPositions.add(tempWorm);
      	      		}
            	}
        	}
        }
    }
    
    
    //Finds the head of the worm given the passed in tail coordinates
    public int[] getHeadCoords(int row, int col, char[][] tempBoard)
    {
    	int[] coords = new int[2];
    	
		Boolean finished = false;
    	
    	while(finished == false)
    	{
    		//Follows the arrows to the head, stops when is finds a letter indicating a tail
    		if(row > 0 && tempBoard[row - 1][col] == 'v')	
    			row--;
    		else if(col < tempBoard[0].length - 1 && tempBoard[row][col + 1] == '<')
    			col++;
    		else if(row < tempBoard.length - 1 && tempBoard[row + 1][col] == '^')
    			row++;
    		else if(col > 0 && tempBoard[row][col - 1] == '>')
    			col--;
    		else if(row > 0 && tempBoard[row - 1][col] == 'D')
    		{
    			row--;
    			finished = true;
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[row][col + 1] == 'L')
    		{
    			col++;
    			finished = true;
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[row + 1][col] == 'U')
    		{
    			row++;
    			finished = true;
    		}
    		else if(col > 0 && tempBoard[row][col - 1] == 'R')
    		{
    			col--;
    			finished = true;
    		}
    	}	
    	
    	coords[0] = row;
    	coords[1] = col;
    	
    	return coords;
    }
    
    
    
    //This function will check if the passed in state has been explored.  If it has not 
    //it will be added into the frontier ordered by the heuristic favoring the moves that bring worm 0 closer to the goal 
    //measured by manhattan distance.  For states that move a non blue worm, the priority considers whether or not that move is 
    //towards or away from the goal.  towardGoal is 1 when true. 
    public void heuristic(State tempState, int wormMoved, int towardGoal) 
    {	
    	int distanceTraveled, distance;
    	//Saves the entire board state concatenated into one string as the key for the hashmap
    	String key = "";
    	key = buildKey(tempState);
    	
    	//Finds the number of parent states the current state has
    	distanceTraveled = distanceTraveled(tempState);
    	
    	if(wormMoved == 0)
    	{
    		tempState.setOrder(0);
        	distance = manhattanDistance(tempState);
        	tempState.setHeuristicDistance(distance + distanceTraveled);
    	}
    	if(wormMoved > 0  && towardGoal == 0)
    	{
    		tempState.setOrder(1);
    		tempState.setHeuristicDistance(-1);
    	}
    	if(wormMoved > 0 && towardGoal == 1)
    	{
    		tempState.setOrder(2);
    		tempState.setHeuristicDistance(-1);
    	}
    	
    
    	//Only add to the frontier if the state has never been evaluated
    	if(!exploredStates.containsKey(key))
    	{
    		//Adds the back if the frontier is empty
	    	if(frontierStates.isEmpty())
	    		frontierStates.add(tempState);
	    	
	    	//Adds to the back if moving a non-goal worm towards the goal
	    	else if(tempState.heuristicOrder == 2)
	    		frontierStates.add(tempState);    	
	    	
	    	//Handles cases when the frontier has only one state
	    	else if(frontierStates.size() == 1)
	    	{
	    		//Moving non-goal worm and compares by moving toward or away from goal
	    		if(frontierStates.get(0).heuristicOrder > 0)
	    		{
		    		if(frontierStates.get(0).heuristicOrder < tempState.heuristicOrder || frontierStates.get(0).heuristicOrder == tempState.heuristicOrder)
			    		frontierStates.add(tempState);
		    		else
		    			frontierStates.add(0, tempState);
	    		}
	    		//Moving goal worm and compares by distance to goal
	    		else
	    		{
	    			if(frontierStates.get(0).heuristicDistance < tempState.heuristicDistance)
	    				frontierStates.add(tempState);
	    			else
	    				frontierStates.add(0, tempState);
	    		}
	    	}
	    	//Increments through the frontier states until the proper position to be inserted is found based on the heuristic
	    	else 
	    	{
	    		int index = 0;
	    		boolean inserted = false;
	    		
	    		//Can bypass search if frontier only contains states of a higher heuristic order (lower priority)
	    		if(frontierStates.get(index).heuristicOrder > tempState.heuristicOrder)
	    		{
	    			frontierStates.add(0, tempState);
	    			inserted = true;
	    		}
	    		//Can bypass search if end of frontier has a state of heuristic order 1 and a state of order 1 is being entered
	    		if(frontierStates.get(frontierStates.size() - 1).heuristicOrder == 1 && tempState.heuristicOrder == 1)
	    		{
	    			frontierStates.add(tempState);
	    			inserted = true;
	    		}
	    		while(index < frontierStates.size() - 1 && inserted == false)
	    		{
		    		//Moving non-goal worm and compares by moving toward or away from goal
	    			if(frontierStates.get(0).heuristicOrder != 0)
		    		{
		    			if(frontierStates.get(index).heuristicOrder <= tempState.heuristicOrder && frontierStates.get(index + 1).heuristicOrder > tempState.heuristicOrder)
		    			{
		    				frontierStates.add(index + 1, tempState);
		    				inserted = true;
		    			}
		    			//If end of frontier is reached and the state ahs not been entered then insert it at the end
		    			if(index == frontierStates.size() - 2 && inserted == false)
		    			{
		    				frontierStates.add(tempState);
		    				inserted = true;
		    			}
		    		}
		    		//Moving goal worm and compares by distance to goal
	    			else
		    		{
		    			if(frontierStates.get(index).heuristicDistance < tempState.heuristicDistance && frontierStates.get(index + 1).heuristicDistance > tempState.heuristicDistance)
		    			{
		    				frontierStates.add(index + 1, tempState);
		    				inserted = true;
		    			}
		    			//If end of frontier is reached and the state ahs not been entered then insert it at the end
		    			if(index == frontierStates.size() - 2 && inserted == false)
		    			{
		    				frontierStates.add(tempState);
		    				inserted = true;
		    			}
		    		}
	    			index++;
	    		}	    		
	    		//Error printed to console of the correct position is never found
	    		if(inserted == false)
	    			System.out.println("ERROR INSERTION FAILED");	
	    	}
    	}
    }
    
    
    //Finds the minimum distance to the goal of the head and the tail
    public int manhattanDistance(State state)
    {
    	int headDistance = 0, tailDistance = 0;
    	
    	//Each index contains a int array that contains {headRow, headCol, tailRow, tailCol}
        int[] tempWorm = new int[4];
        int[] coords = new int[2];      

        for(int i = 0; i < state.board.length; i++)
        {
           	for(int j = 0; j < state.board[0].length; j++)
           	{	
     	      	if(state.board[i][j] == (char) (48))
     	      	{
     	      		tempWorm = new int[4];
    	    		tempWorm[2] = i;
     	     		tempWorm[3] = j;
      	    		coords = getHeadCoords(i, j, state.board);
      	    		tempWorm[0] = coords[0];
      	     		tempWorm[1] = coords[1];
      	     	}
            }
        }    
        
        headDistance = (state.board.length - 1 - tempWorm[0]) + (state.board[0].length - 1 - tempWorm[1]);
    	tailDistance = (state.board.length - 1 - tempWorm[2]) + (state.board[0].length - 1 - tempWorm[3]);
    	
    	if(headDistance < tailDistance)
    		return headDistance;
    	else
    		return tailDistance;
    }
    
    
    public int distanceTraveled(State state)
    {
    	int distance = 0;
    	
    	State currentState = new State(state);

    	//Find the solution path
    	while(currentState.parent.id != 0)
    	{
    		currentState = new State(currentState.parent);
    		distance++;
    	}
    	
    	return distance;
    }
    
    
    //entire board state concatenated into one string as the key for the hashmap
    public String buildKey(State state)
    {
    	String key = "";
    	
    	//generate key for current state
    	for(int i = 0; i < board.length; i++)
    	{
    		String addition = new String(state.board[i]); 
    		key += addition;  
    	}
    	
    	return key;
    }
    
}
	
