import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;



public class printTable {
	public static void main(String[] args) throws FileNotFoundException, IOException {
	//args[0]: "replacemulti" or "regular"
	//args[1]: all activity "EQRXTUV"
	//args[2]: gap
	//args[3]: select_top
	//args[4]: existThreshold
	//args[5]: pThreshold
	//args[6]: t-direction "pos", "neg" or "twoside"
	//args[7]: filename in csv that contains the list of patterns for which we need to calculate the frequency
		
	//filter this with p, then rank with t value.
		
		
		//String input_file="C:\\Users\\Dan Lv\\OneDrive\\Summer Intern\\OneDrive\\SimStudent Project\\Analysis (where we analyze data)\\Study VI Tutoring Pattern (Joyce & Dan)\\activity sequence\\EQRSTUV_sequence_notail_1.6.csv";
		//String output_file="C:\\Users\\Dan Lv\\OneDrive\\Summer Intern\\OneDrive\\SimStudent Project\\Analysis (where we analyze data)\\Study VI Tutoring Pattern (Joyce & Dan)\\Differential Sequence Pattern Mining\\"+args[0]+"_"+args[1]+"_g"+args[2]+"_top"+args[3]+"_e"+args[4]+"_p"+args[5]+"_t"+args[6]+"_notail.csv";
		
		//String input_file="/Users/tasmiashahriar/OneDrive/SimStudent Project/Analysis (where we analyze data)/Study VI Tutoring Pattern (Joyce & Dan)/activity sequence/EQRSTUV_sequence_notail_1.6.csv";
		//String input_file="/Users/tasmiashahriar/OneDrive/SimStudent Project/Analysis (where we analyze data)/Study VI Tutoring Pattern (Joyce & Dan)/activity sequence/Study VII VIII/EQRXTUV_VI_VII_VIII_java.csv";
		//String input_file="/Users/tasmiashahriar/OneDrive/SimStudent Project/Analysis (where we analyze data)/Study VI Tutoring Pattern (Joyce & Dan)/activity sequence/Study VII VIII/EQRXTUV_VI_VII_VIII_java_new_seq.csv";
		String input_file="/Users/tasmiashahriar/OneDrive/SimStudent Project/Analysis (where we analyze data)/Study VI Tutoring Pattern (Joyce & Dan)/activity sequence/Study VII VIII/EQRXTUV_VI_VII_VIII_java_true_X.csv";

		//String output_file="/Users/tasmiashahriar/OneDrive/SimStudent Project/Analysis (where we analyze data)/Study VI Tutoring Pattern (Joyce & Dan)/Differential Sequence Pattern Mining/Study VII VIII/VI_VII_VIII_new_seq"+args[0]+"_"+args[1]+"_g"+args[2]+"_top"+args[3]+"_e"+args[4]+"_p"+args[5]+"_t"+args[6]+"_notail.csv";
		
		tools t=new tools();
		int gap=Integer.parseInt(args[2]);//the largest gap we can ignore in the sequence
		int selectTop=Integer.parseInt(args[3]);//select n top t-value pattern every step
		int existThreshold=Integer.parseInt(args[4]);//the minimum number of students who have the pattern
		double pThreshold=Double.parseDouble(args[5]);//the minimum p-value, the patterns which have greater p-value will be dropped.
		String frequency_list_file="/Users/tasmiashahriar/OneDrive/SimStudent Project/Analysis (where we analyze data)/Study VI Tutoring Pattern (Joyce & Dan)/Differential Sequence Pattern Mining/Study VII VIII/"+args[7];

		
		//String[] iTAS = {"id", "QXT", "XTXQ",	"QXTX",	"XTXQX", "XTX",	"QXTXQ", "XQXTX","XT",	"TXQX",	"TXQXT",	"XQXT",	"TXQ",	"TX",	"TQXTR", "XTQXT",	"TXTXQ", "TXQXQ", "XQ", "QX", "TQT", "TQV", "TQR", "TQE", "TQU","VQT", "RQT", "EQT", "UQT"};
		//String[] iTAS = {"XT","XUX","TRX","XUXT","XTRX","RXTX","TRXT","XTXT","RXTXT","TXTRX","XTRXT","XTXTR","TRXTX"};
		// Getting the list of pattern for which frequency will be written
		CSVReader freq_reader = new CSVReader(new FileReader(frequency_list_file));
		String [] freq_nextLine=freq_reader.readNext(); 
		List<String> iTASList = new ArrayList<String>();
		iTASList.add("id");
		while ((freq_nextLine = freq_reader.readNext()) != null) {
			iTASList.add(freq_nextLine[0]);
		}
		t.setiTAS(iTASList);
		//String[] iTAS = {"id","QXQXT","XUX","QXQ", "QX","QXQX","TRXQ","XTRXQ","XQ","QXTRQ","TRX","TRXQX","TXQXQ","XQXQ","XTQ","XQXQX","RXQ","XTQX","RXQX","XT","XTRQ","XTRQX","XTRX"};
		//String[] iTAS = {"EQE", "RQR",	"TQT",	"UQU", "VQV", 
		//		"EQR","EQT",	"EQU",	"EQV",
		//		"RQE","RQT",	"RQU",	"RQV",
		//		"TQE","TQR",	"TQU",	"TQV",
		//		"UQE","UQR",	"UQT",	"UQV",
		//		"VQE","VQR",	"VQT",	"VQU",
		//		"XQX","XQ","QX"};

		//List<String> iTASList = new ArrayList<>(Arrays.asList(iTAS));

		//t.setiTAS(iTASList);
		
		//reading data
		List<Student> students=new ArrayList<Student>();
		//the location of the input file
		//input file should have student's ID, sequence,condition,class,normalized gain
		// Please review the column no, starting from 0 to match the following variables.
		CSVReader reader = new CSVReader(new FileReader(input_file));
		String [] nextLine=reader.readNext(); 
		final int ID_COLUMN=0; // the order of column that contains student ID
		final int ACTIVITY_COLUMN=11; // the order of column that contains student's activity sequence
		final int COND_COLUMN=1; // the order of column that contains student's assigned condition
		final int CLASS_COLUMN=7; 
		// the order of column that contains student's gain type
		// Col=2 if you want gain class that has 3 groups (high,low and medium gainers)
		// Col=7 if you want gain class that has 2 groups (high and low gainers)
		// we will always use class_column = 7 for the frequency count so that we get the frequency count for all students including the mid gainers.
		final int GAIN_COLUMN=3;

		System.out.println("Reading data from the input file: "+input_file);

		//input file is the pre-cleaned dropped-tail file
		int goodnum=0,badnum=0;
		while ((nextLine = reader.readNext()) != null) {
			Student newstudent=new Student(nextLine[ID_COLUMN],nextLine[ACTIVITY_COLUMN],nextLine[COND_COLUMN],nextLine[CLASS_COLUMN],Double.parseDouble(nextLine[GAIN_COLUMN]));
			//System.out.println(newstudent.id);

			if(args[0].equals("replacemulti")) newstudent.replaceMulti();
			//System.out.println(newstudent.activity);
			if(newstudent.class1.equals("high-gainer")){students.add(newstudent);goodnum++;}
			if(newstudent.class1.equals("low-gainer")){students.add(newstudent);badnum++;}
			
		}
		System.out.println("We have total "+students.size()+" students.");
		System.out.println("High-gainer student: "+goodnum);
		System.out.println("Low-gainer student: "+badnum);
		
		
		System.out.println("Reading completed.\n");

		// Tasmia: Preparing the file for iTAS frequency output for study VII & VIII
		String output_file_2="/Users/tasmiashahriar/OneDrive/SimStudent Project/Analysis (where we analyze data)/Study VI Tutoring Pattern (Joyce & Dan)/Differential Sequence Pattern Mining/Study VII VIII/VI_VII_VIII_genuineX_seq_frequency.csv";
		CSVWriter writer_freq = new CSVWriter(new FileWriter(output_file_2), ',');
	    //String [] row_freq={"id","QXT", "XTXQ",	"QXTX",	"XTXQX", "XTX",	"QXTXQ", "XQXTX","XT",	"TXQX",	"TXQXT",	"XQXT",	"TXQ",	"TX",	"TQXTR", "XTQXT",	"TXTXQ", "TXQXQ","XQ", "QX", "TQT", "TQV", "TQR", "TQE", "TQU","VQT", "RQT", "EQT", "UQT"};
	    //String [] row_freq={"id","XT","XUX","TRX","XUXT","XTRX","RXTX","TRXT","XTXT","RXTXT","TXTRX","XTRXT","XTXTR","TRXTX"};
	    
	    //String [] row_freq={"id","EQE", "RQR",	"TQT",	"UQU", "VQV","EQR","EQT",	"EQU",	"EQV",
		//"RQE","RQT",	"RQU",	"RQV",
		//"TQE","TQR",	"TQU",	"TQV",
		//"UQE","UQR",	"UQT",	"UQV",
		//"VQE","VQR",	"VQT",	"VQU",
		//"XQX","XQ","QX"};
		//Object[] arr = iTASList.toArray();
		String[] iTAS = iTASList.toArray(new String[iTASList.size()]);
		writer_freq.writeNext(iTAS);

		//counting all the possible patterns with length n
		int n=2;
		List<String> patterns=new ArrayList<String>();
		char[] letters=args[1].toCharArray();
		System.out.println("The number of letters: "+letters.length);
		System.out.println(args[0]+": Counting all possible patterns...");
		if(args[0].equals("replacemulti")){
			t.patternMaker_replacemulti(letters,patterns,n);
			System.out.println(patterns);
		}
		if(args[0].equals("regular")){
			t.patternMaker_regular(letters,patterns,n);
			System.out.println(patterns);
		}
		System.out.println("");

		System.out.println("Total number of patterns: "+patterns.size());
		t.countPattern(students,patterns,gap);
		//System.out.println(students.get(1).activity);
		//System.out.println(students.get(1).patternFre);
		
				
		System.out.println("Finish counting.");
		System.out.println("Filtering out the patterns which occur in less student than exist threshold "+existThreshold+" ...");
		
	
		//Neglect the pattern which too few students have
		//put the pattern frequency matrices into the class patternTTest
		//put all class patternTTest into a list Patterns
		List<patternTTest> patterns_result=new ArrayList<patternTTest>();
		t.neglectPattern(patterns,patterns_result,students,existThreshold);
		System.out.println("Finish filtering.");
		System.out.println("");
		
		
		//t test
		t.T_Test(patterns_result,students,pThreshold, goodnum, badnum);
		//CSVWriter writer = new CSVWriter(new FileWriter(output_file), ',');
		
	    //String [] row={"NGram","t statistics","p value","mean of frequency in good students","mean of frequency in bad students","absolute difference of means"};

	    //first row
	    //writer.writeNext(row);	    
	    
	    //write according to t-direction in arg[1]
	    //write 2-gram
		System.out.println("2-gram; Count of patterns that have p value less than threshold: "+patterns_result.size());
	    if(args[6].equals("pos")){
	    for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
	    	if(patterns_result.get(i).tStat<0) break;
	    	/*row[0]=patterns_result.get(i).pattern;
	    	row[1]=Double.toString(patterns_result.get(i).tStat);
	    	row[2]=Double.toString(patterns_result.get(i).p_value);
	    	row[3]=Double.toString(patterns_result.get(i).goodAve);
	    	row[4]=Double.toString(patterns_result.get(i).badAve);
	    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
	    	writer.writeNext(row);	*/
	    }
	    }
	    
	    
	    
