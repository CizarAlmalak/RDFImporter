package rdfimporter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import org.bridgedb.DataSource;
import org.bridgedb.Xref;
import org.pathvisio.core.biopax.PublicationXref;
import org.pathvisio.core.model.AlignType;
import org.pathvisio.core.model.ConnectorType;
import org.pathvisio.core.model.GraphLink.GraphIdContainer;
import org.pathvisio.core.model.GroupStyle;
import org.pathvisio.core.model.IShape;
import org.pathvisio.core.model.LineType;
import org.pathvisio.core.model.MGroup;
import org.pathvisio.core.model.ObjectType;
import org.pathvisio.core.model.PathwayElement;
import org.pathvisio.core.model.PathwayElement.MAnchor;
import org.pathvisio.core.model.PathwayElement.MPoint;
import org.pathvisio.core.model.ValignType;

/**
 * This Class assigns all values to the attributes of a pathway.
 * @author cizaralmalak
 *
 */
public class GPMLWriter {
	
	/**
	 * Pathway information is added to the pathway element model.
	 * @param title
	 * @param version
	 * @param organism
	 * @param commentList
	 * @param bioPaxRefs
	 * @return pwElt
	 */
	public static PathwayElement createMapInfo(
			String title, String version, String organism,
			List<String> commentList, List<String> bioPaxRefs) {
		
		// create pathwayElement line
		PathwayElement pwElt = PathwayElement.createPathwayElement(ObjectType.MAPPINFO);
		if (!(title.equals(""))) pwElt.setMapInfoName(title);
		if (!(version.equals(""))) pwElt.setVersion(version);
		if (!(organism.equals(""))) pwElt.setOrganism(organism);
		
		if (commentList.size() > 0) {
			for (int i=0; i < commentList.size(); i+=2) {
				pwElt.addComment(commentList.get(i), commentList.get(i+1));
			}
		}
		
		if (bioPaxRefs.size() > 0) {
			for (int i = 0; i < bioPaxRefs.size(); i++) {
				pwElt.addBiopaxRef(bioPaxRefs.get(i));
			}
		}
		return pwElt;
	}
	
	/**
	 * Group values are added to the pathway element model.
	 * @param groupRef
	 * @param graphRef
	 * @param type
	 * @return pwElt
	 */
	public static PathwayElement createGroup(
			String groupRef, String graphRef,
			String type) {
		
		// create group element
		MGroup pwElt = (MGroup) PathwayElement.createPathwayElement(ObjectType.GROUP);

		// set ids
		pwElt.setGroupId(groupRef);
		pwElt.setGraphId(graphRef);
		
		// style type
		if (type.toLowerCase() == "complex") {
			GroupStyle gs = GroupStyle.COMPLEX;
			pwElt.setGroupStyle(gs);
		} else if  (type.toLowerCase() == "group") {
			GroupStyle gs = GroupStyle.GROUP;
			pwElt.setGroupStyle(gs);
		} else {}
				
		return pwElt;
	}
	
