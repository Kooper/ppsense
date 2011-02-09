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

import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ti.IGoalEvaluatorFactory;
import org.eclipse.dltk.ti.goals.ExpressionTypeGoal;
import org.eclipse.dltk.ti.goals.GoalEvaluator;
import org.eclipse.dltk.ti.goals.IGoal;
import org.eclipse.php.internal.core.compiler.ast.nodes.Scalar;
import org.eclipse.php.internal.core.compiler.ast.nodes.StaticMethodInvocation;

public class PPGoalEvaluatorFactory implements IGoalEvaluatorFactory {

	public GoalEvaluator createEvaluator(IGoal goal) {
		
		Class<?> goalClass = goal.getClass();
		
		// Override only 'expression' type goal
		if (ExpressionTypeGoal.class != goalClass) {
			return null;
		}
		
		ASTNode expression = ((ExpressionTypeGoal) goal).getExpression();
	 
		// Interested only in static methods calls
		if (!(expression instanceof StaticMethodInvocation)) {
			return null;
		}
	    	  
	    StaticMethodInvocation inv = (StaticMethodInvocation) expression;
	    ASTNode reciever = inv.getReceiver();
	 
	    GoalEvaluator result = null;
	    
   		if (reciever instanceof SimpleReference	&&
	    	"Db_Table_Broker".equals(((SimpleReference) reciever).getName()) &&
	    	"get".equals(inv.getCallName().getName())
	    ) {
	 		result = produceGoalEvaluator(inv, goal);
   		}
	 
   		return result;
	}
	
	/**
	 * Returns goal evaluator created from the name of the first
	 * call argument.
	 */
	private GoalEvaluator produceGoalEvaluator(StaticMethodInvocation inv, IGoal goal) {
		
		// Take the first call argument:
		List<?> arguments = inv.getArgs().getChilds();
		
		if (arguments.size() == 1) {
			Object first = arguments.get(0);

			if (first instanceof Scalar	&&
				((Scalar) first).getScalarType() == Scalar.TYPE_STRING
			) {

				String className = ((Scalar) first).getValue();
				if (className.length() < 3) {
					return null;
				}

				className = className.substring(1, className.length() - 1);

				return new BrokerClassnameGoalEvaluator(goal, className);
			}
		}
		
		return null;
	}
}
