package tiralabrashakki.possibleMoves;

public enum MoveCategory {
	CHECK, CAPTURES, LEGAL, PSEUDO_LEGAL;
	
	public boolean generateCapture() {
		return this == LEGAL || this == PSEUDO_LEGAL || this == CAPTURES;
	}
	
	public boolean generateCheck() {
		return this == LEGAL || this == PSEUDO_LEGAL || this == CHECK;
	}
	
	public boolean generateCheckOrCapture() {
		return this == LEGAL || this == PSEUDO_LEGAL || this == CAPTURES || this == CHECK;
	}
	
	public boolean isPseudoLegal() {
		return this == PSEUDO_LEGAL;
	}
}
