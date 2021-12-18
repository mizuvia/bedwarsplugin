package util;

public class LastDamager {

	private String name;
	private long time;
	
	public LastDamager() {
		this.name = "";
		this.time = 0;
	}
	
	public String get() {
		return time + 10000 > System.currentTimeMillis() ? name : null;
	}
	
	public void put(String name) {
		this.name = name;
		this.time = System.currentTimeMillis();
	}
	
}
