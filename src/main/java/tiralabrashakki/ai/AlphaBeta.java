package tiralabrashakki.ai;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.possibleMoves.PossibleMoves;

public class AlphaBeta {
	
	/**
	 * Finds the best move to given depth using minimax with AlphaBeta pruning.
	 * @param board State of the board
	 * @param depth Search depth
	 * @return The best move
	 */
	public Move findBestMove(Board board, int depth) {
		MoveAndValue mv = abMax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return mv.getMove();
	}
	
	public MoveAndValue abMax(Board board, int depth, int alpha, int beta) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		if (depth == 0 || moves.isEmpty()) {
			//TODO: evaluate board and return move and its value.
			return null;
		}
		
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MIN_VALUE);
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			int val = abMin(board, depth - 1, alpha, beta).getValue(); //call abMin with new board state, lower depth, and different color
			board.unmakeMove(move);
			
			if (val > bestMove.getValue()) { //update best move if the new value is greater
				bestMove.setValue(val);
				bestMove.setMove(move);
				
				if (val > alpha) { //update alpha value
					alpha = val;
					if (beta <= alpha) //prune if alpha was greater than or equal to beta
						break;
				}
			}
		}
		
		return bestMove; //should i return best move val or alpha? and if alpha, should i start best move with the alpha value? IT DOESNT MATTER, maybe not
	}
	
	public MoveAndValue abMin(Board board, int depth, int alpha, int beta) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		if (depth == 0 || moves.isEmpty()) {
			//TODO: evaluate board and return move and it's value.
			return null;
		}
		
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MAX_VALUE);
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			int val = abMax(board, depth - 1, alpha, beta).getValue(); //call abMax with new board state, lower depth, and different color
			board.unmakeMove(move);
			
			if (val < bestMove.getValue()) { //update best move if the new value is lower
				bestMove.setValue(val);
				bestMove.setMove(move);
				
				if (val < beta) { //update beta value
					beta = val;
					if (beta <= alpha) //prune if alpha was greater than or equal to beta
						break;
				}
			}
		}
		
		return bestMove;
	}
}
