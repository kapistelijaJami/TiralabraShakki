package tiralabrashakki.ai;

import tiralabrashakki.Move;

public class MoveAndValue {
	private Move move;
	private int value;
	
	public MoveAndValue(Move move, int value) {
		this.move = move;
		this.value = value;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
