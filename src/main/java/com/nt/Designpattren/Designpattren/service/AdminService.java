package com.nt.Designpattren.Designpattren.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Designpattren.Designpattren.model.Admin;
import com.nt.Designpattren.Designpattren.repo.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository repo;

    public void registerAdmin(Admin admin){
        repo.save(admin);
    }

    public Admin login(String username,String password){
        return repo.findByUsernameAndPassword(username,password);
    }
}
