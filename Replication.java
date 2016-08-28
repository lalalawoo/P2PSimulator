
public class Replication {
	
	private double reliability;
	private int observationLength = 10;
	
	public Replication observationLength(int observationLength){
		this.observationLength = observationLength;
		return this;
	}
	public int observationLenth(){
		return observationLength;
	}
}