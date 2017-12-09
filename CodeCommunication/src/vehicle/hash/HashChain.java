package vehicle.hash;

import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;

import vehicle.dto.CyclicDTO;
import vehicle.ecc.EllipticCurve;
import vehicle.ecc.Point;

public class HashChain {
	EllipticCurve eclip = new EllipticCurve(7,12,103);
	public CyclicDTO H3(int secret, int prime){
		int s = secret% prime;
		CyclicDTO cyclicDTO = new CyclicDTO();
		cyclicDTO = eclip.listMust1(new Point(13, 2, 1)).get(s);
		return cyclicDTO;
	}
	
	public void H2(String str, int prime) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		String arr[] = {"a","b","c","d","e","f","g","i","j","k","l",
				"m"," ","n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z","0","1","2","3","4","5","6","7","8","9","(",";",")"};
		
		MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(str.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        
        System.out.println(hashtext);
		int a = 0;
		for(int i =0;i<hashtext.length();i++){
			for(int j=0;j<arr.length;j++){
				if(hashtext.substring(i,i+1).equals(arr[j]))
					a +=j;
			}
		}
		System.out.println(a);
		System.out.println(a%prime);
	}
}
