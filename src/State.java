public class State
{
	//heuristicOrder is set to 1 if worm 0 is being moved 1 otherwise
	int heuristicOrder;
	
	//distance to the bottom right corner of the board + number of parent states
	int heuristicDistance;
	
	int id, parentID;
	char[][] board;
	State parent;
	int[] solutionStep = new int[4];
	
	
	public State(int theID, char[][] gameBoard, State parentState, int wriggler, int tail, int col, int row)
	{
		id = theID;
		board = gameBoard;
		parent = parentState;
		solutionStep[0] = wriggler;
		solutionStep[1] = tail;
		solutionStep[2] = col;
		solutionStep[3] = row;
	}
	
	public State(int theID, char[][] gameBoard, int passedID, int wriggler, int tail, int col, int row)
	{
		id = theID;
		board = gameBoard;
		parentID = passedID;
		solutionStep[0] = wriggler;
		solutionStep[1] = tail;
		solutionStep[2] = col;
		solutionStep[3] = row;
	}
	
	
	public State(State copy)
	{
		id = copy.id;
		board = copy.board;
		parent = copy.parent;
		solutionStep[0] = copy.solutionStep[0];
		solutionStep[1] = copy.solutionStep[1];
		solutionStep[2] = copy.solutionStep[2];
		solutionStep[3] = copy.solutionStep[3];
	}
	
	
	public void setOrder(int order)
	{
		heuristicOrder = order;
	}
	
	public void setHeuristicDistance(int passedDist)
	{
		heuristicDistance = passedDist;
	}
}