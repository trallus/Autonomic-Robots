package de;

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
 * Implementation of the CRUDIF, now without any sort of checks...
 * 
 * @author Mike Kiekebusch
 * @version 0.3
 */
public class CRUDWorker implements CRUDIF {

    private final EntityManagerFactory emf;

    private final PersistenceUnitUtil util;

    /**
     * Just Initializing the entityManagerFactory
     */
    public CRUDWorker() {
	emf = Persistence.createEntityManagerFactory("monster");
	util = emf.getPersistenceUnitUtil();
    }

    /**
     * @see de.CRUDIF#insert(java.lang.Object)
     */
    @Override
    public void insert(Object obj) {
	final EntityManager em = emf.createEntityManager();
	final EntityTransaction trans = em.getTransaction();
	trans.begin();
	em.persist(obj);
	trans.commit();
	em.close();
    }

    /**
     * @see de.CRUDIF#update(java.lang.Object)
     */
    @Override
    public void update(Object obj) {
	final EntityManager em = emf.createEntityManager();
	final EntityTransaction trans = em.getTransaction();
	trans.begin();
	em.merge(obj);
	trans.commit();
	em.close();
    }

    /**
     * @see de.CRUDIF#remove(java.lang.Object)
     */
    @Override
    public void remove(Object obj) {
	final EntityManager em = emf.createEntityManager();
	final EntityTransaction trans = em.getTransaction();
	final Object id = util.getIdentifier(obj);
	final Object removable = em.find(obj.getClass(), id);
	trans.begin();
	em.remove(removable);
	trans.commit();
	em.close();
    }

    /**
     * @see de.CRUDIF#readID(java.lang.Class, long)
     */
    @Override
    public <T> T readID(Class<T> arg, long id) {
	if(arg == null)
	    throw new IllegalArgumentException();
	final EntityManager em = emf.createEntityManager();
	final T result = em.find(arg, id);
	em.close();
	if(result == null)
	    throw new EntityNotFoundException();
	return result;
    }

    /**
     * @see de.CRUDIF#readAll(java.lang.Class)
     */
    @Override
    public <T> List<T> readAll(Class<T> arg) {
	if(arg == null)
	    throw new IllegalArgumentException();
	final List<T> result = new ArrayList<>();
	final EntityManager em = emf.createEntityManager();
	final Query query = em.createQuery("SELECT x FROM " + arg.getName()
		+ " x");
	@SuppressWarnings("unchecked")
	final List<T> temp = query.getResultList();
	em.close();
	result.addAll(temp);
	return result;
    }

    /**
     * @see de.CRUDIF#readAll(java.lang.Class, java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public <T> List<T> readAll(Class<T> arg, String attributName,
	    Object attributValue) {
	if (arg == null || attributName == null || attributValue == null)
	    throw new IllegalArgumentException();
	final List<T> result = new ArrayList<>();
	final EntityManager em = emf.createEntityManager();
	final Query query = em.createQuery("SELECT x FROM " + arg.getName()
		+ " x WHERE x." + attributName + " = :value");
	query.setParameter("value", attributValue);
	@SuppressWarnings("unchecked")
	final List<T> temp = query.getResultList();
	em.close();
	result.addAll(temp);
	return result;
    }

}
