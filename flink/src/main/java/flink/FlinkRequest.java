package flink;

import java.io.Serializable;

public class FlinkRequest{


	String name;
	int id;
	double earnings;
	String jrdn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getEarnings() {
		return earnings;
	}

	public void setEarnings(double earnings) {
		this.earnings = earnings;
	}

	public String getJrdn() {
		return jrdn;
	}

	public void setJrdn(String jrdn) {
		this.jrdn = jrdn;
	}

}
