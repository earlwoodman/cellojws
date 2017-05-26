/*
 * Created on 2010-07-11
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.adapter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.text.AttributedString;

import com.rallycallsoftware.cellojws.controls.Gradient.Direction;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class Graphics {

	private Graphics2D graphics2D;

	private FontMetrics fontMetrics;

	private boolean highlight = false;

	private FontInfo fontInfo;

	private Color savedColor = null;

	private static final String fontType = "Arial Bold";

	public Graphics(Graphics2D g2d) {
		graphics2D = g2d;

		// Initialize with *something*
		final Font font = new Font("Monospaced", Font.PLAIN, 12);
		graphics2D.setFont(font);
		setColor(Color.WHITE);
		fontMetrics = graphics2D.getFontMetrics(font);
	}

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public void setFontInfo(FontInfo fontInfo) {
		if (fontInfo != null) {
			this.fontInfo = fontInfo;
			final Font font = new Font(fontType, fontInfo.isItalic() ? Font.ITALIC : Font.PLAIN,
					fontInfo.getFontSize());
			graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

			graphics2D.setFont(font);
			fontMetrics = graphics2D.getFontMetrics(font);
		}
	}

	public boolean isHighlight() {
		return highlight;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	@Deprecated
	public void setColor(Color color) {
		graphics2D.setColor(color);
	}

	public void setGraphics2D(Graphics2D graphics2d) {
		graphics2D = graphics2d;
	}

	public void drawText(final String string, final int x, final int y) {
		if (string == null || string.length() == 0) {
			return;
		}

		final int top = y + WindowManager.getTextHeight(fontInfo);

		AttributedString attributed = new AttributedString(fontInfo.isUpperCase() ? string.toUpperCase() : string);
		final Font font = new Font(fontInfo.getFontFace(), Font.PLAIN, fontInfo.getFontSize());
		attributed.addAttribute(TextAttribute.FONT, font);

		// Drop shadow first
		if (highlight) {
			setColor(fontInfo.getDropShadowForHighlight());
		} else {
			setColor(fontInfo.getDropShadow());
		}
		graphics2D.drawString(attributed.getIterator(), x + 1, top - fontMetrics.getDescent() + 1);

		// Now the main text
		if (highlight) {
			setColor(fontInfo.getTextColorForHighlight());
		} else {
			setColor(fontInfo.getTextColor());
		}
		graphics2D.drawString(attributed.getIterator(), x, top - fontMetrics.getDescent());

	}

	/**
	 * Draws the string, up to the amount permitted by widthMax (pixels, not
	 * chars)
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param widthMax
	 */
	public void drawText(final String string, final int x, final int y, final int widthMax) {
		if (string == null || string.length() == 0) {
			return;
		}

		// Don't bother using an ellipsis on any tiny strings regardless of room
		if (string.length() < 3) {
			drawText(string, x, y);
			return;
		}

		// Now figure out how much room we actually have
		final int actualWidth = getTextWidth(string, fontInfo);

		if (actualWidth <= widthMax) {
			drawText(string, x, y);
			return;
		}

		final String ellipsis = "...";

		String modifiedString = string;

		// Loop through all characters, preserving at least the first char
		for (int i = 0; i < string.length() - 1; i++) {
			modifiedString = string.substring(0, string.length() - i - 2) + ellipsis;
			if (getTextWidth(modifiedString, fontInfo) < widthMax) {
				drawText(modifiedString, x, y);
				return;
			}
		}

		// Better draw it anyway
		drawText(string, x, y);
	}

	public void drawRect(final AbsDims dims, final Color color) {
		if (dims != null) {
			final Color save = graphics2D.getColor();

			graphics2D.setColor(color);
			graphics2D.draw(new Rectangle(dims.left, dims.top, dims.right - dims.left, dims.bottom - dims.top));

			graphics2D.setColor(save);
		}
	}

	public void drawRect(final AbsDims dims) {
		drawRect(dims, Color.black);
	}

	public void drawSolidRect(final AbsDims dims, final Color color) {
		final Color saved = graphics2D.getBackground();

		graphics2D.setBackground(color);

		if (dims != null) {
			graphics2D.clearRect(dims.left, dims.top, dims.right - dims.left, dims.bottom - dims.top);
		}

		graphics2D.setBackground(saved);

	}

	public void drawImage(final Image image, final AbsDims dims) {
		if (image != null) {
			drawImage(image.getBufferedImage(), dims);
		} else {
			WorkerLog.error("Image is null for dims: " + dims.toString());
		}
	}

	public void drawImage(final BufferedImage image) {
		final AbsDims dims = new AbsDims(0, 0, image.getWidth(), image.getHeight());
		drawImage(image, dims);
	}

	public void drawImage(final BufferedImage image, final AbsDims dims) {
		graphics2D.drawImage(image, dims.left, dims.top, dims.right - dims.left, dims.bottom - dims.top, null);
	}

	public void drawImage(final BufferedImage image, final AbsDims dims, final AffineTransform at) {
		if (at == null) {
			drawImage(image, dims);
		} else {
			graphics2D.drawImage(image, at, null);
		}
	}

	public void drawImage(final BufferedImage image, final AbsDims dest, final AbsDims src) {
		graphics2D.drawImage(image, dest.left, dest.top, dest.right, dest.bottom, src.left, src.top, src.right,
				src.bottom, null);
	}

	public int getTextHeight(final FontInfo fontInfo) {
		final Font font = new Font(fontInfo.getFontFace(), Font.PLAIN, fontInfo.getFontSize());
		fontMetrics = graphics2D.getFontMetrics(font);

		return fontMetrics.getHeight();
	}

	public int getTextWidth(final String text, final FontInfo fontInfo) {

		if (text == null || text.length() == 0) {
			return 0;
		}

		final Font font = new Font(fontInfo.getFontFace(), Font.PLAIN, fontInfo.getFontSize());
		fontMetrics = graphics2D.getFontMetrics(font);
		final Rectangle2D rect = fontMetrics.getStringBounds(fontInfo.isUpperCase() ? text.toUpperCase() : text,
				graphics2D);
		return (int) rect.getWidth();
	}

	public void drawImage(BufferedImage b, RescaleOp rop, int i, int j) {
		graphics2D.drawImage(b, rop, i, j);
	}

	public void drawImage(BufferedImage b2, AbsDims screenDims, AlphaComposite composite) {
		if (composite != null) {
			graphics2D.setComposite(composite);
			drawImage(b2, screenDims);
		}
	}

	public void drawGradientRect(final Color dark, final Color light, final AbsDims dims, final Direction direction) {
		GradientPaint gp = null;
		if (direction == Direction.Downward || direction == Direction.Upward) {
			gp = new GradientPaint(dims.left, dims.top, dark, dims.left, dims.bottom, light, false);
		} else if (direction == Direction.Leftward || direction == Direction.Rightward) {
			gp = new GradientPaint(dims.left, dims.top, dark, dims.right, dims.top, light, false);
		}

		graphics2D.setPaint(gp);

		graphics2D.fillRect(dims.left, dims.top, dims.right - dims.left, dims.bottom - dims.top);

	}

	public Graphics2D getGraphics2D() {
		return graphics2D;
	}

	public void drawLine(final AbsDims startToFinish, final Color color) {
		final Color save = graphics2D.getColor();

		graphics2D.setColor(color);
		graphics2D.drawLine(startToFinish.left, startToFinish.top, startToFinish.right, startToFinish.bottom);
		graphics2D.setColor(save);
	}

	public void overrideColor(Color color) {
		savedColor = graphics2D.getColor();

		graphics2D.setColor(color);
	}

	public void resetColor() {
		graphics2D.setColor(savedColor);
	}

	public BufferedImage createImage(String text) {
		// Get the bounds needed to render the text
		FontRenderContext frc = graphics2D.getFontRenderContext();
		final Font font = new Font(getFontInfo().getFontFace(), Font.PLAIN, getFontInfo().getFontSize());

		TextLayout layout = new TextLayout(text, font, frc);
		Rectangle2D bounds = layout.getBounds();

		// Get the width needed to render this piece of text
		// 2 pixels were added here due to problem with cutting of end of text
		int stringWidth = (int) (Math.ceil(bounds.getWidth())) + 2;

		// Get the height from generic font info
		// This way, all strings in this font will have same height
		// and vertical alignment
		FontMetrics fm = graphics2D.getFontMetrics();
		int stringHeight = fm.getHeight();

		// Make an image to contain string
		BufferedImage im = new BufferedImage(stringWidth, stringHeight, BufferedImage.TRANSLUCENT);// TYPE_3BYTE_BGR);

		// Set the font and colours on the image
		Graphics2D graphics = im.createGraphics();

		// Set colours and clear rectangle
		graphics.setBackground(Environment.getDarkBlue());// new Color(this.br,
															// this.bg,
															// this.bb));
		graphics.setColor(Environment.getLightBlue());// new Color(this.r,
														// this.g, this.b));
		graphics.clearRect(0, 0, stringWidth, stringHeight);

		// Set the font to use
		graphics.setFont(font);

		// Position text on baseline, with first character exactly against
		// left margin
		layout.draw(graphics, 0, WindowManager.getTextHeight(fontInfo) - fontMetrics.getDescent());

		// Return the image
		return im;
	}

	public void drawGaussianFont(String text, final int x, final int y) {
		if (text == null || text.isEmpty()) {
			return;
		}

		final BufferedImage image = createImage(text);
		final BufferedImage image2 = createImage(text);
		final GaussianFilter filter = new GaussianFilter(3);
		filter.filter(image, image2);
		drawImage(image2, x, y);
		final Color saved = getFontInfo().getTextColor();
		getFontInfo().setTextColor(Environment.getLightBlue());
		drawText(text, x, y);
		getFontInfo().setTextColor(saved);
	}

	private void drawImage(BufferedImage image, int x, int y) {
		final AbsDims dims = new AbsDims(x, y, image.getWidth() + x, image.getHeight() + y);

		drawImage(image, dims);
	}

}
