import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Aldeqeela
 * Jul 28, 2015 - 11:04:20 AM
 * Endang.Ismaya(endang.ismaya@gmail.com)
 * Walnut Creek, CA, USA
 */
public class Sib19Extract {

	
	final String fileName;

	List<String> delList = new ArrayList<String>();
	List<String> creList = new ArrayList<String>();
	List<String> fixList = new ArrayList<String>();
	List<String> freqList = new ArrayList<String>();
	Map<String,String> moDelMap = new HashMap<String,String>();
	Map<String,String> moDefMap = new HashMap<String,String>();
	Map<String,String> moFixMap = new HashMap<String,String>();
	List<String> rncList = new ArrayList<String>();
	List<String> moDelList = new ArrayList<String>();
	List<String> moDefList = new ArrayList<String>();
	List<String> moFixList = new ArrayList<String>();

	public Sib19Extract(String fileName) {
		this.fileName = fileName;
	}

	public void startExtract(){
		
		String data = null;
		boolean boolDel = false;
		boolean boolDef = false;
		boolean boolFix = false;
		
		List<String> delList = new ArrayList<String>();
		List<String> defList = new ArrayList<String>();
		List<String> fixList = new ArrayList<String>();
		List<String> freqList = new ArrayList<String>();
		List<String> moDelList = new ArrayList<String>();
		List<String> moDefList = new ArrayList<String>();
		List<String> moFixList = new ArrayList<String>();
		Set<String> freqSet = new TreeSet<String>();
		Set<String> rncList = new TreeSet<String>();
		
		int i_cellReselectionPriority = 0;
		int i_qRxLevMin = 0;
		int i_redirectionOrder = 0;
		int i_threshHigh = 0;
		int i_threshHigh2 = 0;
		int i_threshLow = 0;
		int i_threshLow2 = 0;		
		
		try {			
				
			FileReader fileInput = null;
			BufferedReader sline = null;
				
			// System.out.println(arg);
			fileInput = new FileReader(getFileName());
			sline = new BufferedReader(fileInput);
			    
			while (true) {
			    	
			data = sline.readLine();
			    	
			if (data == null){
				break;
			}
			else {			
				// System.out.println(data);
				// String[] arr = data.split("\\s");
				// System.out.printf("%-20s %-40s %-20s %-20s %-20s %s%n", arr[0],arr[1],arr[2],arr[3],arr[4],arr[5]);
				if (data.matches("MO\\(s\\)\\.NEED\\.TO\\.BE\\.DELETED.*")){
					boolDel = true;
					boolDef = false;
					boolFix = false;
				} 
				else if (data.matches("MO\\(s\\)\\.NEED\\.TO\\.BE\\.DEFINED.*")){
					boolDel = false;
					boolDef = true;
					boolFix = false;					
				}
				else if (data.matches("PARAMETER\\(s\\)\\.NEED\\.TO\\.BE\\.FIXED.*")){
					boolDel = false;
					boolDef = false;
					boolFix = true;					
				}
				else {
					if (boolDel){
						
						if (data.matches(".*EutranFreqRelation.*")) {
							String[] arr = data.split("\\s+");
							String rnc = arr[0];
							String mo = arr[1];
							String delmo = "del " + mo + "$";
							String rncMO = rnc + ";" + delmo;
							delList.add(rncMO);
							rncList.add(rnc);
							moDelList.add(rnc + ";" + mo);
						}
					}
					else if (boolDef){
						if (data.matches(".*EutranFreqRelation.*")) {

							String[] arr = data.split("\\s+");
							String rnc = arr[0];
							String mo = arr[1];
							String[] arrFreq = mo.split("=");

							String freq = arrFreq[arrFreq.length - 1];
							String rncFreq = rnc + "_" + freq;
							freqSet.add(rncFreq);
							rncList.add(rnc);
							moDefList.add(rnc + ";" + mo);
							
							String completeMO = "RncFunction=1," + mo;
							String cr = "cr " + completeMO + "\n";
								cr = cr + "EutraNetwork=1,EutranFrequency=" + freq + " #eutranFrequencyRef" + "\n";
								// if $command_result = 0
								cr = cr + "if $command_result = 0" + "\n";
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ cellReselectionPriority 7
								cr = cr + "lset " + completeMO + "$ cellReselectionPriority " + arr[i_cellReselectionPriority] + "\n";
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ qQualMin 100
								// cr = cr + "lset " + completeMO + "$ qQualMin 100" + "\n";
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ qRxLevMin -122
								cr = cr + "lset " + completeMO + "$ qRxLevMin " + arr[i_qRxLevMin] + "\n";
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ redirectionOrder 1
								cr = cr + "lset " + completeMO + "$ redirectionOrder " + arr[i_redirectionOrder] + "\n";
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ threshHigh 10
								cr = cr + "lset " + completeMO + "$ threshHigh " + arr[i_threshHigh] + "\n";
								if (i_threshHigh2 > 0){
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ threshHigh2 10
								cr = cr + "lset " + completeMO + "$ threshHigh2 " + arr[i_threshHigh2] + "\n";
								}
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ threshLow 6
								cr = cr + "lset " + completeMO + "$ threshLow " + arr[i_threshLow] + "\n";
								if (i_threshLow2 > 0){
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ threshLow2 10
								cr = cr + "lset " + completeMO + "$ threshLow2 " + arr[i_threshLow2] + "\n";
								}
								// lset RncFunction=1,UtranCell=CNU0990V,EutranFreqRelation=5780$ userLabel LTE
								cr = cr + "lset " + completeMO + "$ userLabel LTE" + "\n";
								// fi
								cr = cr + "fi";
								
								
							String rncMO = rnc + ";" + cr ;
							
							defList.add(rncMO);
							
						} 
						else if (data.matches(".*cellReselectionPriority.*")){
							String[] arr = data.split("\\s+");
							
							for (int i = 2; i < arr.length;i++){
								// System.out.println(arr[i]);
								if (arr[i].equals("cellReselectionPriority"))
									i_cellReselectionPriority = i;
								if (arr[i].equals("qRxLevMin"))
									i_qRxLevMin = i;
								if (arr[i].equals("redirectionOrder"))
									i_redirectionOrder = i;
								if (arr[i].equals("threshHigh"))
									i_threshHigh = i;
								if (arr[i].equals("threshHigh2"))
									i_threshHigh2 = i;
								if (arr[i].equals("threshLow"))
									i_threshLow = i;
								if (arr[i].equals("threshLow2"))
									i_threshLow2 = i;
							}
						}
					}
					else if (boolFix){							
						if (data.matches(".*EutranFreqRelation.*")) {
							String[] arr = data.split("\\s+");
							String rnc = arr[0];
							String mo = arr[1];
							String param = arr[2];
							String baseline = arr[4];
							
							String cr = "set " + mo + " " + param + " " + baseline ;
							String rncMO = rnc + ";" + cr;
							fixList.add(rncMO);
							rncList.add(rnc);
							moFixList.add(rnc + ";" + mo);
						}
					}
				}	
				
			} // else
	
			} // close while(true)
			   
			//Close file
			fileInput.close();
			
			
			for (String s : freqSet){
				
				String[] arr = s.split("_");
				String rnc = arr[0];
				String cr = "pr EutraNetwork=1,EutranFrequency=" + arr[1] + "$" + "\n";
						cr = cr + "if $nr_of_mos = 0" + "\n";
						cr = cr + "cr EutraNetwork=1,EutranFrequency=" + arr[1] + "\n";
						cr = cr + arr[1] + "\n";
						cr = cr + "fi";
//				pr EutraNetwork=1,EutranFrequency=1025$
//				if $nr_of_mos = 0
//				cr EutraNetwork=1,EutranFrequency=1025
//				1025
//				fi
						
				freqList.add(rnc + ";" + cr);
			}
			
			// # rncList 
			for (String s : rncList){
				this.rncList.add(s);
			}
			
			// # MapList for group (ma)
			this.moDelMap = groupingMap(this.rncList,moDelList);
			this.moDefMap = groupingMap(this.rncList,moDefList);
			this.moFixMap = groupingMap(this.rncList,moFixList);
			
			// # AssignList
			this.delList = delList;
			this.creList = defList;
			this.fixList = fixList;
			this.freqList = freqList;
			this.moDelList = moDelList;
			this.moDefList = moDefList;
			this.moFixList = moFixList;
			
		} // Close try
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println(data);
		}
		catch(Exception e) {
				e.printStackTrace();
		} // Close Catch
	}

	private Map<String,String> groupingMap(List<String> rncList, List<String> mList){
		
		String ma = null;
		Map<String,String> Map = new HashMap<String,String>();
		
		if (!mList.isEmpty()){
			for (String s : mList){
				String[] arr = s.split(";");
				String rnc = arr[0];
				String mo = arr[1];
				
				if (!Map.containsKey(rnc)){
					ma = mo + "|";
					Map.put(rnc, ma);
				}else {
					ma = ma + mo + "|";
					Map.put(rnc, ma);
				}
			}
		}
		else {
			for (String s : rncList){
				if (!Map.containsKey(s)){
					Map.put(s, ma);
				}
			}
		}
		
		return Map;
	}
	
	/**
	 * @return the moDelmap
	 */
	public Map<String, String> getMoDelMap() {
		return moDelMap;
	}
	
	/**
	 * @return the moDelmap
	 */
	public Map<String, String> getMoDefMap() {
		return moDefMap;
	}
	
	/**
	 * @return the moDelmap
	 */
	public Map<String, String> getMoFixMap() {
		return moFixMap;
	}

	/**
	 * @return the moDelList
	 */
	public List<String> getMoDelList() {
		return moDelList;
	}

	/**
	 * @return the moDefList
	 */
	public List<String> getMoDefList() {
		return moDefList;
	}

	/**
	 * @return the moFixList
	 */
	public List<String> getMoFixList() {
		return moFixList;
	}

	/**
	 * @return the rncList
	 */
	public List<String> getRncList() {
		return rncList;
	}

	/**
	 * @return the freqList
	 */
	public List<String> getFreqList() {
		return freqList;
	}
	
	/**
	 * @return the delList
	 */
	public List<String> getDelList() {
		return delList;
	}

	/**
	 * @return the creList
	 */
	public List<String> getCreList() {
		return creList;
	}

	/**
	 * @return the fixList
	 */
	public List<String> getFixList() {
		return fixList;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
}
