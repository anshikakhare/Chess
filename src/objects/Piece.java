package objects;
import board.*;
/**
 * 
 * @author Kush Patel
 * @author Alexander Louie
 */
public abstract class Piece {
		/**
		 * The x coordinate of the Piece
		 */
        int x;
        /**
         * The y coordinate of the Piece
         */
        int y;
        /**
         * A value that determines if it is the first time a piece has been moved
         */
        boolean firstmove = true; // true - has not moved | false - moved at least once, for Pawns + Castle
        /**
         * True = White Team<br>
         * False = Black Team
         */
        boolean team; // true - white    false - black
        /**
         * True if enpassanted
         */
		public boolean enpassant; // only for pawns

        // White or Black
		/**
		 * Sets team
		 * @param team True is White, False is Black
		 */
        public void setTeam(boolean team) {
                this.team = team;
        }
        /**
         * Returns Team
         * @return True is White, False is Black
         */
        public boolean getTeam() {
                return team;
        }
        /**
         * Return boolean for first move
         * @return True if it is the Pawn's first move
         */
        public boolean getFirstMove() {
        		return firstmove;
        }
        /**
         * Check to see if the piece is on the opposite team
         * @param x The Piece object
         * @return True if piece is not on the other team
         */
        public boolean isOppositeTeam(Piece x) {
        		boolean team = this.team;
        		boolean team2 = x.team;
        		if (team == team2) {
        			return false;
        		}
        		return true;
        }
        /**
         * Returns the X coordinate of the Piece
         * @return X coordinate
         */
    	public int getx() {
    		return this.x;
    	}
    	/**
    	 * Returns the Y coordinate of the piece
    	 * @return Y coordinate
    	 */
    	public int gety() {
    		return this.y;
    	}



    	/**
    	 * Updates location and deletes past spots
    	 * @param board The board that the piece is on
    	 * @param x X coordinate of the piece
    	 * @param y Y coordinate of the piece
    	 */
	    public void update(Piece[][] board, int x, int y) {
	    		board[x][y] = board[this.x][this.y];
	    		board[x][y].enpassant = board[this.x][this.y].enpassant;
	        board[this.x][this.y] = null;
	        board[x][y].x = x;
	        board[x][y].y = y;
	        this.firstmove = false;
	    }
    
   
	    /**
	     * Undo's move and updates board
	     * @param board The board that the piece is on
	     * @param x X coordinate of the piece
	  	 * @param y Y coordinate of the piece
	     * @param firstmove True if it the Pawn's first move
	     */
	    public void undo(Piece[][] board, int x, int y, boolean firstmove) {
	    		board[x][y] = board[this.x][this.y];
	    		board[x][y].firstmove = firstmove;
	    		board[x][y].enpassant = board[this.x][this.y].enpassant;
	        board[this.x][this.y] = null;
	        board[x][y].x = x;
	        board[x][y].y = y;
	    }
    
    
	    /**
	     * Take out another piece
	     * @param board The board that the piece is on
	     * @param x end X coordinate of the piece
	  	 * @param y end Y coordinate of the piece
	  	 * @param thisx starting x coordinate of the piece
	  	 * @param thisy starting y coordinate of the piece
	     */
	    public void kill(Piece[][] board, int thisx, int thisy, int x, int y) {
	    		board[x][y] = board[thisx][thisy];
			board[thisx][thisy] = null;
			board[x][y].x = x;
			board[x][y].y = y;
			board[x][y].firstmove = false;
	    }
    
