import java.util.Random;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.Math;

public class DataGenerator {
	public Cdata[] educationSet;
	public Cdata[] testSet;
	private Random randomgen;

	public DataGenerator() {
		
		educationSet = new Cdata[3000];
		testSet = new Cdata[3000];
		randomgen  = new Random();
		
		createEducationSet();
		createTestSet();
		
		addNoiseToEducationSet();
	}
	
	private void createEducationSet() {
		
		double r1, r2;
		int C1counter = 0;
		int C2counter = 0;
		int C3counter = 0;
		
		for(int i = 0; i < 3000; i++) {
			r1 = 4*randomgen.nextDouble() - 2;
			r2 = 4*randomgen.nextDouble() - 2;
			if(inC2(r1,r2)) {
				educationSet[i] = new Cdata(r1,r2,"C2");
				C2counter ++;
			}
			else if(inC3(r1,r2)) {
				educationSet[i] = new Cdata(r1,r2,"C3");
				C3counter ++;
			}
			else {
				educationSet[i] = new Cdata(r1,r2,"C1");
				C1counter ++;
			}
		}
		System.out.println("In C1:" + C1counter);
		System.out.println("In C2:" + C2counter);
		System.out.println("In C3:" + C3counter);
		
		
	}
	
	private void createTestSet() {
		
		double r1, r2;
		
		for(int i = 0; i < 3000; i++) {
			
			r1 = 4*randomgen.nextDouble() - 2;
			r2 = 4*randomgen.nextDouble() - 2;
			if(inC2(r1,r2)) {
				testSet[i] = new Cdata(r1,r2,"C2");
			}
			else if(inC3(r1,r2)) {
				testSet[i] = new Cdata(r1,r2,"C3");
			}
			else {
				testSet[i] = new Cdata(r1,r2,"C1");
			}
		}
	}
	
	private boolean inC2(double x1, double x2) {
		if(Math.pow((x1-1),2) + Math.pow((x2-1),2) <= 0.49) {
			return true;
		}
		if(Math.pow((x1+1),2) + Math.pow((x2+1),2) <= 0.49) {
			return true;
		}
		return false;
	}
	
	private boolean inC3(double x1, double x2) {
		if(Math.pow((x1+1),2) + Math.pow((x2-1),2) <= 0.49) {
			return true;
		}
		if(Math.pow((x1-1),2) + Math.pow((x2+1),2) <= 0.49) {
			return true;
		}
		return false;
	}
	
	
	private void addNoiseToEducationSet() {
		
		int noiseCounter = 0;
		for(int i = 0; i < 3000; i++) {
			
			if(isNoise(educationSet[i])) {
				educationSet[i].setC("C1");
				noiseCounter ++;
			}	
		}
		System.out.println("noise:" + noiseCounter);
		
	}
	
	private boolean isNoise(Cdata data) {
		
		double rand = randomgen.nextDouble();
		
		if(rand <= 0.1 && (data.getC().equals("C2") || data.getC().equals("C3") )) {
			return true;
		}
		return false;
	}
	
	public void printData() {
		System.out.println("Education Set");
		for(int i = 0; i < 10; i++) {
			System.out.println(educationSet[i]);
		}
		System.out.println("Test Set");
		for(int i = 0; i < 10; i++) {
			System.out.println(testSet[i]);
		}
	}
	
	public void writeToFile() {
		PrintWriter out = null;
		try
		{
		out = new PrintWriter(new FileOutputStream("Cdata.txt"));
		}
		catch(FileNotFoundException e)
		{
		System.out.println("Error opening the file Cdata.txt");
		System.exit(0);
		}
		for(int i = 0; i < 3000; i++) {
			out.println(educationSet[i]);
		}
		out.println("TEST");
		for(int i = 0; i < 3000; i++) {
			out.println(testSet[i]);
		}
		out.close( );
	}
}
