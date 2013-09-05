package rdfimporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.bridgedb.bio.BioDataSource;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.view.MIMShapes;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * This code reads an RDF file, queries the elements and preps
 * the triples so that it can be parsed in a later stage
 * @author cizaralmalak
 *
 */
public class RDFParser {
	
	// create Model
	static Pathway pathway = new Pathway();

	static String query = "";
	static String resetD = ""; 
	static HashMap<String,List<String>> pDictionary;
	
	/**
	 * In this method the RDF file is read and the names of
	 * the each elements assigned.
	 * Make sure that the PropertyConfigurator.configure has the
	 * correct path to log4j.
	 * The file format used for RDF is TURTLE.
	 * @param args
	 * @throws ConverterException
	 * @throws FileNotFoundException
	 */
	
	public static void main(String[] args) throws ConverterException, FileNotFoundException {
		
		
		// call BioDataSource.init()
		BioDataSource.init();
		
		// register mimshapes
		MIMShapes.registerShapes();
		
		// log4j.properties
		PropertyConfigurator.configure("/home/cizaralmalak/Desktop/JenaLib/lib/log4j.properties");
		
		// read model
		Model model = ModelFactory.createDefaultModel();
		File fileIn = new File("/home/cizaralmalak/Desktop/tdbDirectory/onePathway.ttl");
		model.read(new FileInputStream(fileIn), "", "TURTLE");
		
		// get and run queries
		SPARQLQuery(model, "pathway");
		SPARQLQuery(model, "label");
		SPARQLQuery(model, "datanode");
		
		//TODO: still needs to be finished the Line and biopax 
		//SPARQLQuery(model, "line");
		//SPARQLQuery(model, "biopax");
		
		 // write to GPML
		 pathway.writeToXml(new File("/home/cizaralmalak/Desktop/firstAutomaticPathway.gpml"), true);
		
	}

	/**
	 * In this method the RDF file is read and the names of
	 * the each elements assigned.
	 * The file format used for RDF is TURTLE.
	 * @param args
	 * @throws ConverterException
	 * @throws FileNotFoundException
	 */
	public static Pathway RDFimporter(File file) throws FileNotFoundException {
		
		// call BioDataSource.init()
		BioDataSource.init();
		
		// register mimshapes
		MIMShapes.registerShapes();
		
		// log4j.properties
		//PropertyConfigurator.configure(log4jPath);
		
		// read model
		Model model = ModelFactory.createDefaultModel();
		File fileIn = new File(file.getAbsolutePath());
		model.read(new FileInputStream(fileIn), "", "TURTLE");
		
		// get and run queries
		SPARQLQuery(model, "pathway");
		SPARQLQuery(model, "label");
		SPARQLQuery(model, "datanode");
		
		//TODO: still needs to be finished the Line and biopax 
		//SPARQLQuery(model, "line");
		//SPARQLQuery(model, "biopax");
		
		return pathway;
	    
	}
	
	/**
	 * This method queries the defined elements from the RDF file.
	 * This method forwards the result to parseStatements.
	 * @param model: This is the RDF file model.
	 * @param queryType: this is the element that is going to be parsed from the model.
	 * @return This file does not return anything.
	 */
	public static String SPARQLQuery(Model model, String queryType) {
		
		// get get all statements for a certain element
		if (queryType.equalsIgnoreCase("pathway")) {
			query = "CONSTRUCT { ?s ?p ?o } " +
					"WHERE { ?s a <http://vocabularies.wikipathways.org/wp#Pathway> . " +
					"?s ?p ?o }";			
		} else if (queryType.equalsIgnoreCase("datanode")) {
			query = "CONSTRUCT { ?s ?p ?o } " +
					"WHERE { ?s a <http://vocabularies.wikipathways.org/gpml#DataNode> . " +
					"?s ?p ?o}";			
		} else if (queryType.equalsIgnoreCase("label")) {
			query = "CONSTRUCT { ?s ?p ?o } " +
					"WHERE { ?s a <http://vocabularies.wikipathways.org/gpml#Label> . " +
					"?s ?p ?o}";			
		} else if (queryType.equalsIgnoreCase("line")) {
			query = "CONSTRUCT { ?s ?p ?o } " +
					"WHERE { ?s a <http://vocabularies.wikipathways.org/gpml#Line> . " +
					"?s ?p ?o}";
		} else if (queryType.equalsIgnoreCase("biopax")) {
			query = "CONSTRUCT { ?s ?p ?o } " +
					"where {?s a <http://vocabularies.wikipathways.org/wp#PublicationReference> . " +
					"?s ?p ?o }";
		} else {System.out.println("queryType unkown"); throw new EmptyStackException();}
		
		System.out.println(query);
		
		// run query
		parseStatements(model, query, queryType);
		
		return null;
	}
	
