package com.cellojws.controls.kenburns;

import java.awt.image.BufferedImage;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.image.Image;

public interface KBCommand
{

	void setImage(Image image);

	Image getImage();

	void setStartTime(long totalTime);

	void setFinishTime(long totalTime);

	long getFinishTime();

	long getStartTime();

	void setElapsed(long i);

	long getElapsed();

	BufferedImage getFinishImage();

	BufferedImage getStartImage();

	void render(Graphics graphics, float completePct, AbsDims screenDims);

}
