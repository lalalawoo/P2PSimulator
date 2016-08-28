package P2PSimulator;

import java.util.Random;

public class Churn{
	private static int mMaxPeers = 1000;
	
	public static int generate(int peerSize, double churnRate){
		Random r = new Random();
		return r.nextInt(peerSize);
	}	
}