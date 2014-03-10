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

import org.pathvisio.core.model.GraphLink.GraphRefContainer;
import org.pathvisio.core.model.ObjectType;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.model.PathwayElement;
import org.pathvisio.core.view.Graphics;
import org.pathvisio.core.view.Line;
import org.pathvisio.core.view.VPathway;
import org.pathvisio.core.view.VPathwayElement;

/**
 * Refactoring of interactions and graphical lines
 * - converts interaction to graphical line
 * - converts graphical line to interaction
 * 
 * @author mkutmon
 *
 */
public class LineToGraphicalLine extends AbstractAction {
	
	private Graphics element; 
	private ObjectType lineType;
	
	public LineToGraphicalLine(Graphics element, ObjectType lineType) {
		if(lineType.equals(ObjectType.LINE)) {
			putValue(NAME, "GraphicalLine to Interaction");
		} else if(lineType.equals(ObjectType.GRAPHLINE)) {
			putValue(NAME, "Interaction to GraphicalLine");
		}
		this.element = element;
		this.lineType = lineType;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (element instanceof Line) {
			PathwayElement orig = element.getPathwayElement();
			PathwayElement elt = null;
			if(lineType.equals(ObjectType.GRAPHLINE)) {
				elt = PathwayElement.createPathwayElement(ObjectType.GRAPHLINE);
			} else if(lineType.equals(ObjectType.LINE)) {
				elt = PathwayElement.createPathwayElement(ObjectType.LINE);
			}
			if(elt != null) {
				for (Object key : orig.getPropertyKeys()) {
					if (elt.getPropertyKeys().contains(key))
						elt.setPropertyEx(key, orig.getPropertyEx(key));
				}
				VPathway vPathway = element.getDrawing();
				Pathway pwy = vPathway.getPathwayModel();
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