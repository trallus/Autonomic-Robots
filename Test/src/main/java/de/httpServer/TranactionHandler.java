package de.httpServer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import de.persistence.PersistenceFacade;

public class TranactionHandler implements InvocationHandler {
	
	private final UserManager userManager;
	
	public TranactionHandler ( Object target ) {
		userManager = (UserManager) target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		final PersistenceFacade psf = userManager.getPersistence();
		Object result = null;
		
		psf.beginTransaction();
		
		boolean correct = true;
		try {
			result = method.invoke( userManager, args);
		} catch ( Exception e ) {
			correct = false;
			// alle Exceptions werden in eine java.lang.reflect.InvocationTargetException verpackt
			// wir wollen aber das Original
			throw e.getCause();
		} finally {
			psf.endTransaction(correct);
		}
		
		return result;
	}

}
