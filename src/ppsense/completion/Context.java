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

import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.php.internal.core.codeassist.contexts.ClassObjMemberContext;

import ppsense.Console;
import ppsense.evaluator.BrokerClassnameResolver;

public class Context extends ClassObjMemberContext {
	
	private static Console console = new Console();
	
	private IType[] types;
	private boolean typesInitialized = false;
	
	public IType[] getLhsTypes() {
		
		if (!typesInitialized) {
			return super.getLhsTypes();
		}
		
		console.trace("Context returns lhsTypes!");
		for (IType type : types) {
			console.trace("Type: " + type);
		}
		
		return types;
	}
	
	public void setLhsTypes(IType[] settypes) {
		console.trace("Context setting types");
		
		for (IType type : settypes) {
			console.trace("Type: " + type);
		}
		types = settypes;
		typesInitialized = true;
	}

	public boolean isValid(ISourceModule sourceModule, int offset,
		     CompletionRequestor requestor) {
		
		if (!super.isValid(sourceModule, offset, requestor)) {
			console.trace("Context::isValid - invalid as invalid for parent: " + this.getStatementText());
			return false;
		}

		console.trace("Context::isValid - statement text: " + this.getStatementText());
		
		
		
//		TextSequence statementText = getStatementText();
//		int totalLength = statementText.length();
//		int elementStart = PHPTextSequenceUtilities.readBackwardSpaces(
//				statementText, totalLength);
//		elementStart = PHPTextSequenceUtilities.readIdentifierStartIndex(
//				statementText, elementStart, true);
//		elementStart = PHPTextSequenceUtilities.readBackwardSpaces(
//				statementText, elementStart);
		
//		
//		console.trace("Element start is assumed to be " + elementStart);
//		
//		
//		//IType[] types = getCompanion().getLeftHandType(this);
//		IType[] types = super.getLhsTypes();
//		
//		for (IType type : types) {
//			console.trace("Left hand type reported by companion: " + type);
//		}
		
//		String textSequence = getStatementText().toString();
		
//        String token = "Db_Table_Broker::get(";
        
        BrokerClassnameResolver brokerSearcher = new BrokerClassnameResolver(getStatementText());
		
		if (brokerSearcher.containsBrokerCall()) {
        
//        if (textSequence.indexOf(token) >= 0) {
        
        	console.trace("Context encountered broker token in text sequence - assume context is valid");
        	
        	return true;
        	
//            textSequence = textSequence.substring(textSequence.indexOf(token) + token.length());
//            textSequence = textSequence.substring(0, textSequence.lastIndexOf(")->"));
        } else {
        	// Probably here I should return control...
        	console.trace("Did not encounter broker token in " + getStatementText() + " - exit");
        	return false;
        }
        
        
//        String className = textSequence.substring(1, textSequence.length() - 1);
//        console.trace("Suppose it is a classname " + className);
//        
//        if (className.indexOf(")") > 0) {
//        	return true;
//        }
//		
//		return true;
	}
/*	
	public boolean isValid(ISourceModule sourceModule, int offset,
		      CompletionRequestor requestor) {

		console.trace("Completion context run");
		    // Call to super to verify that cursor is in the class member call
		    // context
		    if (super.isValid(sourceModule, offset, requestor)) {

		      // This context only supports "->" trigger type (not the "::")
		      if (getTriggerType() == Trigger.OBJECT) {

		        IType[] recieverClass = getLhsTypes();
		        
		        console.trace("Statement text: " + this.getStatementText());

		        try {
					console.trace("Prefix text: " + this.getPrefix());
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        console.trace("Reciever class length: " + recieverClass.length);
		        
		        // recieverClass contains types for the expression from the left
		        // side of "->"
		        for (IType c : recieverClass) {
		        	console.trace("Reciever class: " + c);
		          if (!isViewer(c)) {
		            return false;
		          }
		        }
		        return true;
		      } else {
		    	  console.trace("Trigger type is not object");
		      }
		    } else {
		    	console.trace("Superclass mark context as invalid");
		    }

		    return false;
		  }
*/
		  /**
		   * Check that the type of the class is Viewer
		   */
/*		  private boolean isViewer(IType type) {
		    // XXX: add more sophisticated check
		    return "Viewer".equalsIgnoreCase(type.getElementName());
		  }
		  */
}
