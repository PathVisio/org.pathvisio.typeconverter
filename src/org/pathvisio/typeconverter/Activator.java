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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.pathvisio.desktop.plugin.Plugin;

/**
 * Activator of the TypeConverter plugin
 * Used to change the data node or interaction type of an existing pathway element
 * 
 * @author mkutmon
 *
 */
public class Activator implements BundleActivator {

	private TypeConverter plugin;

	public void start(BundleContext context) throws Exception {
		plugin = new TypeConverter();
		context.registerService(Plugin.class.getName(), plugin, null);
	}

	public void stop(BundleContext context) throws Exception {
		plugin.done();
	}

}
