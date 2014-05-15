package de.persistence;

import java.util.List;

/**
 * Declares the four tipicall CRUD (Create Read Update Delete) operations
 * 
 * @author Mike Kiekebusch
 * @version 0.2
 */
public interface CRUDIF {

    /**
     * Inserts the given Entity in the DataBase
     * 
     * @param obj
     *            The Entity that should be inserted
     */
    void insert(Object obj);

    /**
     * Updates the given Entity that already exist in the DataBase
     * 
     * @param obj
     *            The Entity which should be upgraded
     */
    void update(Object obj);

    /**
     * Removes the given Entity from the Database
     * 
     * @param obj
     *            The Entity that should be removed
     */
    void remove(Object obj);

    /**
     * Read the Entity of the given class with the given id from the DataBase
     * 
     * @param arg
     *            The class of the desired Entity
     * @param id
     *            The id of the desired Entity
     * @return The desired Entity
     */
    <T> T readID(Class<T> arg, long id);

    /**
     * Reads all Entities of the given class from the DataBase
     * 
     * @param arg
     *            The class of the desired Entities
     * @return List of all found Entities
     */
    <T> List<T> readAll(Class<T> arg);

    /**
     * Reads all Entities of the given class with the attribut Name-Value pair
     * from the DataBase
     * 
     * @param arg
     *            The class of the desired Entities
     * @param attributName
     *            The EXACT name of the attribute
     * @param attributValue
     *            The value of the attribute
     * @return List of all found Entities
     */
    <T> List<T> readAll(Class<T> arg, String attributName, Object attributValue);
}
