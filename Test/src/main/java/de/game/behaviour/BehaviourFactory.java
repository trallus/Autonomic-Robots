package de.game.behaviour;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.game.Robot;

/**
 * A Factory that returns Behaviour Instances
 * 
 * @author mike
 * @version 0.3
 */
public class BehaviourFactory {

    /**
     * Map that holds the names of Behaviours as Key and the full calified class
     * name as Value.
     */
    private Map<String, String> behaviours; // Not final because the try-catch
					    // in the constructor will invoke
					    // compiler failure

    /**
     * Initializes the BehaviourFactory with the Map stored in the
     * /META-INF/Behaviours.xml
     */
    public BehaviourFactory() {
	try {
	    final JAXBContext jc = JAXBContext.newInstance(BehaviourMap.class);
	    final Unmarshaller um = jc.createUnmarshaller();
	    final String location = "/META-INF/Behaviours.xml";
	    final URL url = BehaviourFactory.class.getClass().getResource(
		    location);
	    BehaviourMap map;
	    map = (BehaviourMap) um.unmarshal(url);
	    behaviours = map.behaviours; // Unmarshall Wrapper no longer needed
					 // so unwrapping the map
	}
	catch (JAXBException e) {
	    final CouldNotInitializeBehaviourFactoryException ex = new CouldNotInitializeBehaviourFactoryException(
		    "BehaviourFactory could not be created", e, false);
	    throw ex;
	}
    }

    /**
     * Returns a Collection of the BehaviourNames that are used to get specific
     * Behaviours.
     * 
     * @return The Collection of known behaviours names
     */
    public Collection<String> getBehaviours() {
	return behaviours.keySet();
    }

    /**
     * Generates a Behaviour with the given name for the specified robot
     * @param name The name of the behaviour
     * @param robot The Robot which will use this behaviour
     * @return The generated Behaviour
     */
    public Behaviour getInstanceOfBehaviour(String name, Robot robot) {
	Behaviour behaviour = null;
	try {
	    behaviour = (Behaviour) Class.forName(behaviours.get(name))
		    .getConstructor(Robot.class, String.class)
		    .newInstance(robot, name);
	}
	catch (Exception e) {
	    CouldNotCreateBehaviourException exception;
	    exception = new CouldNotCreateBehaviourException(
		    "Could not create specified Behaviour", e, true);
	    exception.putParameter("Name", name);
	    exception.putParameter("Robot", robot); // TODO hat kein toString()
	    throw exception;
	}
	return behaviour;
    }

    /**
     * A Wrapper of Map for the unmarshalling of Behaviours.xml
     * 
     * @author mike
     * @version 0.1
     */
    @XmlRootElement(name = "BehaviourFactory-Map")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class BehaviourMap {
	private Map<String, String> behaviours;
    }
}
