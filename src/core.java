import java.util.*;

public class core {
	public static void main(String [] args){
		int incorrectLessEqual50 = 0;
		int incorrectGreat50 = 0;
		int correctLessEqual50 = 0;
		int correctGreat50 = 0;
		ArrayList<String[]> dataStore = dataRead.readFile("/Users/prajwalshrestha/Desktop/adult.train.5fold.csv");
		dataRead.computeMode(dataStore);
		dataStore = dataRead.replaceMissingVal(dataStore);
		String [] actualData = new String[dataStore.size()];
		String [] predData = new String[dataStore.size()];

		ArrayList<String[]> testStore= dataRead.readFile("/Users/prajwalshrestha/Downloads/adult.test.csv");
		dataRead.computeMode(testStore);
		testStore = dataRead.replaceMissingVal(testStore);

		dataRead.normalise(dataStore);
		for(int i=0;i<dataStore.size();i++) {
			actualData[i] = dataStore.get(i)[14];
		}

		CV5.crossValidation(dataStore);

		for(int i = 0; i < dataStore.size(); i++) {
			predData[i] = dataStore.get(i)[14];
		}		

		for(int i=0;i<dataStore.size();i++) {
			if (actualData[i].equals(predData[i]) && actualData[i].equals("<=50K")) {
				correctLessEqual50++;
			}else if(actualData[i].equals(predData[i]) && actualData[i].equals(">50K")){
				correctGreat50++;
			}else if(!actualData[i].equals(predData[i]) && predData[i].equals("<=50K") && actualData[i].equals(">50K")) {
				incorrectLessEqual50++;
			}else {
				incorrectGreat50++;
			}
		}
		System.out.println("Information for the training data set where 5 cross validation was applied : ");
		System.out.println("Correct <= 50K = " + correctLessEqual50);
		System.out.println("Correct > 50K = "+ correctGreat50);
		System.out.println("Incorrect <= 50K = " + incorrectLessEqual50);
		System.out.println("Incorrect > 50K = " + incorrectGreat50);
		System.out.println("Accuracy = " + (1.0*(correctLessEqual50 + correctGreat50) / (dataStore.size())));

		//=====================================================================================================================================================================

		actualData = new String[testStore.size()];
		predData = new String[testStore.size()];
		for(int i = 0;i < testStore.size();i ++) {
			actualData[i] = testStore.get(i)[14];
		}
		CV5.testing(dataStore,testStore);

		for(int i = 0; i < testStore.size(); i++) {
			predData[i] = testStore.get(i)[14];
		}		

		for(int i = 0;i < testStore.size();i++) {
			if (actualData[i].equals(predData[i]) && actualData[i].equals("<=50K")) {
				correctLessEqual50++;
			}else if(actualData[i].equals(predData[i]) && actualData[i].equals(">50K")){
				correctGreat50++;
			}else if(!actualData[i].equals(predData[i]) && predData[i].equals("<=50K") && actualData[i].equals(">50K")) {
				incorrectLessEqual50++;
			}else {
				incorrectGreat50++;
			}
		}
		System.out.println("Information for the testing data set has not been seen by the model: ");
		System.out.println("Correct <= 50K = " + correctLessEqual50);
		System.out.println("Correct > 50K = "+ correctGreat50);
		System.out.println("Incorrect <= 50K = " + incorrectLessEqual50);
		System.out.println("Incorrect > 50K = " + incorrectGreat50);
		System.out.println("Accuracy = " + (1.0*(correctLessEqual50 + correctGreat50) / (testStore.size())));

	}
}
