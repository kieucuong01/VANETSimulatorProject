package vehicle.dto;

public class CyclicDTO {
	private long x;
	private long y;
	
	public CyclicDTO(long x,long y){
		this.x = x;
		this.y = y;
	}
	public CyclicDTO(){}
	public long getX() {
		return x;
	}
	public void setX(long x) {
		this.x = x;
	}
	public long getY() {
		return y;
	}
	public void setY(long y) {
		this.y = y;
	}
	
	
	
}
