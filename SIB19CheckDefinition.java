import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Aldeqeela
 * Aug 20, 2015 - 12:01:17 PM
 * Endang.Ismaya(endang.ismaya@gmail.com)
 * Walnut Creek, CA, USA
 * SIB19 Check for definition only
 */
public class SIB19CheckDefinition {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<String> UtranCells  =  new ArrayList<String>();
		ArrayList<Integer> Frequencies = new ArrayList<Integer>();
		ArrayList<String> MOs = new ArrayList<String>();
		String RNC = null;
		
		//HashMap
		HashMap<String,Integer> cellReselectionPriorities = new HashMap<String,Integer>();
		HashMap<String,Integer> qRxLevMins = new HashMap<String,Integer>();
		HashMap<String,Integer> redirectionOrders = new HashMap<String,Integer>();
		HashMap<String,Integer> threshHighs = new HashMap<String,Integer>();
		HashMap<String,Integer> threshHigh2s = new HashMap<String,Integer>();
		HashMap<String,Integer> threshLows = new HashMap<String,Integer>();
		HashMap<String,Integer> threshLow2s = new HashMap<String,Integer>();
		HashMap<String,Integer> Unproceeds = new HashMap<String,Integer>();
		
		// String RNCname = null;
		
		try {			
				
				FileReader fileInput = null;
				BufferedReader sline = null;
				
			if (args.length == 0 ){		
				throw new Exception("You didn't give any file!");
			}	
			
			// System.out.println(arg);
			fileInput = new FileReader(args[0]);
			sline = new BufferedReader(fileInput);
			    
			while (true) {
			    	
			String data = sline.readLine();
			    	
			if (data == null){
				break;
			}
			else {			
					if (data.contains("RncFunction=1,UtranCell=")){
							
					String[] cells = data.split(",");
					String UtranCell = cells[cells.length - 1];
							
					// System.out.println(UtranCell);
					UtranCells.add(UtranCell);
					}
					else if (data.contains("EutranFreqRelation")){
							
					String[] SIBs = data.split("\\s+");
					String MO = SIBs[0];
					String Param = SIBs[1];
					int Value = Integer.valueOf(SIBs[2]);
					
					// System.out.println(MO);
					if (! MOs.contains(MO)){
						MOs.add(MO);
					}					
						if (Param.equals("cellReselectionPriority"))
							cellReselectionPriorities.put(MO, Value);
						else if (Param.equals("qRxLevMin"))
							qRxLevMins.put(MO, Value);
						else if (Param.equals("redirectionOrder"))
							redirectionOrders.put(MO, Value);
						else if (Param.equals("threshHigh"))
							threshHighs.put(MO, Value);
						else if (Param.equals("threshHigh2"))
							threshHigh2s.put(MO, Value);
						else if (Param.equals("threshLow"))
							threshLows.put(MO, Value);
						else if (Param.equals("threshLow2"))
							threshLow2s.put(MO, Value);
						else
							Unproceeds.put(MO, Value);
					}
				} // if (data.contains("RncFunction=1,UtranCell=")){
			
				if (data.contains("st UtranCell=")){			
					String arrays[] = data.split(">");
					RNC = arrays[0];
				}
			} // close while(true)
			   
			//Close file
			fileInput.close();
			
			// Display MOs
			if (args.length > 1) {				
				if (args[1] != null){					
					String[] Freqs = args[1].split(",");					
					for (String Freq : Freqs){
						int freq = Integer.valueOf(Freq) ;
						Frequencies.add(freq);
					}
				}
			}
			
			// Display MOs
			// get date
			java.util.Date today = new java.util.Date();
			// SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy.hh:mm:ss");
			
			FontColor fcline = new FontColor();

			// Display parameters needs to be defined.
			// Header
			fcline.setString("========================================================================================" +
					"================================================================================================");
			System.out.println(fcline.bpurple());
			System.out.println("MO(s).NEED.TO.BE.DEFINED");
			System.out.println("Date: " + dateFormat.format(today));
			System.out.println(fcline.bpurple());
			if (threshHigh2s.isEmpty() && threshLow2s.isEmpty()){
				System.out.printf("%-25s %-45s %-25s %-15s %-20s %-15s %s%n",
						"RNC","MO", "cellReselectionPriority", "qRxLevMin", "redirectionOrder", "threshHigh", "threshLow");
				System.out.println(fcline.bpurple());
			} else {
				System.out.printf("%-15s %-45s %-25s %-15s %-20s %-15s %-15s %-15s %s%n",
						"RNC","MO", "cellReselectionPriority", "qRxLevMin", "redirectionOrder", "threshHigh", "threshHigh2", "threshLow", "threshLow2");
				System.out.println(fcline.bpurple());
			}
			
			SIB19NeedSet.SetCreate(RNC, MOs, 
					Frequencies,
					UtranCells,
					threshHigh2s,
					threshLow2s);
			
			System.out.println();
			
		} // Close try
		catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
		} // Close Catch
	
	} // Close Main
}
