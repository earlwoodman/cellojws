package com.rallycallsoftware.cellojws.controls;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class Animation extends Control {

	private List<Control> frames = new ArrayList<Control>();

	private boolean loop;

	private long lastTick;

	private int msBetweenFrames;

	private int nextFrame;

	private CompletionCallback callback;

	public Animation(final AbsDims dim, final CommandToken<?> token_, final String frameName, final boolean loop_,
			final int frameRate_, final CompletionCallback callback_, final String fileExtension) {
		super(dim, token_);

		loadFrames(frameName, fileExtension);

		loop = loop_;

		msBetweenFrames = (int) (1000F / (float) frameRate_);

		nextFrame = 0;

		lastTick = System.currentTimeMillis();

		callback = callback_;
	}

	private void loadFrames(final String frameName, final String extension) {
		Image frame;
		Control frameCtl;
		boolean loading = true;
		int i = 0;
		while (loading) {
			final String filename = frameName + getIndexAsString(i) + extension;
			frame = new Image(filename, false);
			frame.load();
			if (!frame.isLoaded()) {
				loading = false;
			} else {
				frameCtl = new Control(getDimensions(), null);
				frameCtl.setImage(frame);
				frames.add(frameCtl);
				i++;
			}
		}
	}

	private String getIndexAsString(final int index) {
		String val = Integer.valueOf(index).toString();
		for (int i = val.length(); i < 3; i++) {
			val = "0" + val;
		}

		return val;
	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {
		final long latestTick = System.currentTimeMillis();

		if (latestTick - lastTick > msBetweenFrames) {
			lastTick = System.currentTimeMillis();
			nextFrame++;

			if (nextFrame == frames.size() && loop) {
				nextFrame = 0;
			} else if (nextFrame == frames.size()) {
				nextFrame--;
				callback.completed();
			}

		}

		frames.get(nextFrame).render(graphics, mousedown);

	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		callback.completed();

		return true;
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) {

	}

}
