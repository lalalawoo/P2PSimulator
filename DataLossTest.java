import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map;
import java.util.Map.Entry;

public class DataLossTest{
	private static int mDataPoints = 500;
	private static int mOL = 10;
	private static int mInitPeerSize = 500;
	private static int mChurnFluctuation = 5;
	private static int mInitChurn = 100;
	private static ArrayList<Integer> peerMap = new ArrayList<>();
	private static int maxPeerNr = 999;
	private static HashMap<Integer,ArrayList<Integer>> dataKeys = new HashMap<>();
	private static int initKeySize;
	
	public static void main(String[] args){
		Churn c = new Churn(mChurnFluctuation);
		int lastChurnSize = mInitChurn;
		// initialize the peerMap
		for(int i=0;i<mInitPeerSize;i++){
			peerMap.add(i);
		}
		ArrayList<Integer> leavingChurns = new ArrayList<>();
		ArrayList<Double> demas = new ArrayList<>();
		Prediction pred = new Prediction();
		int count = 0;
		while(count<mDataPoints){
			int leave = c.generate(lastChurnSize);
			leavingChurns.add(leave);
			ArrayList<Integer> removingPeers = removePeers(leave);
			int join = c.generate(lastChurnSize);
			addPeers(join);			
			lastChurnSize = leave;
			
			if(count<mOL-1){
				demas.add(Double.valueOf(0));
				count++;
				continue;
			}
			// initialize dataKeys 
			if(count==mOL-1){
				demas.add(Double.valueOf(0));
				initKeySize = peerMap.size();
				System.out.println("initKeySize="+initKeySize);
				for(int i=0;i<initKeySize;i++){
					ArrayList<Integer> replicas = new ArrayList<>();
					replicas.add(peerMap.get(i));
					dataKeys.put(i, replicas);
				}
				int rf = Replication.replicationFactor(leave, peerMap.size());
				System.out.println("initRF="+rf);
				replicateData(dataKeys,rf);
				count++;
				continue;
			}			
			double predDema = pred.dema(leavingChurns,demas,mOL);
			demas.add(predDema);
			int predRF = Replication.replicationFactor((int)predDema, peerMap.size());
			removeReplica(dataKeys,removingPeers);
			replicateData(dataKeys,predRF);
			if(count%60==0){
				System.out.println("Interval" + count/60 + ": dataloss is " + (1.0-(double)dataKeys.size()/initKeySize));
			}
			count++;
		}
		System.out.println("Interval" + Math.ceil(count/60) + ": dataloss is " + (1.0-(double)dataKeys.size()/initKeySize));
	}
	
	public static ArrayList<Integer> removePeers(int peerLeavingSize){
		ArrayList<Integer> result = new ArrayList<>();
		Random r = new Random();
		for(int i=0;i<peerLeavingSize;i++){
			Integer peerNr = r.nextInt(maxPeerNr);
			while(!peerMap.contains(peerNr) || result.contains(peerNr)){
				peerNr = r.nextInt(maxPeerNr);
			}
			peerMap.remove(peerNr);
			result.add(peerNr);
		}
		return result;
	}
	
	public static void addPeers(int peerJoiningSize){
		ArrayList<Integer> result = new ArrayList<>();
		Random r = new Random();
		for(int i=0;i<peerJoiningSize;i++){
			Integer peerNr = r.nextInt(maxPeerNr);
			while(peerMap.contains(peerNr) || result.contains(peerNr)){
				peerNr = r.nextInt(maxPeerNr);
			}
			peerMap.add(peerNr);
			result.add(peerNr);
		}
//		return result;
	}	
	
	public static void replicateData(HashMap<Integer,ArrayList<Integer>> dataKeys, int rf){
		ArrayList<Integer> tmp = new ArrayList<Integer>(peerMap);
		Collections.sort(tmp);
		for(Map.Entry<Integer,ArrayList<Integer>> entry : dataKeys.entrySet()){
			ArrayList<Integer> currReplicas = entry.getValue();
			if(currReplicas.size()<rf){
				int index = tmp.indexOf(currReplicas.get(0));
				int offset = 1;
				for(int i=0;i<rf-currReplicas.size();i++){					
					while(index+offset>=tmp.size() || currReplicas.contains(tmp.get(index+offset))){
						if(index+offset>=tmp.size()){
							index=0;
							offset=0;							
						}else{
							offset++;
						}
					}
					entry.getValue().add(tmp.get(index+offset));					
				}
			}
		}
	}
	
	public static void removeReplica(HashMap<Integer,ArrayList<Integer>> dataKeys, ArrayList<Integer> removingPeers){
		for(Integer peerNr : removingPeers){
			Iterator<Entry<Integer, ArrayList<Integer>>> it = dataKeys.entrySet().iterator(); 
			while(it.hasNext()){
				Map.Entry<Integer, ArrayList<Integer>> entry = it.next();
				if(entry.getValue().contains(peerNr)){
					entry.getValue().remove(peerNr);
				}
				if(entry.getValue().size()==0){
					it.remove();
				}
			}
		}
	}
}