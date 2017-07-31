package com.starter.springboot.rest.resources;

import com.starter.springboot.auth.AuthenticationController;
import com.starter.springboot.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers()
    {
        log.debug("CLIENT REST REQUEST!");

        User user = new User();
        user.setFirstname("Heril");
        user.setLastname("Muratovic");
        user.setEmail("hedza@gmail.com");
        user.setEnabled(true);

        List<User> users = new ArrayList<>();
        users.add(user);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
