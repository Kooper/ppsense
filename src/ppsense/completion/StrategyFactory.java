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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionStrategy;
import org.eclipse.php.core.codeassist.ICompletionStrategyFactory;

import ppsense.Console;

public class StrategyFactory implements ICompletionStrategyFactory {

	private static Console console = new Console();

	public ICompletionStrategy[] create(ICompletionContext[] contexts) {

		console.trace("Strategy factory started");

		List<ICompletionStrategy> result = new LinkedList<ICompletionStrategy>();

		for (ICompletionContext context : contexts) {
			
			console.trace("completion context " + context.toString() + " of class " + context.getClass().toString());
			
			if (context.getClass() == ppsense.completion.Context.class) {
				console.trace("has been added");
				result.add(new ppsense.completion.Strategy(context));
			}
		}

		return (ICompletionStrategy[]) result.toArray(new ICompletionStrategy[result.size()]);
	}
}