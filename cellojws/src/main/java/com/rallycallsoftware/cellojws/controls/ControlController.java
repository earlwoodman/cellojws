package com.rallycallsoftware.cellojws.controls;

import com.rallycallsoftware.cellojws.controls.button.RadioButtonInfoBean;
import com.rallycallsoftware.cellojws.controls.button.RadioButtonType;
import com.rallycallsoftware.cellojws.controls.button.TabButton;
import com.rallycallsoftware.cellojws.controls.listbox.ListBox;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class ControlController extends Controller {

	/**
	 * Change the tab and trigger the token for the container
	 * 
	 * @param token
	 */
	public static void changeTab(final CommandToken<TabButton> token) {
		final TabButton tabButton = token.getPayload();
		final Tab tab = tabButton.getTab();
		tab.setCurrentTab(tabButton);
		final CommandToken<?> containerToken = tabButton.getTabContainer().getToken();
		if (containerToken == null) {
			// We've tried to prevent this. Throw an exception
			throw new NullPointerException("CommandToken was null. How did this happen??");
		}
		containerToken.execute();
	}

	public static void turnOnRadioButton(final CommandToken<RadioButtonInfoBean> token) {
		final RadioButtonInfoBean bean = token.getPayload();

		// Turn off all except the one that was clicked
		//
		bean.getButtons().stream().forEach(x -> {
			((RadioButtonType) x.getButtonType()).setChecked(x == bean.getClickedButton());
		});

		// Now chain in the next token
		final CommandToken<?> chainedToken = token.getPayload().getToken();
		if (chainedToken != null) {
			chainedToken.execute();
		}
	}

	public static void dropDown(final CommandToken<DropDownList> token) {
		((DropDownList) token.getPayload()).dropDown();
	}

	public static void up(final CommandToken<PlusMinus> token) {
		token.getPayload().increment();
		token.getPayload().getToken().execute();
	}

	public static void down(final CommandToken<PlusMinus> token) {
		token.getPayload().decrement();
		token.getPayload().getToken().execute();
	}

	public static void upVSB(final CommandToken<VerticalScrollBar> token) {
		token.getPayload().up();
	}

	public static void downVSB(final CommandToken<VerticalScrollBar> token) {
		token.getPayload().down();
	}

	public static void exitGame(final CommandToken<?> token) {
		Environment.getEnvironment().stopRunning();
	}

	public static void close(final CommandToken<?> token) {
		Environment.getEnvironment().getWindowManager().removeTopWindow();
	}

	public static void showWebsite(final CommandToken<?> token) {

	}

	public static void pause(final CommandToken<?> token) {

	}

	public static void save(final CommandToken<?> token) {

	}

	/**
	 * Intentionally do nothing!
	 * 
	 * Replacement for null
	 * 
	 * @param token
	 */
	public static void noOp(final CommandToken<?> token) {

	}

	public static void removeSelectedItem(final CommandToken<ListBox> token) {
		final ListBox box = token.getPayload();

		if (box != null) {
			box.removeSelectedItems();
		}
	}

}
