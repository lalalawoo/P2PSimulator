package P2PSimulator;

import java.util.ArrayList;
import java.util.Random;

public class RandomChurn {
	
	static public void main(){
		ArrayList<Integer> churns = new ArrayList<Integer>();
		double k = 0.1;
		churns.add(1000);
		for(int i = 1; i < 500; i++){
			//do sth
			int temp = churns.get(i-1);
			Random rng = new Random();
			double val = rng.nextGaussian() * 100 + temp;
			int result = (int) Math.round(val);
			churns.add(result);
			
		}		
	}
	
	
}