	/**
	 * DataNode values are added to the pathway element model.
	 * @param centerX
	 * @param centerY
	 * @param width
	 * @param height
	 * @param textLabel
	 * @param identifier
	 * @param source
	 * @param dnType
	 * @param graphId
	 * @param color
	 * @param fontSize
	 * @param zOrder
	 * @param vAlign
	 * @param groupRef
	 * @param bioPaxRefs
	 * @param commentList
	 * @return pwElt
	 */
	public static PathwayElement createDataNode(
			Double centerX, Double centerY, Double width, Double height,
			String textLabel, String identifier, DataSource source,
			String dnType, String graphId, Color color, Double fontSize,
			Integer zOrder, String vAlign, String groupRef,
			List<String> bioPaxRefs, List<String> commentList) {
		
		// create pathwayElement data node
		PathwayElement pwElt = PathwayElement.createPathwayElement(ObjectType.DATANODE);
		
		// for better definition of identifier and database
		Xref ref = new Xref(identifier, source);
		
		// put variables in element
		if (centerX != null) {pwElt.setMCenterX(centerX);}
		else {System.out.println("No CenterX for datanode!!"); throw new EmptyStackException();}
		if (centerY != null) {pwElt.setMCenterY(centerY);}
		else {System.out.println("No centerY for datanode!!"); throw new EmptyStackException();}
		if (width != null) {pwElt.setMWidth(width);}
		else {System.out.println("No width for datanode!!"); throw new EmptyStackException();}
		if (height != null) {pwElt.setMHeight(height);}
		else {System.out.println("No height for datanode!!"); throw new EmptyStackException();}
		if (!(textLabel.equals(""))) pwElt.setTextLabel(textLabel);
		else {System.out.println("No label name for datanode!!"); throw new EmptyStackException();}
		if (!(ref.getId().isEmpty())) pwElt.setElementID(ref.getId());
		if (ref.getDataSource() != null) pwElt.setDataSource(ref.getDataSource());
		if (dnType != null) pwElt.setDataNodeType(dnType);
		if (!(graphId.equals(""))) {pwElt.setGraphId(graphId);}
		else {System.out.println("No GraphId for datanode!!"); throw new EmptyStackException();}
		if (color != null) {pwElt.setColor(color);}
		if (fontSize != null) pwElt.setMFontSize(fontSize);
		if (zOrder != -1) pwElt.setZOrder(zOrder);
		//System.out.println(groupRef);
		//if (!(groupRef.equals(""))) pwElt.setGraphRef(groupRef);
		
		if (vAlign.toLowerCase() == "middle") {pwElt.setValign(ValignType.MIDDLE);} 
		else if (vAlign.toLowerCase() == "bottom") {pwElt.setValign(ValignType.BOTTOM);} 
		else if (vAlign.toLowerCase() == "top") {pwElt.setValign(ValignType.TOP);} 
		else {pwElt.setValign(ValignType.MIDDLE);}
		
		if (bioPaxRefs.size() > 0) {
			for (int i = 0; i < bioPaxRefs.size(); i++) {
				pwElt.addBiopaxRef(bioPaxRefs.get(i));
			}
		}
		
		if (commentList.size() > 0) {
			for (int i=0; i < commentList.size(); i+=2) {
				pwElt.addComment(commentList.get(i), commentList.get(i+1));
			}
		}
		return pwElt;
	}
	
