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
import java.util.ArrayList;
import java.util.List;

import vehicle.dto.CredentialDTO;
import vehicle.dto.CyclicDTO;
import vehicle.ecc.EllipticCurve;
import vehicle.ecc.Point;
import vehicle.hash.HashChain;
import vehicle.prime.GenerateZ;
import vehicle.prime.GeneratorPrime;

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
		System.out.println("Prime Number: "+p );
		EllipticCurve el = new EllipticCurve(a, b, p);
		System.out.println("Generator G1: ");
		System.out.println(el.listMust(new Point(Gx, Gy, 1)));
		
		System.out.println("Generator G2: ");
		System.out.println(el.listMust(new Point(Gx2,Gy2,1)));
		
		System.out.println("Generator P");
		System.out.println(el.listPoints());
		HashChain h = new HashChain();
		System.out.println("Generator secret key");
		GeneratorPrime ge= new GeneratorPrime(103);
		System.out.println(ge.randomNumber());
		
		//Calculate kP
		
		Point P = new Point(Px,Py,1);
		Point result = P.mult(ge.randomNumber(), a, b, p);
		System.out.println("W: "+result);
		
		// Wi
		Point Qi = new Point(h.H1(4, 10).getX(),h.H1(4, 10).getY(),1);
		Point result1 = Qi.mult(ge.randomNumber(), a, b, p);
		System.out.println("Wi: "+result1);
		
		
		System.out.println("----------------------------TA generation for Scenarios1---------------------------");
		GenerateZ gZ = new GenerateZ();
		List<Integer> a1 = gZ.generateZp(103);
		System.out.println(a1);
		int a = (int)ge.randomNumber();
		System.out.println("Secret key for Vehicle: "+a1.get(a));
		List<Integer> listU = new ArrayList<Integer>();
		
		listU.add(a1.get(a));
		listU.add(a1.get(a+1));
		listU.add(a1.get(a+2));
		
		double Px2 = (double)Px;
		double Py2 = (double)Py;
		
		System.out.println("List U: "+listU);
		List<CredentialDTO> listS = new ArrayList<>();
		for(int i =0;i<3;i++){
			double s = (double)1/(ge.randomNumber()+listU.get(i));
			double Sxij = Px2*s;
			double Syij = Py2*s;
			CredentialDTO credentialDTO = new CredentialDTO(Sxij,Syij);
			listS.add(credentialDTO);
			System.out.println("X: "+listS.get(i).getX() +" Y: "+listS.get(i).getY());
		}
		
		
		
		System.out.println(h.H1(4, 10).getX());
		System.out.println(h.H1(4, 10).getY());
		
		
		System.out.println("-------------------------------------------Scenarios1--------------------------------------------");
		List<Point> listPseu = new ArrayList<>();
		for(int i =0;i<3;i++){
			Point kq = Qi.mult(listU.get(i), a, b, p);
			Point p = new Point(kq.getX(),kq.getY());
			listPseu.add(p);
			System.out.println("Pseudonym "+ i +": "+kq);
		}
		
		
		System.out.println("-------------------------------------------Message Signing----------------------------------------");

		
		
		Point pseuxj= listPseu.get(ge.randomNumber(3)-1);
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
		
		
		
		
	}

}
