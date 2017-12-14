package vanetsim.rsa;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class Encrypt {
	 public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
	        final int keySize = 2048;
	        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	        keyPairGenerator.initialize(keySize);      
	        return keyPairGenerator.genKeyPair();
	    }

	    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
	        Cipher cipher = Cipher.getInstance("RSA");  
	        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  

	        return cipher.doFinal(message.getBytes());  
	    }
	    
	    public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
	        Cipher cipher = Cipher.getInstance("RSA");  
	        cipher.init(Cipher.DECRYPT_MODE, publicKey);
	        
	        return cipher.doFinal(encrypted);
	    }
	    public String hashRSA(String str) throws NoSuchAlgorithmException{
	    	 MessageDigest md = MessageDigest.getInstance("MD5");
	         byte[] messageDigest = md.digest(str.getBytes());
	         BigInteger number = new BigInteger(1, messageDigest);
	         String hashtext = number.toString(16);
	         while (hashtext.length() < 32) {
	             hashtext = "0" + hashtext;
	         }
	         System.out.println(hashtext);
	         
	         return hashtext;
	    }
	    
	    public boolean checkRL(String RLTA,String RLV){
	    	if(RLTA.equals(RLV))
	    		return true;
	    	else
	    		return false;
	    }
}