	/**
	 * Label values are added to the pathway element model.
	 * @param centerX
	 * @param centerY
	 * @param width
	 * @param height
	 * @param fontName
	 * @param fontStyle
	 * @param fontDecoration
	 * @param fontStrikeThru
	 * @param fontWeight
	 * @param fontSize
	 * @param align
	 * @param vAlign
	 * @param color
	 * @param lnThickness
	 * @param fillcolor
	 * @param zOrder
	 * @param bioPaxRefs
	 * @param graphId
	 * @param groupId
	 * @param textLabel
	 * @return pwElt
	 */
	public static PathwayElement createLabel(
			Double centerX, Double centerY, Double width, Double height,
			String fontName, Boolean fontStyle, Boolean fontDecoration,
			Boolean fontStrikeThru, Boolean fontWeight, Double fontSize, String align,
			String vAlign, Color color, Double lnThickness,
			Color fillcolor, Integer zOrder,
			List<String> bioPaxRefs, String graphId, String groupId,
			String textLabel) {
		
		// create pathwayElement label
		PathwayElement pwElt = PathwayElement.createPathwayElement(ObjectType.LABEL);
		
		// put variables in element
		if (centerX != null) {pwElt.setMCenterX(centerX);}
		else {System.out.println("No CenterX for label!!"); throw new EmptyStackException();}
		if (centerY != null) {pwElt.setMCenterY(centerY);}
		else {System.out.println("No centerY for label!!"); throw new EmptyStackException();}
		if (width != null) {pwElt.setMWidth(width);}
		else {System.out.println("No width for label!!"); throw new EmptyStackException();}
		if (height != null) {pwElt.setMHeight(height);}
		else {System.out.println("No height for label!!"); throw new EmptyStackException();}
		if (fontSize != null) pwElt.setMFontSize(fontSize);
		if (color != null) {pwElt.setColor(color);}
		if (lnThickness != null) pwElt.setLineThickness(lnThickness);
		if (fillcolor != null) pwElt.setFillColor(fillcolor);
		if (!(graphId.equals(""))) {pwElt.setGraphId(graphId);}
		else {System.out.println("No GraphId for label!!"); throw new EmptyStackException();}
		if (!(textLabel.equals(""))) {pwElt.setTextLabel(textLabel);}
		else {System.out.println("No label name for label!!"); throw new EmptyStackException();}
		if (fontStyle == true) pwElt.setItalic(fontStyle);
		if (fontDecoration == true) pwElt.setUnderline(fontDecoration);
		if (fontStrikeThru == true) pwElt.setStrikethru(fontStrikeThru);
		if (fontWeight == true) pwElt.setBold(fontWeight);
		if (!(fontName.equals(""))) pwElt.setFontName(fontName);
		if (zOrder != -1) pwElt.setZOrder(zOrder);
		if (!(groupId.equals(""))) pwElt.setGraphRef(groupId);

		
		if (align.toLowerCase() == "center") {pwElt.setAlign(AlignType.CENTER);}
		else if (align.toLowerCase() == "left") {pwElt.setAlign(AlignType.LEFT);} 
		else if (align.toLowerCase() == "right") {pwElt.setAlign(AlignType.RIGHT);} 
		else {pwElt.setAlign(AlignType.CENTER);}
		
		if (vAlign.toLowerCase() == "middle") {pwElt.setValign(ValignType.MIDDLE);} 
		else if (vAlign.toLowerCase() == "bottom") {pwElt.setValign(ValignType.BOTTOM);} 
		else if (vAlign.toLowerCase() == "top") {pwElt.setValign(ValignType.TOP);} 
		else {pwElt.setValign(ValignType.MIDDLE);}
		
		if (bioPaxRefs.size() > 0) {
			for (int i = 0; i < bioPaxRefs.size(); i++) {
				pwElt.addBiopaxRef(bioPaxRefs.get(i));
			}
		}		
		return pwElt;
	}
	
	/**
	 * Line values are added to the pathway element model.
	 * @param mPointsDoubles
	 * @param startGraphId
	 * @param endGraphId
	 * @param zOrder
	 * @param lnThickness
	 * @param connectorType
	 * @param arrowHead
	 * @param anchorList
	 * @param startDataNodeId
	 * @param endDataNodeId
	 * @param startRelX
	 * @param startRelY
	 * @param endRelX
	 * @param endRelY
	 * @return pwElt
	 */
	public static PathwayElement createLine(
			List<Double> mPointsDoubles,
			String startGraphId, String endGraphId,
			Integer zOrder, Double lnThickness, String connectorType,
			String arrowHead, List<String> anchorList,
			GraphIdContainer startDataNodeId, GraphIdContainer endDataNodeId,
			Double startRelX, Double startRelY, Double endRelX, Double endRelY) {
		
		// create pathwayElement line
		PathwayElement pwElt = PathwayElement.createPathwayElement(ObjectType.LINE);
		
		// set graph line variables
		if (mPointsDoubles.isEmpty()) {System.out.println("No mPoints for line!!"); throw new EmptyStackException();}
		if (!(startGraphId.equals(""))) pwElt.setStartGraphRef(startGraphId);
		else {System.out.println("No startGraphId for line!!"); throw new EmptyStackException();}
		if (!(endGraphId.equals(""))) pwElt.setEndGraphRef(endGraphId);
		else {System.out.println("No endGraphId for line!!"); throw new EmptyStackException();}
		if (zOrder != -1) pwElt.setZOrder(zOrder);
		if (lnThickness != null) pwElt.setLineThickness(lnThickness);
		if (arrowHead != null) pwElt.setEndLineType(LineType.fromName(arrowHead));
		if (startRelX == null) {System.out.println("No relX 1 for line!!"); throw new EmptyStackException();}
		if (endRelX == null) {System.out.println("No relX 2 for line!!"); throw new EmptyStackException();}
		if (startRelY == null) {System.out.println("No relY 1 for line!!"); throw new EmptyStackException();}
		if (endRelY == null) {System.out.println("No no relY 2 for line!!"); throw new EmptyStackException();}
		
		// add MPoints
		addLineType(pwElt, connectorType, mPointsDoubles);
		
		// add Anchor
		if (anchorList.size() > 0) createAnchor(pwElt, anchorList);
		
		// bind on the correct place
		MPoint startLine = pwElt.getMStart();
		MPoint endLine = pwElt.getMEnd();
		if (startDataNodeId.equals("")) {startLine.linkTo(startDataNodeId, startRelX, startRelY);}
		if (startDataNodeId.equals("")) {endLine.linkTo(endDataNodeId, endRelX, endRelY);}
		
		return pwElt;
	}
	
