

import java.util.HashMap;

public class Student {

	//initialize
		String id;
		String activity;
		String condition;
		String class1;
		double norm_gain;
		HashMap<String,Integer> patternFre;
		// Tasmia: the next two variables are introduced to output the previous iTAS frequency for study VII & VIII
		String[] iTAS_freq;
		int has_init = 0;

		//constructor

		public Student(String ID,String Activity,String Condition,String Class1,double gain){
			//initialize
			id=ID;
			activity=Activity;
			condition=Condition;
			class1=Class1;
			norm_gain=gain;
			patternFre=new HashMap<String,Integer>();
		}
		
		// Tasmia:
		public void setiTASFreq(String[] iTAS_freq) {
			has_init=1;
			this.iTAS_freq = iTAS_freq;
		}
		// Tasmia:
		public String[] getiTASFreq() {
			return iTAS_freq;
		}

		//Drop tails of Q if count_Q > 4
		public void dropTail(){
			int count_Q=0;
			int i;
			int len=this.activity.length();
			for(char c:this.activity.toCharArray()){
				if(c=='Q')
					count_Q++;
			}
			
			if(count_Q>4){
				for(i=0;i<=count_Q-4;i++){
					if(this.activity.charAt(len-1-i)!='Q')
						break;
				}
				if(i>1)
				this.activity=this.activity.substring(0,len-i+1);
			}
			
		}
		
		public void replaceMulti(){
			//replace the multiple activity 
			int n=this.activity.length();
			String temp="";
			int count=0;
			for(int i=1;i<n;i++){
				if(this.activity.charAt(i)==this.activity.charAt(i-1))
					count++;
				else{
					if(count>0)temp+=(char)(this.activity.charAt(i-1));//(this.activity.charAt(i-1)+32)
					else temp+=this.activity.charAt(i-1);
					count=0;
				}
			}
			if(count>0) temp+=(char)(this.activity.charAt(n-1));//(this.activity.charAt(n-1)+32)
			else temp+=(char)this.activity.charAt(n-1);
				
			this.activity=temp;
		}
		
		public void replaceBoth(){
			//replace the multiple activity 
			int n=this.activity.length();
			String temp="";
			int count=0;
			for(int i=1;i<n;i++){
				if(this.activity.charAt(i)!=this.activity.charAt(i-1))
					temp+=(char)(this.activity.charAt(i-1)+32);
			}
			temp+=(char)(this.activity.charAt(n-1)+32);
			this.activity=temp;
		}


	
	
}

