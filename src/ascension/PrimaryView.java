package ascension;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * <p>
 * <code>PrimaryView</code> is the <i>view</i> component of the
 * <b>Ascension</b> project. (Model-View-Controller paradigm)
 * </p>
 * 
 * <p>
 * <code>PrimaryView</code> is responsible for all things graphical.
 * If you see it on the screen, it is because there is some
 * method or other contained in <code>PrimaryView</code> which makes it
 * happen.
 * </p>
 * 
 * @author      Nigel_Baldwen - nigelbaldwen@gmail.com
 * @version     1.0
 */

public class PrimaryView extends JPanel {

	// @formatter:off
	/*
	 * Terrain Key "T_" + "[0...89]": 
	 * 0 * 9 + [0...8] Crystal 
	 * 1 * 9 + [0...8] Dirt 
	 * 2 * 9 + [0...8] Grass 
	 * 3 * 9 + [0...8] Rock 
	 * 4 * 9 + [0...8] Sand
	 * 5 * 9 + [0...8] Darkened Crystal
	 * 6 * 9 + [0...8] Darkened Dirt
	 * 7 * 9 + [0...8] Darkened Grass
	 * 8 * 9 + [0...8] Darkened Rock
	 * 9 * 9 + [0...8] Darkened Sand
	 * 
	 * -------------------------------
	 * 
	 * Unit Key "U_" + "[0...12]": TODO Finish Comment
	 * U_0 Novice Magician
	 * 
	 * 
	 * -------------------------------
	 * 
	 * pixelLength = 64 * (# of unit rows/columns)
	 * 
	 * pixelLength describes the side length of 
	 * the square of pixels occupied by all
	 * of the 64 X 64 pixel images of the
	 * grid of units
	 * 
	 * boundX and boundY describe the
	 * upper-left hand corner of the
	 * lower-right most displayed screen 
	 * in pixels in the graphics
	 * space such that they equal:
	 * boundX = pixelLength - (width of screen)
	 * boundY = pixelLength - (height of screen)
	 * 
	 * visX and visY describe the upper-left 
	 * most pixels in graphics space such that
	 * visX and visY fall into the ranges:
	 * 0 <= visX <= boundX
	 * 0 <= visY <= boundY
	 * 
	 * These fields are used to govern
	 * scrolling display functions.
	 */
	// @formatter:on

	private VolatileImage[] terrainImages, unitImages;
	private VolatileImage informationPanel, clockImage, portrait;
	private GraphicsConfiguration gC;
	private int visX, visY, boundX, boundY, pixelLength, screenWidth, screenHeight, focusC, focusR, focusRad, clockFace;
	private boolean focusing;
	private String focusName;

	/**
	 * Establishes window boundaries and creates <code>VolatileImage</code>s
	 * for use in rendering.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#loadInitialGameState() loadInitialGameState()}
	 * </ul>
	 * </p>
	 * 
	 * @param gC - the <code>GraphicsConfiguration</code> the view operates within
	 * @param units - the number cells in one row of the square grid
	 */

	public void loadInitialViewState(GraphicsConfiguration gC, int units) {
		this.gC = gC;
		this.pixelLength = 64 * units;
		screenWidth = gC.getBounds().width;
		screenHeight = gC.getBounds().height;
		boundX = this.pixelLength - screenWidth;
		boundY = this.pixelLength - screenHeight + 219;
		visX = 0;
		visY = 0;

		terrainImages = new VolatileImage[90];

		for (int i = 0; i < terrainImages.length; i++) {
			terrainImages[i] = gC.createCompatibleVolatileImage(64, 64);
		}

		unitImages = new VolatileImage[1];

		for (int i = 0; i < unitImages.length; i++) {
			unitImages[i] = gC.createCompatibleVolatileImage(64, 64, VolatileImage.TRANSLUCENT);
		}
		
		if (screenWidth == 2560 && screenHeight == 1440) {
			
		}
		else if (screenWidth == 1920 && screenHeight == 1080) {
			informationPanel = gC.createCompatibleVolatileImage(1920, 216);
			clockImage = gC.createCompatibleVolatileImage(128, 128, VolatileImage.TRANSLUCENT);
			portrait = gC.createCompatibleVolatileImage(128, 157, VolatileImage.TRANSLUCENT);
		}
	}

	/**
	 * Handles all visual display operations.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#render(Graphics) render(Graphics)}
	 * </ul>
	 * <b>Calls</b> -
	 * <ul>
	 * <li> {@link PrimaryView#restorePortrait() Restoration Methods}
	 * </ul>
	 * </p>
	 * 
	 * @param gameState - the currently active player's visible information
	 * @param g - the <code>Graphics</code> object supplied by the <code>BufferStrategy</code>
	 */

