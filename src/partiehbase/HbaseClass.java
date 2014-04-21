package partiehbase;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
 
public class HbaseClass {
	
	  static Configuration conf= HBaseConfiguration.create();
	    static HTable table;
	    static ResultScanner scanner = null;
	    public static final String region = "/home/ely/workspace/proget_BDC/region.rdf";
	    public static final String liaison = "/home/ely/workspace/proget_BDC/liaison.rdf";
	    public static final String sprt = "/home/ely/workspace/proget_BDC/sport.rdf";

    public static void creatTable(String tableName, String[] familys)
            throws Exception {
        HBaseAdmin admin1 = new HBaseAdmin(conf);
        if (admin1.tableExists(tableName)) {
            System.out.println("table already exists!");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for (int i = 0; i < familys.length; i++) {
                tableDesc.addFamily(new HColumnDescriptor(familys[i]));
            }
            admin1.createTable(tableDesc);
            System.out.println("create table " + tableName + " ok.");
        }
    }
 
    
 
    /**
     * Put (or insert) a row
     */
    public static void addRecord(String tableName, String rowKey,
            String family, String qualifier, String value) throws Exception {
        try {
            HTable table = new HTable(conf, tableName);
            Put put = new Put(Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
            		Bytes.toBytes(value));
            table.put(put);
            System.out.println("insert recored " + rowKey + " to table "
                    + tableName + " ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    
    public static void main(String[] agrs) {
    		Model ModelHbase= ModelFactory.createDefaultModel();
    		  String NameSpaceH = "http://www.ely.fr#";
    		  ModelHbase.setNsPrefix("sport", NameSpaceH);
    	
    	
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
	    	FileManager.get().readModel( ModelGeneral, sprt );
	
        
    	
    	try {
            String tablename = "equipe";
            String[] familys = { "EquipeCommune" };
                        
            HbaseClass.creatTable(tablename, familys);
// 
            HbaseClass.addRecord(tablename, "Montpellier", "EquipeCommune", "equipeNom", "MSHC");
            HbaseClass.addRecord(tablename, "Paris", "EquipeCommune", "equipeNom", "PSG");
            HbaseClass.addRecord(tablename, "Marseille", "EquipeCommune", "equipeNom", "OM");
          

           try {table = new HTable(conf, "equipe");	
           Scan scan = new Scan();
           scanner = table.getScanner(scan);
           
       } catch (IOException ex) {
           Logger.getLogger(HbaseClass.class.getName()).log(Level.SEVERE, null, ex);
       }
        
       Iterator<Result> it = scanner.iterator();
       while (it.hasNext()) {

           Result result = it.next();
           for (KeyValue kv : result.raw()) {
           	Resource Region = ModelHbase.createResource("http://www.ely.fr#" + Bytes.toString(kv.getRow()));
          	Resource equipe = ModelHbase.createResource( "http://www.sports.fr#" + Bytes.toString(kv.getValue()));
               String column = Bytes.toString(kv.getQualifier());
               if (column.equals("equipeNom")) {
            	   ModelHbase.addLiteral(Region, ModelHbase.createProperty(NameSpace + "ApourEquipe"), 
            			//   kv.getValue());
            			//   "http://www.sports.fr#" + Bytes.toString(kv.getValue()));
            			   equipe);
           }
           }
       }

       

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

       
       
//       
//       
//       String req1=
//          		"PREFIX b: <http://rdf.insee.fr/geo/>" +
//          		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
//          		"PREFIX ely: <http://www.ely.fr#>" +
//          		
//          		"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
//             "SELECT ?c ?n ?nbr " +
//            	"WHERE {  ?y owl:sameAs ?z . ?y b:code_commune ?c . ?y b:nom ?n . ?z ely:ApourEquipe ?nbr } " ;

       
       
       
       
       
       
       
       
//       Query query = QueryFactory.create(req2);
//    	QueryExecution qexec = QueryExecutionFactory.create(query, ModelGeneral) ;
//   	
//   	
//   	
//   	try {
//   	
//   	ResultSet rs = qexec.execSelect() ;
//   	ResultSetFormatter.out(System.out, rs, query);
//   	
//   	
//   	
//   	
//   	}
//   	finally
//   	{
//   	  qexec.close() ;
//   	}
   	
       
//       try {       
//	    	  FileOutputStream outStream = new FileOutputStream("Chomage.rdf");
//	             // exporte le resultat dans un fichier
//	    	  ModelGeneral.write(outStream, "RDF/XML");
//	             outStream.close();
//	             
//	           FileOutputStream outStream1 = new FileOutputStream("Chomage.n3");
//	           ModelGeneral.write(outStream1, "N3");
//	             outStream1.close();
// 	 }
//	      catch (FileNotFoundException e) {System.out.println("File not found");}
//	      catch (IOException e) {System.out.println("IO problem");}
//
//   
//           
       
       
       
      	try {       
   			FileOutputStream outStream = new FileOutputStream("sortiehbase.rdf");
   		
   			//exporte le resultat dans un fichier
   			ModelHbase.write(outStream, "RDF/XML");
   			outStream.close();
   		}
   		catch (FileNotFoundException e) {System.out.println("File not found");}
   		catch (IOException e) {System.out.println("IO problem");}
       
       
       
       
       } catch (Exception e) { e.printStackTrace();}
    	
    	
    	
    	
 
        
    	

    }
    
    
    
    
    }