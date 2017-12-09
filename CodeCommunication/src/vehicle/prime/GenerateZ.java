package vehicle.prime;

import java.util.ArrayList;
import java.util.List;

public class GenerateZ {
	
	List<Integer> Zp = new ArrayList<>();
	public List<Integer> generateZp(int prime){
		for(int i = 1;i<prime;i++){
			Zp.add(i);
		}
		
		return Zp;
	}
}
