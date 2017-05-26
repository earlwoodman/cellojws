package com.rallycallsoftware.cellojws.stock;

import com.rallycallsoftware.cellojws.controls.CompletionCallback;
import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.controls.kenburns.KBControl;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class KenBurnsScreen extends Screen implements CompletionCallback {

	private CompletionCallback callback = null;

	private KBControl main;

	private String overlay;

	private Control overlayCtrl;

	public KenBurnsScreen(final CompletionCallback callback_, final String file, final String overlay_) {
		super(environment.getFullScreenDims(), null);

		AbsDims dims = environment.getFullScreenDims();
		callback = callback_;

		overlay = overlay_;
		overlayCtrl = new Control(dims, null);
		overlayCtrl.setImage(new Image("/" + overlay, true));

		main = new KBControl(file, dims, null);
		addControl(main);
		addControl(overlayCtrl);

	}

	public void start() {
		main.start();
	}

	@Override
	public void refresh() {

	}

	@Override
	public boolean validate() {
		return false;
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
	public void completed() {
		if (callback != null) {
			callback.completed();
		}
	}

}
