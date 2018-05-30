package board;

import objects.*;
/**
 * 
 * @author Kush Patel
 * @author Alexander Louie
 *
 */
public class Board {
	/**
	 * An 8 by 8 Piece Array that holds the value of chess pieces.
	 * Pieces on the board are referenced by board positions.
	 * <p>
	 * Board's top left corner =  a8<br>
	 * Board's top right corner = h1
	 */
	
	/**
	 * Prints a welcome statement with author's name.
	 */
	public static void welcome() {
		System.out.println("===================================");
		System.out.println();
		System.out.println("WELCOME TO CHESS");
		System.out.println("By: Alex Louie + Kush Patel");
		System.out.println();
		System.out.println("===================================");
	}
	
	/**
	 * Builds the chess board with respective pieces in their respective locations 
	 * @return A 2D Piece array object with all the chess pieces
	 */
	public static Piece[][] buildBoard() {
		Piece[][] board = new Piece[8][8];
		
		// Set Init Null
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y] = null;
			}
		}
		
		// Black Pieces
		board[1][0] = new Pawn(1, 0, false);
		board[1][1] = new Pawn(1, 1, false);
		board[1][2] = new Pawn(1, 2, false);
		board[1][3] = new Pawn(1, 3, false);
		board[1][4] = new Pawn(1, 4, false);
		board[1][5] = new Pawn(1, 5, false);
		board[1][6] = new Pawn(1, 6, false);
		board[1][7] = new Pawn(1, 7, false);
		
		board[0][0] = new Rook(0, 0, false);
		board[0][1] = new Knight(0, 1, false);
		board[0][2] = new Bishop(0, 2, false);
		board[0][3] = new Queen(0, 3, false);
		board[0][4] = new King(0, 4, false);
		board[0][5] = new Bishop(0, 5, false);
		board[0][6] = new Knight(0, 6, false);
		board[0][7] = new Rook(0, 7, false);
		
		// White Pieces
		board[6][0] = new Pawn(6, 0, true);
		board[6][1] = new Pawn(6, 1, true);
		board[6][2] = new Pawn(6, 2, true);
		board[6][3] = new Pawn(6, 3, true);
		board[6][4] = new Pawn(6, 4, true);
		board[6][5] = new Pawn(6, 5, true);
		board[6][6] = new Pawn(6, 6, true);
		board[6][7] = new Pawn(6, 7, true);
		
		board[7][0] = new Rook(7, 0, true);
		board[7][1] = new Knight(7, 1, true);
		board[7][2] = new Bishop(7, 2, true);
		board[7][3] = new Queen(7, 3, true);
		board[7][4] = new King(7, 4, true);
		board[7][5] = new Bishop(7, 5, true);
		board[7][6] = new Knight(7, 6, true);
		board[7][7] = new Rook(7, 7, true);
		
		
		
		return board;
	}
	
	// Helper for printBoard
	/**
	 * Prints the name of the piece based on the player.
	 * @param spot The current location of the piece  
	 * @param piece The piece represented by a character
	 */
	private static void printName(Piece spot, char piece) {
		boolean team = spot.getTeam();
		if (team == true) { // white
			System.out.print("w" + piece + " ");
		} else { // black
			System.out.print("b" + piece + " ");
		}
	}
	
	// Checkered Pattern for printBoard
	/**
	 * Prints out a checkered pattern on the board. Double hashtags represent a black spot on the board.
	 * @param x The x coordinate on the board
	 * @param y The y coordinate on the board
	 */
	private static void printChecker(int x, int y) {
		if (x % 2 == 0) {
			if (y % 2 == 0) {
				System.out.print("   ");
			}
			else {
				System.out.print("## ");
			}
		} else if (x % 2 == 1) {
			if (y % 2 == 0) {
				System.out.print("## ");
			} 
			else {
				System.out.print("   ");
			}
		}
		
	}
	
	/**
	 * Prints the complete board with checkers and pieces in their respective location.
	 * @param board A plain 2D piece array object
	 */
	public static void printBoard(Piece[][] board) {
		// PRINT BOARD
		int n = 8;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				
				if (board[x][y] instanceof Pawn) {
					printName(board[x][y], 'P');
				} 
				else if (board[x][y] instanceof Rook) {
					printName(board[x][y], 'R');
				}
				else if (board[x][y] instanceof Knight) {
					printName(board[x][y], 'N');
				}
				else if (board[x][y] instanceof Bishop) {
					printName(board[x][y], 'B');
				}
				else if (board[x][y] instanceof Queen) {
					printName(board[x][y], 'Q');
				}
				else if (board[x][y] instanceof King) {
					printName(board[x][y], 'K');
				}
				else {
					printChecker(x, y);
				}

				// BORDER: RIGHT (Numbers)
				if (y == 7) {
					System.out.print(n);
					n--;
				}
				// BORDER: RIGHT (Numbers)
			}
			System.out.println();
			
			// BORDER: BOTTOM (Letters)
			if (x == 7) {
				char l = 'a';
				for (int bord = 0; bord < 8; bord++) {
					System.out.print(" " + l + " ");
					l = (char) (l + 1);
				}
				System.out.println();
			}
			// BORDER: BOTTOM (Letters)
		}
		System.out.println();
	}
	
	/**
	 * Checks if the spot on the board is empty
	 * @param board Uses the 2D piece array object
	 * @param x The x coordinate of the piece
	 * @param y The y coordinate of the piece
	 * @return true if the coordinate on the board is empty
	 */
	public static boolean isEmpty(Piece[][] board, int x, int y) {
		if (x > 7 || x < 0 || y > 7 || y < 0) { // out of range
			return true;
		}
		
		if (board[x][y] == null) {
			return true;
		}
		
		return false;
	}
	
	
	// Translates human input to board language - string must be inputted correctly
	/**
	 * Translates the human input to a value that can be read by the board.
	 * @param s The initial and final location of the piece as a string format
	 * @return An int[] array object with the translated values
	 */
	public static int[] translate(String s) {
		int[] arr = new int[4];
		
		char a1 = (char) (s.charAt(0)-49);
		arr[1] = Character.getNumericValue(a1);
		arr[0] = 8 - Character.getNumericValue(s.charAt(1));
		char a3 = (char) (s.charAt(3)-49);
		arr[3] = Character.getNumericValue(a3);
		arr[2] = 8 - Character.getNumericValue(s.charAt(4));
		
		return arr;
	}
	
}


