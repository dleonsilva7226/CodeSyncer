package com.codesyncer.backend.repository;
import com.codesyncer.backend.model.User;

//public class UserRepo {
//
//}
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;




@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query(value ="select user_id, first_name, last_name from \"User\"", nativeQuery = true)
    public Optional<List<User>> getAllUsers();
}