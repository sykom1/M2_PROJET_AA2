package m2_idl.project.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    @NotNull
    String title;
    @Column(name = "year")
    @NotNull
    int year;
    @Column(name = "nature")
    @NotNull
    Nature nature;
    @Column(name = "description")
    String desc;
    @Column(name = "web_site")
    String website;

    public Activity(String title,int year, Nature nature, String desc, String website) {

        this.year = year;
        this.nature = nature;
        this.title = title;
        this.desc = desc;
        this.website = website;
    }

    public Activity(String title, int year, Nature nature) {
        this.title = title;
        this.year = year;
        this.nature = nature;
    }
}

