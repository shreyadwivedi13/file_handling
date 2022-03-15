package Assignment.FileHandling;

import org.apache.logging.log4j.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/*
 Name: - Record Maintenance System
a) Read a file (CSV format) e.g ID,Name,Add,Gender,....etc. (ID is unique key of each record)
b) Insert all the records in a target file (in the same format) e.g FileName :- EmployeeData.dat.
c) Make sure it must not duplicate the records in target file, in fact overwrite the record which is already present.
d) At the end, must display how many new records have been added and how many over-written.
e) Save previous record value, for each record over-written.
f) And show all the newly added ids in sorted order.
Note: If Input file contains any duplicate record (ID) then only the latest entry should go through and we have to save the previous value in a separate file (step e).
-
@author shreya.dwivedi
 */
public class EmployeeRecordMaintainance {

	static final Logger logg = LogManager.getLogger(EmployeeRecordMaintainance.class.getName());

	static HashMap<Integer, Employee> uniqueRecords = new HashMap<>(); // contains the unique records of the
																		// original file

	static List<String> overwrittenRecords = new ArrayList<>(); // contains the overwritten records.
	static HashMap<Integer, Employee> mainRecords = new HashMap<>(); // contains the new added records.
	
	static int overwrittenRec_no = 0; // number of overwritten records.
	static int addedRec_no = 0; // number of newly added records.
	static int duplicateRec_no = 0;

	public final static String overwrittenRecordFile = "./dataFiles/ReferenceCopy.csv";// file that will store all the deleted
																			// duplicate records and the overwritten
																			// records.
	public final static String DEFAULT_INPUT_FILE_PATH = "./dataFiles/rawRecord.csv";
	public final static String DEFAULT_TARGET_FILE_PATH = "./dataFiles/finalRecord.dat";

	// file that will store all the deleted duplicate records and the overwritten
	// records.

