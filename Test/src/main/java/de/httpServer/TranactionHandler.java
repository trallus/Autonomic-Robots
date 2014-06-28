package de.httpServer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import de.logger.Log;
import de.persistence.PersistenceFacade;

public class TranactionHandler implements InvocationHandler {
	
	private final Object target;
	private final PersistenceFacade persistence;
	
	public TranactionHandler ( Object target ) {
		this.target = target;
		persistence = new PersistenceFacade();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Log.log("Starting Transaction: " + method.getName() + " with Args: " + Arrays.toString(args));
		// persistence.beginTransaction();
		// persistence.endTransaction(true);
		
		Object result = null;
		
		try {
			result = method.invoke( target, args);
			
		} catch ( Exception e ) {
			// alle Exceptions werden in eine java.lang.reflect.InvocationTargetException verpackt
			// wir wollen aber das Original
			throw e.getCause();
		}

		if ( result == null ) {
			result = "null Object";
		}
		
		Log.log("Transaction Result: " + result.toString());
		return result;
	}

}
