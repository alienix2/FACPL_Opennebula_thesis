package test_shell;

import it.unifi.facpl.lib.policy.*;
import it.unifi.facpl.lib.enums.*;
import it.unifi.facpl.lib.util.*;

@SuppressWarnings("all")	
public class PolicySet_PSet extends PolicySet {
	public PolicySet_PSet(){
		addId("PSet");
		//Algorithm Combining
		addCombiningAlg(new it.unifi.facpl.lib.algorithm.DenyUnlessPermitGreedy());
		//Target
		addTarget(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.Equal(), "READ",new AttributeName("action","id") 
		));
		//PolElements
		addPolicyElement(new Rule_rule1());
		//Obligation
		addObligation(new Obligation("shellCommand",Effect.PERMIT,ObligationType.M,new AttributeName("action","command1") ,
		new AttributeName("action","command2") 
		)
		);
		addObligation(new Obligation("log",Effect.DENY,ObligationType.M,new AttributeName("prova","hello") ,
		"Not allowed subject tried to exec a command"
		)
		);
		}
		
		private class Rule_rule1 extends Rule {
			
				Rule_rule1 (){
					addId("rule1");
					//Effect
					addEffect(Effect.PERMIT);
					//Target
					addTarget(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.Equal(), "allowed",new AttributeName("action","text") 
					));
					//Obligations
			}	
		}
}
