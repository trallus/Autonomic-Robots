package de.game.behaviour;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import de.game.Robot;

/**
 * A Factory that returns Behaviour Instances
 * 
 * @author mike
 * @version 0.2
 */
public class BehaviourFactory {

    private static BehaviourFactory factory;

    public static BehaviourFactory getBehaviourFactory() {
	if (factory == null) {
	    try {
		final JAXBContext jc = JAXBContext
			.newInstance(BehaviourMap.class);
		final Unmarshaller um = jc.createUnmarshaller();
		final String location = "/META-INF/Behaviours.xml";
		final URL url = BehaviourFactory.class.getClass().getResource(location);
		final BehaviourMap map = (BehaviourMap) um.unmarshal(url);
		factory = new BehaviourFactory(map);
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return factory;
    }
    
    private final Map<String,String> behaviours;

    private BehaviourFactory(final BehaviourMap map) {
	behaviours = map.behaviours; //Unmarshall Wrapper no longer needed so unwrapping the map
    }

    public Collection<String> getBehaviours() {
	return behaviours.keySet();
    }
    
    public Behaviour getInstanceOfBehaviour(String name, Robot robot){
	Behaviour behaviour = null;
	try {
	    behaviour = (Behaviour) Class.forName(behaviours.get(name)).getConstructor(Robot.class,String.class).newInstance(robot,name);
	}
	catch(Exception e){
	    CouldNotCreateBehaviourException exception;
	    exception = new CouldNotCreateBehaviourException("Could not create specified Behaviour", e, true);
	    exception.putParameter("Name", name);
	    exception.putParameter("Robot", robot); //TODO hat kein toString()
	    throw exception;
	}
	return behaviour;
    }
    
    /**
     * A Wrapper of Map for the unmarshalling of Behaviours.xml
     * @author mike
     * @version 0.1
     */
    @XmlRootElement(name = "BehaviourFactory-Map")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class BehaviourMap{
	
	private Map<String,String> behaviours;
    }
}