	/**
	 * GraphicalLine values are added to the pathway element model.
	 * @param mPointsDoubles
	 * @param relX
	 * @param relY
	 * @param startGraphRef
	 * @param endGraphRef
	 * @param pointGraphId
	 * @param arrowHead
	 * @param anchorList
	 * @param color
	 * @param lnThickness
	 * @param lineStyle
	 * @param connectorType
	 * @param zOrder
	 * @param groupRef
	 * @param bioPaxRefs
	 * @param graphId
	 * @param type
	 * @return pwElt
	 */
	public static PathwayElement createGraphicalLine(
			List<Double> mPointsDoubles, double relX, double relY, String startGraphRef,
			String endGraphRef, String pointGraphId, String arrowHead, List<String> anchorList,
			Color color, int lnThickness, int lineStyle, String connectorType,
			int zOrder, String groupRef, List<String> bioPaxRefs,
			String graphId, String type) {
		
		// !!! not done yet
		
		// create pathwayElement line
		PathwayElement pwElt = PathwayElement.createPathwayElement(ObjectType.GRAPHLINE);
		
		pwElt.setStartGraphRef(startGraphRef);
		pwElt.setEndGraphRef(endGraphRef);
		pwElt.setGraphId(graphId);
		pwElt.setColor(color);
		pwElt.setLineThickness(lnThickness);
		pwElt.setLineStyle(lineStyle);
		pwElt.setZOrder(zOrder);
		pwElt.setGroupRef(groupRef);
		pwElt.setGraphId(graphId);
		
		pwElt.setEndLineType(LineType.fromName(arrowHead));
		
		
		// add MPoints
		addLineType(pwElt, connectorType, mPointsDoubles);
		
		// add Anchor
		if (anchorList.size() > 0) {
			createAnchor(pwElt, anchorList);
		}
		
		if (bioPaxRefs.size() > 0) {
			for (int i = 0; i < bioPaxRefs.size(); i++) {
				pwElt.addBiopaxRef(bioPaxRefs.get(i));
			}
		}
		
		return null;
	}
	
