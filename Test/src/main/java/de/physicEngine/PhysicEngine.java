package de.physicEngine;

/**
 * A Simple Fassade Interface for the Physic Engine
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 07.10.2014
 */
public interface PhysicEngine {
    /**
     * Adds a PhysikObject to the Physic Engine so that it can be considered
     * @param po The PhysikObject that should be added
     */
    void addPhysicObject(PhysikObject po);
    /**
     * Removes a PhysikObject from the Physic Engine so that it is no longer considered
     * @param po The PhysikObject that should be removed
     */
    void removePhysicObject(PhysikObject po);
    
}
