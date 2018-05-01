package vanetsim;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sun.corba.se.impl.util.PackagePrefixChecker;
import com.sun.org.apache.xpath.internal.operations.Bool;

import vanetsim.dto.CredentialDTO;
import vanetsim.ecc.EllipticCurve;
import vanetsim.ecc.Point;
import vanetsim.hash.HashChain;
import vanetsim.prime.GeneratorPrime;
import vanetsim.rsa.DateAndTime;
import vanetsim.rsa.Encrypt;

public class Scenario3 {
	// Danh sach RL cu
	public Point psudonymSender;
	public List<Point> listOldRL_ = new ArrayList<Point>();
	public List<Point> listLatestRL_ = new ArrayList<Point>();
    public DateAndTime dt = new DateAndTime();
    public PublicKey pubKey; 
    public PrivateKey privateKey;
    
    
	GeneratorPrime ge = new GeneratorPrime(103);
    int alpha = (int)ge.randomNumber();
	int r = (int)ge.randomNumber();
	int r1 = (int)ge.randomNumber();
	HashChain h = new HashChain();
	Point Qi = new Point(h.H1(4, 10).getX(), h.H1(4, 10).getY(), 1);
	private static long Px = 9; /* initially P = (9, 17) */
	private static long Py = 17;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scenario3 scenario3 = new Scenario3();
		// Set up public key and private key
		scenario3.initialFromTA();
		// Set up simulation of RLs
		scenario3.initialRL();
		// Set up sender vehicle
		scenario3.initialSenderVehicle("10,2");
		// Get lasted RL
		List<Point> lastedRL = scenario3.getLastedRL();
		byte[] encyptRL = scenario3.getEncyptRLNTimeString(lastedRL, scenario3.dt.getNewDate());
		String lastedRLString = String.valueOf(lastedRL);
		