	    if(args[6].equals("neg")){
	    	Collections.sort(patterns_result, new Comparator<patternTTest>() {
				public int compare(patternTTest pattern1, patternTTest pattern2)
				{
					return  Double.compare(pattern1.tStat,pattern2.tStat);
				}
			});
	    	
		    for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
		    	if(patterns_result.get(i).tStat>0) break;
		    	/*row[0]=patterns_result.get(i).pattern;
		    	row[1]=Double.toString(patterns_result.get(i).tStat);
		    	row[2]=Double.toString(patterns_result.get(i).p_value);
		    	row[3]=Double.toString(patterns_result.get(i).goodAve);
		    	row[4]=Double.toString(patterns_result.get(i).badAve);
		    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
		    	writer.writeNext(row);	*/
		    }
		}
	    
	    if(args[6].equals("twoside")){
	    	for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
	    		if(patterns_result.get(i).tStat<0) break;
	    		/*row[0]=patterns_result.get(i).pattern;
		    	row[1]=Double.toString(patterns_result.get(i).tStat);
		    	row[2]=Double.toString(patterns_result.get(i).p_value);
		    	row[3]=Double.toString(patterns_result.get(i).goodAve);
		    	row[4]=Double.toString(patterns_result.get(i).badAve);
		    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
		    	writer.writeNext(row);	*/
		    }
	    	Collections.sort(patterns_result, new Comparator<patternTTest>() {
				public int compare(patternTTest pattern1, patternTTest pattern2)
				{
					return  Double.compare(pattern1.tStat,pattern2.tStat);
				}
			});
	    	
		    for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
		    	if(patterns_result.get(i).tStat>0) break;
		    	/*row[0]=patterns_result.get(i).pattern;
		    	row[1]=Double.toString(patterns_result.get(i).tStat);
		    	row[2]=Double.toString(patterns_result.get(i).p_value);
		    	row[3]=Double.toString(patterns_result.get(i).goodAve);
		    	row[4]=Double.toString(patterns_result.get(i).badAve);
		    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
		    	writer.writeNext(row);	*/
		    }
		}

		
	    
		
		
		//for n>2
		while(n<5){
			n++;
			patterns=new ArrayList<String>();

			System.out.println(args[0]+": Counting all possible patterns...");
			if(args[0].equals("replacemulti")){
				t.patternMaker_replacemulti(letters,patterns,n);
				System.out.println(patterns);
			}
			if(args[0].equals("regular")){
				t.patternMaker_regular(letters,patterns,n);
				System.out.println(patterns);
			}
			System.out.println("");

			
			System.out.println("Total number of patterns: "+patterns.size());
			t.countPattern(students,patterns,gap);
			
			//System.out.println(students.get(2).activity);
			//System.out.println(students.get(2).patternFre);
			
			
			
			System.out.println("Finish counting.");
			System.out.println("Filtering out the patterns which occur in less student than exist threshold "+existThreshold+" ...");
			patterns_result=new ArrayList<patternTTest>();
			t.neglectPattern(patterns,patterns_result,students,existThreshold);
			System.out.println("Finish filtering.");
			System.out.println("");
			t.T_Test(patterns_result,students,pThreshold, goodnum, badnum);
			
			/* for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
			    	row[0]=patterns_result.get(i).pattern;
			    	row[1]=Double.toString(patterns_result.get(i).tStat);
			    	row[2]=Double.toString(patterns_result.get(i).p_value);
			    	row[3]=Double.toString(patterns_result.get(i).goodAve);
			    	row[4]=Double.toString(patterns_result.get(i).badAve);
			    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
			    	writer.writeNext(row);	
			 }
			 */
			 
			 
			 
				System.out.println(n+"gram: "+"Count of patterns that have p value less than threshold: "+patterns_result.size());
			    if(args[6].equals("pos")){
			    for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
			    	if(patterns_result.get(i).tStat<0) break;
			    	/*row[0]=patterns_result.get(i).pattern;
			    	row[1]=Double.toString(patterns_result.get(i).tStat);
			    	row[2]=Double.toString(patterns_result.get(i).p_value);
			    	row[3]=Double.toString(patterns_result.get(i).goodAve);
			    	row[4]=Double.toString(patterns_result.get(i).badAve);
			    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
			    	writer.writeNext(row);	*/
			    }
			    }
			    
			    if(args[6].equals("neg")){
			    	Collections.sort(patterns_result, new Comparator<patternTTest>() {
						public int compare(patternTTest pattern1, patternTTest pattern2)
						{
							return  Double.compare(pattern1.tStat,pattern2.tStat);
						}
					});
			    	
				    for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
				    	if(patterns_result.get(i).tStat>0) break;
				    	/*row[0]=patterns_result.get(i).pattern;
				    	row[1]=Double.toString(patterns_result.get(i).tStat);
				    	row[2]=Double.toString(patterns_result.get(i).p_value);
				    	row[3]=Double.toString(patterns_result.get(i).goodAve);
				    	row[4]=Double.toString(patterns_result.get(i).badAve);
				    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
				    	writer.writeNext(row);	*/
				    }
				}
			    
			    if(args[6].equals("twoside")){
			    	for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
			    		if(patterns_result.get(i).tStat<0) break;
			    		/*row[0]=patterns_result.get(i).pattern;
				    	row[1]=Double.toString(patterns_result.get(i).tStat);
				    	row[2]=Double.toString(patterns_result.get(i).p_value);
				    	row[3]=Double.toString(patterns_result.get(i).goodAve);
				    	row[4]=Double.toString(patterns_result.get(i).badAve);
				    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
				    	writer.writeNext(row);*/	
				    }
			    	Collections.sort(patterns_result, new Comparator<patternTTest>() {
						public int compare(patternTTest pattern1, patternTTest pattern2)
						{
							return  Double.compare(pattern1.tStat,pattern2.tStat);
						}
					});
			    	
				    for(int i=0;i<Math.min(selectTop,patterns_result.size());i++){
				    	if(patterns_result.get(i).tStat>0) break;
				    	/*row[0]=patterns_result.get(i).pattern;
				    	row[1]=Double.toString(patterns_result.get(i).tStat);
				    	row[2]=Double.toString(patterns_result.get(i).p_value);
				    	row[3]=Double.toString(patterns_result.get(i).goodAve);
				    	row[4]=Double.toString(patterns_result.get(i).badAve);
				    	row[5]=Double.toString(Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve));
				    	writer.writeNext(row);	*/
				    }
				}


			
			
		}
		//writer.close(); 
		
		// Tasmia Writing the iTAS frequency;
		for(Student stu:students){
			//System.out.println(stu.getiTASFreq());
			writer_freq.writeNext(stu.getiTASFreq());
		}
		// Tasmia
		writer_freq.close();
		
		
	}

}