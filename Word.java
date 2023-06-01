
public class Word {
	private String a;
	private String data;
	public Word(String a,String data) {
		this.a=a;
		this.data=data;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
		
	}
	public String toString() {
		return "("+this.a+" , "+this.data+")";
	}

}
