package tiralabrashakki.ai;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.possibleMoves.MoveCategory;
import static tiralabrashakki.possibleMoves.MoveCategory.LEGAL;
import static tiralabrashakki.possibleMoves.MoveCategory.PSEUDO_LEGAL;
import tiralabrashakki.possibleMoves.PossibleMoves;
import tiralabrashakki.possibleMoves.SquareSafety;

public class Perft implements FindBestMoveI {
	private int leafNodes = 0;
	private int checks = 0;
	private int captures = 0;
	private int mates = 0;
	
	@Override
	public Move findBestMove(Board board, int maxDepth) {
		MoveAndValue mv = new MoveAndValue(null, 0);
		for (int depth = 1; depth <= maxDepth; depth++) {
			leafNodes = 0;
			checks = 0;
			captures = 0;
			mates = 0;
			mv = negamax(board, depth, SquareSafety.isKingSafe(board, board.getTurnColor()), false);
			printDepth(depth, board, mv);
		}
		return mv.getMove();
	}
	
	public int countLeafNodes(Board board, int depth) {
		leafNodes = 0;
		checks = 0;
		captures = 0;
		mates = 0;
		MoveAndValue mv = negamax(board, depth, SquareSafety.isKingSafe(board, board.getTurnColor()), false);
		printDepth(depth, board, mv);
		return leafNodes;
	}
	
	public int doPerft(Board board, int depth) {
		if (depth == 0) {
			return 1;
		}
		int nodes = 0;
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, PSEUDO_LEGAL);
		
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.makeMove(move);
			if (!SquareSafety.isKingSafe(board, board.getTurnColor().opposite())) {
				board.unmakeMove(move);
				continue;
			}
			nodes += doPerft(board, depth - 1);
			board.unmakeMove(move);
		}
		return nodes;
	}
	
	public void dividePerft(Board board, int depth) {
		int total = 0;
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, PSEUDO_LEGAL);
		for (Move move : moves) {
			board.makeMove(move);
			if (!SquareSafety.isKingSafe(board, board.getTurnColor().opposite())) {
				board.unmakeMove(move);
				continue;
			}
			int res = doPerft(board, depth - 1);
			total += res;
			System.out.println(move + " \t" + res);
			board.unmakeMove(move);
		}
		
		System.out.println("Total: " + total);
	}

	private MoveAndValue negamax(Board board, int depth, boolean inCheck, boolean captured) {
		ArrayList<Move> moves = PossibleMoves.getPossibleMoves(board, LEGAL);
		
		if (depth <= 0 || moves.isEmpty()) {
			if (depth == 0) {
				if (moves.isEmpty()) {
					mates++;
				}
				if (inCheck) {
					checks++;
				}
				if (captured) {
					captures++;
				}
				leafNodes++;
			}
			int val = Heuristics.evaluate(board, depth, inCheck, moves.size()) * (board.getTurnColor().isWhite() ? 1 : -1);
			return new MoveAndValue(null, val);
		}
		
		MoveAndValue best = new MoveAndValue(null, Integer.MIN_VALUE + 1);
		
		for (Move move : moves) {
			board.makeMove(move);
			int val = -negamax(board, depth - 1, move.givesCheck(), move.isCapture()).getValue();
			board.unmakeMove(move);
			
			if (val > best.getValue()) {
				best.setMove(move);
				best.setValue(val);
			}
		}
		
		return best;
	}

	private void printDepth(int depth, Board board, MoveAndValue mv) {
		System.out.print("Depth: " + depth);
		
		String print = "\tMove: " + mv.getMove().toString() + "\tvalue: " + mv.getValue();
		System.out.print(print);
		
		System.out.println("\t nodes: " + leafNodes + "\tchecks: " + checks + "\tcaptures: " + captures + "\tmates: " + mates);
	}
}
