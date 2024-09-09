public class patternTTest {
	String pattern;
	double[] goodStudentFre;
	double[] badStudentFre;
	double goodAve;
	double badAve;
	double abs_mean_diff;
	double tStat;
	double p_value;
	
	
	
	
	
	public patternTTest(String pattern){
		this.pattern=pattern;
	}
	
	public String toString() {
		return pattern;
	}
}
