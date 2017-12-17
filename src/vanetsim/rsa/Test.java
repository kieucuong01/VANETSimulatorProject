package vanetsim.rsa;

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

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Test t = new Test();
		//sinh cap khoa trong RSA
		Encrypt en = new Encrypt();
        KeyPair keyPair = en.buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        //RL Mo phong
        //Danh sach RL ban dau
        List<Point> listRL = new ArrayList<Point>();
        listRL.add(new Point(10,2));
        listRL.add(new Point(10,5));
        listRL.add(new Point(10,6));
       
        //danh sach RL moi,
        // TA them pseudonym vao RL
        List<Point> listLatest = new ArrayList<>();
        Point p0 = new Point(10,2);
        Point p1 = new Point(3,9);
        Point p2 = new Point(4,5);
        Point p3 = new Point(2,17);
        
        t.addRL(p0, listLatest);
        t.addRL(p1, listLatest);
        t.addRL(p2, listLatest);
        t.addRL(p3, listLatest);
        //update RL va thoi gian sau do ky vao RL
        
        DateAndTime dt = new DateAndTime();
		String RL = String.valueOf(listRL);
        
        //String RL = dt.getDate() + ","+ s ;
        
        System.out.println("--- Danh sach RL kem voi thoi gian------");
        System.out.println(RL);
        //hash RL co gan thoi gian.
        String hashMD5 = t.hashRL(RL);
        System.out.println(t.hashRL(RL));
        
        System.out.println("--- Hash thoi gian------");
        
        
        // Su dung private key de ma hoa RL
        
        byte[] secret = t.signature(privateKey, hashMD5);
        
        
        // gianh cho xe khi nhan duoc RL va chuoi hash da duoc ma hoa
        String result = t.veryfiRL(pubKey, secret , RL);
        
        System.out.println(result);
        
        
        System.out.println("-----Scenarios 3-------");
        System.out.println("Danh sach RL moi cu: "+listRL);
        System.out.println("Danh sach RL moi moi: "+listLatest);
        
        List<List<Point>> listCheck = new ArrayList<>();
        listCheck.add(listRL); // RL cu
        listCheck.add(listLatest); //RL moi
        listCheck.add(listLatest);
        listCheck.add(listLatest);
        System.out.println(t.communityRL(listCheck));
        
        
        //Kiem tra 1 bi danh trong RL
        Point pseu = new Point(2,5);
        if(t.compareRL(t.communityRL(listCheck), pseu))
        	System.out.println("Pseu co trong RL tu choi xac giao tiep");
        else
        	System.out.println("Pseu khong co trong RL co the giao tiep");
        
        
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
        System.out.println(new String(encrypted));  // <<encrypted message>>
		
		return encrypted;
	}
	
	public  String veryfiRL(PublicKey pubKey, byte[]encrypted, String RL) throws Exception{
		Encrypt en = new Encrypt();
		byte[] secret = en.decrypt(pubKey, encrypted);
		
		System.out.println("----------Sau khi xe thuc hien giai ma----------------- ");
        System.out.println(new String(secret));     // This is a secret message
        
        if(en.checkRL(en.hashRSA(RL), new String(secret) )){
        	return "RL is being update by TA";
        }
        else
        	return "Is not of TA";
	}
	 
	public  List<Point> communityRL(List<List<Point>> listCompare){
		List<Point> p = new ArrayList<>();
		p = listCompare.get(0);
		
		int b = 0;
		for (int i =0;i<listCompare.size();i++){
			int a = 0;
			for(int j = listCompare.size()-1; j> i; j--){
				if(listCompare.get(i) == listCompare.get(j)){
					a++;
				}
			}
			if(a>b){
				b = a;
				p = listCompare.get(i);
			}
				
		}
		System.out.println("Xac xuat nhat cua RL lay cong dong: "+  ((float)(b+1)/(float)listCompare.size()));
		return p;
	}
	//kiem tra pseudonym trong RL
	public Boolean compareRL(List<Point>list, Point p){
		int a = 0;
		for(int i = 0; i< list.size();i++){
			if(p.equals(list.get(i)))
				return true;
		}
		
		return false;
	}
	
	
	
}