	/**
	 * Set the type of line (curved, segmented, etc.) and add mPoints on them.
	 * @param pwElt
	 * @param connectorType
	 * @param mPointsDoubles
	 * @return mPoints
	 */
	private static List<MPoint> addLineType(
			PathwayElement pwElt, String connectorType,
			List<Double> mPointsDoubles) {
		List<MPoint> mPoints = new ArrayList<MPoint>();
		
		// type of line
		if (connectorType.toLowerCase().equals("curved")) {
			pwElt.setConnectorType(ConnectorType.CURVED);
			createMPoints(pwElt, mPoints, mPointsDoubles);
			
			// add MPoints
			pwElt.setMPoints(mPoints);
		} else if (connectorType.toLowerCase().equals("segmented")) {
			pwElt.setConnectorType(ConnectorType.SEGMENTED);
			createMPoints(pwElt, mPoints, mPointsDoubles);
			
			// add MPoints
			pwElt.setMPoints(mPoints);
			
		} else if (connectorType.toLowerCase().equals("elbow")) {
			pwElt.setConnectorType(ConnectorType.ELBOW);
			createMPoints(pwElt, mPoints, mPointsDoubles);
			
			// add MPoints
			pwElt.setMPoints(mPoints);
			
		} else if (connectorType.toLowerCase().equals("straight")) {
			pwElt.setConnectorType(ConnectorType.STRAIGHT);
			createMPoints(pwElt, mPoints, mPointsDoubles);
			
			// add MPoints
			pwElt.setMPoints(mPoints);

		} else {
			pwElt.setConnectorType(ConnectorType.STRAIGHT);
			createMPoints(pwElt, mPoints, mPointsDoubles);
			
			// add MPoints
			pwElt.setMPoints(mPoints);
		}
		return mPoints;
	}
	
	/**
	 * Add anchors to the lines
	 * @param pwElt
	 * @param anchorList
	 * @return pwElt
	 */
	private static PathwayElement createAnchor(
			PathwayElement pwElt, List<String> anchorList) {
		
		// create anchors
		for (int i=0; i<anchorList.size(); i+=2) {
			MAnchor anchor = pwElt.addMAnchor(Double.parseDouble(anchorList.get(i)));
			anchor.setGraphId(anchorList.get(i+1));
		}
		return pwElt;
	}

	/**
	 * Shape values are added to the pathway element model.
	 * @param centerX
	 * @param centerY
	 * @param width
	 * @param height
	 * @param fontStyle
	 * @param fontDecoration
	 * @param fontStrikeThru
	 * @param fontWeight
	 * @param fontName
	 * @param fontSize
	 * @param align
	 * @param vAlign
	 * @param color
	 * @param lineStyle
	 * @param lnThickness
	 * @param fillColor
	 * @param shapeType
	 * @param zOrder
	 * @param rotation
	 * @return pwElt
	 */
	public static PathwayElement createShape(
			double centerX, double centerY, double width,
			double height, boolean fontStyle, boolean fontDecoration,
			boolean fontStrikeThru, boolean fontWeight, String fontName,
			double fontSize, AlignType align, ValignType vAlign, Color color,
			int lineStyle, double lnThickness, Color fillColor,
			IShape shapeType, int zOrder, double rotation) {
		
		// create pathwayElement Shape
		PathwayElement pwElt = PathwayElement.createPathwayElement(ObjectType.SHAPE);
		
		pwElt.setMCenterX(centerX);
		pwElt.setMCenterY(centerY);
		pwElt.setMWidth(width);
		pwElt.setMHeight(height);
		pwElt.setItalic(fontStyle);
		pwElt.setUnderline(fontDecoration);
		pwElt.setStrikethru(fontStrikeThru);
		pwElt.setBold(fontWeight);
		pwElt.setFontName(fontName);
		pwElt.setMFontSize(fontSize);
		pwElt.setAlign(align);
		pwElt.setValign(vAlign);
		pwElt.setColor(color);
		pwElt.setLineStyle(lineStyle);
		pwElt.setLineThickness(lnThickness);
		pwElt.setFillColor(fillColor);
		pwElt.setShapeType(shapeType);
		pwElt.setZOrder(zOrder);
		pwElt.setRotation(rotation);
				
		return pwElt;
	}
	
