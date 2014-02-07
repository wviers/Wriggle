import java.util.ArrayList;
import java.util.Random;



//Iterative Deepening Depth First Tree Search
//I add the heads states to the Frontier queue first, starting with the state above the head
//and then moving clockwise.  I then do the same for the tail.  States are popped off of the 
//frontier queue onto the evalQueue which in turn adds new states to the frontier.  This continues
//until the state on the evalqueue is the goal state.
public class IDDFTS
{
    char[][] board;
    
    //The states to be evaluated
    ArrayList<State> frontierStates = new ArrayList<State>();
    
    //The first state is the current state being evaluated
    ArrayList<State> evalStates = new ArrayList<State>();
    
    int currentState = -1;
    int newState = 1;
    int numWorms;
    long startTime, endTime;
    
    //Each index contains a int array that contains {headRow, headCol, tailRow, tailCol}
    ArrayList<int[]> wormPositions = new ArrayList<int[]>();
    

    //Iterative-Deepening Depth First Tree Search 
    //I add the heads states to the Frontier queue first, starting with the state above the head
    //and then moving clockwise.  I then do the same for the tail, these moves are done randomly on one.  
    //of the worms.  States are popped off of the frontier queue onto the evalQueue which in turn adds 
    //new states to the frontier.  This continues until the state on the eval queue is the goal state.
    //The depth starts at 0 and increases by by 1 each time the goal state is not found and the depth 
    //state is reached.
	public IDDFTS(char[][] passedBoard, int worms)
	{
		board = passedBoard;
		numWorms = worms;
		Random gen = new Random();
		
		//Number of states from the initial state the algorithm will evaluate
		int depth = -1, randWorm;
	      
	    //Setup inital state
	    State initialState = new State(0, board, currentState, 0, 0, 0, 0);
	    frontierStates.add(initialState);
	      
	    //Finds the head and tail coordinates of all of the worms
	    getWormPosistions(frontierStates.get(0).board);
		
		
    	char[][] tempBoard = new char[board.length][board[0].length];
    	Boolean goalFound = false, stateChanged = false;
		State tempState;
    	
    	while(goalFound == false)
    	{
        	startTime = System.nanoTime();
    		evalStates.clear();
    		frontierStates.clear();
    		currentState = 0;
    		newState = 1;
    		stateChanged = false;
    		depth++;
    	    frontierStates.add(initialState);
        	
        	
    		for(int i = 0; i < depth && goalFound == false; i++)
    		{
    			if(stateChanged == true)
    				currentState++;
      			stateChanged = false;
    			randWorm = gen.nextInt(numWorms);
    			
            	//pop frontier for eval
    			if(!frontierStates.isEmpty())
    				evalStates.add(0, frontierStates.remove(0));
            	//Get worm position in the new state
    			getWormPosistions(evalStates.get(0).board);
    
    			//above head is empty
    			if(wormPositions.get(randWorm)[0] > 0 && evalStates.get(0).board[wormPositions.get(randWorm)[0] - 1][wormPositions.get(randWorm)[1]] == 'e')
    			{
    				tempBoard = moveWorm(randWorm, 1, 'U');
    				tempState = new State(newState, tempBoard, currentState, randWorm, 0, wormPositions.get(randWorm)[1], wormPositions.get(randWorm)[0] - 1);
    				newState++;
    				stateChanged = true;
    				frontierStates.add(tempState);
    			}
    			//below head is empty
    			if(wormPositions.get(randWorm)[0] < board[0].length - 1 && evalStates.get(0).board[wormPositions.get(randWorm)[0] + 1][wormPositions.get(randWorm)[1]] == 'e')
    			{
    				tempBoard = moveWorm(randWorm, 1, 'D');
    				tempState = new State(newState, tempBoard, currentState, randWorm, 0, wormPositions.get(randWorm)[1], wormPositions.get(randWorm)[0] + 1);
    				newState++;
    				frontierStates.add(tempState);
    			}
    			//left of head is empty
    			if(wormPositions.get(randWorm)[1] > 0 && evalStates.get(0).board[wormPositions.get(randWorm)[0]][wormPositions.get(randWorm)[1] - 1] == 'e')
    			{
    				tempBoard = moveWorm(randWorm, 1, 'L');
    				tempState = new State(newState, tempBoard, currentState, randWorm, 0, wormPositions.get(randWorm)[1] - 1, wormPositions.get(randWorm)[0]);
    				newState++;
    				stateChanged = true;
    				frontierStates.add(tempState);
    			}
    			//right of head is empty
    			if(wormPositions.get(randWorm)[1] < board.length - 1 && evalStates.get(0).board[wormPositions.get(randWorm)[0]][wormPositions.get(randWorm)[1] + 1] == 'e')
    			{
    				tempBoard = moveWorm(randWorm, 1, 'R');
    				tempState = new State(newState, tempBoard, currentState, randWorm, 0, wormPositions.get(randWorm)[1] + 1, wormPositions.get(randWorm)[0]);
    				newState++;
    				stateChanged = true;
    				frontierStates.add(tempState);
    			}
    			//above tail is empty
    			if(wormPositions.get(randWorm)[2] > 0 && evalStates.get(0).board[wormPositions.get(randWorm)[2] - 1][wormPositions.get(randWorm)[3]] == 'e')
    			{
    				tempBoard = moveWorm(randWorm, 0, 'U');
    				tempState = new State(newState, tempBoard, currentState, randWorm, 1, wormPositions.get(randWorm)[3], wormPositions.get(randWorm)[2] - 1);
    				newState++;
    				stateChanged = true;
    				frontierStates.add(tempState);
    			}
    			//below tail is empty
    			if(wormPositions.get(randWorm)[2] < board[0].length - 1 && evalStates.get(0).board[wormPositions.get(randWorm)[2] + 1][wormPositions.get(randWorm)[3]] == 'e')
    			{
    				tempBoard = moveWorm(randWorm, 0, 'D');
    				tempState = new State(newState, tempBoard, currentState, randWorm, 1, wormPositions.get(randWorm)[3], wormPositions.get(randWorm)[2] + 1);
    				newState++;
    				stateChanged = true;
    				frontierStates.add(tempState);
    			}
    			//left of tail is empty
    			if(wormPositions.get(randWorm)[3] > 0 && evalStates.get(0).board[wormPositions.get(randWorm)[2]][wormPositions.get(randWorm)[3] - 1] == 'e')
    			{
    				tempBoard = moveWorm(randWorm, 0, 'L');
    				tempState = new State(newState, tempBoard, currentState, randWorm, 1, wormPositions.get(randWorm)[3] - 1, wormPositions.get(randWorm)[2]);
    				newState++;
    				stateChanged = true;
    				frontierStates.add(tempState);
    			}
    			//right of tail is empty
    			if(wormPositions.get(randWorm)[3] < board.length - 1 && evalStates.get(0).board[wormPositions.get(randWorm)[2]][wormPositions.get(randWorm)[3] + 1] == 'e')
    			{
    				tempBoard = moveWorm(randWorm, 0, 'R');
    				tempState = new State(newState, tempBoard, currentState, randWorm, 1, wormPositions.get(randWorm)[3] + 1, wormPositions.get(randWorm)[2]);
    				newState++;
    				stateChanged = true;
    				frontierStates.add(tempState);
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
    	
    	boolean foundInitial = false;
    	int nextID;
    	ArrayList<State> printList = new ArrayList<State>();
    	printList.add(0, evalStates.get(0));
    	nextID = evalStates.get(0).parentID;
    	
    	//This loop traverses the printList by finding each steps parent state
    	while(foundInitial == false)
    	{
    		for(int i = 0; i < evalStates.size(); i++)
    		{
    			if(evalStates.get(i).id == nextID)
    			{
    				nextID = evalStates.get(i).parentID;
    				printList.add(0, evalStates.get(i));
    			}		
    		}
    		if(nextID == -1)
    			foundInitial = true;
    	}
    
    	
    	//Initial state is not a move
    	printList.remove(0);
    	//Print the steps in the solution
    	for(int i = 0; i < printList.size(); i++)
    	{
    		System.out.print(printList.get(i).solutionStep[0]);
    		System.out.print(printList.get(i).solutionStep[1]);
    		System.out.print(printList.get(i).solutionStep[2]);
    		System.out.print(printList.get(i).solutionStep[3]); 
    		System.out.println();
    	}
    	
    	//Print final board state
    	board = evalStates.get(0).board;
    	printBoard();
    	
    	//Time in milliseconds
    	System.out.println((endTime - startTime) / 1000000);
    	
    	//NUmber of steps in solution
    	System.out.println(printList.size());
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
    						tempBoard = followBodyToTail(i, j, tempBoard);
    						moveMade = true;
    					}
    					if(tempBoard[i][j] == (char) (wormNumber + 48) && direction == 'D')
    					{
    						tempBoard[i + 1][j] = (char) (wormNumber + 48);
    						tempBoard[i][j] = 'v';
    						tempBoard = followBodyToTail(i, j, tempBoard);
    						moveMade = true;
    					}
    					if(tempBoard[i][j] == (char) (wormNumber + 48) && direction == 'L')
    					{
    						tempBoard[i][j - 1] = (char) (wormNumber + 48);
    						tempBoard[i][j] = '<';
    						tempBoard = followBodyToTail(i, j, tempBoard);
    						moveMade = true;
    					}
    					if(tempBoard[i][j] == (char) (wormNumber + 48) && direction == 'R')
    					{
    						tempBoard[i][j + 1] = (char) (wormNumber + 48);
    						tempBoard[i][j] = '>';
    						tempBoard = followBodyToTail(i, j, tempBoard);
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
    			tempBoard[row][col] = 'v';
    			finished = true;
    		}
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == 'L')
    		{
    			col++;
    			tempBoard[row][col] = '<';
    			finished = true;
    		}
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == 'U')
    		{
    			row++;
    			tempBoard[row][col] = '^';
    			finished = true;
    		}
    		else if(col > 0 && tempBoard[row][col - 1] == 'R')
    		{
    			col--;
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
    public char[][] followBodyToTail(int row, int col, char[][] tempBoard)
    {
    	//If capitol E is ever inserted then ERROR
    	char directionToLastMove = 'E';
		boolean finished = false;
    	
    	while(finished == false)
    	{
    		//Follows the arrows to the tail and moves it
    		if(row > 0 && tempBoard[row - 1][col] == 'v')
    		{
    			row--;
    			directionToLastMove = 'D';
    		}
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == '<')
    		{
    			col++;
    			directionToLastMove = 'L';
    		}
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == '^')
    		{
    			row++;
    			directionToLastMove = 'U';    		
    		}
    		else if(col > 0 && tempBoard[row][col - 1] == '>')
    		{
    			col--;
    			directionToLastMove = 'R';
    		}
    		else if(row > 0 && tempBoard[row - 1][col] == 'D')
    		{
    			tempBoard[row][col] = directionToLastMove;
    			row--;
    			tempBoard[row][col] = 'e';
    			finished = true;
    		}
    		else if(col < tempBoard.length - 1 && tempBoard[row][col + 1] == 'L')
    		{
    			tempBoard[row][col] = directionToLastMove;
    			col++;
    			tempBoard[row][col] = 'e';
    			finished = true;
    		}
    		else if(row < tempBoard[0].length - 1 && tempBoard[row + 1][col] == 'U')
    		{
    			tempBoard[row][col] = directionToLastMove;
    			row++;
    			tempBoard[row][col] = 'e';
    			finished = true;
    		}
    		else if(col > 0 && tempBoard[row][col - 1] == 'R')
    		{
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

    
	public void printBoard() 
	{
        for(int i = 0; i < board.length; i++)
        {
        	for(int j = 0; j < board[0].length; j++)
        	{
        		System.out.print(board[i][j]);
        		System.out.print(" ");
        	}
        		System.out.println();
        }
	}
}
	
