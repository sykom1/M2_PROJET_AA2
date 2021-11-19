package m2_idl.project.service;

import lombok.RequiredArgsConstructor;
import m2_idl.project.model.XUser;
import m2_idl.project.repository.ActivityRepository;
import m2_idl.project.repository.XUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final XUserRepository userRepository;
    private final ActivityRepository activityRepository;




}
