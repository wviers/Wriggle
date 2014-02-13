import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



//Iterative Deepening Depth First Tree Search
//I add the heads states to the Frontier queue first, starting with the state above the head
//and then moving clockwise.  I then do the same for the tail.  States are popped off of the 
//frontier queue onto the evalQueue which in turn adds new states to the frontier.  This continues
//until the state on the evalqueue is the goal state.
public class GBFGS
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
    
    

    //Greedy Best First Graph Search
    //I add the heads states to the Frontier queue first, adding states for each of the worms starting  
    //with 0.  States will be ordered in the frontier using the heuristic where states that move
    //the blue worm closer to the goal are preferred and states that move non blue worms away from
    //the goal are preferred.  States are popped off of the frontier queue onto the evalQueue which 
    //in turn adds new states to the frontier.  Each time a state is added to the eval queue it is also
    //added to a hashmap representing the explored set.  This continues until the state on the eval queue 
    //is the goal state.
	public GBFGS(char[][] passedBoard, int worms) throws IOException
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
    	
    	startTime = System.nanoTime();
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
	    			greedyHeuristic(tempState, h, 0);
	    		}
	    		//below head is empty
	   			if(wormPositions.get(h)[0] < board[0].length - 1 && evalStates.get(0).board[wormPositions.get(h)[0] + 1][wormPositions.get(h)[1]] == 'e')
	    		{
	    			tempBoard = moveWorm(h, 1, 'D');
	    			tempState = new State(newState, tempBoard, evalStates.get(0), h, 0, wormPositions.get(h)[1], wormPositions.get(h)[0] + 1);
	   				newState++;
	   				stateChanged = true;
	   				greedyHeuristic(tempState, h, 1);
	    		}
	    		//left of head is empty
	    		if(wormPositions.get(h)[1] > 0 && evalStates.get(0).board[wormPositions.get(h)[0]][wormPositions.get(h)[1] - 1] == 'e')
	  			{
	    			tempBoard = moveWorm(h, 1, 'L');
	    			tempState = new State(newState, tempBoard, evalStates.get(0), h, 0, wormPositions.get(h)[1] - 1, wormPositions.get(h)[0]);
	    			newState++;
	    			stateChanged = true;
	   				greedyHeuristic(tempState, h, 0);
	   			}
	    		//right of head is empty
	    		if(wormPositions.get(h)[1] < board.length - 1 && evalStates.get(0).board[wormPositions.get(h)[0]][wormPositions.get(h)[1] + 1] == 'e')
	    		{
	   				tempBoard = moveWorm(h, 1, 'R');
	   				tempState = new State(newState, tempBoard, evalStates.get(0), h, 0, wormPositions.get(h)[1] + 1, wormPositions.get(h)[0]);
	    			newState++;
	    			stateChanged = true;
	   				greedyHeuristic(tempState, h, 1);
	    		}
	   			//above tail is empty
	    		if(wormPositions.get(h)[2] > 0 && evalStates.get(0).board[wormPositions.get(h)[2] - 1][wormPositions.get(h)[3]] == 'e')
	    		{
	    			tempBoard = moveWorm(h, 0, 'U');
	   				tempState = new State(newState, tempBoard, evalStates.get(0), h, 1, wormPositions.get(h)[3], wormPositions.get(h)[2] - 1);
	  				newState++;
	   				stateChanged = true;
	   				greedyHeuristic(tempState, h, 0);
	    		}
	    		//below tail is empty
	    		if(wormPositions.get(h)[2] < board[0].length - 1 && evalStates.get(0).board[wormPositions.get(h)[2] + 1][wormPositions.get(h)[3]] == 'e')
	   			{
	   				tempBoard = moveWorm(h, 0, 'D');
	    			tempState = new State(newState, tempBoard, evalStates.get(0), h, 1, wormPositions.get(h)[3], wormPositions.get(h)[2] + 1);
	    			newState++;
	    			stateChanged = true;
	   				greedyHeuristic(tempState, h, 1);
	   			}
	   			//left of tail is empty
	    		if(wormPositions.get(h)[3] > 0 && evalStates.get(0).board[wormPositions.get(h)[2]][wormPositions.get(h)[3] - 1] == 'e')
	    		{
	    			tempBoard = moveWorm(h, 0, 'L');
	   				tempState = new State(newState, tempBoard, evalStates.get(0), h, 1, wormPositions.get(h)[3] - 1, wormPositions.get(h)[2]);
	   				newState++;
	   				stateChanged = true;
	   				greedyHeuristic(tempState, h, 0);
	    		}
	    		//right of tail is empty
	    		if(wormPositions.get(h)[3] < board.length - 1 && evalStates.get(0).board[wormPositions.get(h)[2]][wormPositions.get(h)[3] + 1] == 'e')
	   			{
	   				tempBoard = moveWorm(h, 0, 'R');
	    			tempState = new State(newState, tempBoard, evalStates.get(0), h, 1, wormPositions.get(h)[3] + 1, wormPositions.get(h)[2]);
	    			newState++;
	    			stateChanged = true;
	   				greedyHeuristic(tempState, h, 1);
	    		}
	    		
	        	getWormPosistions(evalStates.get(0).board);
	        	
	           	//check eval for GS 
	           	if((wormPositions.get(0)[0] == board.length - 1 && wormPositions.get(0)[1] == board.length - 1) || (wormPositions.get(0)[2] == board.length - 1 && wormPositions.get(0)[3] == board.length - 1))
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
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == '<')
    		{
    			col++;
    			if(firstLink)
    			{
    				tempBoard[row][col] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    		}
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == '^')
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
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == 'L')
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
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == 'U')
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
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == '<')
    		{
    			col++;
    			directionToLastMove = 'L';
    			firstMove = false;
    		}
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == '^')
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
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == 'L')
    		{
    			if(firstMove == true)
    				tempBoard[row][col] = direction;
    			else
    				tempBoard[row][col] = directionToLastMove;
    			col++;
    			tempBoard[row][col] = 'e';
    			finished = true;
    		}
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == 'U')
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
    

    //Finds the position of the first worm and saves it coordinates in the wormPositions list
    public void getWormPosistions(char[][] currentBoard)
    {
        int[] tempWorm = new int[4];
        int[] coords = new int[2];
        
        wormPositions.clear();
        
        for(int h = 0; h < numWorms; h++)
        {
        	for(int i = 0; i < currentBoard.length; i++)
        	{
            	for(int j = 0; j < currentBoard.length; j++)
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
    
    
    public int[] getHeadCoords(int row, int col, char[][] tempBoard)
    {
    	int[] coords = new int[2];
    	
		Boolean finished = false;
    	
    	while(finished == false)
    	{
    		//Follows the arrows to the head, stops when is finds a letter indicating a tail
    		if(row > 0 && tempBoard[row - 1][col] == 'v')	
    			row--;
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == '<')
    			col++;
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == '^')
    			row++;
    		else if(col > 0 && tempBoard[row][col - 1] == '>')
    			col--;
    		else if(row > 0 && tempBoard[row - 1][col] == 'D')
    		{
    			row--;
    			finished = true;
    		}
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == 'L')
    		{
    			col++;
    			finished = true;
    		}
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == 'U')
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
    //it will be added into the frontier ordered by the heuristic favoring moving worm zero towards the 
    //bottom left and moving all others worms towards the top right.  
    public void greedyHeuristic(State tempState, int wormMoved, int towardGoal) 
    {	
    	String key = "";
    	
    	key = buildKey(tempState);
    	
    	if(wormMoved == 0 && towardGoal == 1)
    		tempState.setOrder(0);
    	if(wormMoved == 0 && towardGoal == 0)
    		tempState.setOrder(1);
    	if(wormMoved > 0 && towardGoal == 1)
    		tempState.setOrder(1);
    	if(wormMoved > 0 && towardGoal == 0)
    		tempState.setOrder(2);
    
    	if(!exploredStates.containsKey(key))
    	{
	    	if(frontierStates.isEmpty())
	    		frontierStates.add(tempState);
	    	else if(tempState.heuristicOrder == 2)
	    		frontierStates.add(tempState);    	
	    	else if(frontierStates.size() == 1)
	    	{
	    		if(frontierStates.get(0).heuristicOrder < tempState.heuristicOrder || frontierStates.get(0).heuristicOrder == tempState.heuristicOrder)
		    		frontierStates.add(tempState);
	    		else
	    			frontierStates.add(0, tempState);
	    	}
	    	else 
	    	{
	    		int index = 0;
	    		boolean inserted = false;
	    		
	    		if(frontierStates.get(index).heuristicOrder > tempState.heuristicOrder)
	    		{
	    			frontierStates.add(0, tempState);
	    			inserted = true;
	    		}
	    		while(index < frontierStates.size() - 1 && inserted == false)
	    		{
	    			if((frontierStates.get(index).heuristicOrder == tempState.heuristicOrder || frontierStates.get(index).heuristicOrder < tempState.heuristicOrder) && frontierStates.get(index + 1).heuristicOrder > tempState.heuristicOrder)
	    			{
	    				frontierStates.add(index + 1, tempState);
	    				inserted = true;
	    			}
	    			index++;
	    		}
	    		if(frontierStates.get(frontierStates.size() - 1).heuristicOrder == tempState.heuristicOrder)
	    		{
	    			frontierStates.add(0, tempState);
	    			inserted = true;
	    		}
	    		if(inserted == false)
	    			System.out.println("ERROR INSERTION FAILED");	
	    	}
    	}
    }
    
    
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
	
