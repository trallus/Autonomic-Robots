package de;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;

/**
 * Implementation of the CRUDIF using the Sigelton Pattern
 * The whole implementation is NOT Thread Safe
 * 
 * @author Mike Kiekebusch
 * @version 0.2
 */
public class CRUDWorker implements CRUDIF {

    /**
     * The Only Instance of this Class
     */
    private static CRUDWorker instance = null;

    /**
     * @return The only Instance of this class
     */
    public final static CRUDWorker getInstance() {
	if(instance==null)
	    instance = new CRUDWorker();
	return instance;
    }

    private final EntityManagerFactory emf;
    
    private final PersistenceUnitUtil util;

    /**
     * Just Initializing the entityManagerFactory
     */
    private CRUDWorker() {
	emf = Persistence.createEntityManagerFactory("monster");
	util = emf.getPersistenceUnitUtil();
    }

    /**
     * @see de.CRUDIF#insert(java.lang.Object)
     */
    @Override
    public boolean insert(Object obj) {
	boolean result = false;
	final EntityManager em = emf.createEntityManager();
	final EntityTransaction trans = em.getTransaction();
	final Object id = util.getIdentifier(obj);
	if(em.find(obj.getClass(), id)==null){ //Instance allready in Database? No then...
	    trans.begin();
	    em.persist(obj); //Insertion of the Instance into the Database
	    trans.commit();
	    result = true;
	}
	em.close();
	return result;
    }

    /**
     * @see de.CRUDIF#update(java.lang.Object)
     */
    @Override
    public boolean update(Object obj) {
	boolean result = false;
	final EntityManager em = emf.createEntityManager();
	final EntityTransaction trans = em.getTransaction();
	final Object id = util.getIdentifier(obj);
	if(em.find(obj.getClass(), id)!=null){ //Instance allready in Database? Yes then...
	    trans.begin();
	    em.merge(obj); //Update of the version in the DataBase
	    trans.commit();
	    result = true;
	}
	em.close();
	return result;
    }

    /**
     * @see de.CRUDIF#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object obj) {
	boolean result = false;
	final EntityManager em = emf.createEntityManager();
	final EntityTransaction trans = em.getTransaction();
	final Object id = util.getIdentifier(obj);
	final Object removable = em.find(obj.getClass(),id); //Without this i wont work... I don't now why myself :(
	if(removable!=null){ //Instance in Database? Yes then...
	    trans.begin();
	    em.remove(removable); //terminate it with the T-800
	    trans.commit();
	    result = true;
	}
	em.close();
	return result;
    }

    /**
     * @see de.CRUDIF#readID(java.lang.Class, long)
     */
    @Override
    public <T> T readID(Class<T> arg, long id) {
	final EntityManager em = emf.createEntityManager();
	final T result = em.find(arg, id);
	em.close();
	return result;
    }

    /**
     * @see de.CRUDIF#readAll(java.lang.Class)
     */
    @Override
    public <T> List<T> readAll(Class<T> arg) {
	final EntityManager em = emf.createEntityManager();
	final Query query = em.createQuery("SELECT x FROM "+arg.getName()+" x");
	@SuppressWarnings("unchecked")
	final List<T> result = query.getResultList();
	em.close();
	return result;
    }

    /**
     * @see de.CRUDIF#readAll(java.lang.Class, java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public <T> List<T> readAll(Class<T> arg, String attributName,
	    Object attributValue) {
	final EntityManager em = emf.createEntityManager();
	final Query query = em.createQuery("SELECT x FROM "+arg.getName()+" x WHERE x."+attributName+" = :value");
	query.setParameter("value", attributValue);
	@SuppressWarnings("unchecked")
	final List<T> result = query.getResultList();
	em.close();
	return result;
    }

}
