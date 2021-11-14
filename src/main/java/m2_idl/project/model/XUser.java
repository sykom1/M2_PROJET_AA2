package m2_idl.project.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	Date birthday;

	@Column(name = "token",unique = true, nullable = false)
	private String token;


	@CollectionTable(name="cv",//
			joinColumns = @JoinColumn(name = "email"))
	@Column(name = "cv")
	@OneToMany(fetch = FetchType.LAZY)
	List<Activity> cv = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "role", //
			joinColumns = @JoinColumn(name = "email"))
	@Column(name = "role")
	List<XUserRole> roles;


	public XUser( String email, String password, String website, Date birthday) {

		this.email = email;
		this.password = password;
		this.website = website;
		this.birthday = birthday;
	}
}
