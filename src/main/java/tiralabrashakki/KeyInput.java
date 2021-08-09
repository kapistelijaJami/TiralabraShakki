package tiralabrashakki;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class KeyInput extends KeyAdapter implements MouseListener, MouseMotionListener {
	private Game game;
	
	public KeyInput(Game game) {
		this.game = game;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_ESCAPE:
				game.stop();
				break;
			case KeyEvent.VK_ENTER:
				game.makeBestMove();
				break;
			case KeyEvent.VK_SPACE:
				game.printBestMove();
				break;
		}
		
		if (key > KeyEvent.VK_0 && key <= KeyEvent.VK_9) {
			int num = key - KeyEvent.VK_0;
			game.setDepth(num);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		game.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//game.mouseMoved(e);
	}
}