package tiralabrashakki.ai;

import tiralabrashakki.Board;
import tiralabrashakki.Move;

public interface FindBestMoveI {
	public Move findBestMove(Board board, int maxDepth);
}
