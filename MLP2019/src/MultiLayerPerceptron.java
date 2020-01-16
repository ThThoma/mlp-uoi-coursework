import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class MultiLayerPerceptron {

	
	public static final int D = 2; 
	public static final int K = 3;
	public static final int H1 = 8;
	public static final int H2 = 5;
	public static final double EDU_RATE = 0.0001;
	public static final String typeOfFunctionInH2 = "tanh";
	public static final int L = 1;
	private  Cdata[] educationSet;
	private  Cdata[] testSet;
	private NeuronHiddenLayer1[] neuronsH1;
	private NeuronHiddenLayer2[] neuronsH2;
	private NeuronOutput[] neuronsOut;
	
	// sum of errors of each education example in one epoch
	private double squaredErrorOfEpoch; 
	private double lastEpochError;
	
	
	//for testing the network
	private int correctExamples = 0;
	private int wrongExamples = 0;
	
	private ArrayList<Cdata> correctList = new ArrayList<Cdata>(); 
	private ArrayList<Cdata> wrongList = new ArrayList<Cdata>();
	
	private int numOfExamplesC1 = 0;
	private int numOfExamplesC2 = 0;
	private int numOfExamplesC3 = 0;
	private int numOfExamplesNOCATEGORY =0;
	
	// D = number of inputs (in H1)
	// K = num of category
	// H1 = num of neurons in H1
	// H2 = num of neurons in H2
	// String typeOfFunction in H2
	// EDU_RATE = education rate//printErrorOfEpoch();

	// L = size of batch
	
	public MultiLayerPerceptron() {
		educationSet = new Cdata[3000];
		testSet = new Cdata[3000];
		
		this.squaredErrorOfEpoch = 0;
		this.lastEpochError = 0;
		setUpNetwork();
	}
	
	public void setUpNetwork() {
		
		System.out.println("---Initializing MLP Network---");
		initHiddenLayer1(H1,D);
		
		initHiddenLayer2(H2,H1);
		
		initOutputLayer(K,H2);
	}
	
	private void initHiddenLayer1(int numOfH1Neurons, int numOfInputs) {
		
		neuronsH1 = new NeuronHiddenLayer1[numOfH1Neurons];
		
		for (int i = 0 ; i < numOfH1Neurons ; i++) {
			neuronsH1[i] = new NeuronHiddenLayer1(numOfInputs);
			
		}
		System.out.println("Hidden Layer H1: " + numOfH1Neurons + " neurons, " + numOfInputs + " inputs.");
		
	}
	
	private void initHiddenLayer2(int numOfH2Neurons, int numOfInputs) {
		
		neuronsH2 = new NeuronHiddenLayer2[numOfH2Neurons];
		
		for (int i = 0 ; i< numOfH2Neurons ; i++) {
			neuronsH2[i] = new NeuronHiddenLayer2(numOfInputs,typeOfFunctionInH2);
		}
		System.out.println("Hidden Layer H2: " + numOfH2Neurons + " neurons, " + numOfInputs + " inputs.");
	
	}
	
	private void initOutputLayer(int numOfNeurons, int numOfInputs) {
		
		neuronsOut = new NeuronOutput[numOfNeurons];
		
		for (int i = 0 ; i < numOfNeurons ; i++) {
			neuronsOut[i] = new NeuronOutput(numOfInputs);
		}
		System.out.println("Final Layer: " + numOfNeurons + " neurons, " + numOfInputs + " inputs.");
	
	}
	
	public void loadCdataFromFile(String filename) {
		Scanner inputReader = null;
		try
		{
			inputReader = new Scanner(new FileInputStream(filename));
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File "+ filename + " was not found");
			System.out.println("or could not be opened.");
			System.exit(0);
		}
		
		int i = 0;
		System.out.println(i);
		while (inputReader.hasNextLine( )) {
			String line = inputReader.nextLine( );
			if(line.equals("TEST")) {
				System.out.println(i);
				break;
			}
			String[] lineData = line.split(",");	
			educationSet[i] = new Cdata(Double.parseDouble(lineData[0]), Double.parseDouble(lineData[1]), lineData[2]);
			i++;
		}
		i = 0;
		while (inputReader.hasNextLine( )) {
			String line = inputReader.nextLine( );
			String[] lineData = line.split(",");
			Cdata data = new Cdata(Double.parseDouble(lineData[0]), Double.parseDouble(lineData[1]), lineData[2]);
			testSet[i] = data;
			i++;
		}
		
		inputReader.close();
	}
	
	/*
	 * returns vector y dimension K, expects vector input x, dimension d
	 */
	public double[] forwardPass(double[] input) {
		
		//System.out.println("---Forward-Pass---");
		
		double[] outputH1 = new double[H1];
		double[] outputH2 = new double[H2];
		double[] networkOutput = new double[K];
		
		//System.out.println("Out of H1: ");
		for (int i = 0 ; i < H1; i++) {
			neuronsH1[i].setInput(input);
			outputH1[i] = neuronsH1[i].getOutput();
			//System.out.print(outputH1[i] + " ");
		}
		//System.out.println();
		
		//System.out.println("Out of H2: ");
		for (int i = 0 ; i < H2; i++) {
			neuronsH2[i].setInput(outputH1);
			outputH2[i] = neuronsH2[i].getOutput();
			//System.out.print(outputH2[i] + " ");
		}
		//System.out.println();
		
		//System.out.println("Network out: ");
		for (int i = 0 ; i < K; i++) {
			neuronsOut[i].setInput(outputH2);
			networkOutput[i] = neuronsOut[i].getOutput();
			//System.out.print(networkOutput[i] + " ");
		}
		//System.out.println();
		
		return networkOutput;
	}
	public double[] forwardPassWithPrints(double[] input) {
		
		System.out.println("---Forward-Pass---");
		
		double[] outputH1 = new double[H1];
		double[] outputH2 = new double[H2];
		double[] networkOutput = new double[K];
		
		System.out.println("Out of H1: ");
		for (int i = 0 ; i < H1; i++) {
			neuronsH1[i].setInput(input);
			outputH1[i] = neuronsH1[i].getOutput();
			System.out.print(outputH1[i] + " ");
		}
		System.out.println();
		
		System.out.println("Out of H2: ");
		for (int i = 0 ; i < H2; i++) {
			neuronsH2[i].setInput(outputH1);
			outputH2[i] = neuronsH2[i].getOutput();
			System.out.print(outputH2[i] + " ");
		}
		System.out.println();
		
		System.out.println("Network out: ");
		for (int i = 0 ; i < K; i++) {
			neuronsOut[i].setInput(outputH2);
			networkOutput[i] = neuronsOut[i].getOutput();
			System.out.print(networkOutput[i] + " ");
		}
		System.out.println();
		
		return networkOutput;
	}
	
	
	public void backPropagation(Cdata example) {
		
		double[] input  = example.toVectorNoBias();
		double[] networkOut = forwardPass(input);
		double[] difference = calculateDiffWithExpected(networkOut, example.getC());
		//double diff = calculateDiffWithExpected(networkOut, example.getC());
		this.squaredErrorOfEpoch += calculateSquaredErrorOfExample(networkOut, example.getC());
		
		
		//System.out.println("/* Calculate error d at output layer */");
		for(int i = 0; i < K; i++){
			
			neuronsOut[i].setError( difference[i] * neuronsOut[i].derivative() );
			//neuronsOut[i].setError(diff * neuronsOut[i].derivative());
	
			neuronsOut[i].calcDerivativeOfWeights();
			//System.out.print(neuronsOut[i].getderivativeOfWeights());
		}
		
		
		//System.out.print(" /* Calculate error d at hidden layer 2 */");
		for (int i = 0; i < H2; i++){
			//System.out.print(i+ "| ");
			double dotProdWeightsError = 0;
			for (int j = 0; j < K; j++) {
				//System.out.print(j+ ": ");
				double weightji = neuronsOut[j].getWeight(i);
				double errorj = neuronsOut[j].getError();
				dotProdWeightsError += weightji * errorj;
			}
			neuronsH2[i].setError( dotProdWeightsError * neuronsH2[i].derivative() ); 
			// update derivatives of weights
			neuronsH2[i].calcDerivativeOfWeights();
			//System.out.println();
		}
		/* Calculate error d at hidden layer 1 */
		for (int i = 0; i < H1; i++){
			double dotProdWeightsError = 0;
			for (int j = 0; j < H2; j++) {
				double weightji = neuronsH2[j].getWeight(i);
				double errorj = neuronsH2[j].getError();
				dotProdWeightsError += weightji * errorj;
			}
			neuronsH1[i].setError( dotProdWeightsError * neuronsH1[i].derivative() );
			// update derivatives of weights
			neuronsH1[i].calcDerivativeOfWeights();
		}
		
		
	}
	

	
	
	/*
	 * calculate the differance from the expected output - category - 
	 * and the output of forward pass from the network
	 * returns vector - dimension K
	 */
	public double[] calculateDiffWithExpected(double[] networkOutput, String exampleCategory) {
		
	//public double calculateDiffWithExpected(double[] networkOutput, String exampleCategory) {
		double[] expectedOut = new double[K];
		switch(exampleCategory) { 
        case "C1": 
        	expectedOut = new double[] {1,0,0};
            break; 
        case "C2": 
        	expectedOut = new double[] {0,1,0};
            break; 
        case "C3": 
        	expectedOut = new double[] {0,0,1};
            break; 
		}
		double[] diff = new double[K];
		
		//double diff = 0;
		for(int i=0; i < K; i++) {
			diff[i] =  networkOutput[i] - expectedOut[i];
			//diff =  Math.pow(Math.abs(networkOutput[i] - expectedOut[i]),2);
		}
		//diff = Math.sqrt(diff);
		//System.out.println();

		
		return diff;
	}
	/*
	 * Calculate the error from ONLY one example, call it after forward pass, 
	 * so that we dont pass the network one extra time. 
	 */
	public double calculateSquaredErrorOfExample(double[] networkOutput, String exampleCategory) {
		
		double[] expectedOut = new double[K];
		switch(exampleCategory) { 
        case "C1": 
        	expectedOut = new double[] {1,0,0};
            break; 
        case "C2": 
        	expectedOut = new double[] {0,1,0};
            break; 
        case "C3": 
        	expectedOut = new double[] {0,0,1};
            break; 
		}
		double sum =0;
		for(int i=0; i < K; i++) {
			sum +=  Math.pow(Math.abs((networkOutput[i] - expectedOut[i])), 2);
		}
		double error = 0.5 * sum;
		
	//	System.out.println("--Error from " + exampleCategory + " :" + error);
		return error;
		
	}
	
	
	
	

	private void clearErrorOfEpoch() {
		this.squaredErrorOfEpoch = 0;
		
	}
	
	public void printErrorOfEpoch() {
		
		System.out.println("Squared Error: " + this.squaredErrorOfEpoch);
	}
	
	public double calcErrorBetween2Epochs() {
		
		double epochDiff = this.lastEpochError - this.squaredErrorOfEpoch;
		this.lastEpochError = this.squaredErrorOfEpoch;
		//System.out.println("Difference of Squared Error: " + epochDiff);
		return epochDiff;
	}
		
	
	
	public void updateAllWeights() {
		for(int i = 0; i < H1; i++) {
			neuronsH1[i].updateWeights(EDU_RATE);
		}
		for(int i = 0; i < H2; i++) {
			neuronsH2[i].updateWeights(EDU_RATE);
		}
		for(int i = 0; i < K; i++) {
			neuronsOut[i].updateWeights(EDU_RATE);
		}
	}
	
	public void clearAllDerivatives() {
		for(int i = 0; i < H1; i++) {
			neuronsH1[i].clearVectorOfDerivatives();
		}
		for(int i = 0; i < H2; i++) {
			neuronsH2[i].clearVectorOfDerivatives();
		}
		for(int i = 0; i < K; i++) {
			neuronsOut[i].clearVectorOfDerivatives();
		}
	}
	
	
	public void printWeights() {
		String str = "";
		for(int i = 0; i < H1; i++) {
			str += neuronsH1[i].toString();
		}
		for(int i = 0; i < H2; i++) {
			str += neuronsH2[i].toString();
		}
		for(int i = 0; i < K; i++) {
			str += neuronsOut[i].toString();
		}
		System.out.println(str);
	}
	

	
	public void educate() {
		
		int epoch = 0;
		
		do {
			System.out.println("---Epoch: " + epoch );
			clearAllDerivatives();
			
			
			clearErrorOfEpoch();
			
			int counterOfBatches = 0;
			for(int i=0; i < educationSet.length; i+=L) {
				counterOfBatches ++;
				for (int j = i; j < counterOfBatches*L; j++) {
					backPropagation(educationSet[j]);
				}
				updateAllWeights();
			}
			
			printErrorOfEpoch();
			epoch ++;
			
			
		}while((epoch < 500 || Math.abs(calcErrorBetween2Epochs()) > 0.01) && epoch < 10000 );

		
	}
	
	
	
	public void testNetwork() {
		
		for (int i=0; i< testSet.length ; i++) {
			double[] input = testSet[i].toVectorNoBias();
			double[] netOut = forwardPass(input);
			compareOutandCorrect(netOut,testSet[i]);
		}

		Plot p = new Plot(correctList,wrongList);
		p.setVisible(true);
		printTestStatistics();
		
	}
	
	private void compareOutandCorrect(double[] netOut, Cdata example) {
		
		/* Find maximum of the networks output */
		double max = -100000000.000; //this is the smallest value
		int maxIndex = 0;
		for(int i=0; i < K; i++) {
			if(netOut[i] > max) {
				max = netOut[i];
				maxIndex = i;
				//System.out.println("Found Max!"+ maxIndex);
			}
		}
		String category = example.getC();
		/* Add it to category & Compare if its actually correct */
		switch(maxIndex) {
		
		case 0: // {1,0,0} C1
			
			this.numOfExamplesC1 ++;
			
			if(category.equals("C1")) {
				this.correctExamples ++;
				this.correctList.add(example);
			}else {
				this.wrongExamples ++;
				this.wrongList.add(example);
			}
			break;
			
		case 1: // {0,1,0} C2
			
			this.numOfExamplesC2 ++;
			
			if(category.equals("C2")) {
				this.correctExamples ++;
				this.correctList.add(example);
			}else {
				this.wrongExamples ++;
				this.wrongList.add(example);
			}
			break;
			
		case 2: // {0,0,1} C3
			
			this.numOfExamplesC3 ++;
			
			if(category.equals("C3")) {
				this.correctExamples ++;
				this.correctList.add(example);
			}else {
				this.wrongExamples ++;
				this.wrongList.add(example);
			}
			break;
		}
		
	}
	
	private void printTestStatistics() {
		
		System.out.println("---After Test Set---");
		System.out.println("Test Set size: "+ testSet.length);
		System.out.println("Correctly Categorized: " + this.correctExamples);
		System.out.println("Wrongly Categorized: " + this.wrongExamples);
		
		double percentage = (this.correctExamples / (double)testSet.length) * 100;
		System.out.println("Percentage: " + percentage + "% correct" );
		
		System.out.println("--------------------");
		System.out.println("In C1: " + this.numOfExamplesC1);
		System.out.println("In C2: " + this.numOfExamplesC2);
		System.out.println("In C3: " + this.numOfExamplesC3);
		System.out.println("In NOCategory: " + this.numOfExamplesNOCATEGORY);
		System.out.println("---Actual---");
		
		printActualCategories();
	}
	
	private void printActualCategories() {
		
		int numOfActualC1 = 0;
		int numOfActualC2 = 0;
		int numOfActualC3 = 0;
		
		for (int i=0; i< testSet.length ; i++) {
			switch(testSet[i].getC()) { 
	        case "C1": 
	        	numOfActualC1 ++;
	            break; 
	        case "C2": 
	        	numOfActualC2 ++;
	            break; 
	        case "C3": 
	        	numOfActualC3 ++;
	            break; 
			}
		}
		
		System.out.println("In C1: " + numOfActualC1);
		System.out.println("In C2: " + numOfActualC2);
		System.out.println("In C3: " + numOfActualC3);
		
	}
	



	public static void main(String[] args) {
		
		DataGenerator generator = new DataGenerator();
		generator.writeToFile();
		
		MultiLayerPerceptron network = new MultiLayerPerceptron();
		
		network.loadCdataFromFile("Cdata.txt");


		
		network.educate();
		//network.printWeights();
		
		network.testNetwork();

		
		//network.printExamplesAndForward();
		
	}

}
