package com.rcdev.main_api.Models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(description = "Details about the contact")
@Entity
@Table(name = "user_details")
public class User_Details implements UserDetails {

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
    @Column(name = "usr_role")
    @ApiModelProperty(notes = "User Role")
    private String usr_role;
    @Column(name = "usr_enabled")
    @ApiModelProperty(notes = "User Enabled")
    private boolean usr_enabled;

    public User_Details() {

    }

    public User_Details(String usr_pswrd) {
        this.usr_pswrd = this.getMd5(usr_pswrd);
    }

    public User_Details(String usr_id, String usr_pswrd, String usr_mail, String usr_fst_name, String usr_lst_name, String usr_role, boolean usr_enabled, boolean isAlreadyHashed) {
        this.usr_id = usr_id;
        if(isAlreadyHashed){
            this.usr_pswrd = usr_pswrd;
        } else {
            this.usr_pswrd = this.getMd5(usr_pswrd);
        }
        this.usr_mail = usr_mail;
        this.usr_fst_name = usr_fst_name;
        this.usr_lst_name = usr_lst_name;
        this.usr_role = usr_role;
        this.usr_enabled = usr_enabled;
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

    public String getUsr_role() {
        return usr_role;
    }

    public void setUsr_role(String usr_role) {
        this.usr_role = usr_role;
    }

    public boolean isUsr_enabled() {
        return usr_enabled;
    }

    public void setUsr_enabled(boolean usr_enabled) {
        this.usr_enabled = usr_enabled;
    }

    public String getMd5(String usr_pswrd) {
        try {
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

        } catch (Error | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities;
        authorities = Arrays.stream(this.getUsr_role().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.usr_pswrd;
    }

    @Override
    public String getUsername() {
        return this.usr_id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.usr_enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.usr_enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.usr_enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.usr_enabled;
    }
}
