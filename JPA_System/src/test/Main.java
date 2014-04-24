package test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import de.CRUDIF;
import de.CRUDWorker;

/**
 * the entrie point for the jpa tech demo
 * @author Mike Kiekebusch
 * @version 0.2
 * @since 16.04.2014
 */
public class Main {

    /**
     * @param args not used
     */
    public static void main(String[] args) {
	//Initializing
	final CRUDIF crud = CRUDWorker.getInstance();
	final Address a1 = new Address("BlubStraße", 12345);
	final TelephoneNumber t1 = new TelephoneNumber(123, 456789);
	final TelephoneNumber t2 = new TelephoneNumber(98, 7654321);
	final ArrayList<TelephoneNumber>numbers = new ArrayList<>();
	numbers.add(t1);
	numbers.add(t2);
	final Person p1 = new Person("Testie", "von Test", a1, numbers, "Hopefully i will be persistend");
	
	//CREATE & READ all
	crud.insert(p1);
	System.out.println("CREATE & READ ALL");
	for(Person p : crud.readAll(p1.getClass()))
	    System.out.println(p);
	
	//UPDATE & READ id
	p1.getNumbers().remove(t2);
	crud.update(p1);
	System.out.println("UPDATE p1 with the remove of t2 & READ id from p1");
	System.out.println(crud.readID(p1.getClass(), p1.getId()));
	
	//READ where
	System.out.println("READ where number==456789");
	for(TelephoneNumber t : crud.readAll(TelephoneNumber.class, "number", new Integer(456789)))
	    System.out.println(t);
	
	//REMOVE & READ ALL
	System.out.println("REMOVE of p1 & read all p's");
	crud.remove(p1);
	for(Person p : crud.readAll(p1.getClass()))
	    System.out.println(p);
	
	//SHUTDOWN DERBY
	try{
	    DriverManager.getConnection("jdbc:derby:;shutdown=true");
	}
	catch(SQLException e){
	    //Will be thrown from the shutdown of the database
	}
    }

}
