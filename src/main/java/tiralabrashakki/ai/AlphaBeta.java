package tiralabrashakki.ai;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.ChessGame;
import tiralabrashakki.Constants;
import static tiralabrashakki.Constants.PAWN_VAL;
import static tiralabrashakki.Constants.QUEEN_VAL;
import tiralabrashakki.Move;
import tiralabrashakki.possibleMoves.MoveCategory;
import static tiralabrashakki.possibleMoves.MoveCategory.CAPTURES;
import static tiralabrashakki.possibleMoves.MoveCategory.LEGAL;
import static tiralabrashakki.possibleMoves.MoveCategory.PSEUDO_LEGAL;
import tiralabrashakki.possibleMoves.PossibleMoves;
import tiralabrashakki.possibleMoves.SquareSafety;

public class AlphaBeta implements FindBestMoveI {
	public int maxSearchDepth = 0;
	
	/**
	 * Finds the best move to given depth using minimax with AlphaBeta pruning.
	 * @param board State of the board
	 * @param maxDepth Search depth
	 * @return The best move
	 */
	@Override
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
			
			if (isWinningMove(val)) {
				break;
			}
			
			
			depth++;
		}
		
		System.out.println();
		System.out.println("All together took: " + (System.currentTimeMillis() - timeOrig) + " ms val: " + val);
		return ChessGame.TT.probe(board, 0, alpha, beta).getMove(board);
	}
	
	/**
	 * Main search algorithm.
	 * Searches all the moves of the node. Uses alpha-beta pruning and other ways of speeding up the search.
	 * Evaluates from the perspective of the node, always negating the result to other player pov during recursion.
	 * @param board
	 * @param depth
	 * @param alpha
	 * @param beta
	 * @param inCheck
	 * @param searchDepth
	 * @return 
	 */
	private int negamax(Board board, int depth, int alpha, int beta, boolean inCheck, int searchDepth) {
		ChessGame.nodes++;
		maxSearchDepth = Math.max(maxSearchDepth, searchDepth);
		
		//Probe the transposition table for a hash move
		TranspositionData data = ChessGame.TT.probe(board, depth, alpha, beta);
		Move hashMove = null;
		if (data != null) {
			hashMove = data.getMove(board);
			
			if (data.valueIsKnown()) {
				//return data.value; //Instant return disabled for now. It needs a little more work
			}
		}
		
		int val;
		
		//null move pruning (If I do nothing and give enemy the turn, and I'm still over beta, I can prune)
		//TODO: Should disable in endgames (because zugswangs are more common).
		//Either with board.getNbrOfPliesPlayed() (plies is fast, but what counts as an end game?), or by material.
		//Check that this is good, and doesnt mess up the result.
		if (depth > 0) {
			board.makeNullMove();
			val = -negamax(board, depth - 1 - 2, -beta, -(beta - 1), false, searchDepth + 1);
			board.unmakeNullMove();
			if (val >= beta)
				return beta;
		}
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, LEGAL);
		
		//Handle check/stalemate and return evaluation or call quiescenceSearch to reduce horizon effect
		if (inCheck && !moves.isEmpty()) {
			depth = Math.max(1, depth);
		} else if (depth <= 0 || moves.isEmpty()) {
			if (inCheck && moves.isEmpty()) { //Checkmate, evaluate instantly.
				val = Heuristics.evaluate(board, depth, inCheck, moves.size()) * (board.getTurnColor().isWhite() ? 1 : -1);
			} else {
				val = quiescenceSearch(board, depth, alpha, beta, inCheck, searchDepth);
			}
			ChessGame.TT.recordHash(board, null, val, depth, HashFlag.HASH_EXACT);
			return val;
		}
		
		if (hashMove != null) {
			sortHashMove(moves, hashMove);
		} else if (depth >= 5) { //position not in TT, decrease search depth
			depth--;
		}
		
		HashFlag hashFlag = HashFlag.HASH_ALPHA;
		MoveAndValue bestMove = new MoveAndValue(null, Integer.MIN_VALUE);
		boolean foundPV = false;
		
		//Go through all the moves
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			
			
			if (foundPV) {
				val = -negamax(board, depth - 1, -(alpha + 1), -alpha, move.givesCheck(), searchDepth + 1); //principal variation search with null window
				if (val > alpha && val < beta) {
					val = -negamax(board, depth - 1, -beta, -alpha, move.givesCheck(), searchDepth + 1); //assumption that previous pv is the best was wrong, have to re-search
				}
			} else {
				val = -negamax(board, depth - 1, -beta, -alpha, move.givesCheck(), searchDepth + 1); //normal search
			}
			board.unmakeMove(move);
			
			if (val > bestMove.getValue()) {
				bestMove.setMove(move);
				bestMove.setValue(val);
			}
			
			if (val >= beta) { //prune if val was greater than or equal to beta
				ChessGame.TT.recordHash(board, move, beta, depth, HashFlag.HASH_BETA);
				return beta;
			}
			
			if (val > alpha) { //update alpha value
				alpha = val;
				hashFlag = HashFlag.HASH_EXACT;
				foundPV = true;
			}
		}
		
		ChessGame.TT.recordHash(board, bestMove.getMove(), alpha, depth, hashFlag);
		return alpha;
	}
	
	
	/**
	 * Searches like {@link #negamax(Board, int, int, int, boolean, int) negamax}, but only good captures, and returns the
	 * evaluation when the situation is quiet in order to reduce the horizon effect.
	 * @param board
	 * @param depth
	 * @param alpha
	 * @param beta
	 * @param inCheck
	 * @param searchDepth
	 * @return 
	 */
	private int quiescenceSearch(Board board, int depth, int alpha, int beta, boolean inCheck, int searchDepth) {
		/*if (inCheck) { //not sure if this should be in or not
			return negamax(board, 1, alpha, beta, inCheck, searchDepth); //TODO: this caused a loop, needs to add to detect a mate (what about stalemates?)
		}*/
		ChessGame.nodes++;
		maxSearchDepth = Math.max(maxSearchDepth, searchDepth);
		
		//TT probe
		TranspositionData data = ChessGame.TT.probe(board, depth, alpha, beta);
		Move hashMove = null;
		if (data != null) {
			hashMove = data.getMove(board);
			
			if (data.valueIsKnown() /*&& data.flag == HashFlag.HASH_EXACT*/) { //these doesnt seem to do much, at least not from the starting position
				//return data.value;
			}
		}
		
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, CAPTURES); //TODO: change to only generating good captures (maybe add "gives check" moves, but see that it doesnt cause infinite recursion)
		
		int standPat = Heuristics.evaluate(board, depth, inCheck, moves.size()) * (board.getTurnColor().isWhite() ? 1 : -1);
		
		if (standPat >= beta) { //beta cutoff
			return beta;
		}
		if (standPat > alpha) {
			alpha = standPat; //update alpha
		}
		
		//futility pruning
		if (standPat + QUEEN_VAL + PAWN_VAL < alpha) {
			return alpha;
		}
		
		if (hashMove != null) {
			sortHashMove(moves, hashMove);
		}
		
		//Searches all the captures where material gained is at least 0. (No need to check QxP etc)
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			if (/*!move.givesCheck() && */!move.isCapture()) { //move is not a capture
				continue;
			}
			
			//if capturing loses material, skip
			if (move.moveMaterialTradeValue() < 0) {
				continue;
			}
			
			if (i > 2) {
				int takesVal = Heuristics.pieceValue(move.getTakes());

				if (standPat + takesVal + 2 * PAWN_VAL < alpha) { //delta pruning, even if value is takes + 200 and still doesnt increase alpha, then continue
					continue; //could even return if the ordering is good
				}
			}
			
			//TODO: add SEE
			
			board.makeMove(move);
			int val = -quiescenceSearch(board, depth - 1, -beta, -alpha, move.givesCheck(), searchDepth + 1); //main search
			board.unmakeMove(move);
			
			if (val >= beta) { //beta cutoff
				return beta;
			}
			if (val > alpha) {
				alpha = val; //update alpha
			}
		}
		
		return alpha;
	}
	
	/**
	 * Puts the hash move to first in order to increase branch pruning.
	 * @param moves
	 * @param hashMove 
	 */
	private void sortHashMove(ArrayList<Move> moves, Move hashMove) {
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			if (move.equals(hashMove)) {
				moves.add(0, moves.remove(i));
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

	private boolean isWinningMove(int val) {
		return Constants.CHECKMATE_VAL - 50 <= val; 
	}
}
