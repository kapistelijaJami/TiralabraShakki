package tiralabrashakki;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static tiralabrashakki.Constants.BOARD_SIZE;
import tiralabrashakki.ai.AlphaBeta2;
import tiralabrashakki.ai.FindBestMoveThread;
import static tiralabrashakki.possibleMoves.MoveCategory.LEGAL;
import tiralabrashakki.possibleMoves.PossibleMoves;

public class Game extends Canvas implements Runnable {
	public static int WIDTH;
	public static int HEIGHT;
	public static final double FPS = 60.0;
	
	private boolean running = false;
	private Thread thread;
	private final Window window;
	
	private final Board board;
	private final AlphaBeta2 alphabeta;
	
	private int fullBoardSize;
	private int squareSize;
	
	private Point highlightedSquare = null;
	private ArrayList<Move> currentPossibleMoves;
	
	private boolean isThinking = false;
	private int depth = 6;
	private ArrayList<Move> allMoves;
	private final BufferedImage chessPieces;
	
	public Game() {
		window = new Window(Constants.WIDTH, Constants.HEIGHT, "Drill and Defend", this);
		alphabeta = new AlphaBeta2();
		board = new Board();
		currentPossibleMoves = PossibleMoves.getPossibleMoves(board, LEGAL);
		
		allMoves = new ArrayList<>();
		chessPieces = ImageLoader.loadImage("/images/chessPieces.png");
	}
	
	public synchronized void start() {
		if (running) {
			return;
		}
		
		running = true;
		
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	private void init() {
		KeyInput input = new KeyInput(this);
		this.addMouseListener(input);
		this.addKeyListener(input);
		this.addMouseMotionListener(input);
		
		setBoardSizes();
	}
	
	public void setBoardSizes() {
		int margin = 20;
		fullBoardSize = Math.min(WIDTH - margin * 2, HEIGHT - margin * 2);
		squareSize = fullBoardSize / BOARD_SIZE;
		fullBoardSize = squareSize * BOARD_SIZE;
	}
	
	@Override
	public void run() {
		init();
		this.requestFocus();
		
		long now = System.nanoTime();
		long nsBetweenUpdates = (long) (1e9 / FPS);
		
		while (running) {
			if (now + nsBetweenUpdates <= System.nanoTime()) {
				now += nsBetweenUpdates;
				update();
				render();
			}
		}
		
		System.exit(0);
	}
	
	public void printBestMove() {
		if (isThinking) {
			return;
		}
		isThinking = true;
		new FindBestMoveThread(board, depth, alphabeta, this::printMove).start();
	}
	
	public void makeBestMove() {
		if (isThinking) {
			return;
		}
		isThinking = true;
		new FindBestMoveThread(board, depth, alphabeta, this::makeMove).start();
	}
	
	public void playFullGame() {
		if (isThinking) {
			return;
		}
		isThinking = true;
		new FindBestMoveThread(board, depth, alphabeta, this::keepMakingMoves).start();
	}
	
	public void printMove(Move move) {
		System.out.println("Move was: " + move);
		isThinking = false;
	}
	
	public void makeMove(Move move) {
		printMove(move);
		board.makeMove(move);
		currentPossibleMoves = PossibleMoves.getPossibleMoves(board, LEGAL);
		isThinking = false;
		allMoves.add(move);
	}
	
	public void undoMove() {
		board.unmakeMove(allMoves.remove(allMoves.size() - 1));
		currentPossibleMoves = PossibleMoves.getPossibleMoves(board, LEGAL);
	}
	
	public void keepMakingMoves(Move move) {
		makeMove(move);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		
		if (!PossibleMoves.getPossibleMoves(board, LEGAL).isEmpty()) {
			playFullGame();
		}
	}
	
	public void update() {
		
	}
	
	public void render() {
		Graphics2D g = getGraphics2D();
		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		renderBoard(g);
		
		g.dispose();
		this.getBufferStrategy().show();
	}
	
	public Graphics2D getGraphics2D() {
		BufferStrategy bs = this.getBufferStrategy();
		while (bs == null) {
			createBufferStrategy(3);
			bs = this.getBufferStrategy();
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		setGraphicsRenderingHints(g);
		return g;
	}
	
	public static void setGraphicsRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); //for image scaling to look sharp, might not be good for all images
	}
	
	public Point getBoardOffset() {
		int xOffset = WIDTH / 2 - fullBoardSize / 2;
		int yOffset = HEIGHT / 2 - fullBoardSize / 2;
		return new Point(xOffset, yOffset);
	}
	
