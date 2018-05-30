package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import objects.*;
import board.*;

/**
 * 
 * @author Kush Patel
 * @author Alexander Louie
 *
 */
public class Chess {
	/**
	 * drawProposal
	 */
	static boolean drawProposal = false; // for draw proposals
	
	/**
	 * Main loop in which chess runs. If either players kings are in a checkmate the game ends. If a player resigns the game also ends.
	 * @param args
	 * @throws IOException
	 */
	public static void main (String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		// INITIALIZE
		//Board.welcome();
		Piece[][] board = Board.buildBoard();	
		Board.printBoard(board);
		
		boolean turn = true; // white = true, black = false
		
		while (!kingDead(board, turn) && !checkMate(board, true) && !checkMate(board, false)) { // ** condition needs to be whether or not the King is dead or game is over			
			King king = null;
			for (int i = 0; i <= 7; i++) {
				for (int j = 0; j<= 7; j++) {
					if (!Board.isEmpty(board, i, j) && board[i][j].getTeam() == turn && board[i][j] instanceof King) {
						king = (King) board[i][j];
					}
				}
			}
			if (!Piece.isChecked(board, king.getx(), king.gety(), turn) && staleMate(board, turn)) {
				endGame(turn, true);
			}
			
			Pawn.resetEnPassant(board, turn);
			
			if (turn) { System.out.print("White's move: ");
			} else { System.out.print("Black's move: "); }
			
			String s = br.readLine().trim().toLowerCase();
			
			System.out.println(); // formatting space
			
			// draw response
			if (drawProposal) {
				if (s.equals("draw")) {
					endGame(turn, true);
				}
				drawProposal = false;
			}
			
			if (checkInput(s, turn, board)) {
				int[] trans = Board.translate(s);
				
				// if start state is NULL
				if (board[trans[0]][trans[1]] == null) {
					//System.out.println("1");
					System.out.println("Illegal move, try again");
					System.out.println();
					continue;
				}
				
				// start state is wrong team
				if (board[trans[0]][trans[1]].getTeam() == !turn) {
					//System.out.println("2");
					System.out.println("Illegal move, try again");
					System.out.println();
					continue;
				}
				
				Piece spottaken = board[trans[2]][trans[3]];
				// if move is not valid
				if (board[trans[0]][trans[1]].move(board, trans[2], trans[3])) {
					// do not allow moves that cause check
					if (Piece.isChecked(board, king.getx(), king.gety(), turn) && king.getTeam() == turn) {
						board[trans[2]][trans[3]].update(board, trans[0], trans[1]);
						board[trans[2]][trans[3]] = spottaken;
						//System.out.println("3");
						System.out.println("Illegal move, try again");
						System.out.println();
						continue;
					}
					// ---------------------------
					King otherking = null;
					for (int i = 0; i <= 7; i++) {
						for (int j = 0; j<= 7; j++) {
							if (!Board.isEmpty(board, i, j) && board[i][j].getTeam() != turn && board[i][j] instanceof King) {
								otherking = (King) board[i][j];
							}
						}
					}
					Board.printBoard(board);
					if (otherking != null) {
						if (Piece.isChecked(board, otherking.getx(), otherking.gety(), !turn)) {
							System.out.println("Check");
						}
					}
					turn = !turn;
				} else {
					//System.out.println("4");
					System.out.println("Illegal move, try again");
					System.out.println();
					continue;
				}
			} else {
				//System.out.println("5");
				System.out.println("Illegal move, try again");
				System.out.println();
				continue;
			}
		}
		
		endGame(!turn, false);
		
	}
	