	/**
	 * This method parses the query and sort the triples into separate elements
	 * Subject, Predicate and Objects.
	 * This method uses the parseTriples and toElementParser method.
	 * @param model The model of the RDF file.
	 * @param queryStringSelectLine This is the query result.
	 * @param elementType this is the element that has been parsed.
	 * @return null
	 */
	public static Model parseStatements(Model model,
			String queryStringSelectLine, String elementType) {

		String queryStringSelect = queryStringSelectLine;
		Query querySelect = QueryFactory.create(queryStringSelect);
		QueryExecution qexecSelect = QueryExecutionFactory.create(querySelect, model);
		Model resultset = qexecSelect.execConstruct();
		StmtIterator iterStatements = resultset.listStatements();
		System.out.println(resultset.listSubjects().toList().size());
		
		
		Boolean skipCall = true;
		while (iterStatements.hasNext()) {
			Statement triple = iterStatements.next();
			
			Resource subj = triple.getSubject();
			Property pred = triple.getPredicate();
			RDFNode obj = triple.getObject();
			
			//System.out.println(subj+"\t"+pred+"\t"+obj);
			
			if (!(subj.toString().equals(resetD))) {
				//System.out.println(subj.toString());
				if (skipCall == true) {
					skipCall = false;
					
				} else {
					toElementParser(pDictionary, elementType);
				}
				
				pDictionary = new HashMap<String,List<String>>();
				resetD = subj.toString();
			}
			parseTriples(pDictionary, subj, pred, obj);
		}
		toElementParser(pDictionary, elementType);

		return null;
	}

	/**
	 * This method adds the triples in a HashMap.
	 * @param pDictionary contains the predicate and object of an element (datanode, label, etc.)
	 * @param subj The Subject of a Triple
	 * @param pred The Predicate of a Triple
	 * @param obj The Object of a Triple
	 * @return null
	 */
	public static HashMap<String,List<String>> parseTriples(
			HashMap<String,List<String>> pDictionary, Resource subj, Property pred, RDFNode obj) {
		
		if (obj.isLiteral()) {
			//System.out.println(obj.asLiteral().getLexicalForm().toString());
			if (!(pDictionary.containsKey(pred.toString()))) {
				ArrayList<String> objList = new ArrayList<String>();
				objList.add(obj.asLiteral().getLexicalForm().toString());
				pDictionary.put(pred.toString(), objList);
			} else {pDictionary.get(pred.toString()).add(obj.asLiteral().getLexicalForm().toString());}
		} 
		else if (obj.isURIResource()) {
			if (!(pDictionary.containsKey(pred.toString()))) {
				ArrayList<String> aL = new ArrayList<String>();
				aL.add(obj.toString());
				pDictionary.put(pred.toString(), aL);
			} else {pDictionary.get(pred.toString()).add(obj.toString());}
			
		} else {
			System.out.println("Not a URI or a literal!!");
			System.out.println(obj.toString());
			throw new EmptyStackException();
		}
		return null;	
	}
	
	/**
	 * This method pushes the HashMap with the elements to their respected
	 * element method in the ElementParser class. 
	 * @param pDictionary contains the predicate and object of an element (datanode, label, etc.)
	 * @param elementType this is a String to specify which element is in the HashMap
	 * @return pDictionary
	 */
	public static HashMap<String,List<String>> toElementParser(
			HashMap<String,List<String>> pDictionary, String elementType) {
		
		if (elementType.equals("pathway")) {ElementParser.parseMapInfo(pathway, pDictionary);} 
		else if (elementType.equals("datanode")) {ElementParser.parseDataNode(pathway, pDictionary);}
		else if (elementType.equals("label")) {ElementParser.parseLabel(pathway, pDictionary);} 
		else if (elementType.equals("line")) {ElementParser.parseLine(pathway, pDictionary);}
		else if (elementType.equals("biopax")) {ElementParser.parseBiopax(pathway, pDictionary);}
		else {
			System.out.println("element type unknown??");
			System.out.println(elementType);
			throw new EmptyStackException();
		}
		return pDictionary;
	}
}
			
			
			

