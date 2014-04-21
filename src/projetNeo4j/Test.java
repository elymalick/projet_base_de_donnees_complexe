package projetNeo4j;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Test {
	
	public static void main(String[] args) {

		Model m = ModelFactory.createOntologyModel();
		m.read("sport.rdf");
		Foot matrix = new Foot();
		matrix.createNodeSpace();
		matrix.printNeoFriends(m);
	}
}
