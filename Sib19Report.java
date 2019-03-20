/**
 * @author Aldeqeela
 * Jul 30, 2015 - 9:21:26 AM
 * Endang.Ismaya(endang.ismaya@gmail.com)
 * Walnut Creek, CA, USA
 */
public class Sib19Report {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length == 0){		
			System.out.println("No file to execute!!");
			System.exit(0);
		} 
		
		// Display MOs
		// get date
//		java.util.Date today = new java.util.Date();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("_MMddyyyy.hhmmss");
		
		Sib19PrePostExtract sibReport = new Sib19PrePostExtract(args[0]);
		sibReport.startExtract();
	}

}
