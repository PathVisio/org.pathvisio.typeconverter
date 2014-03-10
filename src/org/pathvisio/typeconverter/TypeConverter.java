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

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.pathvisio.core.model.DataNodeType;
import org.pathvisio.core.model.ObjectType;
import org.pathvisio.core.view.GeneProduct;
import org.pathvisio.core.view.Graphics;
import org.pathvisio.core.view.Label;
import org.pathvisio.core.view.Line;
import org.pathvisio.core.view.VPathwayElement;
import org.pathvisio.desktop.PvDesktop;
import org.pathvisio.desktop.plugin.Plugin;
import org.pathvisio.gui.PathwayElementMenuListener.PathwayElementMenuHook;

/**
 * TypeConverter plugin for PathVisio
 * Adds right click menus to change the type of a Label or Interaction or GraphicalLine
 * 
 * @author mkutmon
 *
 */
public class TypeConverter implements Plugin, PathwayElementMenuHook {

	private PvDesktop desktop;
	
	@Override
	public void init(PvDesktop desktop) {
		this.desktop = desktop;
		desktop.addPathwayElementMenuHook(this);
	}

	@Override
	public void done() {
		desktop.getSwingEngine().getApplicationPanel().getPathwayElementMenuListener().removePathwayElementMenuHook(this);
	}

	@Override
	public void pathwayElementMenuHook(VPathwayElement e, JPopupMenu menu) {
		JMenu refMenu = new JMenu("Convert type");
		
		System.out.println(e.getClass());
		if(e instanceof Label) {
			if(e instanceof Graphics) {
				LabelToDataNode action1 = new LabelToDataNode((Graphics)e, DataNodeType.GENEPRODUCT);
				refMenu.add(action1);
				LabelToDataNode action2 = new LabelToDataNode((Graphics)e, DataNodeType.PROTEIN);
				refMenu.add(action2);
				LabelToDataNode action3 = new LabelToDataNode((Graphics)e, DataNodeType.METABOLITE);
				refMenu.add(action3);
				LabelToDataNode action4 = new LabelToDataNode((Graphics)e, DataNodeType.PATHWAY);
				refMenu.add(action4);
				LabelToDataNode action5 = new LabelToDataNode((Graphics)e, DataNodeType.RNA);
				refMenu.add(action5);
				menu.add(refMenu);
			} 
		} else if(e instanceof Line) {
			if(((Line) e).getPathwayElement().getObjectType().equals(ObjectType.LINE )) {
				LineToGraphicalLine action1 = new LineToGraphicalLine((Graphics)e, ObjectType.GRAPHLINE);
				refMenu.add(action1);
				menu.add(refMenu);
			} else if (((Line) e).getPathwayElement().getObjectType().equals(ObjectType.GRAPHLINE)) {
				LineToGraphicalLine action1 = new LineToGraphicalLine((Graphics)e, ObjectType.LINE);
				refMenu.add(action1);
				menu.add(refMenu);
			}
		} else if(e instanceof GeneProduct) {
			String dnType = ((GeneProduct) e).getPathwayElement().getDataNodeType();
			if(!DataNodeType.byName(dnType).equals(DataNodeType.GENEPRODUCT)) {
				DataNodeToDataNode action1 = new DataNodeToDataNode((Graphics)e, DataNodeType.GENEPRODUCT);
				refMenu.add(action1);
			}
			if(!DataNodeType.byName(dnType).equals(DataNodeType.PROTEIN)) {
				DataNodeToDataNode action2 = new DataNodeToDataNode((Graphics)e, DataNodeType.PROTEIN);
				refMenu.add(action2);
			}
			if(!DataNodeType.byName(dnType).equals(DataNodeType.METABOLITE)) {
				DataNodeToDataNode action3 = new DataNodeToDataNode((Graphics)e, DataNodeType.METABOLITE);
				refMenu.add(action3);
			}
			if(!DataNodeType.byName(dnType).equals(DataNodeType.PATHWAY)) {
				DataNodeToDataNode action4 = new DataNodeToDataNode((Graphics)e, DataNodeType.PATHWAY);
				refMenu.add(action4);
			}
			if(!DataNodeType.byName(dnType).equals(DataNodeType.RNA)) {
				DataNodeToDataNode action5 = new DataNodeToDataNode((Graphics)e, DataNodeType.RNA);
				refMenu.add(action5);
			}
			menu.add(refMenu);
		}
	}

}
