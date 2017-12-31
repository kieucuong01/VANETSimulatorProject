package vanetsim;

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
import java.util.ArrayList;
import java.util.List;

import vanetsim.dto.CredentialDTO;
import vanetsim.dto.CyclicDTO;
import vanetsim.ecc.EllipticCurve;
import vanetsim.ecc.Point;
import vanetsim.hash.HashChain;
import vanetsim.prime.GenerateZ;
import vanetsim.prime.GeneratorPrime;

public class CallVehicle {
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

	
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		SystemInitial systemInitial = new SystemInitial();
		Scenario1 scenario1 = new Scenario1();
		System.out.println("Prime Number: "+p );
		System.out.println("Generator G1: ");
		System.out.println(systemInitial.generationG1());		
//		System.out.println("Generator G2: ");
//		System.out.println(systemInitial.generationG2());
//		
//		System.out.println("Generator P");
//		System.out.println(systemInitial.generationP());
//		
//		HashChain h = new HashChain();
//		System.out.println("Generator secret key");
//		System.out.println(systemInitial.s);
//		
//		//Calculate kP
//		
//		System.out.println("W: "+systemInitial.generationW());
//		
//		// Wi
//
//		System.out.println("Wi: "+systemInitial.generationWi());
//		
//		
//		System.out.println("----------------------------TA generation for Scenarios1---------------------------");
//
//		System.out.println("Secret key for Vehicle: "+systemInitial.generationForV());
		
		
		
		
		System.out.println("-------------------------------------------Scenarios1--------------------------------------------");
		//System.out.println("Secret key for Vehicle: "+systemInitial.generationForV());
				
		System.out.println("----------------------------Vehicle 1---------------------------------------------");
		List<Integer> V1 = new ArrayList<>();
		V1 = systemInitial.generationU();
		System.out.println("Pseudonum" + scenario1.computePseu(V1));
		
		HashChain h = new HashChain();
		System.out.println("-----------------------------Vehicle 2 --------------------------------------------");
		List<Integer> V2 = new ArrayList<>();
		V2 = systemInitial.generationU();
		System.out.println("Pseudonum" + scenario1.computePseu(V2));

		EllipticCurve el = new EllipticCurve(a, b, p);
		long Xc = el.listMust1(new Point(Gx2,Gy2,1)).get(6).getX();
		long Yc = el.listMust1(new Point(Gx2,Gy2,1)).get(6).getY();
		
//		System.out.println("-------------------------------------------Message Signing----------------------------------------");
//		System.out.println("Pseudonum" + scenario1.computePseu1());
		
		
		/*Point pseuxj= listPseu.get(ge.randomNumber(3)-1);
		System.out.println(pseuxj);
		int alpha = (int)ge.randomNumber();
		int r = (int)ge.randomNumber();
		int r1 = (int)ge.randomNumber();
		System.out.println(alpha +", "+r+", "+r1);
		Point T = new Point(2,3);
		long RGx = r*Qi.getX();
		long RGy = r*Qi.getY();
		Point RG = new Point(RGx,RGy);
		System.out.println("x: "+RG);
		
		//generrate c
		h.H2("Ly thuong kiet dang bi tac"+ T+""+RG+"RG1"+pseuxj+"14" , 103);
		
		long Xc = el.listMust1(new Point(Gx2,Gy2,1)).get(6).getX();
		long Yc = el.listMust1(new Point(Gx2,Gy2,1)).get(6).getY();
		
		int c = (int)(Xc*Xc+Yc*Yc);
		System.out.println("c: "+c);
		
		int z1 = c*alpha + r1;
		
		System.out.println(z1);
		
		int z2 = c*9 + r;
		
		System.out.println("-----------------------------Send Message--------------------------------------");
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
		*/
		
		
		
	}

}
