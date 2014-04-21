package partied2rq;



import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

import de.fuberlin.wiwiss.d2rq.jena.ModelD2RQ;

public class partieD2rq {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model d2rqModel = new ModelD2RQ("file:mapp.n3");
		String Stringquery4=	"PREFIX v: <http://www.lirmm.fr/ely/>" +
				"SELECT ?s ?arrondissement " +
				"WHERE {" +
				"?s v:narm ?arrondissement " +
				
			"}";
				 
		

		
		QueryExecution qexec = QueryExecutionFactory.create(Stringquery4, d2rqModel) ;
				      
				     

				        try {
				            
				            ResultSet rs = qexec.execSelect() ;
				            ResultSetFormatter.out(System.out, rs);

				        }
				        finally
				        {
				                        qexec.close() ;
				        }
				        

	}

}


//map:CODECOMMUNEA a d2rq:PropertyBridge;
//d2rq:belongsToClassMap map:Arrondissement_municipal;
//d2rq:property v:CODECOMMUNEA;
//d2rq:uriPattern "http://www.lirmm.fr/ely/@@ARRONDISSEMENT_MUNICIPAL.codeCommune@@";

//d2rq:refersToClassMap map:Cog;
//d2rq:join "ARRONDISSEMENT_MUNICIPAL.codeCommune = COG.codeCommune";