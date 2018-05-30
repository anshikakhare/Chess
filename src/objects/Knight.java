package objects;

import board.Board;

/**
 * 
 * @author Kush Patel
 * @author Alexander Louie
 */

public class Knight extends Piece {
	/**
	 * 2 Arg constructor for Knight Class
	 * @param x Current X coordinate
	 * @param y Current Y coordinate
	 * @param team True is White, False is Black
	 */
	public Knight(int x, int y, boolean team) {
		this.x = x;
		this.y = y;
		
		this.team = team;
	}

	@Override
	public boolean isPathClear(Piece[][] board, int x, int y) {
		int oldx = this.x;
		int oldy = this.y;
		
		int deltax = Math.abs(oldx - x);
		int deltay = Math.abs(oldy - y);
		
		if ((deltax == 2 && deltay == 1) || (deltax == 1 && deltay == 2)) {
			return true;
		}
		
		return false;
	}
	
	/*@Override
	public boolean move(Piece[][] board, int x, int y) {
		// TODO Auto-generated method stub
		int oldx = this.x;
		int oldy = this.y;
		
		if (isPathClear(board, x, y)) {
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
		
	}*/

}
