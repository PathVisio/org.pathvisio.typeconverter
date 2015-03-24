package org.pathvisio.typeconverter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.pathvisio.core.model.DataNodeType;
import org.pathvisio.core.model.ObjectType;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.model.PathwayElement;
import org.pathvisio.core.model.ShapeType;
import org.pathvisio.core.model.GraphLink.GraphRefContainer;
import org.pathvisio.core.view.GeneProduct;
import org.pathvisio.core.view.Graphics;
import org.pathvisio.core.view.VPathway;
import org.pathvisio.core.view.VPathwayElement;

public class DataNodeToLabel extends AbstractAction {

	private Graphics element; 
	private ObjectType dnType;

	public DataNodeToLabel(Graphics element, ObjectType dnType) {
		putValue(NAME, element.getPathwayElement().getDataNodeType()+" to Label");
		this.element = element;
		this.dnType = dnType;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (element instanceof GeneProduct) {
			PathwayElement orig = ((GeneProduct) element).getPathwayElement();
			PathwayElement elt = PathwayElement.createPathwayElement(ObjectType.LABEL);
			boolean convert = true;
			for (Object key : orig.getPropertyKeys()) {
				if (key.toString().equals("GENEID") && (!orig.getPropertyEx(key).equals(""))){
						int dialogButton = JOptionPane.YES_NO_OPTION;
						int dialogResult = JOptionPane.showConfirmDialog(null,
								"During the convertion, you will lose your identifier annotation.\n"
								+ "Are you sure you want to continue?",
								"Confirm",
								dialogButton);
						if(dialogResult!=0)
							convert = false;	
				}
				if (elt.getPropertyKeys().contains(key))
					elt.setPropertyEx(key, orig.getPropertyEx(key));
			}
			if (convert){
				VPathway vPathway = element.getDrawing();
				Pathway pwy = vPathway.getPathwayModel();

				elt.setGraphId(pwy.getUniqueGraphId());

				if(orig.getDataNodeType().equals(DataNodeType.PATHWAY.toString())) {
					elt.setColor(Color.BLACK);
					elt.setBold(false);
				} else if (orig.getDataNodeType().equals(DataNodeType.METABOLITE.toString())) {
					elt.setColor(Color.BLACK);
				} 

				elt.setShapeType(ShapeType.RECTANGLE);
				elt.setGraphId(pwy.getUniqueGraphId());

				vPathway.getUndoManager().newAction("Change element type");
				pwy.add(elt);
				for (GraphRefContainer r : pwy.getReferringObjects(orig.getGraphId())) {
					r.linkTo(elt, r.getRelX(), r.getRelY());
				}
				vPathway.removeDrawingObjects(Arrays.asList((VPathwayElement)element), true);
			}
		}
	}
}