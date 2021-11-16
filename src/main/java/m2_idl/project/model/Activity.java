package m2_idl.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Activity {

    @Id()
    @GeneratedValue(generator = "activity_id")
    @Column(name = "activity_id")
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

    @JsonIgnore
    @ManyToOne()
    XUser user;

    public Activity(String title,int year, Nature nature, String desc, String website,XUser xUser) {

        this.year = year;
        this.nature = nature;
        this.title = title;
        this.desc = desc;
        this.website = website;
        this.user = xUser;
    }

    public Activity(String title, int year, Nature nature, XUser xUser) {
        this.title = title;
        this.year = year;
        this.nature = nature;
        this.user = xUser;
    }


}

