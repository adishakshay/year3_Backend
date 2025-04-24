package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// import com.example.backend.model.Login;
import com.example.backend.model.Signup;
import com.example.backend.repository.SignupRepo;

@Service
public class SignupService {
    
    @Autowired
    SignupRepo sr;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public Signup create(Signup sp)
    {
        sp.setPassword(passwordEncoder.encode(sp.getPassword()));
        return sr.save(sp);
    }

    public Optional<Signup> findByEmail(String email) {
        return sr.findByEmail(email);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Signup getById(int signupId)
    {
        return sr.findById(signupId).orElse(null);
    }

    public Boolean updateSignup(int signupId, Signup sp) {
        Optional<Signup> existingSignup = sr.findById(signupId);
        if (existingSignup.isEmpty()) {
            return false;
        }
        try {
            Signup signupToUpdate = existingSignup.get();
    
            // Update fields
            if (sp.getPassword() != null) {
                signupToUpdate.setPassword(passwordEncoder.encode(sp.getPassword()));
            }
            signupToUpdate.setFirstName(sp.getFirstName());
            signupToUpdate.setLastName(sp.getLastName());
            signupToUpdate.setEmail(sp.getEmail()); // Ensure email is retained
    
            sr.save(signupToUpdate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void updateUser(int signupId, Signup updatedUser) {
        Optional<Signup> userOptional = sr.findById(signupId);
        if (userOptional.isPresent()) {
            Signup user = userOptional.get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setConfirmPassword(updatedUser.getConfirmPassword());
            user.setRole(updatedUser.getRole());
            sr.save(user);
        }
    }
    

    public List<Signup> getAll()
    {
        return sr.findAll();
    }

    // public Boolean updateSignup(int signupId, Signup sp)
    // {
    //     if(sr.findById(signupId).isEmpty())
    //     {
    //         return false;
    //     }
    //     try
    //     {
    //         if (sp.getPassword() != null) {
    //             sp.setPassword(passwordEncoder.encode(sp.getPassword()));
    //         }
    //         sr.save(sp);
    //     }
    //     catch(Exception e)
    //     {
    //         return false;
    //     }
    //     return true;
    // }

    public Boolean deleteSignup(int signupId)
    {
        if (this.getById(signupId) == null) {
            return false;
        }
        sr.deleteById(signupId);
        return true;
    }

    public Signup authenticate(String email, String password) {
        Optional<Signup> userOptional = sr.findByEmail(email);
        if (userOptional.isPresent()) {
            Signup user = userOptional.get();
            // Verify the provided password matches the stored hashed password
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
