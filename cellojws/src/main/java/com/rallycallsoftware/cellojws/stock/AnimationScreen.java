package com.rallycallsoftware.cellojws.stock;

import com.rallycallsoftware.cellojws.controls.Animation;
import com.rallycallsoftware.cellojws.controls.CompletionCallback;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class AnimationScreen extends Screen {

	private Animation animation;

	public AnimationScreen(final AbsDims dim, final String frameName, final boolean loop, final int frameRate,
			final CompletionCallback callback) {
		super(dim, null);

		setImage(null);

		final AbsDims animDims = new AbsDims(0, 0, dim.getAbsWidth(), dim.getAbsHeight());
		animation = new Animation(animDims, null, environment.getExecutionPath() + frameName, loop, frameRate, callback,
				".jpg");
		addControl(animation);
	}

	@Override
	public CommandToken<?> enterKeyPressed() {
		return null;
	}

	@Override
	public CommandToken<?> escapeKeyPressed() {
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
