package com.rallycallsoftware.cellojws.stock.messagescreen;

import java.awt.Point;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.controls.ControlController;
import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.stock.Screen;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class MessageScreen extends Screen {

	private Button ok;

	private Button cancel;

	private Label message;

	private Control imageCtrl;

	private CommandToken<?> okAction;

	public MessageScreen(final AbsDims dim, final MessageScreenBean bean) {
		super(dim, environment.getSmallPopupImage());

		okAction = bean.getClientToken();

		Point okPos;
		if (bean.getMessageScreenType() == MessageScreenType.YesNo) {
			okPos = new Point((dim.getAbsWidth() - 2 * Button.getStandardButtonWidth() - Control.getStdGap()) / 2,
					dim.getAbsHeight() - Button.getStandardButtonHeight() - Control.getStdGap() * 4);
		} else {
			okPos = new Point((dim.getAbsWidth() - Button.getStandardButtonWidth()) / 2,
					dim.getAbsHeight() - Button.getStandardButtonHeight() - Control.getStdGap() * 4);
		}

		final int messageTop = okPos.y - Button.getStdGap()
				- WindowManager.getTextHeight(environment.getLabelFontInfo());

		String okCaption = "OK";
		if (bean.getMessageScreenType() == MessageScreenType.YesNo) {
			okCaption = "Yes";

			final Point cancelPos = new Point(okPos.x + Button.getStandardButtonWidth() + Button.getStdGap(),
					dim.getAbsHeight() - Button.getStandardButtonHeight() - Control.getStdGap() * 4);
			cancel = new BasicButton(SmallButtonType.getInstance(), "No", getCancelAction(), cancelPos);
			addControl(cancel);
		}

		final CommandToken<?> okToken = bean.getClientToken();
		ok = new BasicButton(SmallButtonType.getInstance(), okCaption, okToken, okPos);
		addControl(ok);

		final Image image = bean.getImage();
		final AbsDims imageDims = new AbsDims(getLeftCentered(image.getWidth()), (messageTop - image.getHeight()) / 2,
				getRightCentered(image.getWidth()), (messageTop - image.getHeight()) / 2 + image.getHeight());
		imageCtrl = new Control(imageDims, null);
		imageCtrl.setImage(image);
		addControl(imageCtrl);

		final AbsDims labelDims = new AbsDims(0, messageTop, dim.getAbsWidth(),
				messageTop + WindowManager.getTextHeight(environment.getLabelFontInfo()));
		final String messageText = bean.getMessage();
		message = new Label(labelDims, messageText);
		addControl(message);
		message.setJustification(Justification.Center);

	}

	private CommandToken<?> getCancelAction() {
		return new CommandToken<Object>(ControlController::close);
	}

	@Override
	public CommandToken<?> enterKeyPressed() {
		return okAction;
	}

	@Override
	public CommandToken<?> escapeKeyPressed() {
		return getCancelAction();
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
