package tiralabrashakki.ai;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.ChessGame;
import tiralabrashakki.Constants;
import static tiralabrashakki.Constants.VALUE_UNKNOWN;
import tiralabrashakki.Location;
import tiralabrashakki.Move;
import tiralabrashakki.PlayerColor;
import static tiralabrashakki.possibleMoves.MoveCategory.LEGAL;
import tiralabrashakki.possibleMoves.PossibleMoves;
import tiralabrashakki.possibleMoves.SquareSafety;

public class AlphaBeta {
	private int maxSearchDepth = 0;
	
	/**
	 * Finds the best move to given depth using minimax with AlphaBeta pruning.
	 * @param board State of the board
	 * @param maxDepth Search depth
	 * @return The best move
	 */
	public Move findBestMove(Board board, int maxDepth) {
		long timeOrig = System.currentTimeMillis();
		
		int aspWindow = 25;

		int aspirationLowFails = 0;
		int aspirationHighFails = 0;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		
		boolean printing = true;
		
		long time = System.currentTimeMillis();
		
		MoveAndValue mv = new MoveAndValue(null, 0);
		for (int depth = 1; depth <= maxDepth; ) {
			if (board.getTurnColor().isWhite()) {
				mv = abMax(board, depth, alpha, beta, SquareSafety.isKingSafe(board, PlayerColor.WHITE), 0);
			} else {
				mv = abMin(board, depth, alpha, beta, SquareSafety.isKingSafe(board, PlayerColor.BLACK), 0);
			}
			
			if (timedOut())
				break;
			
			if (ChessGame.ASPIRATION_WINDOW) {
				if (mv.getValue() <= alpha) {
					aspirationLowFails++;
					alpha -= aspWindow * Math.pow(3, aspirationLowFails);
					continue;
				} else if (mv.getValue() >= beta) {
					aspirationHighFails++;
					beta += aspWindow * Math.pow(3, aspirationHighFails);
					continue;
				}
				
				aspirationLowFails = 0;
				aspirationHighFails = 0;
				alpha = mv.getValue() - aspWindow;
				beta = mv.getValue() + aspWindow;
			}
			
			mv.setMove(mv.getMove() == null ? ChessGame.TT.probe(board, 0, alpha, beta).getMove(board) : mv.getMove());
			
			if (printing) {
				System.out.print("Depth: " + depth);
				System.out.print("\tMove: " + mv.getMove() + " val: " + mv.getValue());

				System.out.println("\ttook: " + (System.currentTimeMillis() - time) + " ms");
				time = System.currentTimeMillis();
			}
			/*if (winningMove()) {
				break;
			}*/
			
			
			depth++;
		}
		
		System.out.println();
		System.out.println("All together took: " + (System.currentTimeMillis() - timeOrig) + " ms");
		return mv.getMove();
	}
	
	private MoveAndValue abMax(Board board, int depth, int alpha, int beta, boolean inCheck, int searchDepth) {
		maxSearchDepth = Math.max(maxSearchDepth, searchDepth);
		
		TranspositionData data = ChessGame.TT.probe(board, depth, alpha, beta);
		Move hashMove = null;
		if (data != null) {
			hashMove = data.getMove(board);
			
			if (data.valueIsKnown()) {
				return new MoveAndValue(null, data.value);
			}
		}
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, LEGAL);
		if (hashMove != null) {
			sortHashMove(moves, hashMove);
		}
		
		if (depth == 0 || moves.isEmpty()) {
			MoveAndValue mv = new MoveAndValue(null, 0);
			mv.setValue(Heuristics.evaluate(board, depth, inCheck, moves.size()));
			ChessGame.TT.recordHash(board, null, mv.getValue(), depth, HashFlag.HASH_EXACT);
			return mv;
		}
		
		HashFlag hashFlag = HashFlag.HASH_ALPHA;
		
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MIN_VALUE); //might be able to remove this (and use only alpha) if using transposition table to look up PV
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			int val = abMin(board, depth - 1, alpha, beta, move.givesCheck(), searchDepth + 1).getValue(); //call abMin with new board state, lower depth, and different color
			board.unmakeMove(move);
			
			if (val > bestMove.getValue()) { //update best move if the new value is greater
				bestMove.setValue(val);
				bestMove.setMove(move);
				
				if (val > alpha) { //update alpha value
					alpha = val;
					hashFlag = HashFlag.HASH_EXACT;
					if (beta <= alpha) { //prune if alpha was greater than or equal to beta
						ChessGame.TT.recordHash(board, bestMove.getMove(), bestMove.getValue(), depth, HashFlag.HASH_BETA);
						return bestMove;
					}
				}
			}
		}
		
		ChessGame.TT.recordHash(board, bestMove.getMove(), bestMove.getValue(), depth, hashFlag);
		
		return bestMove; //should i return best move val or alpha? and if alpha, should i start best move with the alpha value? - IT DOESNT MATTER. - Only matters for transposition table, which I will keep as is, because fail soft should be better at pruning.
	}
	
	private MoveAndValue abMin(Board board, int depth, int alpha, int beta, boolean inCheck, int searchDepth) {
		maxSearchDepth = Math.max(maxSearchDepth, searchDepth);
		
		TranspositionData data = ChessGame.TT.probe(board, depth, alpha, beta);
		Move hashMove = null;
		if (data != null) {
			hashMove = data.getMove(board);
			
			if (data.valueIsKnown()) {
				return new MoveAndValue(null, data.value);
			}
		}
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, LEGAL);
		if (hashMove != null) {
			sortHashMove(moves, hashMove);
		}
		
		if (depth == 0 || moves.isEmpty()) {
			MoveAndValue mv = new MoveAndValue(null, 0);
			mv.setValue(Heuristics.evaluate(board, depth, inCheck, moves.size()));
			ChessGame.TT.recordHash(board, null, mv.getValue(), depth, HashFlag.HASH_EXACT);
			return mv;
		}
		
		HashFlag hashFlag = HashFlag.HASH_ALPHA;
		
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MAX_VALUE);
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			int val = abMax(board, depth - 1, alpha, beta, move.givesCheck(), searchDepth + 1).getValue(); //call abMax with new board state, lower depth, and different color
			board.unmakeMove(move);
			
			if (val < bestMove.getValue()) { //update best move if the new value is lower
				bestMove.setValue(val);
				bestMove.setMove(move);
				
				if (val < beta) { //update beta value
					beta = val;
					hashFlag = HashFlag.HASH_EXACT;
					if (beta <= alpha) { //prune if alpha was greater than or equal to beta
						ChessGame.TT.recordHash(board, bestMove.getMove(), bestMove.getValue(), depth, HashFlag.HASH_BETA);
						return bestMove;
					}
				}
			}
		}
		
		ChessGame.TT.recordHash(board, bestMove.getMove(), bestMove.getValue(), depth, hashFlag);
		
		return bestMove;
	}
	
	private void sortHashMove(ArrayList<Move> moves, Move hashMove) {
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			if (move.equals(hashMove)) {
				moves.set(i, moves.get(0));
				moves.set(0, move);
				break;
			}
		}
	}
	
	private boolean timedOut() {
		return false;
	}
}
