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

// Inefficiently coded including a fair amount of
// repetition and potentially useless code.
// Intended for fix.
public class PrimaryView extends JPanel {

	// @formatter:off
	/*
	 * Terrain Key "T_" + "[0...44]": 
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
	 * visX and visY describe the upper-left 
	 * most pixels in graphics space such that
	 * visX and visY fall into the ranges:
	 * 0 <= visX <= boundX
	 * 0 <= visY <= boundY
	 * 
	 * boundX and boundY describe the
	 * lower-right most pixels in graphics
	 * space such that they equal:
	 * boundX = units - (width of screen)
	 * boundY = units - (height of screen)
	 * 
	 * units describes the number of pixels
	 * in graphics space occupied by all
	 * of the 64 X 64 pixel images of the
	 * grid of units
	 * 
	 * units = 64 * (# of unit rows/columns)
	 * 
	 * These fields are used to govern
	 * scrolling display functions.
	 */
	// @formatter:on

	private VolatileImage[] terrainImages, unitImages;
	private VolatileImage informationPanel;
	private VolatileImage clockImage;
	private VolatileImage portrait;
	private GraphicsConfiguration gC;
	private int visX = 0, visY = 0, boundX, boundY, units, screenWidth, screenHeight;
	private boolean focusing;
	private int focusC;
	private int focusR;
	private int focusRad;
	private int clockFace;
	private String focusName;