	public static void readOriginalCSV(String INPUT_FILE_PATH) throws Exception
	// to read raw data file and store unique records in UniqueRecord hash map.
	{
		if (INPUT_FILE_PATH == null || INPUT_FILE_PATH.isEmpty()) {
			INPUT_FILE_PATH = DEFAULT_INPUT_FILE_PATH;
		}
		TreeMap<Integer, Employee> sorted = new TreeMap<>(); // to sort the new records.
		
		try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH));) {
			String contentLine = br.readLine();
			String[] temp;
			Employee r;
			while (contentLine != null) {
				contentLine = br.readLine();
				if (contentLine != null) {
					r = new Employee();
					temp = contentLine.split(",");
					int inInteger = Integer.parseInt(temp[0]);
					Employee record = setDataFields(temp, r);

					/*
					 * this will read each line and then store the id as key of the hashmap and all
					 * other info of the record as the value of the hash map.
					 */
					Employee updatedrecord = sorted.put(inInteger, record);

					// check for existing record using return value of put method of hashmap
					if (updatedrecord != null) {
						overwrittenRecords.add(contentLine);// entering overwritten into "overwrittenRecord" ArrayList
						duplicateRec_no++;
					}

				}

			}
		} catch (IOException ioe) {
			throw new Exception("Error in closing the BufferedReader(in inputFile function)");
			
		}

		uniqueRecords.putAll(sorted);
		/*logg.info("uniqueRecords HashMap:");
		for (Map.Entry<Integer, Employee> each : uniqueRecords.entrySet()) {
			logg.info(each.getValue());
		}
		logg.info("******************************");*/
		
	}

	public static void readTargetFile(String TARGET_FILE_PATH) {
		// reading the main file with original records and storing it's record in
		// mainRecord array.
		if (TARGET_FILE_PATH == null || TARGET_FILE_PATH.isEmpty()) {
			TARGET_FILE_PATH = DEFAULT_TARGET_FILE_PATH;
		}
		try (BufferedReader br1 = new BufferedReader(new FileReader(TARGET_FILE_PATH));) {
			
			String contentLine1 = br1.readLine();
			String[] temp1;
			Employee r1;
			while (contentLine1 != null) {
				contentLine1 = br1.readLine();
				if (contentLine1 != null) {
					r1 = new Employee();
					temp1 = contentLine1.split(",");
					int inInteger = Integer.parseInt(temp1[0]);
					Employee record = setDataFields(temp1, r1);
					/*
					 * this will read each line and then store the id as key of the hashmap and all
					 * other info of the record as the value of the hash map.
					 */
					Employee updatedrecord = mainRecords.put(inInteger, record);

					// check for existing record using return value of put method of hashmap
					if (updatedrecord != null) {
						overwrittenRecords.add(contentLine1);
						duplicateRec_no++;
					}
				}
			}

		} catch (IOException ioe) {
		logg.info("Error in closing the BufferedReader(while reading the targetFile.)");

		}
		/*logg.info("mainRecord HashMap:");
		for (Map.Entry<Integer, Employee> each : mainRecords.entrySet()) {
			logg.info((each.getValue()));

		}
		logg.info("******************************");*/
	}

	public static Employee setDataFields(String[] temp, Employee rec) {

		// object of template class containing data fields
		// setting data fields from the each line of file using template class
		rec.setId(Integer.parseInt(temp[0]));
		rec.setName(temp[1]);
		rec.setGender(temp[2]);
		rec.setAge(temp[3]);
		rec.setWeight(temp[4]);
		return rec;

	}

	public static LinkedHashMap<Integer, Employee> finalMapping(HashMap<Integer, Employee> uniqueRecords,
			HashMap<Integer, Employee> mainRecords) {
		// this function will merge the hashmap of both targetFile and new record file
		// to get us the final records.

		HashMap<Integer, Employee> mainRecordsCopy = new HashMap<Integer, Employee>(mainRecords);
		HashMap<Integer, Employee> uniqueRecordsCopy = new HashMap<Integer, Employee>(uniqueRecords);
		LinkedHashMap<Integer, Employee> finalMap = new LinkedHashMap<Integer, Employee>(mainRecordsCopy);

		uniqueRecordsCopy.forEach((key, value) -> finalMap.merge(key, value, (v1, v2) -> {
			overwrittenRecords.add(v1 + "");
			overwrittenRec_no++;
			return v2;
		}));

		/*logg.info("merged HashMap:");
		for (Map.Entry<Integer, Employee> each : finalMap.entrySet()) {
			logg.info(each.getValue());
		}
		logg.info("******************************");*/
		return finalMap;
	}

	public static void writeFinalContent(String TARGET_FILE_PATH, HashMap<Integer, Employee> finalMap) {
		// writing the final record file.
		// BufferedWriter br2 = null;
		if (TARGET_FILE_PATH == null || TARGET_FILE_PATH.isEmpty()) {
			TARGET_FILE_PATH = DEFAULT_TARGET_FILE_PATH;
		}
		String toWrite = "";
		logg.info("following are the over-written records:");
		try (FileWriter file2 = new FileWriter(TARGET_FILE_PATH, false);) {

			// br2 = new BufferedWriter(file2);
			for (Map.Entry<Integer, Employee> each : finalMap.entrySet()) {
				// System.out.println(each.getValue());
				toWrite = each.getValue() + "";
				file2.write(toWrite + "\n");
				// br2.newLine();
				logg.info(toWrite);
			}

		} catch (IOException ioe) {
			logg.fatal("some error occured while writing the final content file.");
		}
	}

	public static void OverwrittenContent(List<String> overwrittenRecords) { // to write reference data file.

		// System.out.println("Following entries have been overwritten : \n");
		try (BufferedWriter br1 = new BufferedWriter(new FileWriter(overwrittenRecordFile));) {
			br1.write("Id, Name, Gender, Age, Weight \n");
			for (String r : overwrittenRecords) {

				// System.out.println(r);
				br1.write(r);
				br1.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			logg.fatal("some error occured while writing the reference copy file.");
		}
	}

	public static void resourceIntializer() throws Exception {
		try {
			ReadConfigFile.getFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception("resource intialization failed config file missing.");
		}
	}

	public static void main(String[] agrs) {
		try {

			// intializes the resources (config) file
			resourceIntializer();
			// System.out.println(pathConfigFile);
			String INPUT_FILE_PATH = ReadConfigFile.getResources("inputFile");
			String TARGET_FILE_PATH = ReadConfigFile.getResources("targetFile");

			// System.out.println(targetFile);
			// System.out.println(inputFile);
			readOriginalCSV(INPUT_FILE_PATH);

			// reading contents of the final record file.
			readTargetFile(TARGET_FILE_PATH);
			// reading contents of the file which is to be copied in the target file.
			
			HashMap<Integer, Employee> finalMap = new HashMap<Integer, Employee>();
			finalMap = finalMapping(uniqueRecords, mainRecords);

			// writing the required files.
			writeFinalContent(TARGET_FILE_PATH, finalMap);
			OverwrittenContent(overwrittenRecords);
			addedRec_no = finalMap.size() - overwrittenRec_no;
			logg.info("Number of New records added : " + addedRec_no + "\n");
			logg.info("******************************");
			logg.info("Number of overwritten Records : " + (overwrittenRec_no) + "\n");
			logg.info("******************************");
			logg.info("Number of duplicate Records : " + (duplicateRec_no) + "\n");
			logg.info("******************************");

			logg.info("finalRecords.dat has the uniques records and newly added records in sorted order. \n");
			logg.info("referenceCopy.csv has the overwritten data");
		} catch (Exception e) {
			logg.fatal(e.getMessage());
		}
	}
}
