Wriggle
=======

javac BuildBoard.java
java BuildBoard Puzzle1.txt
java BuildBoard Puzzle1.txt

NOTE: The current algorithm is IDDFTS if another algorithm is to be run
the following lines in SolveBoard.java will have to have the commenting swapped.
The BFTS current version only moves the worm with id 0.
      //BFTS bfts = new BFTS(board);
      IDDFTS iddfts = new IDDFTS(board, numWorms);