	// General image manipulation and instantiation intended for use in the
	// render(Graphics g, int[][] gameState) method
	public void loadInitialViewState(GraphicsConfiguration gC, int units) {
		this.gC = gC;
		this.units = 64 * units;
		screenWidth = gC.getBounds().width;
		screenHeight = gC.getBounds().height;
		boundX = this.units - screenWidth;
		boundY = this.units - screenHeight + 219;
		
		terrainImages = new VolatileImage[90];
		unitImages = new VolatileImage[1];

		for (int i = 0; i < terrainImages.length; i++) {
			
			terrainImages[i] = createVolatileImage();
			Graphics2D g = null;

			do {
				if (terrainImages[i].validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
					terrainImages[i] = createVolatileImage();
				}

				try {
					g = terrainImages[i].createGraphics();

					g.drawImage(
							(new ImageIcon(getClass().getClassLoader()
									.getResource("images/T_" + i + ".jpg")))
									.getImage(), 0, 0, null);
				} finally {
					g.dispose();
				}
			} while (terrainImages[i].contentsLost());
		}

		informationPanel = createInformationImage();
		Graphics2D g = null;

		do {
			if (informationPanel.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				informationPanel = createInformationImage();
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

		clockImage = createClockImage();
		g = null;

		do {
			if (clockImage.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				clockImage = createClockImage();
			}

			try {
				g = clockImage.createGraphics();
				
				g.setComposite(AlphaComposite.Src);
				g.setColor(Color.black);
				g.clearRect(0, 0, 64, 64);

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/clock_" + clockFace + ".png"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (clockImage.contentsLost());
		
		portrait = createPortrait();
		g = null;

		do {
			if (portrait.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				portrait = createPortrait();
			}

			try {
				g = portrait.createGraphics();
				
				g.setComposite(AlphaComposite.Src);
				g.setColor(Color.black);
				g.clearRect(0, 0, 128, 157);

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/portrait.jpg"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (portrait.contentsLost());
		
		unitImages[0] = createVolatileImage();
		g = null;

		do {
			if (unitImages[0].validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				unitImages[0] = createVolatileImage();
			}

			try {
				g = unitImages[0].createGraphics();
				
				g.setComposite(AlphaComposite.Src);
				g.setColor(Color.black);
				g.clearRect(0, 0, 64, 64);

				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/AedainianSeekerPlayer1.png"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (unitImages[0].contentsLost());
	}

	// Virtually identical to createClockImage,
	// createInformationImage, and createVolatileImage
	// Creates a valid VolatileImage to be used for the
	// portrait. May seem to be capable of infinite recursion.
	// This is not possible. Trust me.
	private VolatileImage createPortrait() {
		VolatileImage image = gC.createCompatibleVolatileImage(128, 157, VolatileImage.TRANSLUCENT);

		if (image.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
		
			// Clears the image to transparent so that the image
			// can be transparent.
			Graphics2D g = image.createGraphics();
			g.setComposite(AlphaComposite.Src);
			g.setColor(Color.black);
			g.clearRect(0, 0, 128, 157);
			
			image = createPortrait();
			
			return image;
		}
		return image;
	}

	private VolatileImage createClockImage() {
		VolatileImage image = gC.createCompatibleVolatileImage(128, 128, VolatileImage.TRANSLUCENT);

		if (image.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
			
			Graphics2D g = image.createGraphics();
			g.setComposite(AlphaComposite.Src);
			g.setColor(Color.black);
			g.clearRect(0, 0, 128, 128);
			
			image = createClockImage();
			
			return image;
		}
		return image;
	}

	private VolatileImage createInformationImage() {
		VolatileImage image = gC.createCompatibleVolatileImage(1920, 216);

		if (image.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
			image = createInformationImage();
			return image;
		}
		return image;
	}

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
						terrainImages[i] = createVolatileImage();
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
						unitImages[0] = createVolatileImage();
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
				informationPanel = createVolatileImage();
			} else if (valCode == VolatileImage.IMAGE_OK) {
				g.drawImage(informationPanel, 0, 870, null);
			}
		} while (informationPanel.contentsLost());
		
		// Clock Face
		do {
			restoreClockImage();
			
			int valCode = clockImage.validate(gC);

			if (valCode == VolatileImage.IMAGE_RESTORED) {
				restoreClockImage();
			} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				clockImage = createClockImage();
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
					unitImages[0] = createVolatileImage();
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
					portrait = createPortrait();
				} else if (valCode == VolatileImage.IMAGE_OK) {
					g.drawImage(portrait, 1148, 874, null);
				}
			} while (portrait.contentsLost());
		}	
	}

	// If the contents are lost, the portrait
	// is restored. This is the same for restore:(
	// ClockImage, UnitTile, InformationPanel, and TerrainTile)
	private void restorePortrait() {
		Graphics2D g = null;

		do {

			if (portrait.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				portrait = createPortrait();
			}

			try {
				g = portrait.createGraphics();
				
				g.setComposite(AlphaComposite.Src);
				g.setColor(Color.black);
				g.clearRect(0, 0, 128, 157);
				
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/portrait.jpg"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (portrait.contentsLost());
	}

	private void restoreClockImage() {
		Graphics2D g = null;

		do {

			if (clockImage.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				clockImage = createClockImage();
			}

			try {
				g = clockImage.createGraphics();
				
				g.setComposite(AlphaComposite.Src);
				g.setColor(Color.black);
				g.clearRect(0, 0, 128, 128);
				
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/clock_" + clockFace + ".png"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (clockImage.contentsLost());
	}

	private void restoreUnitTile(int i) {
		Graphics2D g = null;

		do {

			if (unitImages[0].validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				unitImages[0] = createVolatileImage();
			}

			try {
				g = unitImages[0].createGraphics();
				
				g.setComposite(AlphaComposite.Src);
				g.setColor(Color.black);
				g.clearRect(0, 0, 64, 64);
				
				g.drawImage((new ImageIcon(getClass().getClassLoader()
						.getResource("images/AedainianSeekerPlayer1.png"))).getImage(), 0,
						0, null);
			} finally {
				g.dispose();
			}
		} while (unitImages[0].contentsLost());
	}

	private void restoreInformationPanel() {
		Graphics2D g = null;

		do {

			if (informationPanel.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				informationPanel = createVolatileImage();
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

	private void restoreTerrainTile(int i) {
		Graphics2D g = null;

		do {

			if (terrainImages[i].validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
				terrainImages[i] = createVolatileImage();
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

	private VolatileImage createVolatileImage() {
		VolatileImage image = gC.createCompatibleVolatileImage(64, 64, VolatileImage.TRANSLUCENT);

		if (image.validate(gC) == VolatileImage.IMAGE_INCOMPATIBLE) {
			
			Graphics2D g = image.createGraphics();
			g.setComposite(AlphaComposite.Src);
			g.setColor(Color.black);
			g.clearRect(0, 0, 64, 64);
			
			image = createVolatileImage();
			
			return image;
		}
		return image;
	}

	public void scrollWest() {
		if (visX > 9) {
			visX -= 10;
		}
	}

	public void scrollNorthWest() {
		if (visY > 9) {
			visY -= 10;
		}
		if (visX > 9) {
			visX -= 10;
		}
	}

	public void scrollNorth() {
		if (visY > 9) {
			visY -= 10;
		}
	}

	public void scrollNorthEast() {
		if (visY > 9) {
			visY -= 10;
		}
		if (visX < boundX - 9) {
			visX += 10;
		}
	}

	public void scrollEast() {
		if (visX < boundX - 9) {
			visX += 10;
		}
	}

	public void scrollSouthEast() {
		if (visY < boundY - 9) {
			visY += 10;
		}
		if (visX < boundX - 9) {
			visX += 10;
		}
	}

	public void scrollSouth() {
		if (visY < boundY - 9) {
			visY += 10;
		}
	}

	public void scrollSouthWest() {
		if (visY < boundY - 9) {
			visY += 10;
		}
		if (visX > 9) {
			visX -= 10;
		}
	}

	public int getVisX() {
		return visX;
	}

	public int getVisY() {
		return visY;
	}

	// Identifies a region for highlighting when a unit is
	// selected
	public void setFocusTarget(int c, int r, int rad, String name) {
		focusing = true;
		focusC = c;
		focusR = r;
		focusRad = rad;
		focusName = name;
	}

	public void updateClock(int face) {
		clockFace = face;
	}

	
	public Point getFocusTarget() {
		return new Point(focusR, focusC);
	}

	public void clearFocusTarget() {
		focusing = false;
	}
}
