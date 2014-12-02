package de.httpServer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import de.persistence.CRUDIF;
import de.persistence.PersistenceFacade;

/**
 * a Transection handler for dynamic proxyd user manager
 * @author ko
 *
 */
public class TranactionHandler implements InvocationHandler {
	
	/**
	 * the user manager
	 */
	private final UserManager userManager;
	
	/**
	 * set the Target for the dynamic proxy
	 * @param target
	 */
	public TranactionHandler ( final Object target ) {
		userManager = (UserManager) target;
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args)
			throws Throwable {
		
		final PersistenceFacade psf = userManager.getPersistence();
		Object result = null;
		
		final CRUDIF db = psf.getDBController();
		args[0] = db;
		
		psf.beginTransaction(db);
		
		boolean correct = false;
		try {
			result = method.invoke( userManager, args);
			correct = true;
		} catch ( Exception e ) {
			// alle Exceptions werden in eine java.lang.reflect.InvocationTargetException verpackt
			// wir wollen aber das Original
			throw e.getCause();
		} finally {
			psf.endTransaction(correct, db);
		}
		
		return result;
	}

}
