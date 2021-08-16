package tiralabrashakki.ai;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import tiralabrashakki.Board;
import tiralabrashakki.Move;
import tiralabrashakki.possibleMoves.MoveCategory;
import tiralabrashakki.possibleMoves.PossibleMoves;

public class FindBestMoveWorker implements Runnable {
	private final int depth;
	private final Board board;
	private final FindBestMoveI alphabeta;
	private final Consumer<Move> makeMoveFunction;
	private boolean ponder = false;
	private boolean canceled = false;
	public boolean running = false;
	
	public FindBestMoveWorker(Board board, int depth, FindBestMoveI alphabeta, boolean ponder, Consumer<Move> makeMoveFunction) {
		this.board = board.copy();
		this.depth = depth;
		this.alphabeta = alphabeta;
		this.makeMoveFunction = makeMoveFunction;
		this.ponder = ponder;
	}
	
	public synchronized void start() {
		new Thread(this).start();
	}
	
	public void cancel() {
		System.out.println("canceled");
		canceled = true;
		alphabeta.setTimedOut(true);
	}
	
	public void makeMoveInstantly() {
		alphabeta.setTimedOut(true);
	}
	
	@Override
	public void run() {
		if (PossibleMoves.getPossibleMoves(board, MoveCategory.LEGAL).isEmpty()) {
			return;
		}
		
		running = true;
		
		Move move = alphabeta.findBestMove(board, depth, ponder);
		System.out.println("returned from algo"); //TODO: remove these prints and from Game.java
		running = false;
		if (canceled) {
			return;
		}
		makeMoveFunction.accept(move);
	}
}
