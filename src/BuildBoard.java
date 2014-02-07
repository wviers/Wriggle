import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class BuildBoard
{
	public static void main(String argv[]) throws IOException
	{
		int numWorms = 0, rows, cols;
    
		//Find file specified by command line
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(argv[0]));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found");	
		}
    
		String input = br.readLine();
		String[] tokens = input.split(" ");

		//Input from text file
		numWorms = Integer.parseInt(tokens[2]);
		rows = Integer.parseInt(tokens[0]);
		cols = Integer.parseInt(tokens[1]);
		char[][] board = new char[rows][cols];
    
    
		//Fill the board from the text file 
		for(int i = 0; i < rows; i++)
		{
			int index = 0;
			input = br.readLine();
			for(int j = 0; j < cols; j++)
			{
				board[i][j] = input.charAt(index);
				index+= 2;
			}
		}
    
    
        SolveBoard solve = new SolveBoard(board, numWorms);
    }
}