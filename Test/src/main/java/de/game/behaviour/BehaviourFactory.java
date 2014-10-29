package de.game.behaviour;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.game.Robot;

/**
 * A Factory that returns Behaviour Instances
 * 
 * @author mike
 * @version 0.1
 */
@XmlRootElement(name = "BehaviourFactory")
public class BehaviourFactory {

    @XmlTransient
    private static BehaviourFactory factory;

    public static BehaviourFactory getBehaviourFactory() {
	if (factory == null) {
	    try {
		final JAXBContext jc = JAXBContext
			.newInstance(BehaviourFactory.class);
		final Unmarshaller um = jc.createUnmarshaller();
		final String location = "/META-INF/Behaviours.xml";
		final BehaviourFactory bf = (BehaviourFactory) um
			.unmarshal(BehaviourFactory.class.getClass()
				.getResource(location));
		factory = bf;
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return factory;
    }

    @XmlElementWrapper(name = "Fully_callified_class_names")
    @XmlElement(name = "Behaviour")
    private List<String> behaviours;

    private BehaviourFactory() {
	// For JAXB only
    }

    public List<String> getBehaviours() {
	return behaviours;
    }
    
    public Behaviour getInstanceOfBehaviour(String name, Robot robot){
	Behaviour behaviour = null;
	try {
	    behaviour = (Behaviour) Class.forName(name).getConstructor(Robot.class).newInstance(robot);
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
}
