package objects;

import board.Board;

/**
 * 
 * @author Kush Patel
 * @author Alexander Louie
 */

public class King extends Piece {
	
	/**
	 * Whitecastle determines if the whitecastle is taken
	 */
	static boolean whitecastle = false;
	/**
	 * Blackcastle determines if the blackcastle is taken
	 */
	static boolean blackcastle = false;
	
	/**
	 * 2 Arg Constructor for King
	 * @param x Current X coordinate
	 * @param y Current Y coordinate
	 * @param team True is White, False is Black
	 */
	public King(int x, int y, boolean team) {
		this.x = x;
		this.y = y;
		
		this.team = team;
	}
	/**
	 * 
	 */
	@Override
	public boolean isPathClear(Piece[][] board, int x, int y) {
		int oldx = this.x;
		int oldy = this.y;
		
		int deltax = Math.abs(x - oldx);
		int deltay = Math.abs(y - oldy);
		
		if (((deltax == deltay) && deltax == 1 && deltay == 1) || (deltax == 1 && deltay == 0) || (deltax == 0 && deltay == 1)) {
			return true;
		}
		
		Piece king = board[oldx][oldy];
		// Castling
		
		// **NOTICE: must put extra check conditions in here
		if ((king.getTeam() == true && oldx == 7 && oldy == 4 && x == 7 && y == 6 && king.firstmove && board[x][y+1].firstmove) || (king.getTeam() == false && oldx == 0 && oldy == 4 && x == 0 && y == 6 && king.firstmove && board[x][y+1].firstmove)) {
			if (Board.isEmpty(board, oldx, oldy+1) && Board.isEmpty(board, oldx, oldy+2) && !Piece.isChecked(board, oldx, oldy, board[oldx][oldy].getTeam()) && !Piece.isChecked(board, oldx, oldy+1, board[oldx][oldy].getTeam()) && !Piece.isChecked(board, oldx, oldy+2, board[oldx][oldy].getTeam())) {
				if (board[oldx][oldy].getTeam()) whitecastle = true;
				else blackcastle = true;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean move(Piece[][] board, int x, int y) {
		// TODO Auto-generated method stub
		int oldx = this.x;
		int oldy = this.y;
		
		if (isPathClear(board, x, y)) {
			// Move 
			if (Board.isEmpty(board, x, y)) {
				board[oldx][oldy].update(board, x, y);
				if (whitecastle) {
					if (board[x][y].getTeam()) board[7][7].update(board, 7, 5);
					whitecastle = false;
				}
				if (blackcastle) {
					if (!board[x][y].getTeam()) board[0][7].update(board, 0, 5);
					blackcastle = false;
				}
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
