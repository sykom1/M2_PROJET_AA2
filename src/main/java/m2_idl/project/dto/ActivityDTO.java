package m2_idl.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import m2_idl.project.model.Nature;

@Data
@NoArgsConstructor
public class ActivityDTO {
    Long id;
    String title;
    int year;
    Nature nature;
    String desc;
    String website;

}
