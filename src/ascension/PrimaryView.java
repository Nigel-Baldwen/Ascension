package ascension;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
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

	private VolatileImage[] terrainImages, unitImages, unitFocusImages;
	private VolatileImage unitIP, terrainIP, clockImage, portrait;
	private GraphicsConfiguration gC;
	private int unitLength, visX, visY, boundX, boundY, pixelLength, screenWidth, screenHeight, xOffset, yOffset, 
	iPaneWidth, iPaneHeight, iPaneButtonSize, iPaneButtonX, iPaneButtonY, resKey, clockLength, clockOffset, portraitSize, portraitOffset, statsOffsetX, statsOffsetY, statsSeperatorX, statsSeperatorY, 
	focusC, focusR, focusBoxX, focusBoxY, clockFace, endTurnX, endTurnY, endTurnWidth, endTurnHeight;
	private boolean focusingUnit, focusingTerrain;
	private String terDescriptor;
	private String[] unitDescriptor;
	private Font basicText;

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
		screenWidth = gC.getBounds().width;
		screenHeight = gC.getBounds().height;
		visX = 0;
		visY = 0;

		if (screenWidth >= 2560 && screenHeight >= 1440) {
			xOffset = (screenWidth - 2560) / 2;
			yOffset = (screenHeight - 1440) / 2;
			iPaneWidth = 2560;
			iPaneHeight = 140;
			iPaneButtonSize = 64;
			iPaneButtonX = xOffset + 2425;
			iPaneButtonY = screenHeight - yOffset - iPaneHeight + 5;
			unitLength = 52;
			clockLength = 132;
			clockOffset = 4;
			portraitSize = 132;
			portraitOffset = 141;
			statsOffsetX = 315;
			statsOffsetY =  30;
			statsSeperatorX = 64;
			statsSeperatorY = 33;
			endTurnX = xOffset + 12;
			endTurnY = screenHeight - yOffset - iPaneHeight + 196;
			endTurnWidth = 174;
			endTurnHeight = 80;
			resKey = 0;
		} else if (screenWidth >= 1920 && screenHeight >= 1080) {
			xOffset = (screenWidth - 1920) / 2;
			yOffset = (screenHeight - 1080) / 2;
			iPaneWidth = 1920;
			iPaneHeight = 105;
			unitLength = 39;
			clockLength = 99;
			clockOffset = 3;
			portraitSize = 99;
			portraitOffset = 106;
			endTurnX = xOffset + 9;
			endTurnY = screenHeight - yOffset - iPaneHeight + 147;
			endTurnWidth = 130;
			endTurnHeight = 60;
			resKey = 1;
		} else if (screenWidth >= 1600  && screenHeight >= 900) {
			xOffset = (screenWidth - 1600) / 2;
			yOffset = (screenHeight - 900) / 2;
			iPaneWidth = 1600;
			iPaneHeight = 75;
			unitLength = 33;
			clockLength = 105;
			clockOffset = 9;
			portraitSize = 105;
			portraitOffset = -1;
			endTurnX = xOffset + 8;
			endTurnY = screenHeight - yOffset - iPaneHeight + 123;
			endTurnWidth = 108;
			endTurnHeight = 49;
			resKey = 2;
		} else if (screenWidth >= 1280  && screenHeight >= 720) {
			xOffset = (screenWidth - 1280) / 2;
			yOffset = (screenHeight - 720) / 2;
			iPaneWidth = 1280;
			iPaneHeight = 70;
			unitLength = 26;
			clockLength = 85;
			clockOffset = 7;
			portraitSize = 85;
			portraitOffset = -1;
			endTurnX = xOffset + 6;
			endTurnY = screenHeight - yOffset - iPaneHeight + 98;
			endTurnWidth = 87;
			endTurnHeight = 40;
			resKey = 3;
		} else {
			System.exit(0);
		}

		pixelLength = unitLength * units;
		boundX = this.pixelLength - screenWidth;
		boundY = this.pixelLength - screenHeight + iPaneHeight;

		terrainImages = new VolatileImage[90];

		for (int i = 0; i < terrainImages.length; i++) {
			terrainImages[i] = gC.createCompatibleVolatileImage(unitLength, unitLength);
		}

		unitImages = new VolatileImage[40];

		for (int i = 0; i < unitImages.length; i++) {
			unitImages[i] = gC.createCompatibleVolatileImage(unitLength, unitLength, VolatileImage.TRANSLUCENT);
		}

		unitFocusImages = new VolatileImage[40];

		for (int i = 0; i < unitFocusImages.length; i++) {
			unitFocusImages[i] = gC.createCompatibleVolatileImage(unitLength * 3, unitLength * 3);
		}

		unitIP = gC.createCompatibleVolatileImage(iPaneWidth, iPaneHeight);
		terrainIP = gC.createCompatibleVolatileImage(iPaneWidth, iPaneHeight);
		clockImage = gC.createCompatibleVolatileImage(clockLength, clockLength, VolatileImage.TRANSLUCENT);
		portrait = gC.createCompatibleVolatileImage(portraitSize, portraitSize, VolatileImage.TRANSLUCENT);
		basicText = new Font("Veranda", Font.BOLD, 20);
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
		int cStart = visX / unitLength + gameState.length;
		int rStart = visY / unitLength;
		int cEnd = cStart + (screenWidth - 2 * xOffset) / unitLength + 1;
		int rEnd = rStart + (screenHeight - 2 * yOffset) / unitLength + 1;

		// Traversing the 2D gameState array to draw terrain tiles.
		drawTerrainTiles(rStart, rEnd, cStart, cEnd, gameState, g);

		// Traversing the 2D gameState array to draw unit tiles.
		drawUnitTiles(rStart, rEnd, cStart, cEnd, gameState, g);

		// Draw the highlight boxes and focus for a focused unit.
		drawUnitFocus(g);		

		// Draw the relevant information panel.
		drawInformationPanel(g);

		// Draw the current clock face.
		drawClock(g);

		// Draw the target unit's portrait.
		drawUnitPortrait(g);

		// Draw black rectangles in xOffset and yOffset regions
		drawOffsets(g);
	}

	/**
	 * A helper method for render to factor out code.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 * 
	 * @param rStart - the abstract row coordinate of the upper-left-most unit
	 * @param rEnd - the abstract row coordinate of the lower-right-most unit
	 * @param cStart - the abstract column coordinate of the upper-left-most unit
	 * @param cEnd - the abstract column coordinate of the lower-right-most unit
	 * @param gameState - the currently active player's visible information
	 * @param g - the <code>Graphics</code> object supplied by the <code>BufferStrategy</code>
	 */
	private void drawTerrainTiles(int rStart, int rEnd, int cStart, int cEnd, int[][] gameState, Graphics g){
		for (int r = rStart; r < rEnd && r < gameState.length; r++) {
			for (int c = cStart; c < cEnd && c < gameState.length * 2; c++) {

				int i = gameState[r][c];

				do {
					int valCode = terrainImages[i].validate(gC);

					if (valCode == VolatileImage.IMAGE_RESTORED) {
						restoreTerrainTile(i);
					} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
						terrainImages[i] = gC.createCompatibleVolatileImage(unitLength, unitLength);
					} else if (valCode == VolatileImage.IMAGE_OK) {
						g.drawImage(terrainImages[i], ((c - gameState.length) * unitLength) - visX + xOffset,
								(r * unitLength) - visY + yOffset, null);
					}
				} while (terrainImages[i].contentsLost());
			}
		}
	}

	/** 
	 * A helper method for render to factor out code.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 * 
	 * @param rStart - the abstract row coordinate of the upper-left-most unit
	 * @param rEnd - the abstract row coordinate of the lower-right-most unit
	 * @param cStart - the abstract column coordinate of the upper-left-most unit
	 * @param cEnd - the abstract column coordinate of the lower-right-most unit
	 * @param gameState - the currently active player's visible information
	 * @param g - the <code>Graphics</code> object supplied by the <code>BufferStrategy</code>
	 */
	private void drawUnitTiles(int rStart, int rEnd, int cStart, int cEnd, int[][] gameState, Graphics g){
		for (int r = rStart; r < rEnd && r < gameState.length; r++) {
			for (int c = cStart - gameState.length; c < cEnd - gameState.length && c < gameState.length * 2; c++) {

				int i = gameState[r][c];

				if (i > -1) {
					do {
						int valCode = unitImages[i].validate(gC);

						if (valCode == VolatileImage.IMAGE_RESTORED) {
							restoreUnitTile(i);
						} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
							unitImages[i] = gC.createCompatibleVolatileImage(unitLength, unitLength, VolatileImage.TRANSLUCENT);
						} else if (valCode == VolatileImage.IMAGE_OK) {
							g.drawImage(unitImages[i], (c * unitLength) - visX + xOffset,
									(r * unitLength) - visY + yOffset, null);
						}
					} while (unitImages[i].contentsLost());
				}
			}
		}
	}

	/** 
	 * A helper method for render to factor out code.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 * 
	 * @param g - the <code>Graphics</code> object supplied by the <code>BufferStrategy</code>
	 */
	private void drawUnitFocus(Graphics g) {
		if (focusingUnit) {
			g.setColor(Color.GREEN);
			int stroke = 1; // A more complex stroke may be appropriate such as unitLength * 5 / 100
			int focusRad = Integer.parseInt(unitDescriptor[3]);
			for (int r = focusR - focusRad; r < focusR + focusRad + 1; r++)
				for (int c = focusC - focusRad; c < focusC + focusRad + 1; c++) {
					// Upper Stroke
					g.fillRect(c * unitLength - visX + xOffset - stroke, r * unitLength - visY + yOffset - stroke, unitLength + stroke * 2, stroke);
					// Left Stroke
					g.fillRect(c * unitLength - visX + xOffset - stroke, r * unitLength - visY + yOffset - stroke, stroke, unitLength + stroke * 2);
					// Bottom Stroke
					g.fillRect(c * unitLength - visX + xOffset - stroke, r * unitLength - visY + yOffset + unitLength, unitLength + stroke * 2, stroke);
					// Right Stroke
					g.fillRect(c * unitLength - visX + xOffset + unitLength, r * unitLength - visY + yOffset - stroke, stroke, unitLength + stroke * 2);
				}
			
			focusBoxX = focusC * unitLength - unitLength - visX;
			focusBoxY = focusR * unitLength - unitLength - visY;
			if (focusC * unitLength < visX + unitLength) {
				focusBoxX = xOffset;
			}
			if (focusC * unitLength > visX + screenWidth - xOffset - unitLength * 2) {
				focusBoxX = screenWidth - 2 * xOffset - unitLength * 3;
			}
			if (focusR * unitLength < visY + unitLength) {
				focusBoxY = yOffset;
			}
			if (focusR * unitLength > visY + screenHeight - yOffset - iPaneHeight - unitLength * 2) {
				focusBoxY = screenWidth - 2 * yOffset - unitLength * 3;
			}

			do {
				int valCode = unitFocusImages[0].validate(gC);

				if (valCode == VolatileImage.IMAGE_RESTORED) {
					restoreUnitFocusImage(0);
				} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
					unitFocusImages[0] = gC.createCompatibleVolatileImage(unitLength, unitLength);
				} else if (valCode == VolatileImage.IMAGE_OK) {
					g.drawImage(unitFocusImages[0], focusBoxX,
							focusBoxY, null);
				}
			} while (unitFocusImages[0].contentsLost());

		}
	}

	/** 
	 * A helper method for render to factor out code.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 * 
	 * @param g - the <code>Graphics</code> object supplied by the <code>BufferStrategy</code>
	 */
	private void drawInformationPanel(Graphics g) {
		if (focusingUnit) {
			do {
				int valCode = unitIP.validate(gC);

				if (valCode == VolatileImage.IMAGE_RESTORED) {
					restoreUnitIP();
				} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
					unitIP = gC.createCompatibleVolatileImage(iPaneWidth, iPaneHeight);
				} else if (valCode == VolatileImage.IMAGE_OK) {
					g.drawImage(unitIP, xOffset, screenHeight - yOffset - iPaneHeight, null);
					// Fill in the stats for the unit starting at health and finishing at reinforcement
					g.setColor(Color.BLACK);
					g.setFont(basicText);
					int statIterator = 0;
					for (int c = 0; c < 15; c++) {
						for (int r = 0; r < 4; r++) {
							if (Integer.parseInt(unitDescriptor[statIterator]) > 0) {
								g.drawString(unitDescriptor[statIterator],
										xOffset + statsOffsetX + c * statsSeperatorX,
										screenHeight - yOffset - iPaneHeight + statsOffsetY + r * statsSeperatorY);
							}
							statIterator++;
						}
					}
				}
			} while (unitIP.contentsLost());
		} else if(focusingTerrain) {
			do {
				int valCode = terrainIP.validate(gC);

				if (valCode == VolatileImage.IMAGE_RESTORED) {
					restoreTerrainIP();
				} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
					terrainIP = gC.createCompatibleVolatileImage(iPaneWidth, iPaneHeight);
				} else if (valCode == VolatileImage.IMAGE_OK) {
					g.drawImage(terrainIP, xOffset, screenHeight - yOffset - iPaneHeight, null);
					g.setColor(Color.WHITE);
					g.setFont(basicText);
					g.drawString(terDescriptor, xOffset + portraitOffset + clockLength / 4, (int) (screenHeight - yOffset - iPaneHeight + clockOffset + clockLength / 2));
				}
			} while (terrainIP.contentsLost());
		}
	}


	/** 
	 * A helper method for render to factor out code.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 * 
	 * @param g - the <code>Graphics</code> object supplied by the <code>BufferStrategy</code>
	 */
	private void drawClock(Graphics g) {
		do {
			restoreClockImage();

			int valCode = clockImage.validate(gC);

			if (valCode == VolatileImage.IMAGE_RESTORED) {
				restoreClockImage();
			} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				clockImage = gC.createCompatibleVolatileImage(clockLength, clockLength, VolatileImage.TRANSLUCENT);
			} else if (valCode == VolatileImage.IMAGE_OK) {
				g.drawImage(clockImage, xOffset + clockOffset, screenHeight - yOffset - iPaneHeight + clockOffset, null);
			}
		} while (clockImage.contentsLost());
	}


	/** 
	 * A helper method for render to factor out code.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 * 
	 * @param g - the <code>Graphics</code> object supplied by the <code>BufferStrategy</code>
	 */
	private void drawUnitPortrait(Graphics g) {
		if (focusingUnit) {
			do {
				int valCode = portrait.validate(gC);

				if (valCode == VolatileImage.IMAGE_RESTORED) {
					restorePortrait();
				} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
					portrait = gC.createCompatibleVolatileImage(128, 157, VolatileImage.TRANSLUCENT);
				} else if (valCode == VolatileImage.IMAGE_OK) {
					g.drawImage(portrait, xOffset + portraitOffset, screenHeight - yOffset - iPaneHeight + clockOffset, null);
				}
			} while (portrait.contentsLost());
		}
	}

	/** 
	 * A helper method for render to factor out code.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 * 
	 * @param g - the <code>Graphics</code> object supplied by the <code>BufferStrategy</code>
	 */
	private void drawOffsets(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, xOffset, screenHeight);
		g.fillRect(0, 0, screenWidth, yOffset);
		g.fillRect(screenWidth - xOffset, 0, xOffset, screenHeight);
		g.fillRect(0, screenHeight - yOffset, screenWidth, screenHeight);
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
				portrait = gC.createCompatibleVolatileImage(portraitSize, portraitSize, VolatileImage.TRANSLUCENT);
			}

			try {
				g = portrait.createGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/Units/" + resKey + "/Portraits/"+ 0 + "/P_0.jpg"))).getImage(), 0,
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
				clockImage = gC.createCompatibleVolatileImage(clockLength, clockLength, VolatileImage.TRANSLUCENT);
			}

			try {
				g = clockImage.createGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/Clock/" + resKey + "/C_" + clockFace + ".png"))).getImage(), 0,
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
				unitImages[0] = gC.createCompatibleVolatileImage(unitLength, unitLength, VolatileImage.TRANSLUCENT);
			}

			try {
				g = unitImages[0].createGraphics();
				g.setComposite(AlphaComposite.Src);
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/Units/" + resKey + "/Tile/" + 0 + "/U_" + i + ".png"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (unitImages[0].contentsLost());
	}


	/**
	 * Restores the lost contents of the unit focus image.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 */
	private void restoreUnitFocusImage(int i) {
		Graphics2D g = null;

		do {

			if (unitFocusImages[0].validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				unitFocusImages[0] = gC.createCompatibleVolatileImage(unitLength * 3, unitLength * 3);
			}

			try {
				g = unitFocusImages[0].createGraphics();

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/Units/" + resKey + "/Focus/" + 0 + "/U_" + i + "_F.jpg"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (unitImages[0].contentsLost());
	}


	/**
	 * Restores the lost contents of the unit information panel image.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 */
	private void restoreUnitIP() {
		Graphics2D g = null;

		do {

			if (unitIP.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				unitIP = gC.createCompatibleVolatileImage(iPaneWidth, iPaneHeight);
			}

			try {
				g = unitIP.createGraphics();

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/Information Panel/" + resKey + "/I_0.jpg"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (unitIP.contentsLost());
	}

	/**
	 * Restores the lost contents of the terrain information panel image.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryView#render(Graphics, int[][]) render(Graphics, int[][])}
	 * </ul>
	 * </p>
	 */
	private void restoreTerrainIP() {
		Graphics2D g = null;

		do {

			if (terrainIP.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				terrainIP = gC.createCompatibleVolatileImage(iPaneWidth, iPaneHeight);
			}

			try {
				g = terrainIP.createGraphics();

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/Information Panel/" + resKey + "/I_1.jpg"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (terrainIP.contentsLost());
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
				terrainImages[i] = gC.createCompatibleVolatileImage(unitLength, unitLength);
			}

			try {
				g = terrainImages[i].createGraphics();

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/Terrain/" + resKey + "/Tile/T_" + i + ".jpg"))).getImage(), 0,
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
	 * Identifies a selected unit.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseClicked(java.awt.event.MouseEvent) mouseClicked(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @param r - the abstract row coordinate of the unit
	 * @param c - the abstract column coordinate of the unit
	 * @param name - the unit's name
	 */
	public void setUnitFocusTarget(int r, int c, String descriptor) {
		focusingUnit = true;
		focusR = r;
		focusC = c;
		unitDescriptor = descriptor.split(":");
	}


	/**
	 * Identifies a selected terrain tile.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseClicked(java.awt.event.MouseEvent) mouseClicked(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @param c = the abstract column coordinate of the terrain
	 * @param r = the abstract row coordinate of the terrain
	 * @param id - the terrain image ID
	 */
	public void setTerrainFocusTarget(int c, int r, String descriptor) {
		focusC = c;
		focusR = r;
		terDescriptor = descriptor;
		focusingTerrain = true;
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
	 * Returns the information panel height.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the information panel height.
	 */
	public int getIPaneHeight() {
		return iPaneHeight;
	}

	/**
	 * Returns the size of the buttons found on the right-hand side of the information panel.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseClicked(java.awt.event.MouseEvent) mouseClicked(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the information panel button size.
	 */
	public int getIPaneButtonSize() {
		return iPaneButtonSize;
	}

	/**
	 * Returns the upper-left X coordinate of the the buttons found on the right-hand
	 * side of the information panel.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseClicked(java.awt.event.MouseEvent) mouseClicked(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the information upper-left X coordinate of the information panel buttons.
	 */
	public int getIPaneButtonX() {
		return iPaneButtonX;
	}
	
	/**
	 * Returns the upper-left Y coordinate of the the buttons found on the right-hand
	 * side of the information panel.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseClicked(java.awt.event.MouseEvent) mouseClicked(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the information upper-left Y coordinate of the information panel buttons.
	 */
	public int getIPaneButtonY() {
		return iPaneButtonY;
	}
	
	/**
	 * Returns the x dimension offset.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the x dimension offset.
	 */
	public int getXOffset() {
		return xOffset;
	}


	/**
	 * Returns the y dimension offset.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the y dimension offset.
	 */
	public int getYOffset() {
		return yOffset;
	}


	/**
	 * Returns the unit length.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the unit length.
	 */
	public int getUnitLength() {
		return unitLength;
	}


	/**
	 * Returns the x coordinate of the end turn button.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the end turn button x coordinate.
	 */
	public int getEndTurnX() {
		return endTurnX;
	}


	/**
	 * Returns the y coordinate of the end turn button.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the end turn button y coordinate.
	 */
	public int getEndTurnY() {
		return endTurnY;
	}


	/**
	 * Returns the width of the end turn button.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the end turn button width.
	 */
	public int getEndTurnWidth() {
		return endTurnWidth;
	}


	/**
	 * Returns the height of the end turn button.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mouseReleased(java.awt.event.MouseEvent) mouseReleased(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the end turn button height.
	 */
	public int getEndTurnHeight() {
		return endTurnHeight;
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
		focusingUnit = false;
		focusingTerrain = false;
	}


	/**
	 * Returns the on-screen coordinates of the unit focus box.
	 * 
	 * <p>
	 * <b>Called By</b> -
	 * <ul>
	 * <li> {@link PrimaryController#mousePressed(java.awt.event.MouseEvent) mousePressed(java.awt.event.MouseEvent)}
	 * </ul>
	 * </p>
	 * 
	 * @return the unit focus box coordinates.
	 */
	public Point getFocusBoxCoords() {
		return new Point(focusBoxX, focusBoxY);
	}
}
