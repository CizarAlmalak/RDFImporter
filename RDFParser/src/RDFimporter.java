package rdfimporter;

import org.pathvisio.desktop.PvDesktop;
import org.pathvisio.desktop.plugin.Plugin;

public class RDFimporter implements Plugin {

	@Override
	public void done() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(PvDesktop desktop) {
		// TODO Auto-generated method stub
	    
		// instantiate TextLinesImporter, our own importer, and register it
	    desktop.getSwingEngine().getEngine().addPathwayImporter(new Importer());
		
	}
}
