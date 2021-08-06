package tiralabrashakki.ai;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;
import tiralabrashakki.possibleMoves.PossibleMoves;
import tiralabrashakki.possibleMoves.SquareSafety;

public class AlphaBeta {
	
	/**
	 * Finds the best move to given depth using minimax with AlphaBeta pruning.
	 * @param board State of the board
	 * @param depth Search depth
	 * @return The best move
	 */
	public Move findBestMove(Board board, int depth) {
		long time = System.currentTimeMillis();
		
		MoveAndValue mv;
		if (board.getTurnColor().isWhite()) {
			mv = abMax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, SquareSafety.isKingSafe(board, PlayerColor.WHITE));
		} else {
			mv = abMin(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, SquareSafety.isKingSafe(board, PlayerColor.BLACK));
		}
		
		System.out.println("That took: " + (System.currentTimeMillis() - time) + " ms");
		return mv.getMove();
	}
	
	private MoveAndValue abMax(Board board, int depth, int alpha, int beta, boolean inCheck) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		if (depth == 0 || moves.isEmpty()) {
			MoveAndValue mv = new MoveAndValue(null, 0);
			mv.setValue(Heuristics.evaluate(board, depth, inCheck, moves.size()));
			return mv;
		}
		
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MIN_VALUE); //might be able to remove this (and use only alpha) if using transposition table to look up PV
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			int val = abMin(board, depth - 1, alpha, beta, move.givesCheck()).getValue(); //call abMin with new board state, lower depth, and different color
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
		
		return bestMove; //should i return best move val or alpha? and if alpha, should i start best move with the alpha value? - IT DOESNT MATTER. - Only matters for transposition table, which I will keep as is, because fail soft should be better at pruning.
	}
	
	private MoveAndValue abMin(Board board, int depth, int alpha, int beta, boolean inCheck) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		if (depth == 0 || moves.isEmpty()) {
			MoveAndValue mv = new MoveAndValue(null, 0);
			mv.setValue(Heuristics.evaluate(board, depth, inCheck, moves.size()));
			return mv;
		}
		
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MAX_VALUE);
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			int val = abMax(board, depth - 1, alpha, beta, move.givesCheck()).getValue(); //call abMax with new board state, lower depth, and different color
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
