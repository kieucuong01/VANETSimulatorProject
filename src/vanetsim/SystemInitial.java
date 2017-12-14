package vanetsim;

import java.util.ArrayList;
import java.util.List;

import vanetsim.dto.CredentialDTO;
import vanetsim.ecc.EllipticCurve;
import vanetsim.ecc.Point;
import vanetsim.hash.HashChain;
import vanetsim.prime.GenerateZ;
import vanetsim.prime.GeneratorPrime;

public class SystemInitial {
	
	private static long a = 7;
	private static long b = 12;
	private static long p = 103; /* initial curve: E(F_103) : y^2 = x^3 + 7x + 12 */
	private static long Gx = 102; /* initial generator: G = (102, 2) */
	private static long Gy = 2;
	private static long Px = 9; /* initially P = (9, 17) */
	private static long Py = 17;
	private static long Gx2 = 13;  /*initial G2 = (13,5)*/
	private static long Gy2 = 5;
	private static long Qx = 19; /* initially Q = (19, 0) */
	private static long Qy = 0;
	private static long k = 7; /* initially k = 33 */
	private static int n = 100; /* initially n = 100 random curves */
	EllipticCurve el = new EllipticCurve(a, b, p);
	GeneratorPrime ge= new GeneratorPrime(103);
	Point P = new Point(Px,Py,1);
	public long s = ge.randomNumber();
	HashChain h = new HashChain();
	GenerateZ gZ = new GenerateZ();
	List<Integer> a1 = gZ.generateZp(103); // Zp = {1->p-1}
	int uxj = (int)ge.randomNumber();
	
	public String generationG1(){
		String G1 = el.listMust(new Point(Gx, Gy, 1));
		return G1;
	}
	
	public String generationG2(){
		
		String G2 = el.listMust(new Point(Gx2,Gy2,1));
		
		return G2;
	}
	
	public String generationP(){
		String P = el.listPoints();
		return P;
	}
	
	public String generationPrime(){
		
		String secret = String.valueOf(s);
		
		return secret;
	}
	
	public String generationW(){
		
		Point result = P.mult(s, a, b, p);
		
		return String.valueOf(result);
	}
	public String generationWi(){
		// Wi
		Point Qi = new Point(h.H1(4, 10).getX(), h.H1(4, 10).getY(), 1);
		Point result1 = Qi.mult(ge.randomNumber(), a, b, p);

		return String.valueOf(result1);
	}
	public List<Integer> generationU(){ //U <==> gia tri nuy cua moi xe
		List<Integer> listU = new ArrayList<Integer>();
		
		listU.add(a1.get(uxj));
		listU.add(a1.get(uxj+1));
		listU.add(a1.get(uxj+2));
		
		
		return listU;
	}
	public List<CredentialDTO> generationVj(){
		SystemInitial si = new SystemInitial();
		double Px2 = (double)Px;
		double Py2 = (double)Py;
		
		System.out.println("List U: "+si.generationU());
		List<CredentialDTO> listS = new ArrayList<>();
		for(int i =0;i<3;i++){
			double k = (double)1/(s+si.generationU().get(i));
			double Sxij = Px2*k;
			double Syij = Py2*k;
			CredentialDTO credentialDTO = new CredentialDTO(Sxij,Syij);
			listS.add(credentialDTO);
			System.out.println("X: "+listS.get(i).getX() +" Y: "+listS.get(i).getY());
		}
		
		return listS;
	}
	
	public String generationForV(){
		SystemInitial si = new SystemInitial();
		String Cre = "Secret key: "+si.generationU() +" Credential: " ;
		for(int i =0;i<3;i++){
			Cre += "("+si.generationVj().get(i).getX()+","+si.generationVj().get(i).getY()+"),";
		}
		
		return Cre;
	}
	
	
}
