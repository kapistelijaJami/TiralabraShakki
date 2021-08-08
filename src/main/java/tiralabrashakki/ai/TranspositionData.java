package tiralabrashakki.ai;

import tiralabrashakki.Board;
import static tiralabrashakki.Constants.VALUE_UNKNOWN;
import tiralabrashakki.Move;
import static tiralabrashakki.ai.HashFlag.HASH_VALUE_UNKNOWN;

public class TranspositionData {
	public long hash;
	public byte startX = -1, startY = -1;
	public byte destX = -1, destY = -1;
	public int value;
	public byte depth;
	public HashFlag flag;
	
	public TranspositionData(byte startX, byte startY, byte destX, byte destY, int value, byte depth) {
		this.startX = startX;
		this.startY = startY;
		this.destX = destX;
		this.destY = destY;
		this.value = value;
		this.depth = depth;
		this.flag = HASH_VALUE_UNKNOWN;
	}
	
	public TranspositionData(Move bestMove, int value, int depth, HashFlag flag) {
		if (bestMove != null) {
			this.startX = (byte) bestMove.getStart().getX();
			this.startY = (byte) bestMove.getStart().getY();
			this.destX = (byte) bestMove.getDest().getX();
			this.destY = (byte) bestMove.getDest().getY();
		}
		this.value = value;
		this.depth = (byte) depth;
		this.flag = flag;
	}
	
	public Move getMove(Board board) {
		if (startX != -1) {
			return Move.createMove(board, startX, startY, destX, destY);
		}
		return null;
	}
	
	public boolean valueIsKnown() {
		return value != VALUE_UNKNOWN;
	}
}
