package de.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;

/**
 * Implementation of the CRUDIF
 * 
 * @author Mike Kiekebusch
 * @version 0.3
 */
public class CRUDWorker implements CRUDIF {
    
    private final EntityManager em;

    private final PersistenceUnitUtil util;

    /**
     * Just Initializing the entityManagerFactory
     */
    protected CRUDWorker(final EntityManager em, final PersistenceUnitUtil util) {
	try {
	    this.util = util;
	    this.em = em;
	}
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
	}
    }

    /**
     * @see de.persistence.CRUDIF#insert(java.lang.Object)
     */
    @Override
    public void insert(Object obj) {
	try {
	    final EntityTransaction trans = em.getTransaction();
	    trans.begin();
	    em.persist(obj);
	    trans.commit();
	}
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
	}
    }

    /**
     * @see de.persistence.CRUDIF#update(java.lang.Object)
     */
    @Override
    public void update(Object obj) {
	try {
	    final EntityTransaction trans = em.getTransaction();
	    trans.begin();
	    em.merge(obj);
	    trans.commit();
	}
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
	}
    }

    /**
     * @see de.persistence.CRUDIF#remove(java.lang.Object)
     */
    @Override
    public void remove(Object obj) {
	try {
	    final EntityTransaction trans = em.getTransaction();
	    final Object id = util.getIdentifier(obj);
	    final Object removable = em.find(obj.getClass(), id);
	    trans.begin();
	    em.remove(removable);
	    trans.commit();
	}
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
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
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
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
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
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
	catch (Throwable arg0) {
	    throw new PersistenceException(arg0);
	}
    }

}
