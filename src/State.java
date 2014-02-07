public class State
{
	int id;
	char[][] board;
	int parentID;
	int[] solutionStep = new int[4];
	
	
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
}