    /**
     * Only possible moves of the type of piece
     * @param board The board that the piece is on
     * @param x X coordinate of the Piece
     * @param y Y coordinate of the Piece
     * @return True if the move is successful
     */
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
	}
    
    /**
     * Check to see if a king is in check
     * @param board The board that the piece is on
     * @param x X coordinate of the Piece
     * @param y Y coordinate of the Piece
     * @param turn True is White, False is Black
     * @return Ture if king is checked
     */
    public static boolean isChecked(Piece[][] board, int x, int y, boolean turn) {
    		for (int i = x+1; i <= 7; i++) { // south (Rook + Queen)
    			if (!Board.isEmpty(board, i, y)) {
    	    			if ((board[i][y] instanceof Rook || board[i][y] instanceof Queen) && board[i][y].team != turn) {
    	    				return true;
    	    			} else {
    	    				break;
    	    			}
    			}	
    		}
    		
    		for (int i = x-1; i >= 0; i--) { // north (Rook + Queen)
    			if (!Board.isEmpty(board, i, y)) {
    	    			if ((board[i][y] instanceof Rook || board[i][y] instanceof Queen) && board[i][y].team != turn) {
    	    				return true;
    	    			} else {
    	    				break;
    	    			}
    			}	
    		}
    		
    		for (int i = y+1; i <= 7; i++) { // east (Rook + Queen)
    			if (!Board.isEmpty(board, x, i)) {
    	    			if ((board[x][i] instanceof Rook || board[x][i] instanceof Queen) && board[x][i].team != turn) {
    	    				return true;
    	    			} else {
    	    				break;
    	    			}
    			}	
    		}
    		
    		for (int i = y-1; i >= 0; i--) { // west (Rook + Queen)
    			if (!Board.isEmpty(board, x, i)) {
    	    			if ((board[x][i] instanceof Rook || board[x][i] instanceof Queen) && board[x][i].team != turn) {
    	    				return true;
    	    			} else {
    	    				break;
    	    			}
    			}	
    		}
    		
    		for (int i = x+1, j = y-1; i <= 7 && j >= 0; i++, j--) { // northeast (Bishop + Queen)
    			if (!Board.isEmpty(board, i, j)) { 
    	    			if ((board[i][j] instanceof Bishop || board[i][j] instanceof Queen) && board[i][j].team != turn) {
    	    				return true;
    	    			} else {
    	    				break;
    	    			}
    			}	
    		}
    		
    		for (int i = x-1, j = y-1; i >= 0 && j >= 0; i--, j--) { // northwest (Bishop + Queen)
    			if (!Board.isEmpty(board, i, j)) {
    	    			if ((board[i][j] instanceof Bishop || board[i][j] instanceof Queen) && board[i][j].team != turn) {
    	    				return true;
    	    			} else {
    	    				break;
    	    			}
    			}	
    		}
    		
    		for (int i = x+1, j = y+1; i <= 7 && j <= 7; i++, j++) { // southeast (Bishop + Queen)
    			if (!Board.isEmpty(board, i, j)) { 
    	    			if ((board[i][j] instanceof Bishop || board[i][j] instanceof Queen) && board[i][j].team != turn) {
    	    				return true;
    	    			} else {
    	    				break;
    	    			}
    			}	
    		}
    		
    		for (int i = x-1, j = y+1; i >= 0 && j <= 7; i--, j++) { // southwest (Bishop + Queen)
    			if (!Board.isEmpty(board, i, j)) {
    	    			if ((board[i][j] instanceof Bishop || board[i][j] instanceof Queen) && board[i][j].team != turn) {
    	    				return true;
    	    			} else {
    	    				break;
    	    			}
    			}	
    		}
    		
    		if (turn) { // white piece, checking for black Pawn
    			if ((!Board.isEmpty(board, x-1, y-1) && board[x-1][y-1] instanceof Pawn && board[x-1][y-1].team != turn) || (!Board.isEmpty(board, x-1, y+1) && board[x-1][y+1] instanceof Pawn && board[x-1][y+1].team != turn)) { 
    				return true;
    			}
    		} else { // black piece, checking for white Pawn
    			if ((!Board.isEmpty(board, x+1, y-1) && board[x+1][y-1] instanceof Pawn && board[x+1][y-1].team != turn) || (!Board.isEmpty(board, x+1, y+1) && board[x+1][y+1] instanceof Pawn && board[x+1][y+1].team != turn)) { 
    				return true;
    			}
    		}
    		
    		// Knight
    		if (!Board.isEmpty(board, x+2, y+1) && board[x+2][y+1] instanceof Knight && board[x+2][y+1].team != turn) return true;
    		if (!Board.isEmpty(board, x+2, y-1) && board[x+2][y-1] instanceof Knight && board[x+2][y-1].team != turn) return true;
    		if (!Board.isEmpty(board, x-2, y+1) && board[x-2][y+1] instanceof Knight && board[x-2][y+1].team != turn) return true;
    		if (!Board.isEmpty(board, x-2, y-1) && board[x-2][y-1] instanceof Knight && board[x-2][y-1].team != turn) return true;
    		
    		if (!Board.isEmpty(board, x+1, y+2) && board[x+1][y+2] instanceof Knight && board[x+1][y+2].team != turn) return true;
    		if (!Board.isEmpty(board, x+1, y-2) && board[x+1][y-2] instanceof Knight && board[x+1][y-2].team != turn) return true;
    		if (!Board.isEmpty(board, x-1, y+2) && board[x-1][y+2] instanceof Knight && board[x-1][y+2].team != turn) return true;
    		if (!Board.isEmpty(board, x-1, y-2) && board[x-1][y-2] instanceof Knight && board[x-1][y-2].team != turn) return true;
    		
    		// King
    		if (!Board.isEmpty(board, x+1, y) && board[x+1][y] instanceof King && board[x+1][y].team != turn) return true;
    		if (!Board.isEmpty(board, x-1, y) && board[x-1][y] instanceof King && board[x-1][y].team != turn) return true;
    		if (!Board.isEmpty(board, x, y+1) && board[x][y+1] instanceof King && board[x][y+1].team != turn) return true;
    		if (!Board.isEmpty(board, x, y-1) && board[x][y-1] instanceof King && board[x][y-1].team != turn) return true;
    		
    		if (!Board.isEmpty(board, x+1, y+1) && board[x+1][y+1] instanceof King && board[x+1][y+1].team != turn) return true;
    		if (!Board.isEmpty(board, x-1, y+1) && board[x-1][y+1] instanceof King && board[x-1][y+1].team != turn) return true;
    		if (!Board.isEmpty(board, x+1, y-1) && board[x+1][y-1] instanceof King && board[x+1][y-1].team != turn) return true;
    		if (!Board.isEmpty(board, x-1, y-1) && board[x-1][y-1] instanceof King && board[x-1][y-1].team != turn) return true;
    		
    		return false;
    }
    
    /**
     * A check to determine if a path is clear
     * @param board The board that the piece is on
     * @param x X coordinate of the Piece
     * @param y Y coordinate of the Piece
     * @return True if path is clear
     */
   public abstract boolean isPathClear(Piece[][] board, int x, int y);

}
