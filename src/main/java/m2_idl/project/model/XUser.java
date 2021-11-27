package m2_idl.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data // Create getters and setters
@NoArgsConstructor
public class XUser {

	@Id
	@GeneratedValue(generator = "user_id")
	@Column(name = "user_id")
	Long id;

	@Column(name = "email")
	String email;

	@Column(name = "pass_word")
	@NotNull
	String password;

	@Column(name = "firstname")
	String firstname;

	@Column(name = "lastname")
	String lastname;


	@Column(name = "web_site")
	String website;

	@Column(name = "birthday")
	@JsonFormat
			(shape = JsonFormat.Shape.STRING, pattern ="dd-mm-yyyy")
					Date birthday;

	@Column(name = "token",unique = true)
	private String token;


/*	@CollectionTable(name="cv",//
			joinColumns = @JoinColumn(name = "email"))
	@Column(name = "cv")*/

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	List<Activity> cv = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "role", //
			joinColumns = @JoinColumn(name = "email"))
	@Column(name = "role")
	List<XUserRole> roles;


	public void setCv(List<Activity> cv) {
		this.cv.clear();
		if(cv != null){
			this.cv.addAll(cv);
		}
	}
}
