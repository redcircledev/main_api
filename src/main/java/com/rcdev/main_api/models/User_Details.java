package com.rcdev.main_api.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ApiModel(description = "Details about the contact")
@Entity
@Table(name = "user_details")
public class User_Details {

    @Id
    @ApiModelProperty(notes = "Unique id of the user")
    private String usr_id;
    @Column(name = "usr_pswrd")
    @ApiModelProperty(notes = "Hashed User Password")
    private String usr_pswrd;
    @Column(name = "usr_mail")
    @ApiModelProperty(notes = "User mail")
    private String usr_mail;
    @Column(name = "usr_fst_name")
    @ApiModelProperty(notes = "User First Name")
    private String usr_fst_name;
    @Column(name = "usr_lst_name")
    @ApiModelProperty(notes = "User Last Name")
    private String usr_lst_name;

    protected User_Details(){

    }

    public User_Details(String usr_pswrd){
        this.usr_pswrd = this.getMd5(usr_pswrd);
    }

    public User_Details(String usr_id, String usr_pswrd, String usr_mail, String usr_fst_name, String usr_lst_name){
        this.usr_id = usr_id;
        this.usr_pswrd = this.getMd5(usr_pswrd);
        this.usr_mail = usr_mail;
        this.usr_fst_name = usr_fst_name;
        this.usr_lst_name = usr_lst_name;
    }

    public String getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(String usr_id) {
        this.usr_id = usr_id;
    }

    public String getUsr_mail() {
        return usr_mail;
    }

    public void setUsr_mail(String usr_mail) {
        this.usr_mail = usr_mail;
    }

    public String getUsr_fst_name() {
        return usr_fst_name;
    }

    public void setUsr_fst_name(String usr_fst_name) {
        this.usr_fst_name = usr_fst_name;
    }

    public String getUsr_lst_name() {
        return usr_lst_name;
    }

    public void setUsr_lst_name(String usr_lst_name) {
        this.usr_lst_name = usr_lst_name;
    }

    public String getUsr_pswrd() {
        return usr_pswrd;
    }

    public String getMd5(String usr_pswrd) {
        try{
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(usr_pswrd.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();

        }catch (Error | NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return null;
    }
}
