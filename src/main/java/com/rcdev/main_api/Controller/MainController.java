package com.rcdev.main_api.Controller;

import com.rcdev.main_api.models.Contact;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api")
public class MainController {

    ConcurrentMap<String, Contact> contacts = new ConcurrentHashMap<>();
    Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String getPingResponse(){

        logger.info("getPingResponse service was called...");
        return "The application is running";

    }

    @GetMapping("/getContact_{id}")
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
            @PathVariable String id) {

        logger.info("getContact service was called...");
        return contacts.get(id);

    }

    @GetMapping("/getContacts")
    public ArrayList<Contact> getAllContacts(){

        logger.info("getAllContacts service was called...");
        return new ArrayList<Contact>(contacts.values());

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
}
