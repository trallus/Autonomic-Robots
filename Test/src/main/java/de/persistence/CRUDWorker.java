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
	if(em == null){
	    final PersistenceException pex = new PersistenceException("CRUDWorker could not be initialized", new NullPointerException(), false);
	    pex.putParameter("EntityManager", em);
	    throw pex;
	}
	this.em = em;
    }
    
    protected EntityManager getEntityManager(){
	return em;
    }

    /**
     * @see de.persistence.CRUDIF#insert(java.lang.Object)
     */
    @Override
    public void insert(final Object obj) {
	try {
	    if(!em.getTransaction().isActive())
		throw new IllegalStateException("You must begin transaction first");
	    em.persist(obj);
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not insert Object into database", arg0, false);
	    pex.putParameter("Object", obj);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#update(java.lang.Object)
     */
    @Override
    public void update(final Object obj) {
	try {
	    if(!em.getTransaction().isActive())
		throw new IllegalStateException("You must begin transaction first");
	    em.merge(obj);
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not update Object in database", arg0, false);
	    pex.putParameter("Object", obj);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#remove(java.lang.Object)
     */
    @Override
    public void remove(final Object obj) {
	try {
	    if(!em.getTransaction().isActive())
		throw new IllegalStateException("You must begin transaction first");
	    em.remove(obj);
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not remove Object from database", arg0, false);
	    pex.putParameter("Object", obj);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#readID(java.lang.Class, long)
     */
    @Override
    public <T> T readID(final Class<T> arg, final long id) {
	try {
	    final T result = em.find(arg, id);
	    if (result == null)
		throw new EntityNotFoundException();
	    return result;
	}
	catch (Exception arg0) {
	    final PersistenceException pex = new PersistenceException("Could not read Object from database", arg0, false);
	    pex.putParameter("Type of Object", arg.getName());
	    pex.putParameter("Id", id);
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#readAll(java.lang.Class)
     */
    @Override
    public <T> List<T> readAll(final Class<T> arg) {
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
	    final PersistenceException pex = new PersistenceException("Could not read Objects from database", arg0, false);
	    pex.putParameter("Type of Objects", arg.getName());
	    throw pex;
	}
    }

    /**
     * @see de.persistence.CRUDIF#readAll(java.lang.Class, java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public <T> List<T> readAll(final Class<T> arg, final String attributName,
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
	    final PersistenceException pex = new PersistenceException("Could not read Objects from database", arg0, false);
	    pex.putParameter("Type of Objects", arg.getName());
	    pex.putParameter("Attribut Name", attributName);
	    pex.putParameter("Attribut Value", attributValue);
	    throw pex;
	}
    }

}
