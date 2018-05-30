package objects;
import board.*;
/**
 * 
 * @author Kush Patel
 * @author Alexander Louie
 *
 */
public class Pawn extends Piece {
	// int x
	// int y
	// boolean firstmove = false;
	// boolean team;
	// boolean alive = true;
	
	/**
	 * Returns True if vulnerable to enpassant
	 */
	boolean enpassant; // marks vulnerability
	
	public static char promotion = 'x'; // x is no promotion
	static boolean enPassantKill = false; 
	// static boolean enPassantDirection; // true = right, false = left
	/**
	 * 
	 * @param x
	 * @param y
	 * @param team
	 */
	public Pawn(int x, int y, boolean team) {
		this.x = x;
		this.y = y;
		
		this.team = team;
	}
	/**
	 * Executes promotion
	 * @param c Character of the Piece
	 * @param board The board where the piece is on
	 * @param x Current X coordinate
	 * @param y Current Y coordinate
	 * @return True if promotion is possible
	 */
	// change a pawn to another type
	public static boolean promote(char c, Piece[][] board, int x, int y) {
		if (c == 'p') {
			board[x][y] = new Pawn(x, y, board[x][y].getTeam());
		} else if (c == 'r') {
			board[x][y] = new Rook(x, y, board[x][y].getTeam());
		} else if (c == 'n') {
			board[x][y] = new Knight(x, y, board[x][y].getTeam());
		} else if (c == 'b') {
			board[x][y] = new Bishop(x, y, board[x][y].getTeam());
		} else if (c == 'q') {
			board[x][y] = new Queen(x, y, board[x][y].getTeam());
		} else {
			return false;
		}
		 
		board[x][y].firstmove = false;
		return true;
	}
	/**
	 * Resets Enpassant move
	 * @param board The board where	the piece is on
	 * @param turn True is White's turn, False is Black's turn
	 */
	// resets all enpassants to false after each move
	public static void resetEnPassant(Piece[][] board, boolean turn) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (board[x][y] instanceof Pawn && board[x][y].team == turn) {
					board[x][y].enpassant = false;
				}
			}
		}
	}

	
	@Override
	public boolean isPathClear(Piece[][] board, int x, int y) {
		// Black Pawn 
		if (this.team == false) {
			// diagonal kill
			if (!(Board.isEmpty(board, x, y)) && board[x][y].team == true && (x - this.x == 1) && (Math.abs(y - this.y) == 1)) return true;
			// move 2 spaces forward
			if (this.firstmove && Board.isEmpty(board, x, y) && Board.isEmpty(board, x-1, y) && (x - this.x == 1 || x - this.x == 2) && (y - this.y == 0)) {
				board[this.x][this.y].enpassant = true;
				return true;
			}
			// move 1 space forward
			if ((Board.isEmpty(board, x, y)) && (x - this.x == 1) && (y - this.y == 0)) return true;
			// enPassant Kill
			if ((x - this.x == 1) && (Math.abs(y - this.y) == 1)) {
				//if (y - this.y == -1) enPassantDirection = false;
				//else enPassantDirection = true;
				
				if (board[x-1][y] == null) {
					return false;
				}
				
				if (board[x-1][y] instanceof Pawn && board[this.x][this.y].isOppositeTeam(board[x-1][y]) && board[x-1][y].enpassant) {
					board[x-1][y] = null;
					return true;
				} else {
					return false;
				}
			}
		} 
		// White Pawn
		else {
			// diagonal kill
			if (!(Board.isEmpty(board, x, y)) && board[x][y].team == false && (this.x - x == 1) && (Math.abs(y - this.y) == 1)) return true;
			// move 2 spaces forward
			if (this.firstmove && Board.isEmpty(board, x, y) && Board.isEmpty(board, x+1, y) && (this.x - x == 1 || this.x - x == 2) && (y - this.y == 0)) {
				board[this.x][this.y].enpassant = true;
				return true;
			}
			// move 1 space forwrad
			if ((Board.isEmpty(board, x, y)) && (this.x - x == 1) && (this.y - y == 0)) return true;
			// enPassantKill
			if ((x - this.x == -1) && (Math.abs(this.y - y) == 1)) {
				//if (y - this.y == -1) enPassantDirection = false;
				//else enPassantDirection = true;
				
				if (board[x+1][y] == null) {
					return false;
				}
				
				if (board[x+1][y] instanceof Pawn && board[this.x][this.y].isOppositeTeam(board[x+1][y]) && board[x+1][y].enpassant) {
					board[x+1][y] = null;
					return true;
				} else {
					return false;
				}
			}
		}
		
		return false;
	}

	
	@Override
	public boolean move(Piece[][] board, int x, int y) {
		// TODO Auto-generated method stub
		int oldx = this.x;
		int oldy = this.y;
		if (isPathClear(board, x, y)) {
			if (enPassantKill) {
				board[oldx][oldy].update(board, x, y);
				if (board[x][y].team) {
					board[x+1][y] = null;
				} else {
					board[x-1][y] = null;
				}
				enPassantKill = false;
			}
			
			// pawn promotion
			if (promotion != 'x') {
				if (Board.isEmpty(board, x, y)) {
					board[oldx][oldy].update(board, x, y); // move
				} else if (board[oldx][oldy].isOppositeTeam(board[x][y])) { // kill
					board[oldx][oldy].kill(board, this.x, this.y, x, y);
				} else {
					promotion = 'x';
					return false;
				}
				
				if (promote(promotion, board, x, y)) {
					promotion = 'x';
					return true;
				} else {
					promotion = 'x';
					return false;
				}
			}
			
			// Move 
			if (Board.isEmpty(board, x, y)) {
				board[oldx][oldy].update(board, x, y);
				return true;
			}
			
			// Kill
			if (board[oldx][oldy].isOppositeTeam(board[x][y])) {
				board[oldx][oldy].kill(board, this.x, this.y, x, y);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
}
