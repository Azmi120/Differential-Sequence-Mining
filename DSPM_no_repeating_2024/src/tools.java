/*
 *  Copyright (c), Innovative and Educational Computing Laboratory
 *  All rights reserved.
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.inference.TTest;


public class tools {

	//put all possible patterns in a list with argument "replacemulti"
	public void patternMaker_replacemulti(char[] letters,List<String> patterns,int n){
		myPatternMaker1(letters,patterns,n,0,"");

	}


	//reversion inner algorithm
	public void myPatternMaker1(char[] letters,List<String> patterns,int n,int current, String temp){
		if(current==0){
			for(int i=0;i<letters.length;i++){
				myPatternMaker1(letters,patterns,n,current+1, temp+letters[i]);
			}
		}
		else if(current<n){
			for(int i=0;i<letters.length;i++){
				if(letters[i]==temp.charAt(current-1)) continue;
				myPatternMaker1(letters,patterns,n,current+1, temp+letters[i]);
			}
		}
		else patterns.add(temp);

	}

	//put all possible patterns in a list with argument "regular"
	public void patternMaker_regular(char[] letters,List<String> patterns,int n){
		myPatternMaker2(letters,patterns,n,0,"");

	}


	//Inner reversion algorithm
	public void myPatternMaker2(char[] letters,List<String> patterns,int n,int current, String temp){
		if(current==n) patterns.add(temp);
		else{
			for(int i=0;i<letters.length;i++){
				myPatternMaker2(letters,patterns,n,current+1, temp+letters[i]);
			}
		}
	}

	//counting patterns for all student
	public void countPattern(List<Student> students, List<String> patterns,int gap){
		System.out.println("Counting "+ patterns.get(0).length() +"gram pattern frequency with gap "+gap+" for every students...");
		for(Student stu:students){
			//System.out.println(stu.activity);
			//System.out.println(stu.class1);
			//System.out.println(stu.activity);
			stu.patternFre=new HashMap<String,Integer>();
			for(int j=0;j<patterns.size();j++){
				stu.patternFre.put(patterns.get(j), 0);
				myCountingPattern(stu,patterns.get(j),gap);
			}
			//System.out.println(stu.patternFre);
		}
	}

	public void myCountingPattern(Student stu, String pattern,int gap){
		int[] flag=new int[stu.activity.length()];//if flag is 0, this letter is still available, if flag is 1, we cannot use this letter
		Arrays.fill(flag, 0);
		for(int start=0;start<stu.activity.length()-pattern.length();start++){
			int[] temp=new int[pattern.length()];
			myInnerSearch(stu,pattern,start,0,gap,flag,temp);
		}


	}

	public void myInnerSearch(Student stu,String pattern,int index_activity,int index_pattern,int gap,int[] flag, int[] temp){
		if(index_pattern==pattern.length()){
			stu.patternFre.put(pattern, stu.patternFre.get(pattern)+1);
			for(int k=0;k<temp.length;k++)
				flag[temp[k]]=1;
		}
		else if(index_activity<stu.activity.length() && stu.activity.charAt(index_activity)==pattern.charAt(index_pattern)){
			for(int currentgap=0;currentgap<=gap;currentgap++)
				if(index_activity+currentgap+1<flag.length&& flag[index_activity+currentgap+1]==0&& flag[index_activity]==0){
					temp[index_pattern]=index_activity;
					myInnerSearch(stu,pattern,index_activity+1+currentgap,index_pattern+1,gap,flag,temp);

				}
							}
	}
	public void neglectPattern(List<String> patterns,List<patternTTest> patterns_result,List<Student> students,int existThreshold){
		for(int i=0;i<patterns.size();i++){
			String temppattern=patterns.get(i);
			int exist=0;
			for(int j=0;j<students.size();j++){
				if(students.get(j).patternFre.get(temppattern)>0) exist++;
			}
			if(exist>existThreshold){
				patternTTest newpattern_result=new patternTTest(temppattern);
				patterns_result.add(newpattern_result);
				System.out.print(temppattern+", ");
			}
		}

		System.out.println("\nThe total qualified pattern number: " + patterns_result.size());
	}
	public void T_Test(List<patternTTest> patterns_result,List<Student> students,double pThreshold){
		System.out.println("Couducting t-test for each patterns...");

		TTest tt=new TTest();
		for(int i=0;i<patterns_result.size();i++){

			int goodnum=86,badnum=86;

			patterns_result.get(i).goodStudentFre=new double[goodnum];
			patterns_result.get(i).badStudentFre=new double[badnum];
			patterns_result.get(i).goodAve=0;
			patterns_result.get(i).badAve=0;
			int goodp=0,badp=0;
			for(int j=0;j<students.size();j++){
				if(students.get(j).class1.equals("high-gainer")){
					patterns_result.get(i).goodStudentFre[goodp]=students.get(j).patternFre.get(patterns_result.get(i).pattern);
					patterns_result.get(i).goodAve+=patterns_result.get(i).goodStudentFre[goodp];
					goodp++;
				}
				if(students.get(j).class1.equals("low-gainer"
						+ ""
						+ "")){
					patterns_result.get(i).badStudentFre[badp]=students.get(j).patternFre.get(patterns_result.get(i).pattern);
					patterns_result.get(i).badAve+=patterns_result.get(i).badStudentFre[badp];
					badp++;
				}
			}
			patterns_result.get(i).goodAve/=goodnum;
			patterns_result.get(i).badAve/=badnum;
			patterns_result.get(i).abs_mean_diff=Math.abs(patterns_result.get(i).goodAve-patterns_result.get(i).badAve);
			patterns_result.get(i).tStat=tt.t(patterns_result.get(i).goodStudentFre, patterns_result.get(i).badStudentFre);
			patterns_result.get(i).p_value=tt.tTest(patterns_result.get(i).goodStudentFre, patterns_result.get(i).badStudentFre);
			//System.out.println("p value:" +patterns_result.get(i).p_value);

		}
		System.out.println("t-test is completed.");

		System.out.println("Sorting patterns based on t-value...");
		Collections.sort(patterns_result, new Comparator<patternTTest>() {
			public int compare(patternTTest pattern1, patternTTest pattern2)
			{
				return  Double.compare(pattern2.tStat,pattern1.tStat);
			}
		});
		System.out.println("Sorting is completed.\n");
		System.out.println("Drop the patterns whose p-value is greater than "+pThreshold+"...");
		for(int i=0;i<patterns_result.size();i++){
			if(patterns_result.get(i).p_value<pThreshold)
				continue;
			else
				patterns_result.remove(i--);
		}
		/*
		System.out.println("Printing the top "+selecttop+" patterns based on tTest...");
		System.out.println("Pattern tStat p-value goodAve badAve");
		for(int i=0;i<Math.min(selecttop,patterns_result.size());i++){
			System.out.println(patterns_result.get(i).pattern+" "+patterns_result.get(i).tStat+" "+patterns_result.get(i).p_value+" "+patterns_result.get(i).goodAve+" "+patterns_result.get(i).badAve );
		}
		*/
	}

}
