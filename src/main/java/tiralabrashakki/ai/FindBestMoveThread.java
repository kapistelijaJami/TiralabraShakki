package tiralabrashakki.ai;

import java.util.function.Consumer;
import tiralabrashakki.Board;
import tiralabrashakki.Move;

public class FindBestMoveThread implements Runnable {
	private final int depth;
	private final Board board;
	private final AlphaBeta2 alphabeta;
	private final Consumer<Move> makeMoveFunction;
	
	public FindBestMoveThread(Board board, int depth, AlphaBeta2 alphabeta, Consumer<Move> makeMoveFunction) {
		this.board = board.copy();
		this.depth = depth;
		this.alphabeta = alphabeta;
		this.makeMoveFunction = makeMoveFunction;
	}
	
	public synchronized void start() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		Move move = alphabeta.findBestMove(board, depth);
		makeMoveFunction.accept(move);
		//System.out.println("Best move was: " + move);
	}
}
