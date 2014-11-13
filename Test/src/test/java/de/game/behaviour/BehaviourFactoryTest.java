package de.game.behaviour;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author mike
 * @version 0.1
 */
public class BehaviourFactoryTest {

    private static BehaviourFactory bf;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	bf = new BehaviourFactory();
    }

    @Test
    public void test() {
	final Collection<String>names = bf.getBehaviours();
	assertEquals("At least one Behaviour in Behaviour.xml",true,names.size()>0);
	final String name = names.iterator().next();
	final Behaviour behaviour = bf.getInstanceOfBehaviour(name, null);
	assertEquals(name, behaviour.getName());
    }

}
