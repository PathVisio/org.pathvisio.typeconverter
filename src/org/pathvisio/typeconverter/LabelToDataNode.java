// PathVisio TypeConverter plugin,
// a tool to convert pathway elements into different types
// Copyright 2014 BiGCaT Bioinformatics
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.pathvisio.typeconverter;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;

import org.pathvisio.core.model.DataNodeType;
import org.pathvisio.core.model.GraphLink.GraphRefContainer;
import org.pathvisio.core.model.ObjectType;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.model.PathwayElement;
import org.pathvisio.core.model.ShapeType;
import org.pathvisio.core.view.DefaultTemplates;
import org.pathvisio.core.view.Graphics;
import org.pathvisio.core.view.Label;
import org.pathvisio.core.view.VPathway;
import org.pathvisio.core.view.VPathwayElement;

/**
 * Refactoring of labels to different data node types
 * - converts label to gene product
 * - converts label to protein
 * - converts label to metabolite
 * - converts label to pathway
 * - converts label to RNA
 * 
 * @author mkutmon
 *
 */
public class LabelToDataNode extends AbstractAction {
		
	private Graphics element; 
	private DataNodeType dnType;
	
	public LabelToDataNode(Graphics element, DataNodeType dnType) {
		putValue(NAME, "Label to " + dnType);
		this.element = element;
		this.dnType = dnType;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (element instanceof Label) {
			PathwayElement orig = ((Label) element).getPathwayElement();
			PathwayElement elt = PathwayElement.createPathwayElement(ObjectType.DATANODE);
			
			for (Object key : orig.getPropertyKeys()) {
				if (elt.getPropertyKeys().contains(key))
					elt.setPropertyEx(key, orig.getPropertyEx(key));
			}
			VPathway vPathway = element.getDrawing();
			Pathway pwy = vPathway.getPathwayModel();
			elt.setDataNodeType(dnType);
			if(dnType.equals(DataNodeType.PATHWAY)) {
				elt.setColor(DefaultTemplates.COLOR_PATHWAY);
				elt.setBold(true);
			} else if (dnType.equals(DataNodeType.METABOLITE)) {
				elt.setColor(DefaultTemplates.COLOR_METABOLITE);
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