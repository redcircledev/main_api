package com.rcdev.main_api.Controller;

import com.rcdev.main_api.Models.AuthenticationRequest;
import com.rcdev.main_api.Models.AuthenticationResponse;
import com.rcdev.main_api.Service.UserService;
import com.rcdev.main_api.Models.Contact;
import com.rcdev.main_api.Models.User_Details;
import com.rcdev.main_api.Util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api")
public class ApiController {

    ConcurrentMap<String, Contact> contacts = new ConcurrentHashMap<>();
    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService user_details;
    @Autowired
    private JwtUtil jwtTokenUtil;

    @GetMapping("/ping")
    public String getPingResponse(){

        logger.info("getPingResponse service was called...");
        return "The application is running";

    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password", e);
        }
        final User_Details user_details = userService
                .loadUserByUsername(authenticationRequest.getUserName());
        final String jwt = jwtTokenUtil.generateToken(user_details);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
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
            ) @RequestParam String usr_lst_name,
            @ApiParam(
                    value = "User role",
                    required = true,
                    type = "application/json"
            ) @RequestParam String usr_role,
            @ApiParam(
                    value = "User enabled",
                    required = true,
                    type = "application/json"
            ) @RequestParam boolean usr_enabled

    ){
        logger.info("addUser service was called");
        User_Details userDetails = new User_Details(user_id, usr_pswrd, usr_mail, usr_fst_name, usr_lst_name, usr_role, usr_enabled , false);
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

    @GetMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(
            @ApiParam(
                    value = "user id also known as username",
                    required = true,
                    type = "application/json"
            ) @RequestParam String usr_id
    ){
        try{
            userService.deteleUser(usr_id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<Optional<User_Details>> getUser(
            @ApiParam(
                    value = "user id also known as username",
                    required = true,
                    type = "application/json"
            ) @RequestParam String usr_id
    ){
        Optional<User_Details> tmp;
        try{

            tmp = userService.findUser(usr_id);
            return ResponseEntity.ok().body(tmp);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
