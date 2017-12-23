package vanetsim;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sun.org.apache.xpath.internal.operations.Bool;

import vanetsim.ecc.Point;
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