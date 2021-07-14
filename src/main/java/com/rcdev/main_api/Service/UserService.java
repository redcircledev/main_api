package com.rcdev.main_api.Service;

import com.rcdev.main_api.Config.SecurityConfiguration;
import com.rcdev.main_api.Repository.UserRepository;
import com.rcdev.main_api.Models.User_Details;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User_Details createUser(User_Details user_details) {
        return userRepository.save(user_details);
    }

    public List<User_Details> getAllUsers() {
        return userRepository.findAll();
    }

    public void deteleUser(String user_id) {
        userRepository.deleteById(user_id);
    }

    public Optional<User_Details> findUser(String usr_id) {
        return userRepository.findById(usr_id);
    }

    @Override
    public User_Details loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<User_Details> tmp_user = this.findUser(userName);
        User_Details user = new User_Details(tmp_user.get().getUsr_id(), tmp_user.get().getUsr_pswrd(), tmp_user.get().getUsr_mail(), tmp_user.get().getUsr_fst_name(), tmp_user.get().getUsr_lst_name(), tmp_user.get().getUsr_role(), tmp_user.get().isEnabled(), true);

        logger.info("User Password Retreived: " + user.getUsr_pswrd());

        return user;
    }
}
