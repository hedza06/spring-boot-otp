package com.starter.springboot.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunningState {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> rootPageCheck()
    {
        return new ResponseEntity<>("Application is running!", HttpStatus.OK);
    }
}
