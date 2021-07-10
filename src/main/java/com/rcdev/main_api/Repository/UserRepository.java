package com.rcdev.main_api.Repository;

import com.rcdev.main_api.models.User_Details;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User_Details, String> {
}
