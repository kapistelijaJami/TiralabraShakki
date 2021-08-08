package tiralabrashakki.ai;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.ChessGame;
import tiralabrashakki.Move;
import tiralabrashakki.possibleMoves.PossibleMoves;
import tiralabrashakki.possibleMoves.SquareSafety;

public class AlphaBeta2 {
	public int maxSearchDepth = 0;
	
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
		int alpha = Integer.MIN_VALUE + 1; //dont do min value when negamax, because when * -1 it doesnt change sign
		int beta = Integer.MAX_VALUE;
		
		boolean printing = true;
		
		long time = System.currentTimeMillis();
		
		int val = 0;
		for (int depth = 1; depth <= maxDepth; ) {
			ChessGame.nodes = 0;
			maxSearchDepth = 0;
			val = negamax(board, depth, alpha, beta, SquareSafety.isKingSafe(board, board.getTurnColor()), 0);
			
			if (timedOut())
				break;
			
			if (ChessGame.ASPIRATION_WINDOW) {
				if (val <= alpha) {
					aspirationLowFails++;
					alpha -= aspWindow * Math.pow(3, aspirationLowFails);
					continue;
				} else if (val >= beta) {
					aspirationHighFails++;
					beta += aspWindow * Math.pow(3, aspirationHighFails);
					continue;
				}
				
				aspirationLowFails = 0;
				aspirationHighFails = 0;
				alpha = val - aspWindow;
				beta = val + aspWindow;
			}
			
			if (printing) {
				printDepth(depth, ChessGame.TT.probe(board, depth, alpha, beta), board, val, System.currentTimeMillis() - time);
			}
			time = System.currentTimeMillis();
			/*if (winningMove()) {
				break;
			}*/
			
			
			depth++;
		}
		
		System.out.println();
		System.out.println("All together took: " + (System.currentTimeMillis() - timeOrig) + " ms val: " + val);
		if (ChessGame.TRANSPOSITION_TABLE) {
			return ChessGame.TT.probe(board, 0, alpha, beta).getMove(board);
		}
		
		return null;
	}
	
	private int negamax(Board board, int depth, int alpha, int beta, boolean inCheck, int searchDepth) {
		ChessGame.nodes++;
		maxSearchDepth = Math.max(maxSearchDepth, searchDepth);
		
		TranspositionData data = ChessGame.TT.probe(board, depth, alpha, beta);
		Move hashMove = null;
		if (data != null) {
			hashMove = data.getMove(board);
			
			if (data.valueIsKnown()) {
				return data.value;
			}
		}
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board);
		
		
		if (depth == 0 || moves.isEmpty()) {
			int val = Heuristics.evaluate(board, depth, inCheck, moves.size()) * (board.getTurnColor().isWhite() ? 1 : -1);
			ChessGame.TT.recordHash(board, null, val, depth, HashFlag.HASH_EXACT);
			return val;
		}
		
		if (hashMove != null) {
			sortHashMove(moves, hashMove);
		}
		
		HashFlag hashFlag = HashFlag.HASH_ALPHA;
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MIN_VALUE);
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			int val = -negamax(board, depth - 1, -beta, -alpha, move.givesCheck(), searchDepth + 1);
			board.unmakeMove(move);
			
			if (val > bestMove.getValue()) {
				bestMove.setMove(move);
				bestMove.setValue(val);
			}
			
			if (val >= beta) { //prune if alpha was greater than or equal to beta
				ChessGame.TT.recordHash(board, move, beta, depth, HashFlag.HASH_BETA);
				if (ChessGame.ALPHABETA_PRUNING) {
					return beta;
				}
			}
			
			if (val > alpha) { //update alpha value
				alpha = val;
				hashFlag = HashFlag.HASH_EXACT;
				
			}
		}
		
		ChessGame.TT.recordHash(board, bestMove.getMove(), alpha, depth, hashFlag);
		return alpha;
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
	
	private void printDepth(int depth, TranspositionData data, Board board, int value, long timeTook) {
		System.out.print("Depth: " + depth);
		
		String print = "\t" + (data != null ? " Move: " + data.getMove(board).toString() : "") + " value: " + value;
		System.out.print(print);
		
		System.out.println("\ttook: " + timeTook + " ms (max search depth: " + maxSearchDepth + ")\t nodes: " + ChessGame.nodes);
	}
}