	public void render(Graphics g, int[][] gameState) {
		// Calculate drawn squares
		int cStart = visX / 64 + gameState.length;
		int rStart = visY / 64;
		int cEnd = cStart + screenWidth / 64 + 1;
		int rEnd = rStart + screenHeight / 64 + 1;

		// Traversing the 2D gameState array to draw 
		// terrain tiles. The do while ensures the images
		// actually get drawn. Applies for the populate units
		// region as well.
		for (int r = rStart; r < rEnd && r < gameState.length; r++) {
			for (int c = cStart; c < cEnd && c < gameState.length * 2; c++) {

				int i = gameState[r][c];

				do {
					int valCode = terrainImages[i].validate(gC);

					if (valCode == VolatileImage.IMAGE_RESTORED) {
						restoreTerrainTile(i);
					} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
						terrainImages[i] = gC.createCompatibleVolatileImage(64, 64);
					} else if (valCode == VolatileImage.IMAGE_OK) {
						g.drawImage(terrainImages[i], ((c - gameState.length) * 64) - visX,
								(r * 64) - visY, null);
					}
				} while (terrainImages[i].contentsLost());
			}
		}

		// Populate Units
		for (int r = rStart; r < rEnd && r < gameState.length; r++) {
			for (int c = cStart - gameState.length; c < cEnd - gameState.length && c < gameState.length * 2; c++) {

				int i = gameState[r][c];

				do {
					int valCode = unitImages[0].validate(gC);

					if (valCode == VolatileImage.IMAGE_RESTORED) {
						restoreUnitTile(i);
					} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
						unitImages[0] = gC.createCompatibleVolatileImage(64, 64, VolatileImage.TRANSLUCENT);
					} else if (valCode == VolatileImage.IMAGE_OK && gameState[r][c] > 0) {
						g.drawImage(unitImages[0], (c * 64) - visX,
								(r * 64) - visY, null);
					}
				} while (unitImages[0].contentsLost());
			}
		}

		// Used to draw the highlight boxes for displaying the unit's
		// movement radius.
		if (focusing) {
			g.setColor(Color.GREEN);
			for (int r = focusR - focusRad; r < focusR + focusRad + 1; r++)
				for (int c = focusC - focusRad; c < focusC + focusRad + 1; c++) {
					g.drawRect(c * 64 - visX, r * 64 - visY, 64, 64);
					g.drawRect(c * 64 - 1  - visX, r * 64 - 1 - visY, 66, 66);
					g.drawRect(c * 64 + 1  - visX, r * 64 + 1 - visY, 62, 62);
				}
		}

		// Information Panel
		do {
			int valCode = informationPanel.validate(gC);

			if (valCode == VolatileImage.IMAGE_RESTORED) {
				restoreInformationPanel();
			} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				informationPanel = gC.createCompatibleVolatileImage(1920, 216);
			} else if (valCode == VolatileImage.IMAGE_OK) {
				g.drawImage(informationPanel, 0, 864, null);
			}
		} while (informationPanel.contentsLost());

		// Clock Face
		do {
			restoreClockImage();

			int valCode = clockImage.validate(gC);

			if (valCode == VolatileImage.IMAGE_RESTORED) {
				restoreClockImage();
			} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				clockImage = gC.createCompatibleVolatileImage(128, 128, VolatileImage.TRANSLUCENT);
			} else if (valCode == VolatileImage.IMAGE_OK) {
				g.drawImage(clockImage, 10, 874, null);
			}
		} while (clockImage.contentsLost());

		if (focusing) {
			do {
				int i = gameState[focusR][focusC];

				int valCode = unitImages[0].validate(gC);

				if (valCode == VolatileImage.IMAGE_RESTORED) {
					restoreUnitTile(i);
				} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
					unitImages[0] = gC.createCompatibleVolatileImage(64, 64, VolatileImage.TRANSLUCENT);
				} else if (valCode == VolatileImage.IMAGE_OK) {
					g.drawImage(unitImages[0], 170, 886, null);
					g.setColor(Color.WHITE);
					g.drawString(focusName, 1162, 1061);
				}
			} while (unitImages[0].contentsLost());

			do {
				int valCode = portrait.validate(gC);

				if (valCode == VolatileImage.IMAGE_RESTORED) {
					restorePortrait();
				} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
					portrait = gC.createCompatibleVolatileImage(128, 157, VolatileImage.TRANSLUCENT);
				} else if (valCode == VolatileImage.IMAGE_OK) {
					g.drawImage(portrait, 1148, 874, null);
				}
			} while (portrait.contentsLost());
		}	
	}

	/**
	 * Restores the lost contents of the portrait image.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 */

	private void restorePortrait() {
		Graphics2D g = null;

		do {

			if (portrait.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				portrait = gC.createCompatibleVolatileImage(128, 157, VolatileImage.TRANSLUCENT);
			}

			try {
				g = portrait.createGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/portrait.jpg"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (portrait.contentsLost());
	}

	/**
	 * Restores the lost contents of the clock image.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 */

	private void restoreClockImage() {
		Graphics2D g = null;

		do {

			if (clockImage.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				clockImage = gC.createCompatibleVolatileImage(128, 128, VolatileImage.TRANSLUCENT);
			}

			try {
				g = clockImage.createGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/clock_" + clockFace + ".png"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (clockImage.contentsLost());
	}

	/**
	 * Restores the lost contents of the unit image.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 */

	private void restoreUnitTile(int i) {
		Graphics2D g = null;

		do {

			if (unitImages[0].validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				unitImages[0] = gC.createCompatibleVolatileImage(64, 64, VolatileImage.TRANSLUCENT);
			}

			try {
				g = unitImages[0].createGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/AedainianSeekerPlayer1.png"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (unitImages[0].contentsLost());
	}

	/**
	 * Restores the lost contents of the information panel image.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 */

	private void restoreInformationPanel() {
		Graphics2D g = null;

		do {

			if (informationPanel.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				informationPanel = gC.createCompatibleVolatileImage(1920, 216);
			}

			try {
				g = informationPanel.createGraphics();

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/I_" + 0 + ".png"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (informationPanel.contentsLost());
	}

	/**
	 * Restores the lost contents of the terrain tile image.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 */

	private void restoreTerrainTile(int i) {
		Graphics2D g = null;

		do {

			if (terrainImages[i].validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				terrainImages[i] = gC.createCompatibleVolatileImage(64, 64);
			}

			try {
				g = terrainImages[i].createGraphics();

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/T_" + i + ".jpg"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (terrainImages[i].contentsLost());
	}

	/**
	 * Scrolls the display to the west.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 */

	public void scrollWest() {
		if (visX > 9) {
			visX -= 10;
		}
	}

	/**
	 * Scrolls the display to the north-west.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 */

	public void scrollNorthWest() {
		if (visY > 9) {
			visY -= 10;
		}
		if (visX > 9) {
			visX -= 10;
		}
	}

	/**
	 * Scrolls the display to the north.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 */

	public void scrollNorth() {
		if (visY > 9) {
			visY -= 10;
		}
	}

	/**
	 * Scrolls the display to the north-east.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 */

	public void scrollNorthEast() {
		if (visY > 9) {
			visY -= 10;
		}
		if (visX < boundX - 9) {
			visX += 10;
		}
	}

	/**
	 * Scrolls the display to the east.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 */

	public void scrollEast() {
		if (visX < boundX - 9) {
			visX += 10;
		}
	}

	/**
	 * Scrolls the display to the south-east.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 */

	public void scrollSouthEast() {
		if (visY < boundY - 9) {
			visY += 10;
		}
		if (visX < boundX - 9) {
			visX += 10;
		}
	}

	/**
	 * Scrolls the display to the south.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 */

	public void scrollSouth() {
		if (visY < boundY - 9) {
			visY += 10;
		}
	}

	/**
	 * Scrolls the display to the south-west.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 */

	public void scrollSouthWest() {
		if (visY < boundY - 9) {
			visY += 10;
		}
		if (visX > 9) {
			visX -= 10;
		}
	}

	/**
	 * Returns the upper-left-most visible pixel's x coordinate relative to the entire graphics space.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the upper-left-most visible pixel's x coordinate
	 */

	public int getVisX() {
		return visX;
	}

	/**
	 * Returns the upper-left-most visible pixel's y coordinate relative to the entire graphics space.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the upper-left-most visible pixel's y coordinate
	 */

	public int getVisY() {
		return visY;
	}

	/**
	 * Identifies a selected unit and its movement radius.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @param c - the abstract column coordinate of the unit
	 * @param r - the abstract row coordinate of the unit
	 * @param rad - the unit's movement radius
	 * @param name - the unit's name
	 */

	public void setFocusTarget(int c, int r, int rad, String name) {
		focusing = true;
		focusC = c;
		focusR = r;
		focusRad = rad;
		focusName = name;
	}

	/**
	 * Replaces the clock face in order to keep it up to date.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#viewUpdateController viewUpdateController}
	 * </ul>
	 * </p>
	 * 
	 * @param face - the current clock face
	 */

	public void updateClock(int face) {
		clockFace = face;
	}

	/**
	 * Returns the current focus target.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the focus target
	 */

	public Point getFocusTarget() {
		return new Point(focusR, focusC);
	}

	/**
	 * Clears the focus target.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 */

	public void clearFocusTarget() {
		focusing = false;
	}
}
