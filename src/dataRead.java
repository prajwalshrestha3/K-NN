
import java.util.*;	//imports
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class dataRead {		
	public static ArrayList<String []> readFile(String path) {	//function that reads the file an stores it in an arraylist with String arrays
		ArrayList<String []> dataStore = new ArrayList<String []>();
		BufferedReader br = null;	//creating a new bufferedreader object and setting it to null	
		String line = "";	//creating a string variable
		String cvsSplitBy = ",";		//string set to "," which is used in csv to seperate the values in the file
		try {
			br = new BufferedReader(new FileReader(path));	//reading the file
			br.readLine();	//reading the first line as we dont want the attributes
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] data = line.split(cvsSplitBy);
				dataStore.add(data);	//storing the data which is read from the file to the arraylist
			}

			for(int row = 0; row < dataStore.size();row++){	//trimming the extra spaces if there are any in the data
				for(int col = 0;col < dataStore.get(row).length;col++){
					dataStore.get(row)[col]=dataStore.get(row)[col].trim();
				}
			}

		} catch (FileNotFoundException e) {		//exceptions
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataStore;	//returning dataStore arraylist
	}

	public static ArrayList<String> computeMode(ArrayList<String[]> dataStore){	//function to get the mode of the respective columns
		HashMap<String,Integer> mode = new HashMap<String,Integer>();		//new hashmap created
		ArrayList<String> colModes = new ArrayList<String>();		//string arraylist to hold the mode values for each and every column
		int count = 0;
		for(int col = 0; col < dataStore.get(1).length; col++){	
			for(int row = 0; row < dataStore.size(); row++){

				if((!dataStore.get(row)[col].equals("?")) && mode.containsKey(dataStore.get(row)[col])){
					count = mode.get(dataStore.get(row)[col]) + 1;
				}else{
					count = 1;
				}
				mode.put(dataStore.get(row)[col], count);
			}

			int maxValueInMap = (Collections.max(mode.values()));
			for (Entry<String, Integer> entry : mode.entrySet()) {  // Iterate through hashmap
				if (entry.getValue()==maxValueInMap) {
					//System.out.println(entry.getKey());     // Print the key with max value
					colModes.add(entry.getKey());
				}
			}
			mode.clear();
		}
		return colModes;
	}

	public static ArrayList<String[]> replaceMissingVal(ArrayList<String[]> dataStore){	//function to replace the "?" with mode of that column
		ArrayList<String> colModes = computeMode (dataStore);
		for (int row = 0; row < dataStore.size(); row++) {
			for (int col = 0; col < dataStore.get(row).length; col++) {
				if(dataStore.get(row)[col].equals("?")){	//if the data in the given row and col number is a "?" then it is replaced by the mode of that column
					dataStore.get(row)[col] = colModes.get(col+1); //+1 to adjust to the dataStore arrayList
				}
			}
		}
		return dataStore;
	}

	public static void normalise(ArrayList<String[]> dataStore){	//feature scaling normalisation
		double normalVal;
		double max;
		double min;
		//get the columns max and min value then apply the formula, except for the nominal values
		for(int col=0;col<15;col++){
			ArrayList<Double> a = new ArrayList<Double>();

			for(int row=0;row<dataStore.size();row++){
				try{	//tries to change the string value to double
					a.add(Double.valueOf((dataStore.get(row)[col])));
				}catch(NumberFormatException e){	//if the column has nominal values it skips that columns
					col++;
					//System.out.println(col);
				}
			}
			max = Collections.max(a);	//max value stored in the variable
			//System.out.println("max"+max);
			min = Collections.min(a);	//min value stored in the variable
			//System.out.println("min"+min);
			if(col != 15){	//excluding the normalization for the final column
				for(int row = 0; row<dataStore.size();row++){
					normalVal = (((Double.valueOf(dataStore.get(row)[col])))-min)/(max-min);				
					dataStore.get(row)[col] = Double.toString(normalVal);
				}
			}
		}
	}
}
