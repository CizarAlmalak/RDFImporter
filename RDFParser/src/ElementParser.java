package rdfimporter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;

import org.bridgedb.DataSource;
import org.bridgedb.bio.BioDataSource;
import org.pathvisio.core.biopax.BiopaxElement;
import org.pathvisio.core.biopax.PublicationXref;
import org.pathvisio.core.model.Pathway;

/**
 * This Class assigns the variables to the Pathway model.
 * @author Cizar
 *
 */
public class ElementParser {
	
	static HashMap<String,Object> allURIs = new HashMap<String,Object>();;
	static HashMap<String,Object> allVars = new HashMap<String,Object>();
	static List<String> biopaxrefs = new ArrayList<String>();
	static List<String> comments = new ArrayList<String>();
	static List<String> anchorList = new ArrayList<String>();
	static List<Double> mPointsDoubles = new ArrayList<Double>();
	static List<Double> X = new ArrayList<Double>();
	static List<Double> Y = new ArrayList<Double>();
	static List<Double> relX = new ArrayList<Double>();
	static List<Double> relY = new ArrayList<Double>();
	
	/**
	 * This method adds all the variables of the DataNode element to the Pathway model.
	 * @param pathway Model used to store all pathway element information in it
	 * @param pDictionary contains the predicate and object of the DataNode element
	 * @return pathway
	 */
	public static Pathway parseDataNode(
			Pathway pathway, HashMap<String,List<String>> pDictionary) {
		
		// get for each attribute the correct value and puts it in allVars
		assignVar(pDictionary);
		
		// adds the element datanode with all its values in the pathway model
		try {
			pathway.add(GPMLWriter.createDataNode(
					Double.parseDouble(allVars.get("centerx").toString()), 
					Double.parseDouble(allVars.get("centery").toString()),
					Double.parseDouble(allVars.get("width").toString()), 
					Double.parseDouble(allVars.get("height").toString()), 
					allVars.get("label").toString(), 
					allVars.get("dctermsIdentifier").toString(), 
					(DataSource) allVars.get("dcSource"), 
					allVars.get("dnType").toString(), 
					allVars.get("graphId").toString(), 
					Color.decode("0x"+allVars.get("color").toString()), 
					Double.parseDouble(allVars.get("fontsize").toString()), 
					Integer.parseInt(allVars.get("zorder").toString()), 
					allVars.get("valign").toString(), 
					allVars.get("groupId").toString(), 
					biopaxrefs, 
					comments));
		} catch (Exception e) {
			System.out.println("The RDF is missing essential parameters for DataNode!!");}
		
			/*
			// Controle
			System.out.println(Double.parseDouble(allVars.get("centerx").toString()));
			System.out.println(Double.parseDouble(allVars.get("centery").toString()));
			System.out.println(Double.parseDouble(allVars.get("width").toString()));
			System.out.println(Double.parseDouble(allVars.get("height").toString()));
			System.out.println(allVars.get("label").toString());
			System.out.println(allVars.get("dctermsIdentifier").toString());
			System.out.println(allVars.get("dcSource"));
			System.out.println(allVars.get("dnType").toString());
			System.out.println(allVars.get("graphId").toString());
			System.out.println(allVars.get("color"));
			System.out.println(Double.parseDouble(allVars.get("fontsize").toString()));
			System.out.println(Integer.parseInt(allVars.get("zorder").toString()));
			System.out.println(allVars.get("valign").toString());
			System.out.println(allVars.get("groupId").toString());
			*/
			
		
		return pathway;
	}

