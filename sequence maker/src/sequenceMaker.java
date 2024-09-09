import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class sequenceMaker {    
	public static void main(String[] args) throws FileNotFoundException, IOException {

		//reading data
		int STUDENT_ID=0;
		int STUDENT_ACTIVITY=5;

		String input="C:\\Users\\Dan Lv\\OneDrive\\Summer Intern\\OneDrive\\SimStudent Project\\Analysis (where we analyze data)\\Study VI Tutoring Pattern (Joyce & Dan)\\Differential Sequence Pattern Mining\\EQXRTUV.csv";
		CSVReader reader = new CSVReader(new FileReader(input));
		String [] nextLine; 
		HashMap<String,String> student_activity=new HashMap<String,String>();
		nextLine = reader.readNext();
		while ((nextLine = reader.readNext()) != null) {
			char shortName='*';
			if(nextLine[STUDENT_ACTIVITY].equals("Example Solution Viewed"))
				shortName='E';
			if(nextLine[STUDENT_ACTIVITY].equals("Quiz Completed"))
				shortName='Q';
			if(nextLine[STUDENT_ACTIVITY].contains("Quiz Solution Viewed"))
				shortName='X';
			if(nextLine[STUDENT_ACTIVITY].equals("Restart Problem Button Clicked"))
				shortName='R';
			if(nextLine[STUDENT_ACTIVITY].equals("New Problem Entered"))
				shortName='T';
			if(nextLine[STUDENT_ACTIVITY].equals("Unit Overview"))
				shortName='U';
			if(nextLine[STUDENT_ACTIVITY].equals("Introduction Video"))
				shortName='V';
			
			if(student_activity.containsKey(nextLine[STUDENT_ID])){
				student_activity.put(nextLine[STUDENT_ID],student_activity.get(nextLine[STUDENT_ID])+shortName);
			}	
			else student_activity.put(nextLine[STUDENT_ID],""+shortName);
		}
		reader.close();
		//drop QQQ tail
		for(String id:student_activity.keySet()){
			int count_tail=0;
			int len=student_activity.get(id).length();
			while(student_activity.get(id).charAt(len-1-count_tail)=='Q') count_tail++;
			student_activity.put(id,student_activity.get(id).substring(0, len-count_tail));
			System.out.println(student_activity.get(id));
		}
		
		
		//writing
		String output="C:\\Users\\Dan Lv\\OneDrive\\Summer Intern\\OneDrive\\SimStudent Project\\Analysis (where we analyze data)\\Study VI Tutoring Pattern (Joyce & Dan)\\Differential Sequence Pattern Mining\\EQXRTUV_sequence.csv";
		String [] row={"ID","Sequence"};
		CSVWriter writer = new CSVWriter(new FileWriter(output), ',');
		writer.writeNext(row);
		for(String id:student_activity.keySet()){
			row[0]=id;
			row[1]=student_activity.get(id);
			writer.writeNext(row);
		}
		writer.writeNext(row);
		
	}
}

