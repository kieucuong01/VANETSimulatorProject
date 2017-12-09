package vehicle.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import vehicle.ecc.EllipticCurve;
import vehicle.ecc.Point;
import vehicle.hash.HashChain;
import vehicle.prime.GenerateZ;
import vehicle.prime.GeneratorPrime;

public class CallVehicle {
	private static long a = 7;
	private static long b = 12;
	private static long p = 103; /* initial curve: E(F_103) : y^2 = x^3 + 7x + 12 */
	private static long Gx = 13; /* initial generator: G = (102, 2) */
	private static long Gy = 2;
	private static long Px = 9; /* initially P = (9, 17) */
	private static long Py = 17;
	private static long Gx2 = 13;
	private static long Gy2 = 5;
	private static long Qx = 19; /* initially Q = (19, 0) */
	private static long Qy = 0;
	private static long k = 7; /* initially k = 33 */
	private static int n = 100; /* initially n = 100 random curves */

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		
		EllipticCurve el = new EllipticCurve(a, b, p);
		System.out.println("Generator G1: ");
		System.out.println(el.listMust(new Point(Gx, Gy, 1)));
		
		System.out.println("Generator G2: ");
		System.out.println(el.listMust(new Point(Gx2,Gy2,1)));
		
		System.out.println("Generator P");
		System.out.println(el.listPoints());
		
		System.out.println("Generator secret key");
		GeneratorPrime ge= new GeneratorPrime(103);
		System.out.println(ge.randomNumber());
		
		//Calculate kP
		
		Point P = new Point(Px,Py,1);
		Point result = P.mult(ge.randomNumber(), a, b, p);
		System.out.println("W: "+result);
		
		System.out.println("----------------------------Vehicle---------------------------");
		GenerateZ gZ = new GenerateZ();
		List<Integer> a1 = gZ.generateZp(41);
		System.out.println(a1);
		HashChain h = new HashChain();
		System.out.println(a1.get(8));
		
		double s = (double)1/(55+9);
		double Px2 = (double)Px;
		double Py2 = (double)Py;
		double Sxij = Px2*s;
		double Syij = Py2*s;
		System.out.println("Sij("+Sxij+","+Syij+")");
		
		System.out.println(h.H3(4, 3).getX());
		System.out.println(h.H3(4, 3).getY());
		Point P1 = new Point(h.H3(4, 3).getX(),h.H3(4, 3).getY(),1);
		System.out.println("Pseudonym: "+P.mult(5, a, b, p));
		int alpha = (int)ge.randomNumber();
		int r = (int)ge.randomNumber();
		int r1 = (int)ge.randomNumber();
		System.out.println(alpha +", "+r+", "+r1);
		//generratr c
		h.H2("Doan duowng nay bi tac(2;3)(23)dadadaadadadadadada", 45);
		
		long Xc = el.listMust1(new Point(Gx2,Gy2,1)).get(6).getX();
		long Yc = el.listMust1(new Point(Gx2,Gy2,1)).get(6).getY();
		
		int c = (int)(Xc*Xc+Yc*Yc);
		System.out.println(c);
		
		int z1 = c*alpha + r1;
		
		System.out.println(z1);
		
		int z2 = c*9 + r;
		String M = "aaaaaaaaaa";
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try{
			String content = "Nội dung mình muốn viết vào file\n";
			 
			fw = new FileWriter("data.txt");
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.write("dddddddddddddddddddzdddddddd\n");
			bw.write(String.valueOf(z1));
			bw.write("\n");
			bw.write(String.valueOf(z2));
			bw.write("\n");
			bw.write("(32,2)\n");
			System.out.println("Xong");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		try{
			
            FileInputStream fis = new FileInputStream(new File("data.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line != null && !line.isEmpty()) {
                    System.out.println("line data: " + line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
		
		
	}

}
