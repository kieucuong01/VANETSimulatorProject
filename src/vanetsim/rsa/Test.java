package vanetsim.rsa;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import vanetsim.ecc.Point;

public class Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//sinh thoi gian khi update RL
		DateAndTime dt = new DateAndTime();
		
		//sinh cap khoa trong RSA
		Encrypt en = new Encrypt();
        KeyPair keyPair = en.buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        //RL Mo phong
        List<Point> point = new ArrayList<Point>();
        point.add(new Point(10,2));
        point.add(new Point(10,5));
        point.add(new Point(10,6));
        String RL = String.valueOf(point);
        String time = RL+ dt.getDate();
        
        String s = "(" + RL + "," + dt.getDate()+")";
        System.out.println(s);

        String signature = en.hashRSA(time);
        
        // Ma hoa RL
        byte [] encrypted = en. encrypt(privateKey, signature);     
        System.out.println(new String(encrypted));  // <<encrypted message>>
        
        
        
        // Giai ma RL buoc nay chen vao 1 vehicle nao do
        byte[] secret = en.decrypt(pubKey, encrypted);                                 
        System.out.println(new String(secret));     // This is a secret message
        if(en.checkRL(en.hashRSA(time), new String(secret) )){
        	System.out.println("Success");
        }
        else
        	System.out.println("Fail");
       
	}

}
