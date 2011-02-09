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

package ppsense;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class Console {

	private static final String CONSOLE_NAME = "ppsense.console";
	
	private static boolean enabled = false;

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++) {
			if (name.equals(existing[i].getName())) {
				return (MessageConsole) existing[i];
			}
		}

		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	public MessageConsoleStream getConsoleOutputSteam() {
		MessageConsole myConsole = this.findConsole(CONSOLE_NAME);
		MessageConsoleStream out = myConsole.newMessageStream();
		return out;
	}
	
	public void trace(String msg) {
		if (enabled) {
			getConsoleOutputSteam().println("[PP] "  + msg);
		}
	}
	
	public void error(String msg) {
		getConsoleOutputSteam().println("[PP ERROR] " + msg);
	}
	
	public static void enableLogging() {
		enabled = true;
	}
	
	public static void disableLogging() {
		enabled = false;
	}
}
