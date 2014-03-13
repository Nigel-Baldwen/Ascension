package ascension;

import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class PrimaryController extends JFrame implements MouseListener, KeyListener {

	private GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private GraphicsDevice primaryGDev = gEnv.getDefaultScreenDevice();
	private boolean isFSSupported = primaryGDev.isFullScreenSupported();
	private PrimaryModel gameModel = new PrimaryModel();
	private PrimaryView gameView = new PrimaryView();
	private Container contentPane;
	private Timer viewUpdateController;
	private BufferStrategy bufferStrategy;
	private Graphics graphicsPlaceHolder = null;
	private int boundX, boundY;
	private boolean unitIsSelected = false;
	
	// Constructor
	public PrimaryController() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	    addMouseListener(this);
	    addKeyListener(this);
	    setEnabled(false);
	    contentPane = getContentPane();
	}

	public void loadInitialGameState() {
		gameModel.loadInitialModelState(50, 4);
		gameView.loadInitialViewState(getGraphicsConfiguration(), 50);
		boundX = getGraphicsConfiguration().getBounds().width;
		boundY = getGraphicsConfiguration().getBounds().height;
		contentPane.add(gameView);
	}

	public void startGame() {
		setUndecorated(isFSSupported);
        setResizable(!isFSSupported);
        if (isFSSupported) {
            // Full-screen mode
        	setIgnoreRepaint(isFSSupported);
        	setUndecorated(isFSSupported);
            primaryGDev.setFullScreenWindow(this);
            validate();
            int refreshHertz = primaryGDev.getDisplayMode().getRefreshRate();
            if(refreshHertz == DisplayMode.REFRESH_RATE_UNKNOWN) {
            	refreshHertz = 30;
            }
            viewUpdateController = new Timer(1000/refreshHertz, new ActionListener() {
				
            	// Generally handles scrolling and other time or frame dependent
            	// Updates. For example, rendering the graphics.
				@Override
				public void actionPerformed(ActionEvent arg0) {
					gameView.updateClock(gameModel.getClockFace());
					render(graphicsPlaceHolder);
					
					int x = MouseInfo.getPointerInfo().getLocation().x, y = MouseInfo
							.getPointerInfo().getLocation().y;

					if (x < 5 && y < 5) {
						gameView.scrollNorthWest();

					}

					if (x < 5 && y > 5 && y < boundY - 5) {
						gameView.scrollWest();
					}

					if (x < 5 && y > boundY - 5) {
						gameView.scrollSouthWest();
					}

					if (x > 5 && x < boundX - 5
							&& y > boundY - 5) {
						gameView.scrollSouth();
					}

					if (x > boundX - 5 && y > boundY - 5) {
						gameView.scrollSouthEast();
					}

					if (x > boundX - 5 && y > 5
							&& y < boundY - 5) {
						gameView.scrollEast();
					}

					if (x > boundX - 5 && y < 5) {
						gameView.scrollNorthEast();
					}

					if (x > 5 && x < boundX - 5 && y < 5) {
						gameView.scrollNorth();
					}
				}
			});
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
            viewUpdateController.start();
        } else {
            // Windowed mode
            pack();
            setVisible(true);
        }
		setEnabled(true);
	}

	protected void render(Graphics g) {
		try {
            g = bufferStrategy.getDrawGraphics();
            // Calling Primary View render(Graphics g, int[][] gameState)
            gameView.render(g, gameModel.getVisualModel());
        } finally {
        	// Prudent to free system resources when finished.
            g.dispose();
        }
        bufferStrategy.show();
	}

	// Intended for future development
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Preferable to segment usage into pressed and
		// released actions.
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Do Nothing. This makes no sense in a full-screen window.
		// You cannot exit or enter.
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Do Nothing. This makes no sense in a full-screen window.
		// You cannot exit or enter.
	}

	// Translates MouseEvent coordinates into meaningful
	// gameModel coordinates in order to set a focus target for the
	// view
	@Override
	public void mousePressed(MouseEvent arg0) {
		int x = arg0.getLocationOnScreen().x, y = arg0.getLocationOnScreen().y;
		if (y < 864) {
			int c = gameView.getVisX(), r = gameView.getVisY(),
					idTag = gameModel.getVisualModel()[((r + y) / 64)][((c + x) / 64)];
			if (idTag != 0) {
				unitIsSelected = true;
				gameView.setFocusTarget(((c + x) / 64), ((r + y) / 64), InformationIndex.getMovementRadius(idTag), InformationIndex.getName(idTag));
			}
		}
	}

	// Completes the actions initiated in mousePressed
	@Override
	public void mouseReleased(MouseEvent arg0) {
		int x = arg0.getLocationOnScreen().x, y = arg0.getLocationOnScreen().y;
		if (unitIsSelected) {
			if (y < 864) {
				int c = gameView.getVisX(), r = gameView.getVisY(),
						idTag = gameModel.getVisualModel()[((r + y) / 64)][((c + x) / 64)];
				unitIsSelected = false;
				
				if (idTag == 0) {
					Point p = gameView.getFocusTarget();
					gameView.clearFocusTarget();
					gameModel.transferUnit(p.x, p.y, ((r + y) / 64), ((c + x) / 64));
				}
			}
		}
	}
}
