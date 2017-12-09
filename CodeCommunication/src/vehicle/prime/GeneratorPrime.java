package vehicle.prime;

import java.util.Random;

public class GeneratorPrime {
	
	private int prime;
	public GeneratorPrime(int prime){
		this.prime = prime;
	}
	//random so nguyen to
	
	public long randomPrime(){
		long num;
		Random random = new Random();
		
		num = random.nextInt(prime)+1;
		if(checkPrime(num)==true){
			return num;
		}
		else
			return randomPrime();
		
	}
	public Boolean checkPrime(long num){
		for(int i=2;i<num;i++){
			if(num % i == 0)
				return false;
		}
		return true;
	}
	// random number trong khoang so nguyen to
	public long randomNumber(){
		long num;
		Random random = new Random();
		int a = (int)randomPrime();
		num = random.nextInt(a)+1;
		
		return num;
	}
	
	public int randomNumber(int number){
		int num;
		Random random = new Random();
		num = random.nextInt(number)+1;
		
		return num;
	}
	
}
