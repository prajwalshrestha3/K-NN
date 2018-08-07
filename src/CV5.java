import java.util.*;
public class CV5 {
	public static ArrayList<String[]> training = new ArrayList<String[]>();	//arraylist for training set
	public static ArrayList<String[]> test = new ArrayList<String[]>();	//arraylist for test set
	public static ArrayList<String[]> validationSet = new ArrayList<String []>();	//arraylist for validation set

	public static void crossValidation(ArrayList<String[]> dataStore){
		TreeMap<Double,Double> index = new TreeMap<Double,Double>();
		for(int fold = 1; fold < 6; fold++){		//this loop runs 5 times as it is a 5 cross validation
			System.out.println("Fold : " + fold + "........");
			for(int i = 0;i < dataStore.size(); i++){	//creating training and test datasets
				if(Integer.parseInt(dataStore.get(i)[15]) == fold){		//seperating between test data and the training data
					test.add(dataStore.get(i));
				}else{
					training.add(dataStore.get(i));
				}
			}

			for(int i = 0; i < test.size(); i++){	//calculate the distance for each test data set with all training data set 
				int zeroCounter=0;
				int oneCounter=0;
				for(int j = 0;j < training.size(); j++){
					index.put(euclidean(training.get(j),test.get(i)),Double.valueOf(j));
				}
				setLabel(index,oneCounter,zeroCounter,i,1);
			}
			validationSet.addAll(test);
			training.clear();
			test.clear();
		}	
	}

	public static void testing(ArrayList<String[]> dataStore, ArrayList<String[]> testStore){
		TreeMap<Double,Double> index = new TreeMap<Double,Double>();
		training = dataStore;
		test = testStore;
		for(int i = 0; i < test.size(); i++){	//calculate the distance for each test data set with all training data set 
			int zeroCounter=0;
			int oneCounter=0;
			for(int j = 0;j < training.size(); j++){
				index.put(euclidean(training.get(j),test.get(i)),Double.valueOf(j));
			}
			setLabel(index,oneCounter,zeroCounter,i,1);
		}
	}

	public static double euclidean(String [] train, String [] test){	//calculating euclidean distance for the values given
		double distance=0;
		double trainVal;
		double testVal;

		for(int col = 0; col < 15; col++){	//until 14 as we dont want to include the fold column
			try{
				trainVal=Double.valueOf(train[col]);
				testVal=Double.valueOf(test[col]);
				distance += Math.pow(trainVal - testVal, 2);
			}catch(NumberFormatException e){
				//skips the column
			}
		}
		return Math.sqrt(distance);
	}



	public static void setLabel(TreeMap<Double,Double> index,int oneCounter,int zeroCounter,int i,int neighbours) {	 //function that sets the label according to the cross validation
		Double toKey = null;
		int n = 0;
		for (Double key : index.keySet()) {
			if (n++ == neighbours) {
				toKey = key;
				break;
			}
		}
		SortedMap<Double, Double> copyMap = index.headMap(toKey);
		for(int l = 0;l < copyMap.size();l++) {	//loop to find if the label is <=50k or >50k and change it into 0 or 1(this loop runs for k(number of neighbour) times)
			double ind = (double) (copyMap.values().toArray()[l]);		//index of the value with smallest key
			String label;

			label = training.get((int) ind)[14];	//stores the label(>50K or <=50K for the index with the least euclidean distance)
			if(label.equals(">50K")) {	//if statement to turn the label into 0 if the income <=50K or 1 if it is >50K
				label = "1";
				oneCounter++;
			}else {
				label = "0";
				zeroCounter++;
			}
		}
		//System.out.println("Before: " + test.get(i)[14]);
		if(zeroCounter > oneCounter) {	//setting the value of test label according to majority between 0's and 1's
			test.get(i)[14] = "<=50K";
		}else {
			test.get(i)[14] = ">50K";
		}

		index.clear();
	}
}