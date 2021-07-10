package com.rcdev.main_api.Service;

import com.rcdev.main_api.Repository.UserRepository;
import com.rcdev.main_api.models.User_Details;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User_Details createUser(User_Details user_details){
        return userRepository.save(user_details);
    }

    public List<User_Details> getAllUsers(){
        return userRepository.findAll();
    }

    public void deteleUser(User_Details user_details){
        userRepository.delete(user_details);
    }

    public Optional<User_Details> findUser(String usr_id){
        return userRepository.findById(usr_id);
    }

}
