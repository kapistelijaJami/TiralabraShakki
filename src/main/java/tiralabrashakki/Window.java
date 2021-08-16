package tiralabrashakki;

import tiralabrashakki.ui.Game;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Window {
	private JFrame frame;

	public Window(int width, int height, String title, Game game) {
		frame = new JFrame(title);
		
		frame.addWindowListener(new WindowAdapter() { //window closes with X
			@Override
			public void windowClosing(WindowEvent e) {
				game.stop();
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				//minimized
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				//back from minimized
			}
		});
		
		frame.setPreferredSize(new Dimension(width, height));	//set window size
		frame.setMinimumSize(new Dimension(width, height));
		//frame.setMaximumSize(new Dimension(width, height));
		
		//frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //window closes with X
		frame.setResizable(true);								//cant be stretched
		frame.setLocationRelativeTo(null);						//window spawns in the middle of the screen
		frame.add(game);										//add game to the frame
		frame.pack();											//makes the window fit the preferred size
		frame.setVisible(true);
		
		Game.WIDTH = width;
		Game.HEIGHT = height;
		
		Insets inset = frame.getInsets();
		Game.WIDTH -= inset.left + inset.right;
		Game.HEIGHT -= inset.top + inset.bottom;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public void setTitle(String string) {
		frame.setTitle(string);
	}
}
