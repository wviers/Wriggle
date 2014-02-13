import java.io.IOException;
import java.util.ArrayList;

public class SolveBoard
{
    char[][] board;
    
    //Each index contains a int array that contains {headRow, headCol, tailRow, tailCol}
    ArrayList<int[]> wormPositions = new ArrayList<int[]>();
  
    public SolveBoard(char[][] gameBoard, int numWorms) throws IOException
    {
      board = gameBoard;
      
      //BFTS bfts = new BFTS(board);
      //IDDFTS iddfts = new IDDFTS(board, numWorms);
      GBFGS gbfgs = new GBFGS(board, numWorms);
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