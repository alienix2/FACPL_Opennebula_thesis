
import it.unifi.facpl.lib.policy.*;
import it.unifi.facpl.lib.enums.*;
import it.unifi.facpl.lib.util.*;

@SuppressWarnings("all")	
public class PolicySet_Energy_Saving extends PolicySet {
	public PolicySet_Energy_Saving(){
		addId("Energy_Saving");
		//Algorithm Combining
		addCombiningAlg(new it.unifi.facpl.lib.algorithm.PermitOverridesGreedy());
		//PolElements
		addPolicyElement(new PolicySet_Create_Policies());
		addPolicyElement(new PolicySet_Release_Policies());
		//Obligation
		}
		
}