	/**
	 * This method adds all the variables of the Label element to the Pathway model.
	 * @param pathway Model used to store all pathway element information in it
	 * @param pDictionary contains the predicate and object of the Label element
	 * @return pathway
	 */
	public static Pathway parseLabel(
			Pathway pathway, HashMap<String,List<String>> pDictionary) {
		
		assignVar(pDictionary);
		
		try {
			pathway.add(GPMLWriter.createLabel(
				Double.parseDouble(allVars.get("centerx").toString()), 
				Double.parseDouble(allVars.get("centery").toString()),
				Double.parseDouble(allVars.get("width").toString()), 
				Double.parseDouble(allVars.get("height").toString()), 
				allVars.get("fontname").toString(),
				Boolean.parseBoolean(allVars.get("fontstyle").toString()),
				Boolean.parseBoolean(allVars.get("fontdecoration").toString()),
				Boolean.parseBoolean(allVars.get("fontstrikethru").toString()),
				Boolean.parseBoolean(allVars.get("fontweight").toString()),
				Double.parseDouble(allVars.get("fontsize").toString()),
				allVars.get("align").toString(),
				allVars.get("valign").toString(),
				Color.decode("0x"+allVars.get("color").toString()),
				Double.parseDouble(allVars.get("linethickness").toString()),
				Color.decode("0x"+allVars.get("fillcolor").toString()),
				Integer.parseInt(allVars.get("zorder").toString()),
				biopaxrefs,
				allVars.get("graphId").toString(),
				allVars.get("groupId").toString(),
				allVars.get("label").toString()));
		} catch (Exception e) {
			System.out.println("The RDF is missing essential parameters for Label!!");}

		return pathway;
	}
	
	/**
	 * This method adds all the variables of the pathway element to the Pathway model.
	 * @param pathway Model used to store all pathway element information in it
	 * @param pDictionary contains the predicate and object of the pathway element
	 * @return pathway
	 */
	public static Pathway parseMapInfo(
			Pathway pathway, HashMap<String,List<String>> pDictionary) {
		
		assignVar(pDictionary);
		try {
			pathway.add(GPMLWriter.createMapInfo(
					allVars.get("dcTitle").toString(),
					allVars.get("pavVersion").toString(),
					allVars.get("wpOrganism").toString(),
					comments,
					biopaxrefs));
		} catch (Exception e) {
			System.out.println("The RDF is missing essential parameters for Label!!");}
		
		return pathway;
	}
	
	/**
	 * This method takes all the information about an element out of pDirectory and puts them in allVars.
	 * attributes of an element that have more then one value, are given an exception.
	 * @param pDictionary
	 * @return null
	 */
	public static String assignVar(HashMap<String,List<String>> pDictionary) {
		
		resetVars();
		
		// assign the variables
		for (int i=0; i < allVars.keySet().size(); i++) {
			if (pDictionary.get(allURIs.get(allVars.keySet().toArray()[i])) != null) {
				if (allVars.keySet().toArray()[i].equals("dnType")) {defineDnType(pDictionary);}
				else if (allVars.keySet().toArray()[i].equals("groupId")) {defineGroup(pDictionary);}
				else if (allVars.keySet().toArray()[i].equals("dcSource")) {defineDcSource(pDictionary);}
				else if (allVars.keySet().toArray()[i].equals("x")) {defineCord(pDictionary, "x");}
				else if (allVars.keySet().toArray()[i].equals("y")) {defineCord(pDictionary, "y");}
				else if (allVars.keySet().toArray()[i].equals("relx")) {defineCord(pDictionary, "relx");}
				else if (allVars.keySet().toArray()[i].equals("rely")) {defineCord(pDictionary, "rely");}				
				else {allVars.put(allVars.keySet().toArray()[i].toString(), pDictionary.get(allURIs.get(allVars.keySet().toArray()[i])).get(0));}
				// TODO: add other exceptions for hits that have a list size bigger than 1. since in the else only the first element is taken
			}
		}

		defineDescription(pDictionary);
		// TODO: add biopax too
		
		return null;
	}
	
