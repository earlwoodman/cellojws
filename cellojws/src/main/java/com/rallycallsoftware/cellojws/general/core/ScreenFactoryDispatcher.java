package com.rallycallsoftware.cellojws.general.core;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.rallycallsoftware.cellojws.controls.CompletionCallback;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.Window;
import com.rallycallsoftware.cellojws.windowing.WindowBean;

public abstract class ScreenFactoryDispatcher implements CompletionCallback {

	public ScreenFactoryDispatcher() {

	}

	public void dispatch(final CommandToken<?> token) {

		if (token != null) {
			if (token.execute()) {
				return;
			} else {
				WorkerLog.error("Token " + token.toString() + " failed on execution.");
			}
		}
	}

	public void startOver() {
		final Environment environment = Environment.getEnvironment();
		environment.getWindowManager().removeAllWindows();
		environment.getWindowManager().nullAllWindows();
		environment.setTutorialMode(false);
	}

	public abstract void refresh();

	public <T extends WindowBean, U extends Window<T>> Window<T> getScreen(final Class<U> class1, final T bean, final AbsDims dims) {
		
		final Constructor<?> ctor = findWindowConstructor(class1);

		if (ctor != null) 
		{
			try {
				// This is a safe cast, since U extends Window<T> and class1 is a Class<U>.
				@SuppressWarnings("unchecked")
				Window<T> window = (Window<T>)ctor.newInstance(dims, bean);
				return window;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	private <U> Constructor<?> findWindowConstructor(Class<U> class1) {
		
		for (final Constructor<?> ctor : class1.getConstructors())
		{
			final AnnotatedType[] types = ctor.getAnnotatedParameterTypes();
			if (types.length == 2)
			{
				try {
					if (Class.forName(types[0].getType().getTypeName()) == AbsDims.class
							&& WindowBean.class.isAssignableFrom(Class.forName(types[1].getType().getTypeName())))
					{
						return ctor;
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	private ScreenFactoryDispatcher screenFactoryDispatcher;

	public ScreenFactoryDispatcher getScreenFactoryDispatcher() {
		return screenFactoryDispatcher;
	}

}
