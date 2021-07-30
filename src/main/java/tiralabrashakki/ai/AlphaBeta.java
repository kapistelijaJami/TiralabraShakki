package tiralabrashakki.ai;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;
import tiralabrashakki.possibleMoves.PossibleMoves;

public class AlphaBeta {
	
	/**
	 * Finds the best move to given depth using minimax with AlphaBeta pruning.
	 * @param board State of the board
	 * @param depth Search depth
	 * @param colorTurn Whose turn is it
	 * @return The best move
	 */
	public Move findBestMove(Board board, int depth, PlayerColor colorTurn) {
		MoveAndValue mv = abMax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, colorTurn);
		return mv.getMove();
	}
	
	public MoveAndValue abMax(Board board, int depth, int alpha, int beta, PlayerColor colorTurn) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, colorTurn);
		if (depth == 0 || moves.isEmpty()) {
			//evaluate board and return move and it's value.
			return null;
		}
		
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MIN_VALUE);
		
		for (int i = 0; i < moves.size(); i++) {
			//make move on board
			//call abMin with new board state, lower depth, and different color
			//unmake move on board
			
			//update best move if the new value is greater
			
			//update alpha value, and prune if alpha is greater than or equal to beta
		}
		
		return bestMove;
	}
	
	public MoveAndValue abMin(Board board, int depth, int alpha, int beta, PlayerColor colorTurn) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, colorTurn);
		if (depth == 0 || moves.isEmpty()) {
			//evaluate board and return move and it's value.
			return null;
		}
		
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MAX_VALUE);
		
		for (int i = 0; i < moves.size(); i++) {
			//make move on board
			//call abMax with new board state, lower depth, and different color
			//unmake move on board
			
			//update best move if the new value is lower
			
			//update beta value, and prune if alpha is greater than or equal to beta
		}
		
		return bestMove;
	}
}
