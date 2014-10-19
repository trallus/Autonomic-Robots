package de.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

/**
 * Implementation of the CRUDIF
 * 
 * @author Mike Kiekebusch
 * @version 0.5
 */
public class CRUDWorker implements CRUDIF {
    
    private final EntityManager em;

    /**
     * Just Initializing the entityManagerFactory
     */
    protected CRUDWorker(final EntityManager em) {
	try {
	    this.em = em;
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not initialize CRUDWorker", arg0, false);
	    pex.putParameter("em", em);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#insert(java.lang.Object)
     */
    @Override
    public void insert(Object obj) {
	try {
	    if(!em.getTransaction().isActive())
		throw new IllegalStateException("You must begin transaction first");
	    em.persist(obj);
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not insert Object into database", arg0, false);
	    pex.putParameter("obj", obj);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#update(java.lang.Object)
     */
    @Override
    public void update(Object obj) {
	try {
	    if(!em.getTransaction().isActive())
		throw new IllegalStateException("You must begin transaction first");
	    em.merge(obj);
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not update Object in database", arg0, false);
	    pex.putParameter("obj", obj);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#remove(java.lang.Object)
     */
    @Override
    public void remove(Object obj) {
	try {
	    if(!em.getTransaction().isActive())
		throw new IllegalStateException("You must begin transaction first");
	    em.remove(obj);
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Couldn't remove Object from database", arg0, false);
	    pex.putParameter("obj", obj);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#readID(java.lang.Class, long)
     */
    @Override
    public <T> T readID(Class<T> arg, long id) {
	try {
	    final T result = em.find(arg, id);
	    if (result == null)
		throw new EntityNotFoundException();
	    return result;
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Couldn't read Object from database", arg0, false);
	    pex.putParameter("Object type", arg.getName());
	    pex.putParameter("id", ""+id);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#readAll(java.lang.Class)
     */
    @Override
    public <T> List<T> readAll(Class<T> arg) {
	try {
	    final List<T> result = new ArrayList<>();
	    final Query query = em.createQuery("SELECT x FROM " + arg.getName()
		    + " x");
	    @SuppressWarnings("unchecked")
	    final List<T> temp = query.getResultList();
	    result.addAll(temp);
	    return result;
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Couldn't read Objects from database", arg0, false);
	    pex.putParameter("Object type", arg.getName());
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#readAll(java.lang.Class, java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public <T> List<T> readAll(Class<T> arg, String attributName,
	    Object attributValue) {
	try {
	    final List<T> result = new ArrayList<>();
	    final Query query = em.createQuery("SELECT x FROM " + arg.getName()
		    + " x WHERE x." + attributName + " = :value");
	    query.setParameter("value", attributValue);
	    @SuppressWarnings("unchecked")
	    final List<T> temp = query.getResultList();
	    result.addAll(temp);
	    return result;
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Couldn't read Objects from database", arg0, false);
	    pex.putParameter("Object type", arg.getName());
	    pex.putParameter("attributName", attributName);
	    pex.putParameter("attributValue", attributValue);
	    throw pex;
	}
    }

}
