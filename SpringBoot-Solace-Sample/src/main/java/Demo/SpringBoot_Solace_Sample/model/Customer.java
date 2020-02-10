package Demo.SpringBoot_Solace_Sample.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Customer {
	
	  private String id;
	    private String firstName;
	    private String lastName;
	    @NotEmpty(message = "Query Cannot be Empty or Null")
	    @NotNull(message = "Query Cannot be Empty or Null")
	    private String query;
	    
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
}
