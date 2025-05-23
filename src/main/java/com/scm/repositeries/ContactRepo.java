package com.scm.repositeries;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;

import java.util.List;



@Repository
public interface ContactRepo extends JpaRepository<Contact,String> {
 
    Page<Contact> findByUser(User user, Pageable pageable);
    @Query("Select c from Contact c where c.user.id=:userId")
    List<Contact> findByUserId(@Param("userId") String userId);
    
    Page<Contact> findByUserAndNameContaining(User user,String namekeyword, Pageable pageable);

    Page<Contact> findByUserAndEmailContaining(User user,String emailkeyword, Pageable pageable);

    Page<Contact> findByphonenumberContaining(User user, String phonenumberkeyword, Pageable pageable);


}
