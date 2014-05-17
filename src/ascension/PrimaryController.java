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

/**
 * <p>
 * <code>PrimaryController</code> is the <i>controller</i> component
 * of the <b>Ascension</b> project. (Model-View-Controller paradigm)
 * </p>
 * 
 * <p>
 * <code>PrimaryController</code> is responsible for intercepting user input
 * as well as coordinating between the <i>model</i> and <i>view</i> components.
 * </p>
 * 
 * @author Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version 1.0
 */

public class PrimaryController extends JFrame implements MouseListener, KeyListener {

	/**
	 * A timer, initialized in {@link PrimaryController#startGame() startGame()}, which
	 * manages rendering and scrolling.
	 * 
	 * <p>
	 * <b>Calls</b> -
     * <ul>
     * <li> {@link PrimaryController#render(Graphics) render(Graphics)}
     * <li> {@link PrimaryView#scrollEast() Scrolling Methods}
     * </ul>
	 * </p>
	 */
	private Timer viewUpdateController;
	private static boolean actionDisabled;
	private GraphicsEnvironment gEnv;
	private GraphicsDevice primaryGDev;
	private boolean isFSSupported;
	private PrimaryModel visualModel;
	private PrimaryView gameView;
	private Container contentPane;
	private BufferStrategy bufferStrategy;
	private Graphics graphicsPlaceHolder = null;
	private int boundX, boundY;
	private boolean unitIsSelected = false;
	
	/**
	 * Creates a new, disabled <code>PrimaryController</code> object.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link AppMain}
	 * </ul>
	 * </p>
	 */
	
	public PrimaryController() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	    addMouseListener(this);
	    addKeyListener(this);
	    setEnabled(false);
	    contentPane = getContentPane();
	}

	/**
	 * Initializes the <i>model</i> and <i>view</i> components
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link AppMain}
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link PrimaryModel PrimaryModel}
     * <li> {@link PrimaryView PrimaryView}
     * </ul>
	 * </p>
	 */
	
	public void loadInitialGameState() {
		visualModel = new PrimaryModel();
		visualModel.loadInitialModelState(50, 4);
		gameView = new PrimaryView();
		gameView.loadInitialViewState(getGraphicsConfiguration(), 50);
		boundX = getGraphicsConfiguration().getBounds().width;
		boundY = getGraphicsConfiguration().getBounds().height;
		contentPane.add(gameView);
	}

	/**
	 * Establishes the graphical context and starts the timer for visual rendering.
	 * 
	 * <p>
	 * If supported by the default <code>GraphicsDevice</code>, the game opens
	 * in full screen mode with a refresh rate matching the device's refresh
	 * rate. 
	 * 
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link AppMain AppMain}
	 * </ul>
     * </p>
	 */
	
	public void startGame() {
		gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		primaryGDev  = gEnv.getDefaultScreenDevice();
		isFSSupported = primaryGDev.isFullScreenSupported();
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
					gameView.updateClock(visualModel.getClockFace());
					render(graphicsPlaceHolder);
					
					int x = MouseInfo.getPointerInfo().getLocation().x, y = MouseInfo
							.getPointerInfo().getLocation().y;

					if (!actionDisabled) {
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
	
	/**
	 * Utilizes a <code>BufferStrategy</code> in order to all the
	 * <i>view</i> to draw upon a <code>Graphics</code> object.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
     * </ul>
	 * </p>
	 * 
	 * @param g - A <code>Graphics</code> object which gets replaced each render
	 * by the <code>BufferStrategy</code>'s next available <code>Graphics</code> object.
	 */

	protected void render(Graphics g) {
		try {
            g = bufferStrategy.getDrawGraphics();
            gameView.render(g, visualModel.getVisualModel());
        } finally {
        	// Prudent to free system resources when finished.
            g.dispose();
        }
        bufferStrategy.show();
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Preferable to segment usage into pressed and
		// released actions.
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Do Nothing. This makes no sense in a full-screen window.
		// You cannot exit or enter.
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		// Do Nothing. This makes no sense in a full-screen window.
		// You cannot exit or enter.
	}

	// Translates MouseEvent coordinates into meaningful
	// gameModel coordinates in order to set a focus target for the
	// view

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		int x = arg0.getLocationOnScreen().x, y = arg0.getLocationOnScreen().y;
		if (y < 864) {
			int c = gameView.getVisX(), r = gameView.getVisY(),
					idTag = visualModel.getVisualModel()[((r + y) / 64)][((c + x) / 64)];
			if (idTag > 0) {
				unitIsSelected = true;
				gameView.setFocusTarget(((c + x) / 64), ((r + y) / 64), InformationIndex.getMovementRadius(idTag), InformationIndex.getName(idTag));
			}
		}
	}

	// Completes the actions initiated in mousePressed

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		int x = arg0.getLocationOnScreen().x, y = arg0.getLocationOnScreen().y;
		if (unitIsSelected) {
			if (y < 864) {
				int c = gameView.getVisX(), r = gameView.getVisY(),
						idTag = visualModel.getVisualModel()[((r + y) / 64)][((c + x) / 64)];
				unitIsSelected = false;
				
				if (idTag == 0) {
					Point p = gameView.getFocusTarget();
					gameView.clearFocusTarget();
					visualModel.transferUnit(p.x, p.y, ((r + y) / 64), ((c + x) / 64));
				}
			}
		}
	}

	/**
	 * Comment
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link }
	 * </ul>
	 * <b>Creates</b> -
	 * <ul>
	 * <li> {@link }
     * </ul>
     * <b>Calls</b> -
     * <ul>
     * <li> {@link }
     * </ul>
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	
	public static void generateNotification(String notification, int type) {
		switch (type) {
		case 0:
			actionDisabled = true;
			break;
		}
	}
}
