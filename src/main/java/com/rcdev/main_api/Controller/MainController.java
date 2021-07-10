package com.rcdev.main_api.Controller;

import com.rcdev.main_api.Service.UserService;
import com.rcdev.main_api.models.Contact;
import com.rcdev.main_api.models.User_Details;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api")
public class MainController {

    ConcurrentMap<String, Contact> contacts = new ConcurrentHashMap<>();
    Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/ping")
    public String getPingResponse(){

        logger.info("getPingResponse service was called...");
        return "The application is running";

    }

    @GetMapping("/getContact")
    @ApiOperation(
            value = "Finds a Contact using it's ID",
            notes = "Provide and id to look up specific contact from the address book",
            response = Contact.class
    )
    public Contact getContact(
            @ApiParam(
                    value = "ID value for the contact you need to retrieve",
                    required = true
            )
            @RequestParam String id) {

        logger.info("getContact service was called...");
        return contacts.get(id);

    }

    @GetMapping("/getContacts")
    public ArrayList<Contact> getAllContacts(){

        logger.info("getAllContacts service was called...");
        return new ArrayList<>(contacts.values());

    }

    @PostMapping("/addContact")
    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "Contact added successfully", response = Contact.class)
    })
    public Contact addContact(
            @ApiParam(
                    value = "Contact model that is going to be added to the Contact list",
                    required = true,
                    type = "application/json"
            )
            @RequestBody Contact contact){

        logger.info("addContact service was called...");
        contacts.put(contact.getId(), contact);
        return contact;

    }

    @PostMapping("/getMD5")
    public String getMD5(
            @ApiParam(
                   value = "String that you want to get MD5 hash from",
                    required = true,
                    type = "application/json"
            ) @RequestParam String usr_pswrd){

        logger.info("getMD5 service was called");
        User_Details userDetails = new User_Details(usr_pswrd);
        return userDetails.getUsr_pswrd();
    }

    @PostMapping("/addUser")
    public ResponseEntity<User_Details> addUser(
            @ApiParam(
                    value = "username could be an alias",
                    required = true,
                    type = "application/json"
            ) @RequestParam String user_id,
            @ApiParam(
                    value = "Password of the user",
                    required = true,
                    type = "application/json"
            ) @RequestParam String usr_pswrd,
            @ApiParam(
                    value = "Email of the user",
                    required = true,
                    type = "application/json"
            ) @RequestParam String usr_mail,
            @ApiParam(
                    value = "User first name",
                    required = true,
                    type = "application/json"
            ) @RequestParam String usr_fst_name,
            @ApiParam(
                    value = "User last name",
                    required = true,
                    type = "application/json"
            ) @RequestParam String usr_lst_name
    ){
        logger.info("addUser service was called");
        User_Details userDetails = new User_Details(user_id, usr_pswrd, usr_mail, usr_fst_name, usr_lst_name);
        User_Details tmp = userService.createUser(userDetails);
        try{
            return ResponseEntity.created(new URI("/api/" + tmp.getUsr_id())).body(tmp);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User_Details>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