	/**
	 * State values are added to the pathway element model.
	 * @param relX
	 * @param relY
	 * @param width
	 * @param height
	 * @param color
	 * @param lineStyle
	 * @param lnThickness
	 * @param fillColor
	 * @param shapeType
	 * @param zOrder
	 * @param database
	 * @param ID
	 * @param bioPaxRefs
	 * @param graphId
	 * @param graphRef
	 * @param textLabel
	 * @param stateType
	 * @return pwElt
	 */
	public static PathwayElement createState(
			double relX, double relY, double width, double height,
			Color color, int lineStyle, int lnThickness,
			Color fillColor, IShape shapeType, int zOrder,
			DataSource database, String ID, List<String> bioPaxRefs,
			String graphId, String graphRef, String textLabel, String stateType) {
		
		// create pathwayElement State
		PathwayElement pwElt = PathwayElement.createPathwayElement(ObjectType.STATE);
		
		// for better definition of identifier and database
		Xref ref = new Xref(ID, database);
		
		pwElt.setRelX(relX);
		pwElt.setRelY(relY);
		pwElt.setMWidth(width);
		pwElt.setMHeight(height);
		pwElt.setColor(color);
		pwElt.setLineStyle(lineStyle);
		pwElt.setLineThickness(lnThickness);
		pwElt.setFillColor(fillColor);
		pwElt.setShapeType(shapeType);
		pwElt.setZOrder(zOrder);
		pwElt.setDataSource(ref.getDataSource());
		pwElt.setElementID(ref.getId());
		pwElt.setGraphId(graphId);
		pwElt.setGraphRef(graphRef);
		pwElt.setTextLabel(textLabel);
		
		if (stateType == "unknown") {} else {}
				
		if (bioPaxRefs.size() > 0) {
			for (int i = 0; i < bioPaxRefs.size(); i++) {
				pwElt.addBiopaxRef(bioPaxRefs.get(i));
			}
		}
			return pwElt;
	}
	
	/**
	 * Biopax information is added to the pathway element model.
	 * @param bioPaxRefId
	 * @param pubmedId
	 * @return xref
	 */
	public static PublicationXref addBioPaxAttributes(
			List<String> bioPaxRefId, String pubmedId) {
		
		// create publicationXref object
		PublicationXref xref = new PublicationXref();
		
		if (!(bioPaxRefId.isEmpty())) xref.setId(bioPaxRefId.get(0));
		else {xref.setId("");}
		if (!(pubmedId.equals(""))) xref.setPubmedId(pubmedId);
		else {System.out.println("No pubmedId for biopaxref!!"); throw new EmptyStackException();}
		
		return xref;
		
	}
	
	// not according to wpRDF
	/**public static PublicationXref addBioPaxAttributes(
			String bioPaxRefId, String pubmedId, String title,
			String source, String year, List<String> authorList) {
		
		// create publicationXref object
		PublicationXref xref = new PublicationXref();
		
		xref.setId(bioPaxRefId);
		xref.setPubmedId(pubmedId);
		xref.setTitle(title);
		xref.setSource(source);
		xref.setYear(year);
		
		if (authorList.size() > 0) {
			for (int i=0; i<authorList.size(); i++) {
				xref.setAuthors(authorList.get(i));
			}
		}
		
		return xref;
		
	}*/
	
	/**
	 * mPoints are added to the pathway element model.
	 * @param pwElt
	 * @param mPoints
	 * @param mPointsDoubles
	 * @return mPoints
	 */
	private static List<MPoint> createMPoints(
			PathwayElement pwElt, List<MPoint> mPoints, List<Double> mPointsDoubles) {
		
		for (int i=0; i<mPointsDoubles.size(); i+=2) {
			MPoint cord = pwElt.new MPoint(Double.parseDouble(mPointsDoubles.get(i).toString()),
					Double.parseDouble(mPointsDoubles.get(i+1).toString()));
			mPoints.add(cord);
		}		
		return mPoints;
	}
}