	public void renderBoard(Graphics2D g) {
		Point offset = getBoardOffset();
		
		g.setColor(Color.BLACK);
		g.fillRect(offset.x - 5, offset.y - 5, fullBoardSize + 10, fullBoardSize + 10);
		
		
		int counter = 0;
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				g.setColor(counter % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
				
				if (highlightedSquare != null && highlightedSquare.equals(new Point(x, y))) {
					g.setColor(Color.YELLOW);
				}
				
				g.fillRect(x * squareSize + offset.x, y * squareSize + offset.y, squareSize, squareSize);
				
				if (!allMoves.isEmpty()
						&& (getLastMove().getStart().equals(new Location(x, y))
						|| getLastMove().getDest().equals(new Location(x, y)))) {
					//g.setColor(new Color(255, 241, 120, 150));
					g.setColor(new Color(208, 219, 103, 200));
					g.fillRect(x * squareSize + offset.x, y * squareSize + offset.y, squareSize, squareSize);
				}
				
				boolean drawDot = false;
				if (squareIsPossible(x, y)) {
					drawDot = true;
				}
				
				char piece = board.get(x, y);
				if (piece == ' ') {
					if (drawDot) {
						g.setColor(new Color(153, 76, 0, 200));
						g.fillOval(x * squareSize + offset.x + squareSize / 3, y * squareSize + offset.y + squareSize / 3, squareSize / 3, squareSize / 3);
					}
					
					counter++;
					continue;
				}
				
				int h = 0;
				if (PlayerColor.BLACK.isMyPiece(piece)) {
					h = 1;
				}
				
				int n = ChessGame.TT.getPieceNumberByChar(piece) % 6;
				
				BufferedImage pieceImg = ImageLoader.getSprite(chessPieces, n, h, chessPieces.getWidth() / 6, chessPieces.getHeight() / 2);
				
				g.drawImage(pieceImg, x * squareSize + offset.x, y * squareSize + offset.y, squareSize, squareSize, null);
				
				
				if (drawDot) {
					g.setColor(new Color(153, 76, 0, 200));
					g.fillOval(x * squareSize + offset.x + squareSize / 3, y * squareSize + offset.y + squareSize / 3, squareSize / 3, squareSize / 3);
				}
				counter++;
			}
			counter--;
		}
	}
	
	private boolean squareIsPossible(int x, int y) {
		return highlightedSquare != null && currentPossibleMoves.contains(createMoveFromHighlight(new Point(x, y)));
	}
	
	public Move getLastMove() {
		return allMoves.get(allMoves.size() -1);
	}
	
	public boolean isInsideBoard(int x, int y) {
		Point boardOffset = getBoardOffset();
		return x >= boardOffset.x && x < boardOffset.x + fullBoardSize
				&& y >= boardOffset.y && y < boardOffset.y + fullBoardSize;
	}
	
	public Point getSquare(int x, int y) {
		Point boardOffset = getBoardOffset();
		x -= boardOffset.x;
		y -= boardOffset.y;
		
		return new Point(x / squareSize, y / squareSize);
	}
	
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Point p = getSquare(x, y);
		
		if (highlightedSquare == null && isInsideBoard(x, y)) {
			if (board.get(p.x, p.y) != ' ') {
				highlightedSquare = p;
			}
			
		} else if (highlightedSquare != null && isInsideBoard(x, y)) {
			
			Move move = createMoveFromHighlight(getSquare(x, y));
			if (PossibleMoves.isPossibleMove(board, move)) {
				makeMove(move);
				highlightedSquare = null;
			} else if (board.get(p.x, p.y) != ' ' && !highlightedSquare.equals(p)) {
				highlightedSquare = p;
			} else {
				highlightedSquare = null;
			}
		} else {
			highlightedSquare = null;
		}
	}
	
	private Move createMoveFromHighlight(Point target) {
		if (highlightedSquare == null) {
			return null;
		}
		return Move.createMove(board, highlightedSquare.x, highlightedSquare.y, target.x, target.y);
	}

	public void setDepth(int depth) {
		System.out.println("Depth set to: " + depth);
		this.depth = depth;
	}

	public void hover(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (!isInsideBoard(x, y)) {
			window.getFrame().getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			return;
		}
		Point p = getSquare(x, y);
		if (board.get(p.x, p.y) == ' ' && !squareIsPossible(p.x, p.y)) {
			window.getFrame().getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else {
			window.getFrame().getContentPane().setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	public Board getBoard() {
		return board;
	}
}
