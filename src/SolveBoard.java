import java.util.ArrayList;

public class SolveBoard
{
    char[][] board;
    ArrayList<State> frontierStates = new ArrayList<State>();
    ArrayList<State> evalStates = new ArrayList<State>();
    int currentState = 0;
    int newState = 1;
    
    //Each index contains a int array that contains {headRow, headCol, tailRow, tailCol}
    ArrayList<int[]> wormPositions = new ArrayList<int[]>();
  
    public SolveBoard(char[][] gameBoard, int numWorms)
    {
      board = gameBoard;
      
      //Setup inital state
      State initialState = new State(currentState, board, currentState, 0, 0, 0, 0);
      frontierStates.add(initialState);
      
      getWormPosistions(frontierStates.get(0).board);
    
      BFTS();
    }
	
    
    //Breadth First Tree Search 
    //I add the heads states to the queue first, starting with the state above the head
    //and then moving clockwise.  I then do the same for the tail.
    public void BFTS()
    {
    	char[][] tempBoard = new char[board.length][board[0].length];
    	Boolean goalFound = false;
		State tempState;
    	
    	while(goalFound == false)
    	{
			currentState++;
			
        	//pop frontier for eval
        	evalStates.add(0, frontierStates.remove(0));
			getWormPosistions(evalStates.get(0).board);
        	
        	
    		for(int i = 0; i < wormPositions.size(); i++)
    		{
    			//above head is empty
    			if(wormPositions.get(i)[0] > 0 && evalStates.get(0).board[wormPositions.get(i)[0] - 1][wormPositions.get(i)[1]] == 'e')
    			{
    				tempBoard = moveWorm(i, 1, 'U');
    				tempState = new State(newState, tempBoard, currentState, i, 0, wormPositions.get(i)[1], wormPositions.get(i)[0] - 1);
    				newState++;
    				frontierStates.add(tempState);
    			}
    			//below head is empty
    			if(wormPositions.get(i)[0] < board[0].length - 1 && evalStates.get(0).board[wormPositions.get(i)[0] + 1][wormPositions.get(i)[1]] == 'e')
    			{
    				tempBoard = moveWorm(i, 1, 'D');
    				tempState = new State(newState, tempBoard, currentState, i, 0, wormPositions.get(i)[1], wormPositions.get(i)[0] + 1);
    				newState++;
    				frontierStates.add(tempState);
    			}
    			//left of head is empty
    			if(wormPositions.get(i)[1] > 0 && evalStates.get(0).board[wormPositions.get(i)[0]][wormPositions.get(i)[1] - 1] == 'e')
    			{
    				tempBoard = moveWorm(i, 1, 'L');
    				tempState = new State(newState, tempBoard, currentState, i, 0, wormPositions.get(i)[1] - 1, wormPositions.get(i)[0]);
    				newState++;
    				frontierStates.add(tempState);
    			}
    			//right of head is empty
    			if(wormPositions.get(i)[1] < board.length - 1 && evalStates.get(0).board[wormPositions.get(i)[0]][wormPositions.get(i)[1] + 1] == 'e')
    			{
    				tempBoard = moveWorm(i, 1, 'R');
    				tempState = new State(newState, tempBoard, currentState, i, 0, wormPositions.get(i)[1] + 1, wormPositions.get(i)[0]);
    				newState++;
    				frontierStates.add(tempState);
    			}
    			//above tail is empty
    			if(wormPositions.get(i)[2] > 0 && evalStates.get(0).board[wormPositions.get(i)[2] - 1][wormPositions.get(i)[3]] == 'e')
    			{
    				tempBoard = moveWorm(i, 0, 'U');
    				tempState = new State(newState, tempBoard, currentState, i, 1, wormPositions.get(i)[3], wormPositions.get(i)[2] - 1);
    				newState++;
    				frontierStates.add(tempState);
    			}
    			//below tail is empty
    			if(wormPositions.get(i)[2] < board[0].length - 1 && evalStates.get(0).board[wormPositions.get(i)[2] + 1][wormPositions.get(i)[3]] == 'e')
    			{
    				tempBoard = moveWorm(i, 0, 'D');
    				tempState = new State(newState, tempBoard, currentState, i, 1, wormPositions.get(i)[3], wormPositions.get(i)[2] + 1);
    				newState++;
    				frontierStates.add(tempState);
    			}
    			//left of tail is empty
    			if(wormPositions.get(i)[3] > 0 && evalStates.get(0).board[wormPositions.get(i)[2]][wormPositions.get(i)[3] - 1] == 'e')
    			{
    				tempBoard = moveWorm(i, 0, 'L');
    				tempState = new State(newState, tempBoard, currentState, i, 1, wormPositions.get(i)[3] - 1, wormPositions.get(i)[2]);
    				newState++;
    				frontierStates.add(tempState);
    			}
    			//right of tail is empty
    			if(wormPositions.get(i)[3] < board.length - 1 && evalStates.get(0).board[wormPositions.get(i)[2]][wormPositions.get(i)[3] + 1] == 'e')
    			{
    				tempBoard = moveWorm(i, 0, 'R');
    				tempState = new State(newState, tempBoard, currentState, i, 1, wormPositions.get(i)[3] + 1, wormPositions.get(i)[2]);
    				newState++;
    				frontierStates.add(tempState);
    			}
    		}
        	
    		//Prints each board state
    		/*
        	System.out.println();
        	board = evalStates.get(0).board;
        	printBoard();
        	System.out.println();
        	*/
        	
        	//check eval for GS ///%@#%@*&#^$%#$ ONLY WORKS for one worm right now
        	if(evalStates.get(0).board[board.length - 1][board[0].length - 1] == '0' || evalStates.get(0).board[board.length - 1][board[0].length - 1] == 'U' || evalStates.get(0).board[board.length - 1][board[0].length - 1] == 'L')
        		goalFound = true;
    	} 	
    	
    	System.out.println("GOAL FOUND");
    	board = evalStates.get(0).board;
    	printBoard();
    }
 
 
    
