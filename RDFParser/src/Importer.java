package rdfimporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.model.PathwayImporter;

public class Importer implements PathwayImporter{

	@Override
	public String[] getExtensions() {
		// TODO Auto-generated method stub
		String [] array = {"tll"};
		return array;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "RDF";
	}

	@Override
	public List<String> getWarnings() {
		// TODO Auto-generated method stub
		
		return new ArrayList<String>();
	}

	@Override
	public Pathway doImport(File arg0) throws ConverterException {
		Pathway pathway = new Pathway();
		//RDFParser parser = new RDFParser();
		try {
			pathway = RDFParser.RDFimporter(arg0);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return parser.parseFile(arg0);
		return pathway;
	}

	@Override
	public boolean isCorrectType(File arg0) {
		if(arg0.getName().endsWith("tll")) {
			return true;
		}
		// TODO Auto-generated method stub
		return false;
	}

	public void done() {
		// TODO Auto-generated method stub
		
	}

}
