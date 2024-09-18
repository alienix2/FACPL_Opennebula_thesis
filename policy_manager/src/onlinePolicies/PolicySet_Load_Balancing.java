
import it.unifi.facpl.lib.policy.*;
import it.unifi.facpl.lib.enums.*;
import it.unifi.facpl.lib.util.*;

@SuppressWarnings("all")	
public class PolicySet_Load_Balancing extends PolicySet {
	public PolicySet_Load_Balancing(){
		addId("Load_Balancing");
		//Algorithm Combining
		addCombiningAlg(new it.unifi.facpl.lib.algorithm.PermitOverridesGreedy());
		//PolElements
		addPolicyElement(new PolicySet_Create_Policies());
		addPolicyElement(new PolicySet_Release_Policies());
		//Obligation
		}
		
}