	/**
	 * This method is an exception to the assignVar method. here the line coordinates are stored separately.
	 * @param pDictionary
	 * @param cordType The predicate in question
	 * @return cordType
	 */
	private static String defineCord(HashMap<String, List<String>> pDictionary, String cordType) {
		for (int i=0; i < pDictionary.get(allURIs.get(cordType)).size(); i++) {
			if (cordType.equalsIgnoreCase("x")) {X.add(Double.parseDouble(pDictionary.get(allURIs.get(cordType)).get(i)));}
			else if (cordType.equalsIgnoreCase("y")) {Y.add(Double.parseDouble(pDictionary.get(allURIs.get(cordType)).get(i)));}
			else if (cordType.equalsIgnoreCase("relx")) {
				if (pDictionary.get(allURIs.get(cordType)).size() > 1) {relX.add(Double.parseDouble(pDictionary.get(allURIs.get(cordType)).get(i)));}
				else {relX.add(Double.parseDouble(pDictionary.get(allURIs.get(cordType)).get(i))); relX.add(Double.parseDouble(pDictionary.get(allURIs.get(cordType)).get(i)));}
			}
			else if (cordType.equalsIgnoreCase("rely")) {
				if (pDictionary.get(allURIs.get(cordType)).size() > 1) {relY.add(Double.parseDouble(pDictionary.get(allURIs.get(cordType)).get(i)));}
				else {relY.add(Double.parseDouble(pDictionary.get(allURIs.get(cordType)).get(i))); relY.add(Double.parseDouble(pDictionary.get(allURIs.get(cordType)).get(i)));}
			}
		}
		
		return cordType;	
	}

	/**
	 * This method is an exception to the assignVar method. Here the type of DataNode is checked. 
	 * @param pDictionary
	 * @return null
	 */
	public static String defineDnType(HashMap<String,List<String>> pDictionary) {
		
		// DataNode type
		List<String> dnTypeList = pDictionary.get("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"); 

		if (dnTypeList.toString().contains("Rna")) {allVars.put("dnType", "Rna");}
		else if (dnTypeList.toString().contains("Pathway")) {allVars.put("dnType", "Pathway");}
		else if (dnTypeList.toString().contains("Protein")) {allVars.put("dnType", "Protein");} 
		else if (dnTypeList.toString().contains("Complex")) {allVars.put("dnType", "Complex");} 
		else if (dnTypeList.toString().contains("Metabolite")) {allVars.put("dnType", "Metabolite");}
		else if (dnTypeList.toString().contains("GeneProduct")) {allVars.put("dnType", "GeneProduct");}
		else {allVars.put("dnType", "Unknown");}

		
		return null;
	}
	
	/**
	 * This method is an exception to the assignVar method. The groupId is determined here.
	 * @param pDictionary
	 * @return null
	 */
	public static String defineGroup(HashMap<String,List<String>> pDictionary) {
		
		// groupref
		String isPartOf = "http://purl.org/dc/terms/isPartOf";
		if (pDictionary.containsKey(isPartOf)) {
			List<String> check_groupref = pDictionary.get(isPartOf);
			if (check_groupref.size() > 1) {
				for (int i=0; i < check_groupref.size() ; i++) {
					if (check_groupref.get(i).toString().contains("/group/")) {
						allVars.put("groupId", 
								check_groupref.get(i).toString().split("/")
								[check_groupref.get(i).toString().split("/").length-1].toString());
						break;
					} else {allVars.put("groupId", ".");}
				}
			} else {allVars.put("groupId", "");}
		}
		return null;
	}
	
	/**
	 * This method is an exception to the assignVar method. The database is determined here.
	 * @param pDictionary
	 * @return null
	 */
	public static DataSource defineDcSource(HashMap<String,List<String>> pDictionary) {
		
		// Data source
		String namespace = "http://purl.org/dc/elements/1.1/source";
		String dcSourceString = pDictionary.get(namespace).get(0);
		
		if (pDictionary.containsKey(namespace)) {
			if (dcSourceString.equals("Entrez Gene")) {allVars.put("dcSource", BioDataSource.ENTREZ_GENE);}
			else if (dcSourceString.equals("HMDB")) {allVars.put("dcSource", BioDataSource.HMDB);} 
			else if (dcSourceString.equals("Ensembl")) {allVars.put("dcSource", BioDataSource.ENSEMBL);}
			else if (dcSourceString.equals("CAS")) {allVars.put("dcSource", BioDataSource.CAS);}
			else {allVars.put("dcSource", null);}
		}
		return null;
	}
	
