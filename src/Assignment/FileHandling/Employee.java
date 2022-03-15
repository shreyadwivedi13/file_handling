package Assignment.FileHandling;
//class to set fields.
public class Employee {

	private int id;
	private String name;
	private String gender;
	private String age;
	private String weight;

	public Employee(String name, String gender, String age, String weight, int id) {
		this.id = 0;
		this.name = "john";
		this.gender = "M";
		this.age = "0";
		this.weight = "10";
	}

	public Employee() {
		// TODO Auto-generated constructor stub
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return (id + ", " + name + " ," + gender + ", " + age + "," + weight);
	}

}
