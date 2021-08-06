package tiralabrashakki.ai;

import tiralabrashakki.Move;

public class TranspositionData {
	public Move bestMove; //TODO: could be only the coordinates instead of all the data if it takes too much space.
	public int value;
	public int depth;
}