	/**
	 * This method is an exception to the assignVar method. The category and description are
	 * isolated.
	 * @param pDictionary
	 * @return null
	 */
	public static List<String> defineDescription(HashMap<String,List<String>> pDictionary) {
		
		// category
		String namespaceCat = "http://vocabularies.wikipathways.org/wp#category";
		String namespaceDes = "http://purl.org/dc/terms/description";
				
		comments.clear();
		List<String> tempList = new ArrayList<String>();
		if (pDictionary.containsKey(namespaceCat)) {
			tempList = pDictionary.get(namespaceCat);
			for (int i=0; i<tempList.size(); i++) {
				comments.add(tempList.get(i).split("#")[1].toString());
				comments.add("WikiPathways-category");
			}
		}
		
		// description
		tempList.clear();
		if (pDictionary.containsKey(namespaceDes)) {
			tempList = pDictionary.get(namespaceDes);
			for (int i=0; i<tempList.size(); i++) {
				comments.add(tempList.get(i));
				comments.add("WikiPathways-description");
			}
		}
		return null;
	}
	
	/**
	 * This method resets and adds all values and URIs for allVars and allURIs dictionary
	 */
	public static void resetVars() {
		
		// reset all
		allVars.clear();
		allURIs.clear();
		comments.clear();
		biopaxrefs.clear();
		anchorList.clear();
		X.clear();
		Y.clear();
		relX.clear();
		relY.clear();
		mPointsDoubles.clear();

		allVars.put("centerx", 0.0); allURIs.put("centerx", "http://vocabularies.wikipathways.org/gpml#centerx");
		allVars.put("centery", 0.0); allURIs.put("centery", "http://vocabularies.wikipathways.org/gpml#centery");
		allVars.put("width", 0.0); allURIs.put("width", "http://vocabularies.wikipathways.org/gpml#width");
		allVars.put("height", 0.0); allURIs.put("height", "http://vocabularies.wikipathways.org/gpml#height");
		allVars.put("dnType", ""); allURIs.put("dnType", "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		allVars.put("dctermsTitle", ""); allURIs.put("dctermsTitle", "http://purl.org/dc/terms/title");
		allVars.put("dcTitle", ""); allURIs.put("dcTitle", "http://purl.org/dc/elements/1.1/title");
		allVars.put("dctermsSource", ""); allURIs.put("dctermsSource", "http://purl.org/dc/terms/source");
		allVars.put("dcSource", null); allURIs.put("dcSource", "http://purl.org/dc/elements/1.1/source");
		allVars.put("pavVersion",""); allURIs.put("pavVersion","http://purl.org/pav/version");
		allVars.put("wpOrganism", ""); allURIs.put("wpOrganism", "http://vocabularies.wikipathways.org/wp#organism");
		allVars.put("dctermsDate", ""); allURIs.put("dctermsDate", "http://purl.org/dc/terms/date");
		allVars.put("zorder", -1); allURIs.put("zorder", "http://vocabularies.wikipathways.org/gpml#zorder");
		allVars.put("valign", "middle"); allURIs.put("valign", "http://vocabularies.wikipathways.org/gpml#valign");
		allVars.put("align", "center"); allURIs.put("align", "http://vocabularies.wikipathways.org/gpml#align");
		allVars.put("label", ""); allURIs.put("label", "http://www.w3.org/2000/01/rdf-schema#label");
		allVars.put("dctermsIdentifier", ""); allURIs.put("dctermsIdentifier", "http://purl.org/dc/terms/identifier");
		allVars.put("graphId", ""); allURIs.put("graphId", "http://vocabularies.wikipathways.org/gpml#graphid");
		allVars.put("groupId", ""); allURIs.put("groupId", "http://purl.org/dc/terms/isPartOf");
		allVars.put("connectorType", X); allURIs.put("x", "http://vocabularies.wikipathways.org/gpml#X");
		allVars.put("x", X); allURIs.put("x", "http://vocabularies.wikipathways.org/gpml#X");
		allVars.put("y", Y); allURIs.put("y", "http://vocabularies.wikipathways.org/gpml#Y");
		allVars.put("relx", relX); allURIs.put("relx", "http://vocabularies.wikipathways.org/gpml#relX");
		allVars.put("rely", relY); allURIs.put("rely", "http://vocabularies.wikipathways.org/gpml#relY");
		allVars.put("graphref", ""); allURIs.put("graphref", "http://vocabularies.wikipathways.org/gpml#graphref");
		allVars.put("linethickness", 1); allURIs.put("linethickness", "http://vocabularies.wikipathways.org/gpml#linethickness");
		allVars.put("arrowHead", "Arrow"); allURIs.put("arrowHead", "http://vocabularies.wikipathways.org/gpml#Arrow");
		allVars.put("startdatanoderef", ""); allURIs.put("startdatanoderef", "http://vocabularies.wikipathways.org/gpml#graphref");
		allVars.put("fontweight", false); allURIs.put("fontweight", "http://vocabularies.wikipathways.org/gpml#fontweight");
		allVars.put("fontstyle", false); allURIs.put("fontstyle", "http://vocabularies.wikipathways.org/gpml#fontstyle");
		allVars.put("fontdecoration", false); allURIs.put("fontdecoration", "http://vocabularies.wikipathways.org/gpml#fontdecoration");
		allVars.put("fontstrikethru", false); allURIs.put("fontstrikethru", "http://vocabularies.wikipathways.org/gpml#fontstrikethru");
		allVars.put("fontname", ""); allURIs.put("fontname", "http://vocabularies.wikipathways.org/gpml#fontname");
		allVars.put("fontsize", 10.0); allURIs.put("fontsize", "http://vocabularies.wikipathways.org/gpml#fontsize");
		allVars.put("color", "000000"); allURIs.put("color", "http://vocabularies.wikipathways.org/gpml#color");
		allVars.put("fillcolor", "000000"); allURIs.put("fillcolor", "http://vocabularies.wikipathways.org/gpml#fillcolor");
		allVars.put("page", ""); allURIs.put("page", "http://xmlns.com/foaf/0.1/page");
	}
	
	/**
	 * This method adds the biopax information to the pathway.
	 * @param pathway
	 * @param pDictionary
	 * @return pathway
	 */
	//TODO: needs to be adjusted according to the adjustments to the RDF file
	public static Pathway parseBiopax(
		Pathway pathway, HashMap<String,List<String>> pDictionary) {
	
		assignVar(pDictionary);
		BiopaxElement refMgr = pathway.getBiopax();
		
		try {
			PublicationXref xref = GPMLWriter.addBioPaxAttributes(
					biopaxrefs,
					allVars.get("page").toString().split("/")[allVars.get("page").toString().split("/").length-1]);
			refMgr.addElement(xref);
		} catch (Exception e) {
			System.out.println("The RDF is missing essential parameters for Biopax!!");}
		
		return pathway;
	}
	
	/**
	 * This method adds all the variables of the Line element to the Pathway model.
	 * @param pathway
	 * @param pDictionary contains the predicate and object of the Line element
	 * @return pathway
	 */
	public static Pathway parseLine(
			Pathway pathway, HashMap<String,List<String>> pDictionary) {
		
		//TODO: This whole method needs to be adjusted when the coordinateds are added to the wikipathways RDF
		
		assignVar(pDictionary);

		// coordinated x and y
		if (X.size() > 1 && Y.size() > 1) {
			mPointsDoubles.add(X.get(0));
			mPointsDoubles.add(Y.get(0));
			mPointsDoubles.add(X.get(1));
			mPointsDoubles.add(Y.get(1));
		} else if (X.size() > 1 && Y.size() == 1) {
			mPointsDoubles.add(X.get(0));
			mPointsDoubles.add(Y.get(0));
			mPointsDoubles.add(X.get(1));
			mPointsDoubles.add(Y.get(0));
		} else if (X.size() == 1 && Y.size() > 1) {
			mPointsDoubles.add(X.get(0));
			mPointsDoubles.add(Y.get(0));
			mPointsDoubles.add(X.get(0));
			mPointsDoubles.add(Y.get(1));
		} else if (X.size() == 1 && Y.size() == 1) {
			System.out.println(X.get(0).toString());
			mPointsDoubles.add(X.get(0));
			mPointsDoubles.add(Y.get(0));
			mPointsDoubles.add(X.get(0));
			mPointsDoubles.add(Y.get(0));
		} else {throw new EmptyStackException();}
		
		String graphref_start = "";
		String graphref_end = "";
		String startdatanoderef = "";
		String enddatanoderef = "";
		
		// graphref and containers
		if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#graphref")) {
			if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#arrowTowards")) {
				if (pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString().equals(
						pDictionary.get("http://vocabularies.wikipathways.org/gpml#arrowTowards").get(0).toString())) {
					graphref_end = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
					enddatanoderef = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
				} else {
					graphref_start = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
					startdatanoderef = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
				}
			} 
			graphref_start = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
			startdatanoderef = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
		} else {graphref_start = ""; graphref_end = "";}
		
