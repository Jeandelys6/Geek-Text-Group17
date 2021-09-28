package com.example.geektext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller //class is a controller (MVC)
@RequestMapping (path = "/user")    //URL's start with "/user" after application path
public class UserController
{
    // 	The preceding example explicitly specifies POST and GET for the two endpoints.
    // 	By default, @RequestMapping maps all HTTP operations.

    @Autowired  //get the bean called TestRepository    //auto gen'd by spring, used to handle data
    private UserRepository userRepository;

    //test curl: curl localhost:8080/user/add -d userName=testUserName -d pw=pa$$w0rd -d email=email@provided.com -d fullName=FirstLast -d address=test
    //make sure to test removing optional parameters as well
    //Feature: Must be able to create a User with username(email), password and optional fields  (name, email address, home address)
    @PostMapping (path = "/addUser")    //Map *only* POST requests
    public @ResponseBody String addUser (@RequestParam String userName, @RequestParam String pw,
                                         @RequestParam(required = false) String email, @RequestParam(required = false) String fullName,
                                         @RequestParam(required = false) String address)
    {
        User user = new User();
        user.setUserName(userName);
        user.setUserPw(pw);
        if(email != null) //required = false was used for optional fields. needs to be tested for if they were put in or not
            user.setUserEmail(email);
        if(fullName != null)
            user.setUserFullName(fullName);
        if(address != null)
            user.setUserAddress(address);
        userRepository.save(user);
        return "Saved user";
    }

    //test curl: curl localhost:8080/user/add -d userName=testUserName -d fullName = NewName
    //Feature: Must be able to update the user and any of their fields except for mail
    @PutMapping(path = "/updateUser")
    public @ResponseBody String updateUser (@RequestParam String userName, @RequestParam(required = false) String pw,
                                            @RequestParam(required = false) String fullName, @RequestParam(required = false) String address)
    {
        List<User> users = userRepository.findByuserName(userName);
        User user = users.get(0); //there should only be 1 instance of that username existing, hence index 0
        if(pw != null) //need to check which optional value is to be updated
            user.setUserEmail(pw);
        if(fullName != null)
            user.setUserFullName(fullName);
        if(address != null)
            user.setUserAddress(address);
        return "Updated user";
    }

    //test curl: curl localhost:8080/user/add -d userName=testUserName
    //Feature: Must be able to retrieve a User Object and its fields by their username
    @GetMapping (path = "/findUser")
    public @ResponseBody User getUser (@RequestParam String userName)
    {
        List<User> users = userRepository.findByuserName(userName);
        return users.get(0); //should theoretically only be one user with that username
    }

    @GetMapping (path = "/allUsers")
    public @ResponseBody Iterable<User> getAllUsers ()
    {
        //returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
