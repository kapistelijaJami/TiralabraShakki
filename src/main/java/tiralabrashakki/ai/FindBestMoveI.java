package tiralabrashakki.ai;

import java.util.ArrayList;
import tiralabrashakki.Board;
import tiralabrashakki.Move;

public interface FindBestMoveI {
	public Move findBestMove(Board board, int maxDepth, boolean ponder);
	public int getCurrentEval();
	public void setTimedOut(boolean b);
	public ArrayList<Move> getPV();
}
