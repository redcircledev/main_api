package com.rcdev.main_api.Repository;

import com.rcdev.main_api.Models.User_Details;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User_Details, String> {
    
}
