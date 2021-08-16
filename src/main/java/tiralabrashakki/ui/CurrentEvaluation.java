package tiralabrashakki.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import tiralabrashakki.ai.Heuristics;

public class CurrentEvaluation {
	private final int width;
	private final int height;
	private int currentEval = 0;
	private int currentShowingEval = 0;
	private final int moveSpeed = 7;
	
	public CurrentEvaluation(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void render(Graphics2D g, int x, int y) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		int padding = 5;
		int fullHeight = height - padding * 2;
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x + padding, y + padding, width - padding * 2, fullHeight);
		
		g.setColor(Color.WHITE);
		int matePadding = 10;
		if (Heuristics.isWinningMove(currentEval) || Heuristics.isLosingMove(currentEval)) {
			matePadding = 0;
		}
		int h = (int) (getWhiteMultiplier() * (fullHeight));
		h = clamp(h, matePadding, fullHeight - matePadding);
		
		g.fillRect(x + padding, y + padding + (fullHeight - h), width - padding * 2, h);
		
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		g.drawLine(x + 5, y + padding + fullHeight / 2, x + width - 5, y + padding + fullHeight / 2);
	}
	
	public void update() {
		int diff = currentEval - currentShowingEval;
		double multiplier = clamp(Math.abs(diff) / 100.0, 1, 5);
		
		if (diff > 0) {
			currentShowingEval += Math.min(diff, moveSpeed * multiplier);
		} else if (diff < 0) {
			currentShowingEval -= Math.min(Math.abs(diff), moveSpeed * multiplier);
		}
		
		currentShowingEval = clamp(currentShowingEval, -1000, 1000);
	}
	
	public void setEval(int eval) {
		currentEval = eval;
	}

	public int getWidth() {
		return width;
	}

	private double getWhiteMultiplier() {
		return (currentShowingEval + 1000) / 2000.0; //eval -1000 - 1000
	}
	
	public static int clamp(int val, int min, int max) {
		return Math.min(max, Math.max(min, val));
	}
	
	public static double clamp(double val, double min, double max) {
		return Math.min(max, Math.max(min, val));
	}
}