    //Moves the passed in worms, passed in body part (1 for head, 0 for tail), in the passed in direction (U, D, L, R)
    public char[][] moveWorm(int wormNumber, int head, char direction)
    {
    	char[][] tempBoard = new char[board.length][board[0].length];
    	boolean moveMade = false;
    	
    	for(int i = 0; i < board.length; i++)
        {
             for(int j = 0; j < board[0].length; j++)
            	 tempBoard[i][j] = evalStates.get(0).board[i][j];
        }
    	 
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
    					tempBoard = findAndMoveTail(i, j, tempBoard, wormNumber, direction);
    					moveMade = true;
    				}
    			}
    		}
    	}  	
    	return tempBoard;
    }
    
    
    //This function is called when the move originates in the head and handles the shifting of the worm,
    //by following the arrows from the head of the worm that is currently moving.
    public char[][] findAndMoveTail(int col, int row, char[][] tempBoard, int wormNumber, char direction)
    {
		Boolean firstLink = true, finished = false;
    	
    	while(finished == false)
    	{
    		//Follows the arrows to the tail and moves it
    		if(col > 0 && tempBoard[col - 1][row] == 'v')
    		{		
    			col--;
    			if(firstLink)
    			{
    				tempBoard[col][row] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[col][row + 1] == '<')
    		{
    			row++;
    			if(firstLink)
    			{
    				tempBoard[col][row] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[col + 1][row] == '^')
    		{
    			col++;
    			if(firstLink)
    			{
    				tempBoard[col][row] = (char) (wormNumber + 48);
    				firstLink = false;
    			}		
    		}
    		else if(row > 0 && tempBoard[col][row - 1] == '>')
    		{
    			row--;
    			if(firstLink)
    			{
    				tempBoard[col][row] = (char) (wormNumber + 48);
    				firstLink = false;
    			}
    		}
    		else if(col > 0 && tempBoard[col - 1][row] == 'D')
    		{
    			col--;
    			tempBoard[col][row] = 'v';
    			finished = true;
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[col][row + 1] == 'L')
    		{
    			row++;
    			tempBoard[col][row] = '<';
    			finished = true;
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[col + 1][row] == 'U')
    		{
    			col++;
    			tempBoard[col][row] = '^';
    			finished = true;
    		}
    		else if(row > 0 && tempBoard[col][row - 1] == 'R')
    		{
    			row--;
    			tempBoard[col][row] = '>';
    			finished = true;
    		}
    	}
    	
    	if(direction == 'D')
    		tempBoard[col + 1][row] = 'U';
    	else if(direction == 'U')
    		tempBoard[col - 1][row] = 'D';
    	else if(direction == 'L')
    		tempBoard[col][row - 1] = 'R';
    	else
    		tempBoard[col][row + 1] = 'L';
    	
    	return tempBoard;
    	
    }
    
    
    //This function is called when the move originates from the tail of the worm it performs the
    //changes to move the tail forward. The tail is found using the passed in head coordinates.
    public char[][] followBodyToTail(int col, int row, char[][] tempBoard)
    {
    	//If capitol E is ever inserted then ERROR
    	char directionToLastMove = 'E';
		boolean finished = false;
    	
    	while(finished == false)
    	{
    		//Follows the arrows to the tail and moves it
    		if(col > 0 && tempBoard[col - 1][row] == 'v')
    		{
    			col--;
    			directionToLastMove = 'D';
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[col][row + 1] == '<')
    		{
    			row++;
    			directionToLastMove = 'L';
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[col + 1][row] == '^')
    		{
    			col++;
    			directionToLastMove = 'U';    		
    		}
    		else if(row > 0 && tempBoard[col][row - 1] == '>')
    		{
    			row--;
    			directionToLastMove = 'R';
    		}
    		else if(col > 0 && tempBoard[col - 1][row] == 'D')
    		{
    			tempBoard[col][row] = directionToLastMove;
    			col--;
    			tempBoard[col][row] = 'e';
    			finished = true;
    		}
    		else if(row < tempBoard.length - 1 && tempBoard[col][row + 1] == 'L')
    		{
    			tempBoard[col][row] = directionToLastMove;
    			row++;
    			tempBoard[col][row] = 'e';
    			finished = true;
    		}
    		else if(col < tempBoard[0].length - 1 && tempBoard[col + 1][row] == 'U')
    		{
    			tempBoard[col][row] = directionToLastMove;
    			col++;
    			tempBoard[col][row] = 'e';
    			finished = true;
    		}
    		else if(row > 0 && tempBoard[col][row - 1] == 'R')
    		{
    			tempBoard[col][row] = directionToLastMove;
    			row--;
    			tempBoard[col][row] = 'e';
    			finished = true;
    		}   		
    	}  	
    	return tempBoard;
    }
    

    //Finds the position of the first worm and saves it coordinates in the wormPositions list
    public void getWormPosistions(char[][] currentBoard)
    {
        int[] tempWorm = new int[4];
        
        wormPositions.clear();
        
        //Find the head and tail of the worm %%%%%%%@%@%@%@%@//Doesn't scale for mult worms yet
        for(int i = 0; i < currentBoard.length; i++)
        {
            for(int j = 0; j < currentBoard.length; j++)
            {
      	      if(currentBoard[i][j] == '0')
      	      {
      		      tempWorm[2] = i;
      		      tempWorm[3] = j;
      	      }
      	      else if(currentBoard[i][j] == 'U' || currentBoard[i][j] == 'D' || currentBoard[i][j] == 'L' || currentBoard[i][j] == 'R')
      	      {
      	    	  tempWorm[0] = i;
      	    	  tempWorm[1] = j;
      	      }
            }
        }
        
        wormPositions.add(tempWorm);
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