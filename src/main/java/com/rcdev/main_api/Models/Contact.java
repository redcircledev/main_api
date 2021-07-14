package com.rcdev.main_api.Models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details about the contact")
public class Contact {

    @ApiModelProperty(notes = "Unique id of the contact")
    private String id;
    @ApiModelProperty(notes = "The person's name")
    private String name;
    @ApiModelProperty(notes = "The person's phone number")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
