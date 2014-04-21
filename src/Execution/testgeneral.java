package Execution;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;

public class testgeneral {

	
	
	  public static final String region = "/home/ely/workspace/proget_BDC/region.rdf";
	    public static final String liaison = "/home/ely/workspace/proget_BDC/liaison.rdf";
	    public static final String sprt = "/home/ely/workspace/proget_BDC/sport.rdf";
	    public static final String sportc = "/home/ely/workspace/proget_BDC/Chomage.rdf";
	public static void main(String[] args) {
		  Model ModelGeneral= ModelFactory.createDefaultModel();
		  
		  String NameSpace = "http://www.ely.fr#";
		  ModelGeneral.setNsPrefix("sport", NameSpace);
		  
			 
		   String directory = "/home/ely/workspace/proget_BDC/TDB" ;
	        
		   Dataset ds = TDBFactory.createDataset(directory) ;
		   Model modelTdb = ds.getNamedModel("Region" );
	        	    	
	    	String nsm1 = "http://rdf.insee.fr/def/geo#";	    	
	    	modelTdb.setNsPrefix("insee", nsm1);
	    	FileManager.get().readModel( modelTdb, region );
	    	
	    	ModelGeneral.add(modelTdb);
	    	
	    	FileManager.get().readModel( ModelGeneral, liaison );
	    	FileManager.get().readModel( ModelGeneral, sportc );
	    	FileManager.get().readModel( ModelGeneral, sprt );
	    	
	    	String req2=
	           		"PREFIX b: <http://rdf.insee.fr/geo/>" +
	           		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
	           		"PREFIX ely: <http://www.ely.fr#>" +
	           		"PREFIX spt: <http://www.sport.fr#>" +
	           		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
	           		"PREFIX sports: <http://www.sportsfoot.fr#>" +
	              		"SELECT ?c ?n ?nbr ?e " +
	              		"WHERE {  ?y owl:sameAs ?z . "+
	              		"?y b:code_commune ?c . "+
	              		"?y b:nom ?n . "+
	              		"?z ely:ApourEquipe ?nbr . "+
	              		"?nbr owl:sameAs ?x . ?x sports:joueurs ?e} " ;
	    	
	    	
	    	
	       String req1=
  		"PREFIX b: <http://rdf.insee.fr/geo/>" +
  		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
  		"PREFIX ely: <http://www.ely.fr#>" +
  		
  		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
     "SELECT ?c ?n ?nbr " +
    	"WHERE {  ?y owl:sameAs ?z . ?y b:code_commune ?c . ?y b:nom ?n . ?z ely:ApourEquipe ?nbr } " ;
	    	
	       
	       
	       
	       
	       String req22=
	    	  		"PREFIX b: <http://rdf.insee.fr/geo/>" +
	    	  		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
	    	  		"PREFIX ely: <http://www.lirmmm.fr/ely/>" +
	    	  		
	    	  		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
	    	     "SELECT ?c ?n ?nbr " +
	    	    	"WHERE {  ?y owl:sameAs ?z . "
	    	    	+ "?y b:code_commune ?c . "
	    	    	+ "?y b:nom ?n . "
	    	    	+ "?z ely:arronr ?nbr } " ;
	       
	       
	       
	       
	       
	       
	   	String req3=
           		"PREFIX b: <http://rdf.insee.fr/geo/>" +
           		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
           		"PREFIX ely: <http://www.ely.fr#>" +
           		"PREFIX com: <http://www.lirmmm.fr/ely/>" +
           		"PREFIX spt: <http://www.sport.fr#>" +
           		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
           		"PREFIX sports: <http://www.sportsfoot.fr#>" +
              		"SELECT ?codeCommune ?ville ?equipe ?e  ?arrondissement " +
              		"WHERE {  ?t owl:sameAs ?d .   ?d owl:sameAs ?h . "+
              		"?t b:code_commune ?codeCommune . "+
              		"?t b:nom ?ville . "+
              		"?h ely:ApourEquipe ?equipe . "+
              		"?equipe owl:sameAs ?x . " +
              		" ?x sports:joueurs ?e . "
              	// + "?d com:narm ?arrondissement "
              	 + "} " ;
	       
	       
	       
	       
	       
	       
	       Query query = QueryFactory.create(req3);
	    	QueryExecution qexec = QueryExecutionFactory.create(query, ModelGeneral) ;
	       
	       
	   	try {
	  	
  	ResultSet rs = qexec.execSelect() ;
	   	ResultSetFormatter.out(System.out, rs, query);
  	
	   	}
	   	finally
	   	{
	   	  qexec.close() ;
	   	}
	   	
	       
	       try {       
		    	  FileOutputStream outStream = new FileOutputStream("result.rdf");
		             // exporte le resultat dans un fichier
		    //	  ModelGeneral.write(System.out, "RDF/XML");
		             outStream.close();
		             
		         
	 	 }
		      catch (FileNotFoundException e) {System.out.println("File not found");}
		      catch (IOException e) {System.out.println("IO problem");}
	}

}