	// checks if your move kills the King
	/**
	 * Checks if a move puts the king in check
	 * @param board The board in which chess is being played on
	 * @param turn True if it is the user's turn is white, False is black
	 * @return True if a move can remove the king
	 */
	public static boolean kingDead(Piece[][] board, boolean turn) {
		King king = null;
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j<= 7; j++) {
				if (!Board.isEmpty(board, i, j) && board[i][j].getTeam() == turn && board[i][j] instanceof King) {
					king = (King) board[i][j];
				}
			}
		}
		if (king == null) {
			endGame(!turn, false);
			return true;
		}
		return false;
	}
	
	/**
	 * A check to see if there is no valid move when the king is not in check
	 * @param board The board in which chess is being played on
	 * @param turn True if it is the user's turn is white, False is black
	 * @return True if the board is in a stalemate
	 */
	public static boolean staleMate(Piece[][] board, boolean turn) {
		// basic explanation: 
		King king = null;
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j<= 7; j++) {
				if (!Board.isEmpty(board, i, j) && board[i][j].getTeam() == turn && board[i][j] instanceof King) {
					king = (King) board[i][j];
				}
			}
		}
		
		Piece piece = null;
		Piece spottaken = null;
		if (!Piece.isChecked(board, king.getx(), king.gety(), turn)) {
			for (int i = 0; i <= 7; i++) {
				for (int j = 0; j <= 7; j++) {
					if (!Board.isEmpty(board, i, j) && board[i][j].getTeam() == turn) {
						piece = board[i][j];
						for (int i2 = 0; i2 <= 7; i2++) {
							for (int j2 = 0; j2 <= 7; j2++) {
								int oldx = piece.getx();
								int oldy = piece.gety();
							    spottaken = board[i2][j2];
							    boolean firstmove = board[oldx][oldy].getFirstMove();
							    if (piece.move(board, i2, j2)) {
									if (!Piece.isChecked(board, king.getx(), king.gety(), king.getTeam())) {
										board[piece.getx()][piece.gety()].undo(board, oldx, oldy, firstmove);
										board[i2][j2] = spottaken;
										// must make this move
										return false;
									}
									board[piece.getx()][piece.gety()].undo(board, oldx, oldy, firstmove);
									board[i2][j2] = spottaken;
								}
							}
						}
					}
				}
			}
		}
		
		System.out.println("Stalemate");
		return true;
	}
	/**
	 * A check to see if the king has any possible moves that might result in the king being captured
	 * @param board The board in which chess is being played on
	 * @param turn True if it is the user's turn is white, false is black
	 * @return
	 */
	public static boolean checkMate(Piece[][] board, boolean turn) {
		King king = null;
		for (int i = 0; i <= 7; i++) {
			for (int j = 0; j <= 7; j++) {
				if (!Board.isEmpty(board, i, j) && board[i][j].getTeam() == turn && board[i][j] instanceof King) {
					king = (King) board[i][j];
				}
			}
		}
		
		if (Piece.isChecked(board, king.getx(), king.gety(), king.getTeam())) {
			//System.out.println("check!!!!");
			Piece piece = null;
			Piece spottaken = null;
			
			for (int i = 0; i <= 7; i++) {
				for (int j = 0; j <= 7; j++) {
					if (!Board.isEmpty(board, i, j) && board[i][j].getTeam() == turn) {
						// do every move and check if check works or not
						piece = board[i][j];
						for (int i2 = 0; i2 <= 7; i2++) {
							for (int j2 = 0; j2 <= 7; j2++) {
								int oldx = piece.getx();
								int oldy = piece.gety();
							    spottaken = board[i2][j2];
							    boolean firstmove = board[oldx][oldy].getFirstMove();
								if (piece.move(board, i2, j2)) {
									if (!Piece.isChecked(board, king.getx(), king.gety(), king.getTeam())) {
										board[piece.getx()][piece.gety()].undo(board, oldx, oldy, firstmove);
										board[i2][j2] = spottaken;
										// must make this move
										return false;
									}
									board[piece.getx()][piece.gety()].undo(board, oldx, oldy, firstmove);
									board[i2][j2] = spottaken;
								}
							}
						}
					}
				}
			}	

			System.out.println("Checkmate");
			return true;
		} 

		return false;
	}
	/**
	 * A check that determines what the outcome will be dependent on the user's input
	 * @param s The user input
	 * @param turn The board in which chess is being played on
	 * @param board True if it is the user's turn is white, false is black
	 * @return True if an input is valid.
	 */
	public static boolean checkInput(String s, boolean turn, Piece[][] board) {
		if (s.length() != 5 || s.charAt(2) != ' ') {
			if (s.length() >= 6) {
				// resign
				if (s.equals("resign")) {
					endGame(!turn, false);
				}
				
				// draw
				if (checkInput(s.substring(0, 5), turn, board)) {
					if (s.substring(6).equals("draw?")) {
						drawProposal = true;
						return true;
					}
				}
				
				// pawn promotion with argument
				int[] trans = Board.translate(s);
				if (board[trans[0]][trans[1]] instanceof Pawn) {
					if ((board[trans[0]][trans[1]].getTeam() && trans[2] == 0) || (!board[trans[0]][trans[1]].getTeam() && trans[2] == 7)) {
						// white or black
						if ((s.length() == 7) && checkInput(s.substring(0, 5), turn, board) && s.charAt(5) == ' ' && Character.isLetter(s.charAt(6))) {
							Pawn.promotion = s.charAt(6);
							return true;
						} 						
					}
				}
				
				
			}
			
			// System.out.println("Invalid Input: format incorrect");
			return false;
		}
		
		if (!Character.isLetter(s.charAt(0)) || !Character.isDigit(s.charAt(1)) || !Character.isLetter(s.charAt(3)) || !Character.isDigit(s.charAt(4))) {
			// System.out.println("Invalid Input: format incorrect");
			return false;
		}
		
		// make sure letters and numbers are within range
		if ((!(s.charAt(0) >= 'a' && s.charAt(0) <= 'h')) || (!(s.charAt(3) >= 'a' && s.charAt(3) <= 'h'))) {
			// System.out.println("Invalid Input: letters are out of range");
			return false;
		}
		
		if ((!(s.charAt(1) >= '1' && s.charAt(1) <= '8')) || (!(s.charAt(4) >= '1' && s.charAt(4) <= '8'))) {
			// System.out.println("Invalid Input: numbers are out of range");
			return false;
		}
		
		// no duplicate entries
		if ((s.charAt(0) == s.charAt(3)) && (s.charAt(1) == s.charAt(4))) {
			// System.out.println("Invalid Input: Target location cannot be the same as state location");
			return false;
		}
		
		// pawn promotion - w/o argument - queen
		int[] trans = Board.translate(s);
		if (board[trans[0]][trans[1]] instanceof Pawn) {
			if ((board[trans[0]][trans[1]].getTeam() && trans[2] == 0) || (!board[trans[0]][trans[1]].getTeam() && trans[2] == 7)) {
				// white or black
				Pawn.promotion = 'q';				
			}
		}
		
		
		return true;
	}
	
	/**
	 * A check to determine if a user's turn is correct
	 * @param board The board in which chess is being played on
	 * @param arr An int[] array that contains the moves of the user
	 * @param turn True if it is the user's turn is white, false is black
	 * @return True if the user's turn is correct
	 */
	public static boolean checkTurn(Piece[][] board, int[] arr, boolean turn) {
		boolean team = board[arr[0]][arr[1]].getTeam();
		
		if (turn == team) { // correct team
			turn = !turn;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines if it is the end of a game
	 * @param team True is white, false is black 
	 * @param draw True if outcome is a draw
	 */
	public static void endGame(boolean team, boolean draw) { // input is winner, draw is true if draw
		if (draw) System.out.println("draw");
		else if (team) System.out.println("White wins");
		else System.out.println("Black wins");
		
		System.exit(0);
	}
}
