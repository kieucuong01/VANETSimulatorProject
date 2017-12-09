package vanetsim;

import java.util.ArrayList;
import java.util.List;

import vanetsim.ecc.Point;
import vanetsim.hash.HashChain;

public class Scenario1 {
	
	
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
	SystemInitial si = new SystemInitial();
	HashChain h = new HashChain();
	
	public List<Point> computePseu(){
		Point Qi = new Point(h.H1(4, 10).getX(), h.H1(4, 10).getY(), 1);
		List<Point> listPseu = new ArrayList<>();
		for(int i =0;i<3;i++){
			Point kq = Qi.mult(si.generationU().get(i), a, b, p);
			Point p = new Point(kq.getX(),kq.getY());
			listPseu.add(p);
			System.out.println("Pseudonym "+ i +": "+kq);
		}
		
		return listPseu; 
	}
}
