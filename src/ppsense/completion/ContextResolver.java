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

package ppsense.completion;

import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionContextResolver;
import org.eclipse.php.internal.core.codeassist.contexts.CompletionContextResolver;

import ppsense.Console;

public class ContextResolver extends CompletionContextResolver implements ICompletionContextResolver {
	
	private static Console console = new Console();
	
    public ICompletionContext[] createContexts() {
    	console.trace("Completion context resolver started");
        return new ICompletionContext[] { new ppsense.completion.Context() };
    }
}
