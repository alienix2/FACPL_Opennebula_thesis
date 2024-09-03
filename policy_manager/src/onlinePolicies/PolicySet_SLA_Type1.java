
import it.unifi.facpl.lib.policy.*;
import it.unifi.facpl.lib.enums.*;
import it.unifi.facpl.lib.util.*;

@SuppressWarnings("all")	
public class PolicySet_SLA_Type1 extends PolicySet {
	public PolicySet_SLA_Type1(){
		addId("SLA_Type1");
		//Algorithm Combining
		addCombiningAlg(new it.unifi.facpl.lib.algorithm.DenyUnlessPermitGreedy());
		//Target
		addTarget(new ExpressionBooleanTree(ExprBooleanConnector.AND,new ExpressionBooleanTree(new ExpressionBooleanTree(ExprBooleanConnector.OR,new ExpressionBooleanTree(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.Equal(), "P_1",new AttributeName("subject","profile-id") 
		)),new ExpressionBooleanTree(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.Equal(), "P_2",new AttributeName("subject","profile-id") 
		)))
		),new ExpressionBooleanTree(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.Equal(), "6",new AttributeName("resource","vm-type") 
		)))
		);
		//PolElements
		addPolicyElement(new Rule_hyper_1());
		addPolicyElement(new Rule_hyper_2());
		//Obligation
		addObligation(new Obligation("log",Effect.DENY,ObligationType.O,"Not enough available resources for TYPE_1 VMs"
		)
		);
		}
		
		private class Rule_hyper_1 extends Rule {
			
				Rule_hyper_1 (){
					addId("hyper_1");
					//Effect
					addEffect(Effect.PERMIT);
					//Target
					addTarget(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.LessThanOrEqual(), 100,new AttributeName("system","hyper1.availableResources") 
					));
					//Obligations
					addObligation(new Obligation("create",Effect.PERMIT,ObligationType.M,0,
					new AttributeName("system","vm-name") ,
					6
					)
					);
			}	
		}
		private class Rule_hyper_2 extends Rule {
			
				Rule_hyper_2 (){
					addId("hyper_2");
					//Effect
					addEffect(Effect.PERMIT);
					//Target
					addTarget(new ExpressionFunction(new it.unifi.facpl.lib.function.comparison.LessThanOrEqual(), 100,new AttributeName("system","hyper2.availableResources") 
					));
					//Obligations
					addObligation(new Obligation("create",Effect.PERMIT,ObligationType.M,6,
					new AttributeName("system","vm-name") ,
					6
					)
					);
			}	
		}
}
