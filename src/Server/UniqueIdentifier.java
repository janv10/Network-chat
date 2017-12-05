package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueIdentifier {
	
	private static List<Integer> ids = new ArrayList<Integer>(); 
	private static final int RANGE = 10000; //allow for 100 unique clients 
	
	private static int index = 0;
	
	static {
		for (int i = 0; i < RANGE; i++ ) {
			ids.add(i);
		}
		Collections.shuffle(ids);		//random values; never the same 
	}
	private UniqueIdentifier() {
		
	}
	
	public static int getIdentifier() {
		if (index > ids.size() - 1) {	//If more than 10000 users connection, start re-using index values/reset
			index = 0; 
		}
		return ids.get(index++); 
	}
	


}
