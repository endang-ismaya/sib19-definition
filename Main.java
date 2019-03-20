import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Aldeqeela
 * Jul 28, 2015 - 9:20:21 AM
 * Endang.Ismaya(endang.ismaya@gmail.com)
 * Walnut Creek, CA, USA
 */
public class Main {

	static List<String> delList = new ArrayList<String>();
	static List<String> defList = new ArrayList<String>();
	static List<String> fixList = new ArrayList<String>();
	static List<String> freqList = new ArrayList<String>();
	static List<String> rncList = new ArrayList<String>();
	static List<String> moDelList = new ArrayList<String>();
	static List<String> moDefList = new ArrayList<String>();
	static List<String> moFixList = new ArrayList<String>();
	static String Today;
	static String delEutranFreqList;
	static String defEutranFreqList;
	static String fixEutranFreqList;
	static Map<String,String> moDelMap = new HashMap<String,String>();
	static Map<String,String> moDefMap = new HashMap<String,String>();
	static Map<String,String> moFixMap = new HashMap<String,String>();
	static List<String> fileList = new ArrayList<String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length == 0){		
			System.out.println("No file to execute!!");
			System.exit(0);
		} 

		// Display MOs
		// get date
		java.util.Date today = new java.util.Date();
		// SimpleDateFormat dateFormat = new SimpleDateFormat("_MMddyyyy.hhmmss");
		// Today = dateFormat.format(today) + ".mos";
		
		Sib19Extract sib19a = new Sib19Extract(args[0]);
		sib19a.startExtract();
		freqList = sib19a.getFreqList();
		delList = sib19a.getDelList();
		defList = sib19a.getCreList();
		fixList = sib19a.getFixList();
		rncList = sib19a.getRncList();
		moDelList = sib19a.getMoDelList();
		moDefList = sib19a.getMoDefList();
		moFixList = sib19a.getMoFixList();
		moDelMap = sib19a.getMoDelMap();
		moDefMap = sib19a.getMoDefMap();
		moFixMap = sib19a.getMoFixMap();
		
		PreCheck(rncList,today,moDelMap,moDefMap,moFixMap);
		Execute(rncList,today,freqList,delList,defList,fixList);
		PostCheck(rncList,today,moDelMap,moDefMap,moFixMap);
			
	} // Close Main
	
	private static void PrintToFile(String FileName, String element){
		
		if (fileList.contains(FileName)){
			try {
				
				PrintWriter outFile = new PrintWriter(new FileWriter(FileName,true));
				
				outFile.write(element);
				outFile.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				
				PrintWriter outFile = new PrintWriter(new FileWriter(FileName));
				
				outFile.write(element);
				outFile.close();
				
				fileList.add(FileName);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void PreCheck(List<String> l, Date Today, Map<String,String> mDel, Map<String,String> mDef, Map<String,String> mFix){

	String fileName = null;
	String element;
	
		for (String alist : l) {
			
			delEutranFreqList = mDel.get(alist);
			defEutranFreqList = mDef.get(alist);
			fixEutranFreqList = mFix.get(alist);
			
			fileName = alist + "_SIB19.mos";
			
			element = "# ///////////////////////////////////////////////////////////////////////////// #\n" +
						"# Application Name	: eiosssibnineteen\n" +
						"# Version						: v.1.0.0\n" +
						"# Author 						: endang.ismaya@gmail.com\n" + 
						"# Date Created			: " + Today + "\n" +
						"# Comment						:	Create scripts for SIB19 & (UMTS -> LTE EutranFreqRelation)" + "\n" +
						"# ///////////////////////////////////////////////////////////////////////////// #";
			
			element = element + "\n\n" + "unset all" + "\n"
			+ "$date = `date \"+%y%m%d_%H%M%S\"`" + "\n" 
			+ "ylt ^UtranCell|EutranFreqRelation|EutranFrequency" + "\n"
			+ "l+ $nodename_GS_SIB19_$date.log" + "\n"
			+ "//PRE-CHECK" + "\n"
			+ "alt" + "\n"
			+ "st UtranCell 0.*0" + "\n"
			+ "st UtranCell 1.*0" + "\n\n";
	
		if (delEutranFreqList != null){
			delEutranFreqList = delEutranFreqList.substring(0, delEutranFreqList.length() - 1);
			element = element + 
			"// PRE-DELETION" + "\n" +
			"lma idel " + delEutranFreqList + "\n" +
			"pr idel" + "\n\n";
		} else {
			element = element + 
			"// PRE-DELETION" + "\n" +
			"// Total: 0 MOs" + "\n\n";
		}
		
		if (defEutranFreqList != null){
			defEutranFreqList = defEutranFreqList.substring(0, defEutranFreqList.length() - 1);
			element = element + 
			"// PRE-DEFINITION" + "\n" +
			"lma idef " + defEutranFreqList + "\n" + 
			"pr idef" + "\n\n";
		} else {
			element = element + 
			"// PRE-DEFINITION" + "\n" +
			"// Total: 0 MOs" + "\n\n";				
		}
		
		if (fixEutranFreqList != null){
			fixEutranFreqList = fixEutranFreqList.substring(0, fixEutranFreqList.length() - 1);
			element = element + 
			"// PRE-PARAMETER-FIX" + "\n" +
			"// Total: " + moFixList.size() + " Parameter(s)" + "\n" +	
			"lma ifix " + fixEutranFreqList + "\n" + 
			"lhget ifix redirectionOrder|cellReselectionPriority|threshHigh|qRxLevMin|threshLow" + "\n\n";
		} else {
			element = element + 
			"// PRE-PARAMETER-FIX" + "\n" +
			"// Total: 0 Parameter(s)" + "\n\n";				
		}
	
	PrintToFile(fileName,element);
	}
	

	}
	
	private static void PostCheck(List<String> rncList, Date Today, Map<String,String> mDel, Map<String,String> mDef, Map<String,String> mFix){

	String fileName = null;
	String element;
	
		for (String alist : rncList) {
			
			delEutranFreqList = mDel.get(alist);
			defEutranFreqList = mDef.get(alist);
			fixEutranFreqList = mFix.get(alist);
		
		fileName = alist + "_SIB19.mos";
		element = "// POST-CHECK" + "\n";
		
		if (delEutranFreqList != null){
			delEutranFreqList = delEutranFreqList.substring(0, delEutranFreqList.length() - 1);
			element = element + "// POST-DELETION" + "\n" +
			"pr idel" + "\n\n";
		} else {
			element = element + "// POST-DELETION" + "\n" +
			"// Total: 0 MOs" + "\n\n";
		}
		
		if (defEutranFreqList != null){
			defEutranFreqList = defEutranFreqList.substring(0, defEutranFreqList.length() - 1);
			element = element + 
			"// POST-DEFINITION" + "\n" +
			"lma idef " + defEutranFreqList + "\n" + 
			"pr idef" + "\n" +
			"lhget idef redirectionOrder|cellReselectionPriority|threshHigh|qRxLevMin|threshLow" + "\n\n";
		} else {
			element = element + 
			"// POST-DEFINITION" + "\n" +
			"// Total: 0 MOs" + "\n\n";				
		}
		
		if (fixEutranFreqList != null){
			fixEutranFreqList = fixEutranFreqList.substring(0, fixEutranFreqList.length() - 1);
			element = element + 
			"// POST-PARAMETER-FIX" + "\n" +
			"// Total: " + moFixList.size() + " Parameter(s)" + "\n" +	 
			"lhget ifix redirectionOrder|cellReselectionPriority|threshHigh|qRxLevMin|threshLow" + "\n\n";
		} else {
			element = element + 
			"// POST-PARAMETER-FIX" + "\n" +
			"// Total: 0 Parameter(s)" + "\n\n";				
		}
	
		element = element + "alt" + "\n"
		+ "st UtranCell 0.*0" + "\n"
		+ "st UtranCell 1.*0" + "\n"
		+ "l-" + "\n"
		+ "mr idel" + "\n" 
		+ "mr idef" + "\n"
		+ "mr ifix" + "\n"
		+ "l java -cp /home/fs664c/endang/jvSib19Set Sib19Report $nodename_GS_SIB19_$date.log" + "\n\n"
		+ "unset all" + "\n\n" ;
		
	PrintToFile(fileName,element);
	}
	
	//PrintToFile(fileName,"\n");
	}
	
	private static void Execute(List<String> rncList, Date Today, List<String> freqList,
						List<String> delList,List<String> defList, List<String> fixList ){

	String fileName = null;
	String element = "\n";
	List<String> eFreqList = new ArrayList<String>();
	List<String> edelList = new ArrayList<String>();
	List<String> edefList = new ArrayList<String>();
	List<String> efixList = new ArrayList<String>();
	
		for (String rnc : rncList) {
		
			fileName = rnc + "_SIB19.mos";
			element = "confb+" + "\n" + "gs+" + "\n";
			element = element + "u+ $nodename_Undo_GS_$date.mos" + "\n";
			
			eFreqList.clear();
			for (String freq : freqList){
				String[] arr = freq.split(";");
				String rncA = arr[0];
				if (rncA.equals(rnc)){
					eFreqList.add(freq);
				}
			}
			edelList.clear();
			for (String freq : delList){
				String[] arr = freq.split(";");
				String rncA = arr[0];
				if (rncA.equals(rnc)){
					edelList.add(freq);
				}
			}
			edefList.clear();
			for (String freq : defList){
				String[] arr = freq.split(";");
				String rncA = arr[0];
				if (rncA.equals(rnc)){
					edefList.add(freq);
				}
			}
			efixList.clear();
			for (String freq : fixList){
				String[] arr = freq.split(";");
				String rncA = arr[0];
				if (rncA.equals(rnc)){
					efixList.add(freq);
				}
			}

			if (!eFreqList.isEmpty()){
				element = element + "\n"
						+ "// EXE-FREQDEFINITION" + "\n";
				for (String s : eFreqList){
					String[] arr = s.split(";");
					String rncA = arr[0];
					String moA = arr[1];
					
					if (rncA.equals(rnc)){
						element = element + moA + "\n";
					}
				}
			}
			
			if (!edelList.isEmpty()){
				element = element + "\n";
				element = element + "// EXE-DELETION" + "\n";
				
				for (String s : edelList){
					String[] arr = s.split(";");
					String rncA = arr[0];
					String moA = arr[1];
					
					if (rncA.equals(rnc)){
						element = element + moA + "\n";
					}
				}
			}
			
			if (!edefList.isEmpty()){
				element = element + "\n";
				element = element + "// EXE-DEFINITION" + "\n";
				
				for (String s : edefList){
					String[] arr = s.split(";");
					String rncA = arr[0];
					String moA = arr[1];
					
					if (rncA.equals(rnc)){
						element = element + moA + "\n";
					}
				}
			}
			
			if (!efixList.isEmpty()){
				element = element + "\n";
				element = element + "// EXE-PARAMETER-FIX" + "\n";
				
				for (String s : efixList){
					String[] arr = s.split(";");
					String rncA = arr[0];
					String moA = arr[1];
					
					if (rncA.equals(rnc)){
						element = element + moA + "\n";
					}
				}
			}
			
			element = element + "\n" + "u- " + "\n";
			element = element + "gs-" + "\n" + "confb-" + "\n\n";
			
		PrintToFile(fileName,element);
	}
	
	}
	

	/**
	 * @return the fileList
	 */
	public static List<String> getFileList() {
		return fileList;
	}
}
