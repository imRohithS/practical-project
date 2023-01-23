package flink;

public class Customers {
	int id;
	String name;

	public Customers(int i, String string) {
		this.id = i;
		this.name = string;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
