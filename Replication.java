package P2PSimulator;


public class Replication {
	
	private static double reliability = 0.9;
	private static int minReplicationFactor = 2;
	private static int maxReplicationFactor = 100;
	
	public static int replicationFactor(int m, int n) {
		// rf = replication factor
		for (int rf = minReplicationFactor; rf < maxReplicationFactor; rf++) {
			double p = 1.0;
			for (int i = 0; i < rf; i++) {
				p = p * (m - i) / (n - i);
			}
			// 1 − p gives the probability of f peers not being within predicted
			// departing m peers. As automatic replication mechanism is supposed
			// to provide at least reliability r,
			// 1 − p should be more than or equal to r
			//
			// 1-p >= r
			if (1 - p >= reliability) {
				return rf;
			}
		}
		return maxReplicationFactor;
	}
}
