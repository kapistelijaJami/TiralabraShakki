package tiralabrashakki.ai;

import tiralabrashakki.Board;
import tiralabrashakki.Move;

public interface FindBestMoveInterface {
	public Move findBestMove(Board board, int maxDepth);
}
