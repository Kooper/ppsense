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

package ppsense.evaluator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.php.internal.core.typeinference.PHPClassType;
import org.eclipse.php.internal.core.util.text.TextSequence;

import ppsense.Console;

/**
 * Given a string evaluates if string contains broker static call.
 * inputString may be produced as AbstractCompletionContext.getStatementText()
 * 
 * @param contextString
 * @return
 */

public class BrokerClassnameResolver {
	
	private TextSequence contextString;
	private String effectiveClassName;
	private int offset = 0;

	private static Console console = new Console();
	
	public BrokerClassnameResolver(TextSequence inputString) {
		
		console.trace("BrokerClassnameResolver called for context: " + inputString);
		
		contextString = inputString;
		
		Pattern brokerPattern = Pattern.compile(
			"\\s*Db_Table_Broker\\s*::\\s*get\\s*[(]\\s*(['\"])(\\w+)\\1\\s*[)]\\s*"
		);
	
		Matcher tableNameSearcher = brokerPattern.matcher(contextString);
		
		if (tableNameSearcher.find()) {
			String tableName = tableNameSearcher.group(2);
			
			console.trace("Table name found: " + tableName);
			
			effectiveClassName = getClassname(tableName);
			offset = tableNameSearcher.end();
			
		} else {
			console.trace("Table name was not found");
		}
	}


	/**
	 * Returns true if input string contains broker call.
	 * 
	 * @return true or false
	 */
	public boolean containsBrokerCall() {
		return !effectiveClassName.isEmpty();
	}
	
	/**
	 * Returns PHP type deducted from broker call
	 * 
	 * @return PHP class type
	 */
	public PHPClassType getClassType() {
		return new PHPClassType(effectiveClassName);
	}
	
	/**
	 * Returns offset in context string where broker::get static
	 * call ends.
	 * 
	 * @return end position of broker call
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * Transforms supplied table name into PHP class name according
	 * to our conventions.
	 * 
	 * 'smb_xxx' -> Smb_Db_Table_Broker_Xxx
	 * 'xxx' -> Db_Table_Broker_Xxx
	 * 
	 * @param brokerTableName
	 * @return
	 */
	public static String getClassname(String brokerTableName) {
		
		if (brokerTableName.length() < 1) {
			return "";
		}
		
		String classPrefix = "Db_Table_Broker_";
		String tableName = brokerTableName;
		
		// Find out the real name of the broker-referenced class
		if (tableName.indexOf("smb_") >= 0) {
			classPrefix = "Smb_" + classPrefix;
			tableName = tableName.substring("smb_".length());
		}
		
		String capitalizedTableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
		
		System.err.println("Evaluated classname: " + classPrefix + capitalizedTableName);
		
		return classPrefix + capitalizedTableName;
	}
}
