package provaloadBalancing;

import it.unifi.facpl.lib.policy.*;
import it.unifi.facpl.lib.enums.*;
import it.unifi.facpl.lib.util.*;

@SuppressWarnings("all")	
public class PolicySet_Release_Policies extends PolicySet {
	public PolicySet_Release_Policies(){
		addId("Release_Policies");
		//Algorithm Combining
		addCombiningAlg(new it.unifi.facpl.lib.algorithm.PermitOverridesGreedy());
		//Target
		addTarget(new ExpressionBooleanTree(ExprBooleanConnector.AND,new ExpressionBooleanTree(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.Equal(), "RELEASE",new AttributeName("action","action-id") 
		)),new ExpressionBooleanTree(new ExpressionBooleanTree(ExprBooleanConnector.OR,new ExpressionBooleanTree(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.Equal(), "P_1",new AttributeName("subject","profile-id") 
		)),new ExpressionBooleanTree(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.Equal(), "P_2",new AttributeName("subject","profile-id") 
		)))
		))
		);
		//PolElements
		addPolicyElement(new Rule_hyper_1_release());
		addPolicyElement(new Rule_hyper_2_release());
		//Obligation
		}
		
		private class Rule_hyper_1_release extends Rule {
			
				Rule_hyper_1_release (){
					addId("hyper_1_release");
					//Effect
					addEffect(Effect.PERMIT);
					//Target
					addTarget(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.In(), new AttributeName("resource","vm-name") 
					,new AttributeName("system","hyper1.vm-names") 
					));
					//Obligations
					addObligation(new Obligation("release",Effect.PERMIT,ObligationType.M,"localhost",
					new AttributeName("resource","vm-name") 
					)
					);
			}	
		}
		private class Rule_hyper_2_release extends Rule {
			
				Rule_hyper_2_release (){
					addId("hyper_2_release");
					//Effect
					addEffect(Effect.PERMIT);
					//Target
					addTarget(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.In(), new AttributeName("resource","vm-name") 
					,new AttributeName("system","hyper2.vm-names") 
					));
					//Obligations
					addObligation(new Obligation("release",Effect.PERMIT,ObligationType.M,"192.168.1.11",
					new AttributeName("resource","vm-name") 
					)
					);
			}	
		}
}