		// CHECK IS RL IS BELONG TO SYSTEM
		Boolean isBelongToSystem = scenario3.isRLBelongToSystem(scenario3.pubKey, encyptRL, lastedRLString, scenario3.dt.getNewDate());
		if (isBelongToSystem) {
			Boolean isVehicleInRL = scenario3.isVehicleInRL(lastedRL, scenario3.psudonymSender);
			if (isVehicleInRL) {
				System.out.println("Tu choi giao tiep voi xe co psudonym " + scenario3.psudonymSender);
			}
			else {
				System.out.println("Chap nhan giao tiep voi xe co psudonym " + scenario3.psudonymSender);
			}
		}
		else {
			System.out.println("RL khong dc sinh ra boi TA");
		}
		
	}
	
	
	
	
	public String getPackage(String message, List<Integer>listU,Point p) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		Scenario1 sce1 = new Scenario1();
		String Packt = "("+ message + "||"+this.generaSignature(message, listU, p)+"||"+ p.toString() +")";
		
		return Packt;
	}
	double T;
	int c;
	int z1;
	int z2;

	public String generaSignature(String message,List<Integer>listU, Point p) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		SystemInitial si = new SystemInitial();
		List<CredentialDTO> listS = new ArrayList<>();
		Scenario1 sce1 = new Scenario1();
		listS  = si.generationVj(listU);

		long RGx = r*Qi.getX();
		long RGy = r*Qi.getY();
		Point RG = new Point(RGx,RGy);
		
		int rG = (int)((RG.getX() * RG.getX()) + (RG.getY()*RG.getY()));
		
		double S = ((listS.get(0).getX()*listS.get(0).getX())+(listS.get(0).getY()*listS.get(0).getY()));
		
		T = ((double)alpha)*S;
		
		c = h.H2(message +String.valueOf(T)+  String.valueOf(rG) + String.valueOf(generaR()) + p.toString() + "4");
		
		z1 = c*alpha + r1;
		
		System.out.println(z1);
		
		z2 = c* listU.get(0)  + r;
		System.out.println(z2);
		
		String sig = "("+ T + "," + c+","+ z1 +","+z2 +  ")";
		
		return sig;
	}
	public int generaR(){
		int p = (int)((Px*Px)+(Py*Py));
		int q = (int)((Qi.getX()*Qi.getX())+(Qi.getY()*Qi.getY()));
		
		int R = r1*p*q;
		return R;
	}
	
	public Boolean belongIsSystem(String message,Point Wi, Point pseu) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		int pseudonym = (int)((pseu.getX()*pseu.getX()) + (pseu.getY()*pseu.getY()) );
		int q = (int)((Qi.getX()*Qi.getX())+(Qi.getY()*Qi.getY()));
		int RG1 = z2* q - c*pseudonym;
		
		int c1 = h.H2(String.valueOf(T)+  String.valueOf(RG1) + String.valueOf(generaR1(Wi,pseu)) + pseu.toString()+ "4");
		
		if(c1 == c)
			return true;
		else
			return false;
	}
	
	public int generaR1(Point key,Point pseu){
		int p = (int)((Px*Px)+(Py*Py));
		int q = (int)((Qi.getX()*Qi.getX())+(Qi.getY()*Qi.getY()));
		double up = (double)(z1*q*q);
		
		int pseudonym = (int)((pseu.getX()*pseu.getX()) + (pseu.getY()*pseu.getY()) );
		int wi = (int)((key.getX()*key.getX()) + (key.getY()*key.getY()) );
		
		double R1 = (up)/((double)(pseudonym+wi)*T*(double)c);
		return (int)R1;
	}
	
	//add pseudonym vao trong RL 
	
	
	public List<Point> addRL(Point p,List<Point>listRecent){ 
		
		listRecent.add(p);
		return listRecent;
		
	}
	
	public  String hashRL(String time) throws NoSuchAlgorithmException{
		Encrypt en = new Encrypt();
        String signature = en.hashRSA(time);
        
        return signature;
	}

	public  byte[] signature(PrivateKey privateKey,String signature) throws Exception{
		Encrypt en = new Encrypt();
		byte [] encrypted = en. encrypt(privateKey, signature);     
		
		return encrypted;
	}
	
	public  Boolean isRLBelongToSystem(PublicKey pubKey, byte[]encrypted, String RL, String time) throws Exception{
		Encrypt en = new Encrypt();
		byte[] secret = en.decrypt(pubKey, encrypted);
        
        if(en.checkRL(en.hashRSA(RL+time), new String(secret) )){
        	return true;
        }
        else
        	return false;
	}
	
	//kiem tra pseudonym trong RL
	public Boolean isVehicleInRL(List<Point>list, Point p){
		int a = 0;
		for(int i = 0; i< list.size();i++){
			if(p.equals(list.get(i)))
				return true;
		}
		
		return false;
	}
	
	public void initialSenderVehicle(String point) {
		String[] parts = point.split(",");
		String part1 = parts[0]; // 004
		String part2 = parts[1]; // 034556
		this.psudonymSender = new Point(Long.parseLong(part1), Long.parseLong(part2));
	}
	public void initialRL() {
        //RL Mo phong
        //Danh sach RL ban dau
		listOldRL_.add(new Point(10,2));
		listOldRL_.add(new Point(10,5));
		listOldRL_.add(new Point(10,6));
       
        //danh sach RL moi,
        // TA them pseudonym vao RL
		listLatestRL_.add(new Point(10,2));
		listLatestRL_.add(new Point(10,5));
		listLatestRL_.add(new Point(10,6));
		listLatestRL_.add(new Point(3,9));
	}
	
	public void initialFromTA() throws NoSuchAlgorithmException {
		//sinh cap khoa trong RSA
		Encrypt en = new Encrypt();
        KeyPair keyPair = en.buildKeyPair();
        this.pubKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
	}
	
	public List<Point> getLastedRL(){
		return listLatestRL_;
	} 
	
	public byte[] getEncyptRLNTimeString(List<Point> listRL, String time) throws Exception {
		String RLString = String.valueOf(listLatestRL_);
        String hashMD5RLnTime = this.hashRL(RLString+time);

        byte[] secretRLnTime = this.signature(privateKey, hashMD5RLnTime);

        return secretRLnTime;
	}	
}