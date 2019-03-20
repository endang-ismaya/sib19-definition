import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aldeqeela
 * Jul 30, 2015 - 9:14:08 AM
 * Endang.Ismaya(endang.ismaya@gmail.com)
 * Walnut Creek, CA, USA
 */
public class Sib19PrePostExtract {

	String fileName;
	
	Map<String, String> PreExecute = new HashMap<String, String>();
	Map<String, String> PostExecute = new HashMap<String, String>();

	public Sib19PrePostExtract(String fileName) {
		this.fileName = fileName;
	}

	public void startExtract(){
		
	try {			
			
		FileReader fileInput = null;
		BufferedReader sline = null;
			
		fileInput = new FileReader(getFileName());
		sline = new BufferedReader(fileInput);
		
		Map<String, String> PreExecute = new HashMap<String, String>();
		Map<String, String> PostExecute = new HashMap<String, String>();
		
		boolean precheck = false;
		boolean postcheck = false;
		boolean execution = false;
		
		String Category = "none";
		String NumberMO = "none";
		int noMO = 0;
		    
		    while (true) {
		    	
		    	String data = sline.readLine();
		    	
				if (data == null){
					break;
				}
				else {
					
					if (data.matches(".*PRE-CHECK.*")){
						precheck = true;
						postcheck = false;
//						execution = false;
					}
					else if (data.matches(".*POST-CHECK.*")){
						postcheck = true;
						precheck = false;
//						execution = false;
					}
//					else if (data.matches(".*u+.*")){
//						postcheck = false;
//						precheck = false;
////						execution = true;
//					}

					if (Category.equals("Parameter fix") && execution){
							if (data.contains("Total: 1 MOs attempted, 1 MOs set")){
								noMO = noMO + 1;
								NumberMO = "Executed: " + String.valueOf(noMO) + " Parameter(s) set";
//								System.out.println(data);
//								System.out.println(NumberMO);
							}
							else if (noMO == 0){
								NumberMO = "Total: 0 Parameter(s)";
//								System.out.println(NumberMO);
							}
					}
					
					if (data.matches(".*alt$")){
						// System.out.println(data);
						Category = "Alarms";
					} else if (data.matches(".*st UtranCell 1\\.\\*0.*")){
						Category = "st Utrancell (Unlocked and Disabled)";	
					} else if (data.matches(".*st UtranCell 0\\.\\*0.*")){
						Category = "st Utrancell (Locked and Disabled)";	
					} else if (data.matches(".*// PRE-DELETION.*") || data.matches(".*// POST-DELETION.*")){
						Category = "Deletion";	
					} else if (data.matches(".*// PRE-DEFINITION.*") || data.matches(".*// POST-DEFINITION.*")){
						Category = "Definition";	
					}else if (data.matches(".*// PRE-PARAMETER-FIX.*")){
//						System.out.println(data);
						Category = "Parameter fix";	
//						System.out.println(Category);
					}else if (data.matches(".*// POST-PARAMETER-FIX.*")){
//						System.out.println(data);
						Category = "Parameter fix";	
//						System.out.println(Category);
					}else if (data.matches(".*// EXE-PARAMETER-FIX.*")){
//						System.out.println(data);
						Category = "Parameter fix";	
						execution = true;
//						System.out.println(Category);
					}else if (data.matches(".*u-.*")){
//						System.out.println(Category);
//						System.out.println(NumberMO);
						PostExecute.put(Category, NumberMO);
						Category = "none";	
						execution = false;
//						postcheck = true;
//						precheck = false;
					}
					else if (data.matches(".*Total:.*MOs$") || data.matches(".*Total:.*Alarm.*") || 
							data.matches(".*// Total: 0 Parameter.*")) {
						
						if (data.matches(".*// Total: 0 MOs.*")) {
							NumberMO = "Total: 0 MOs";
						} else if (data.matches(".*// Total: 0 Parameter.*")) {
							NumberMO = "Total: 0 Parameter(s)";
						} else {
							NumberMO = data.replace(">>> ", "");
							if (Category.equals("Parameter fix")){
								if (precheck){
									NumberMO = NumberMO.replace("MOs", "Parameters");
									NumberMO = NumberMO.replace("Total", "Target");
								}else if (postcheck){
									NumberMO = NumberMO.replace("MOs", "Parameters");
									NumberMO = NumberMO.replace("Total", "Executed");									
								}
							}
						}
						
						if (precheck){
							if (Category.equals("Alarms") || Category.equals("st Utrancell (Unlocked and Disabled)") ||
								Category.equals("st Utrancell (Locked and Disabled)") || 
								Category.equals("Deletion")|| Category.equals("Definition") || Category.equals("Parameter fix")){
								if (!PreExecute.containsKey(Category)){
									PreExecute.put(Category, NumberMO);
								}
							}
							
							Category = "none";
							
						}else if (postcheck) {
							if (Category.equals("Alarms") || Category.equals("st Utrancell (Unlocked and Disabled)") ||
									Category.equals("st Utrancell (Locked and Disabled)") || 
									Category.equals("Deletion")|| Category.equals("Definition") || Category.equals("Parameter fix")){	
								if (!PostExecute.containsKey(Category)){
									PostExecute.put(Category, NumberMO);
								}
							}
							
							Category = "none";
						}
					}
					
				}
				
		    } // Close while
		    
	//Close file
	fileInput.close();
	
	
	DisplayPrePost(PreExecute, PostExecute);
		
	} // Close try
	catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
	} // Close Catch
	} // startExtract
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	
	public static void DisplayPrePost(Map<String, String> a, Map<String, String> b ){
		
		ArrayList<String> arrays = new ArrayList<String>();
		FontColor fcline = new FontColor();
		fcline.setString("========================================================================================" +
					"===================================================================");

		arrays.add("Alarms");
		arrays.add("st Utrancell (Unlocked and Disabled)");
		arrays.add("st Utrancell (Locked and Disabled)");
		arrays.add("Deletion");
		arrays.add("Definition");
		arrays.add("Parameter fix");

		// Header
		System.out.println(fcline.bcyan());
		System.out.printf("%-50s %-60s %s%n","CATEGORY","PRECHECK", "POSTCHECK");
		System.out.println(fcline.bcyan());
		for (String array : arrays){
			System.out.printf("%-50s %-60s %s%n",array,a.get(array), b.get(array));
		}
		// System.out.println(fcline.bcyan());
		System.out.println();
	}
}
