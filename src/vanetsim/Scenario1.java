package vanetsim;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import vanetsim.ecc.EllipticCurve;
import vanetsim.ecc.Point;
import vanetsim.hash.HashChain;
import vanetsim.prime.GeneratorPrime;

public class Scenario1 {

	private static long a = 7;
	private static long b = 12;
	private static long p = 103; /* initial curve: E(F_103) : y^2 = x^3 + 7x + 12 */
	private static long Gx = 102; /* initial generator: G = (102, 2) */
	private static long Gy = 2;
	private static long Px = 9; /* initially P = (9, 17) */
	private static long Py = 17;
	private static long Gx2 = 13; /* initial G2 = (13,5) */
	private static long Gy2 = 5;
	private static long Qx = 19; /* initially Q = (19, 0) */
	private static long Qy = 0;
	private static long k = 7; /* initially k = 33 */
	private static int n = 100; /* initially n = 100 random curves */
	SystemInitial si = new SystemInitial();
	HashChain h = new HashChain();

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		SystemInitial systemInitial = new SystemInitial();
		Scenario1 scenario1 = new Scenario1();
		System.out.println("----------------------------Vehicle 1---------------------------------------------");
		List<Integer> V1 = new ArrayList<>();
		V1 = systemInitial.generationU();
		System.out.println(scenario1.getSetPsuedonymVehicle1());

		System.out.println("-----------------------------Vehicle 2 --------------------------------------------");
		List<Integer> V2 = new ArrayList<>();
		V2 = systemInitial.generationU();
		System.out.println(scenario1.getSetPsuedonymVehicle2());
		
		//Messgae signing.
		GeneratorPrime ge = new GeneratorPrime(103);
		EllipticCurve el = new EllipticCurve(a, b, p);
		long Xc = el.listMust1(new Point(Gx2,Gy2,1)).get(6).getX();
		long Yc = el.listMust1(new Point(Gx2,Gy2,1)).get(6).getY();
		HashChain h = new HashChain();
		int alpha = (int)ge.randomNumber();
		int r = (int)ge.randomNumber();
		int r1 = (int)ge.randomNumber();
		
		Point Qi = new Point(h.H1(4, 10).getX(), h.H1(4, 10).getY(), 1);
		System.out.println(alpha +", "+r+", "+r1);
		Point T = new Point(2,3);
		long RGx = r*Qi.getX();
		long RGy = r*Qi.getY();
		Point RG = new Point(RGx,RGy);
		System.out.println("x: "+RG);
		
		int c = (int)(Xc*Xc+Yc*Yc)%103;
		System.out.println("c: "+c);
		
		int z1 = c*alpha + r1;
		
		System.out.println(z1);
		
		int z2 = c*9 + r;
		System.out.println(z2);
		
		System.out.println("Qi: "+Qi);
	}

	public List<Point> computePseu(List<Integer> listV) {
		Point Qi = new Point(h.H1(4, 10).getX(), h.H1(4, 10).getY(), 1);
		List<Point> listPseu = new ArrayList<>();
		for (int i = 0; i < 3; i++) {

			Point kq = Qi.mult(listV.get(i), a, b, p);

			Point p = new Point(kq.getX(), kq.getY());
			listPseu.add(p);

		}

		return listPseu;
	}

	public String getSetPsuedonymVehicle1() {
		List<Integer> V2 = new ArrayList<>();
		Boolean isValid = false;
		
		while (isValid == false) {
			V2 = si.generationU();
			isValid = true;
			for (int i = 0; i < 3; i++) {
				Point Qi = new Point(h.H1(4, 10).getX(), h.H1(4, 10).getY(), 1);
				Point kq = Qi.mult(V2.get(i), a, b, p);
				if (kq.getX() == 0) {
					isValid = false;
					break;
				}
			}
		}
		
		return String.valueOf(this.computePseu(V2));
	}

	public String getSetPsuedonymVehicle2() {
		List<Integer> V2 = new ArrayList<>();
		Boolean isValid = false;
		
		while (isValid == false) {
			V2 = si.generationU();
			isValid = true;
			for (int i = 0; i < 3; i++) {
				Point Qi = new Point(h.H1(4, 10).getX(), h.H1(4, 10).getY(), 1);
				Point kq = Qi.mult(V2.get(i), a, b, p);
				if (kq.getX() == 0) {
					isValid = false;
					break;
				}
			}
		}
		
		return String.valueOf(this.computePseu(V2));
	}

}
