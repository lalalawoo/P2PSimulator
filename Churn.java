package P2PSimulator;

import java.util.Random;

public class Churn{
	
	private int churnFluctuation;
	
	public Churn(int churnFluctuation){
		this.churnFluctuation = churnFluctuation;
	}
	
	public int generate(int lastChurnSize){
		Random r = new Random();
		return Math.max(0,(int)(r.nextGaussian()*churnFluctuation)+lastChurnSize);		
	}	
}
