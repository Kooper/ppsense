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

import org.eclipse.dltk.ti.GoalState;
import org.eclipse.dltk.ti.goals.GoalEvaluator;
import org.eclipse.dltk.ti.goals.IGoal;
import org.eclipse.php.internal.core.typeinference.PHPClassType;

public class BrokerClassnameGoalEvaluator extends GoalEvaluator {

	private String tableName;

	public BrokerClassnameGoalEvaluator(IGoal goal, String tableName) {
		super(goal);
		this.tableName = tableName;
	}

	public Object produceResult() {
		
		String effectiveClassname =
			BrokerClassnameResolver.getClassname(tableName);
		
		return effectiveClassname.length() > 0
			? new PHPClassType(effectiveClassname)
			: null;
	}

	public IGoal[] init() {
		return null;
	}

	public IGoal[] subGoalDone(IGoal subgoal, Object result, GoalState state) {
		return null;
	}
}
