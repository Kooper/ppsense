/****************************************************************************
 * 
 * Copyright (c) 2011 Victor Kupriyanov <victor.kupriyanov@gmail.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 ****************************************************************************/

package ppsense.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import ppsense.Activator;

public class PreferencesPage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	private BooleanFieldEditor debugEnabledFieldEditor;
	
	public PreferencesPage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	public void createFieldEditors() {
		
		debugEnabledFieldEditor = new BooleanFieldEditor(
			PreferenceConstants.P_BOOLEAN,
			"Enable logging into Eclipse console",
			getFieldEditorParent()
		);
		
		addField(debugEnabledFieldEditor);
	}

	public void init(IWorkbench workbench) {
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		
        super.propertyChange(event);
        
        if (event.getProperty().equals(FieldEditor.VALUE)) {
        	
        	if (debugEnabledFieldEditor.getBooleanValue()) {
        		ppsense.Console.enableLogging();
        	} else {
        		ppsense.Console.disableLogging();
        	}
        }
	}
}