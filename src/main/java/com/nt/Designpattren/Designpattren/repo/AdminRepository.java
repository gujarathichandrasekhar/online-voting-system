package com.nt.Designpattren.Designpattren.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.Designpattren.Designpattren.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Long>{

    Admin findByUsernameAndPassword(String username,String password);

}
