package vanetsim.ecc;

import java.util.ArrayList;
import java.util.List;

import vanetsim.dto.CyclicDTO;


public class EllipticCurve {
	private long a;
	private long b;
	private long p;
	
	public EllipticCurve(long a, long b,long p){
		this.a = a;
		this.b = b;
		this.p = p;
		
	}
	
	public EllipticCurve(EllipticCurve E) {
        this.a = E.getA();
        this.b = E.getB();
        this.p = E.getP();
    }
    
    public long getA() {
    	return a;
    }
    
    public long getB() {
    	return b;
    }
    
    public long getP() {
    	return p;
    }
    
    public void setA(long a) {
    	this.a = a;
    }
    
    public void setB(long b) {
    	this.b = b;
    }
    
    public void setP(long p) {
    	this.p = p;
    }
	
	public String toString(){
		String t = "E(F_" + p + ") : "; 
		String s = "y^2 = x^3 + ";
		if(a==0){
		}
		else if(a==1){
			s+="x + ";
		}
		else{
			s += a + "x + ";
		}
		s+=b;
		
		if(b == 0) {
			if(a == 0) {
				s = s.substring(0, s.indexOf('3') + 1);
			}
			else {
				s = s.substring(0, s.indexOf('x', 8) + 1);
			}
		}
		return t+s;
	}
	List<CyclicDTO> listPoint;
	
	public String listMust(Point G){
		listPoint = new ArrayList<>();
		String s = "";
		long N = pointOrder(G);
		
		Point temp = new Point(G);
		for(int i = 1; i < N; i++) {
			
			s += temp + ", ";
			temp = temp.add(G, getA(), getB(), getP());
						
			
		}

		s += temp + "\n";
		return s;
	}
	public List<CyclicDTO> listMust1(Point G){
		listPoint = new ArrayList<>();
		String s = "";
		long N = pointOrder(G);
		
		Point temp = new Point(G);
		for(int i = 1; i < N; i++) {
			
			s += temp + ", ";
			temp = temp.add(G, getA(), getB(), getP());
			CyclicDTO cyclicDTO = new CyclicDTO();
			cyclicDTO.setX(temp.getX());
			cyclicDTO.setY(temp.getY());
			listPoint.add(cyclicDTO);
			
			
			
		}

		s += temp + "\n";
		return listPoint;
	}
	
	public long pointOrder(Point G) {
		List<Long> factors = ECMath.allFactors(order());
		for(long l : factors) {
			if(G.mult(l, getA(), getB(), getP()).equals(new Point())) {
				return l;
			}
		}
		return -1;
	}
	public long order() { /* O(plogp) algorithm */
		long order = getP() + 1;
		for(long x = 0; x < getP(); x++) {
			long jac = ECMath.jacobi(x*x*x + getA()*x + getB(), getP());
			order += jac;
		}
		return order;
	}
	
	public String listPoints() {
		String s = "";
		for(long x = 0; x < getP(); x++) {
			long temp = (x*x*x + getA()*x + getB()) % getP();
			if(ECMath.jacobi(temp, getP()) == 1) {
				Point P = new Point(x, ECMath.sqrt(temp, getP()), 1);
				if(P.getY() > getP() - P.getY()) {
					P.setY(getP() - P.getY());
				}
				Point Q = new Point(P.getX(), getP() - P.getY(), 1);
				s += P + ", " + Q + ", ";
			}
		}
		s += new Point() + "\n";
		return s;
	}
}
