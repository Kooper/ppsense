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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.*;
import org.eclipse.dltk.internal.core.SourceRange;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionStrategy;

import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.contexts.AbstractCompletionContext;
import org.eclipse.php.internal.core.codeassist.contexts.ClassMemberContext;
import org.eclipse.php.internal.core.codeassist.strategies.ClassMethodsStrategy;
import org.eclipse.php.internal.core.typeinference.PHPModelUtils;
import org.eclipse.php.internal.core.util.text.PHPTextSequenceUtilities;
import org.eclipse.php.internal.core.util.text.TextSequence;


@SuppressWarnings("restriction")
public class Strategy extends ClassMethodsStrategy implements ICompletionStrategy {
	
	private static ppsense.Console console = new ppsense.Console();

	public Strategy(ICompletionContext context) {
		super(context);
	}

	public void apply(ICompletionReporter reporter) throws BadLocationException {
		
		ICompletionContext context = getContext();
		
        if (!(context instanceof ClassMemberContext)) {
        	console.trace("Strategy not applied as context is not ClassMemberContext");
            return;
        }

		console.trace("Strategy::apply() started");
		
		Context concreteContext = (Context) context;
        CompletionRequestor requestor = concreteContext.getCompletionRequestor();
        String prefix = concreteContext.getPrefix();
        String suffix = getSuffix(concreteContext);
        
        
        SourceRange replaceRange = suffix.equals("")
        	? getReplacementRange(concreteContext)
        	: getReplacementRangeWithBraces(concreteContext);

        
        AbstractCompletionContext aContext = (AbstractCompletionContext) context;
		int offset = aContext.getOffset();
		TextSequence statementText = aContext.getStatementText();
		
		int triggerEnd = getTriggerEnd(statementText);
		
		IType[] types = ContextParser.getTypesFor(
			aContext.getSourceModule(),
			statementText,
			triggerEnd,
			offset
		);
        
		boolean exactName = requestor.isContextInformationMode();
		List<IMethod> methods = getMethodsFromTypes(types, prefix, exactName, concreteContext);
        
        for (IMethod method : methods) {
            reporter.reportMethod(method, suffix, replaceRange);
        }
	}

	/**
	 * Returns methods extracted from supplied types.
	 * 
	 * @param types
	 * @param prefix
	 * @param exactName
	 * @param concreteContext
	 * @return List of methods
	 */
	private List<IMethod> getMethodsFromTypes(
		IType[] types,
		String prefix,
		boolean exactName,
		Context concreteContext
	) {
		
		List<IMethod> result = new LinkedList<IMethod>();
		
		for (IType type : types) {
        	
            try {
                ITypeHierarchy hierarchy = getCompanion().getSuperTypeHierarchy(type, null);

                IMethod[] methods = PHPModelUtils.getTypeHierarchyMethod(
                	type,
                	hierarchy,
                	prefix,
                	exactName,
                	null
                );

                for (IMethod method : removeOverriddenElements(Arrays.asList(methods))) {

                	if (!isFiltered(method, type, concreteContext)) {
                      	console.trace("method: " + method.toString());
                        result.add(method);
                	}
                }
                
            } catch (CoreException e) {
                console.error(e.toString());
            }
            
        }
		
		return result;
	}
	
	private int getTriggerEnd(TextSequence statementText) {
		
		int triggerEnd = PHPTextSequenceUtilities.readBackwardSpaces(
				statementText, statementText.length());
		
		triggerEnd = PHPTextSequenceUtilities.readIdentifierStartIndex(
				statementText, triggerEnd, true);
		
		triggerEnd = PHPTextSequenceUtilities.readBackwardSpaces(
				statementText, triggerEnd);
		
		return triggerEnd;
	}
	
	/**
	 * Borrowed from class instantiation strategy from php-grammar-in-antlr
	 * @param abstractContext
	 * @return
	 */
    public String getSuffix(AbstractCompletionContext abstractContext) {
        String nextWord = null;
        try {
                nextWord = abstractContext.getNextWord();
        } catch (BadLocationException e) {
                if (DLTKCore.DEBUG_COMPLETION) {
                        e.printStackTrace();
                }
        }
        return "(".equals(nextWord) ? "" : "()";
}

}