		System.out.println(enddatanoderef);
		System.out.println(startdatanoderef);
	
		try {
			pathway.add(GPMLWriter.createLine(
					mPointsDoubles,
					graphref_start,
					graphref_end,
					Integer.parseInt(allVars.get("zorder").toString()),
					Double.parseDouble(allVars.get("linethickness").toString()),
					"straight",
					allVars.get("arrowHead").toString(),
					anchorList,
					pathway.getGraphIdContainer(startdatanoderef),
					pathway.getGraphIdContainer(enddatanoderef),
					relX.get(0),
					relY.get(0),
					relX.get(1),
					relY.get(1)));
		} catch (Exception e) {
			System.out.println("The RDF is missing essential parameters: ");}
		
		
	/**	// graphref and containers
		if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#graphref")) {
			if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#arrowTowards")) {
				if (pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString().equals(
						pDictionary.get("http://vocabularies.wikipathways.org/gpml#arrowTowards").get(0).toString())) {
					graphref_end = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
				} else {
					graphref_start = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
				}
			}
			graphref_start = pDictionary.get("http://vocabularies.wikipathways.org/gpml#graphref").get(0).toString();
		} else {graphref_start = ""; graphref_end = "";}
		
		// arrowhead
		if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#arrowHead")) {
			arrowhead = pDictionary.get("http://vocabularies.wikipathways.org/gpml#arrowHead").get(0).toString();
		} else {arrowhead = "Arrow";}
		
		// anchorList
		if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#hasAnchor")) {
			anchorList = pDictionary.get("http://vocabularies.wikipathways.org/gpml#hasAnchor");
		} else {anchorList = new ArrayList<String>();}
		
		// relx
		if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#relX")) {
			if (pDictionary.get("http://vocabularies.wikipathways.org/gpml#relX").size() == 2) {
				relx_start = Double.parseDouble(pDictionary.get("http://vocabularies.wikipathways.org/gpml#relX").get(0).toString());
				relx_end = Double.parseDouble(pDictionary.get("http://vocabularies.wikipathways.org/gpml#relX").get(1).toString());
			} else if (pDictionary.get("http://vocabularies.wikipathways.org/gpml#relX").size() <= 1) {
				relx_start = 0.0; relx_end = 0.0;
			} else {
				System.out.println("to many relx variables");
				throw new EmptyStackException();
			}
		} else {relx_start = 0.0; relx_end = 0.0;}
		
		// rely
		if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#relY")) {
			if (pDictionary.get("http://vocabularies.wikipathways.org/gpml#relY").size() == 2) {
				rely_start = Double.parseDouble(pDictionary.get("http://vocabularies.wikipathways.org/gpml#relY").get(0).toString());
				rely_end = Double.parseDouble(pDictionary.get("http://vocabularies.wikipathways.org/gpml#relY").get(1).toString());
			} else if (pDictionary.get("http://vocabularies.wikipathways.org/gpml#relY").size() <= 1) {
				rely_start = 0.0; rely_end = 0.0;
			} else {
				System.out.println("to many rely variables");
				throw new EmptyStackException();
			}
		} else {rely_start = 0.0; rely_end = 0.0;}
		
		// connector Type
		// missing in action
		
		// mPoints
		if (pDictionary.containsKey("http://vocabularies.wikipathways.org/gpml#x")) {
			// missing in action
		}
		
		try {
			pathway.add(GPMLWriter.createLine(
					//List<Double> mPointsDoubles,
					graphref_start,
					graphref_end,
					zorder,
					linethickness,
					//String connectorType,
					arrowhead,
					anchorList,
					//pathway.getGraphIdContainer(startdatanoderef),
					//pathway.getGraphIdContainer(enddatanoderef),
					relx_start,
					rely_start,
					relx_end,
					rely_end));
		} catch (Exception e) {
			System.out.println("The RDF is missing essential parameters: ");
		}*/
		
		return pathway;
		}
}