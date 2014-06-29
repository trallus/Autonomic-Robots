package de.httpServer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import de.logger.Log;

public class TranactionHandler implements InvocationHandler {
	
	private final UserManager userManager;
	
	public TranactionHandler ( Object target ) {
		userManager = (UserManager) target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Log.log("Starting Transaction: " + method.getName() + " with Args: " + Arrays.toString(args));
		
		Object result = null;
		
		userManager.getPersistence().beginTransaction();
		
		boolean correct = true;
		try {
			result = method.invoke( userManager, args);
		} catch ( Exception e ) {
			correct = false;
			// alle Exceptions werden in eine java.lang.reflect.InvocationTargetException verpackt
			// wir wollen aber das Original
			throw e.getCause();
		} finally {
			userManager.getPersistence().endTransaction(correct);
		}
		
		return result;
	}

}
