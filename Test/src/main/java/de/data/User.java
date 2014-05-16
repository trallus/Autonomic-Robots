package de.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import de.persistence.CRUDIF;

public class User {
	private final CRUDIF db;
	private DBUser dbUser;
	
	public User ( CRUDIF db ) {
		this.db = db;
	}
	
	public void logIn ( String eMail, String password ) throws EmailNotFoundException {
		if ( eMail == null || password == null )
			throw new IllegalArgumentException();

		// check if eMail alredy in use
		List<DBUser> list = db.readAll ( DBUser.class, "eMail", eMail );
		if ( list.size() > 1 ) {
			throw new RuntimeException ("(To) many Users with this eMail found");
		}
		
		if ( list.size() == 0 ) {
			throw new EmailNotFoundException();
		}
		
		dbUser = list.get(0);
	}
	
	public void register ( String eMail, String password, String name ) throws Exception {
		if ( eMail == null || password == null || name == null )
			throw new IllegalArgumentException();
		
		if ( dbUser != null ) {
			throw new UnsupportedOperationException ("User alredy registed.");
		}
		
		// check if eMail alredy in use
		List<DBUser> list = db.readAll ( DBUser.class, "eMail", eMail );
		if ( list.size() > 0 ) {
			throw new EmailInUseException();
		}
		
		// create DBUser
		dbUser = new DBUser();
		dbUser.seteMail(eMail);
		dbUser.setName(name);
		dbUser.setPwHash ( convertToMD5Hash ( password ) );
		
		writeToDB();
	}
	
	void writeToDB () {
		db.insert( dbUser );
	}
	
	String convertToMD5Hash ( String string ) throws NoSuchAlgorithmException {
		
		// generate md5 String
		byte[] pwBytes = ( string ).getBytes();
		
		MessageDigest md = MessageDigest.getInstance( "MD5" );
		
		byte[] byteHash = md.digest( pwBytes );
		
		String md5String = "";
		
		for ( byte b : byteHash ) {
			md5String += b;
		}
		
		return ( md5String );
	}
}
