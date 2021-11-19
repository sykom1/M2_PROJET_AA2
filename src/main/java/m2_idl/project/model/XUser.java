package m2_idl.project.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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

	@Column(name = "token",unique = true)
	private String token;


/*	@CollectionTable(name="cv",//
			joinColumns = @JoinColumn(name = "email"))
	@Column(name = "cv")*/

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	List<Activity> cv = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "role", //
			joinColumns = @JoinColumn(name = "email"))
	@Column(name = "role")
	List<XUserRole> roles;

}
