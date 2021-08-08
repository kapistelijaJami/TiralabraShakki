package tiralabrashakki.ai;

public class TT {
	private TranspositionData[] TT = new TranspositionData[8000000];
	
	public void put(long hash, TranspositionData data) { //TODO: dont overwrite if different hash and it's possibly a pv move (unless you save pv somewhere else)
		int id = (int) (Math.abs(hash) % TT.length);
		
		TranspositionData prev = TT[id];
		if (prev != null && prev.hash == hash) {
			if (prev.depth > data.depth) {
				return;
			}
		}
		
		data.hash = hash;
		TT[id] = data;
	}
	
	public TranspositionData get(long hash) {
		int id = (int) (Math.abs(hash) % TT.length);
		
		TranspositionData data = TT[id];
		if (data != null && data.hash == hash) {
			return data;
		}
		return null;
	}
}
