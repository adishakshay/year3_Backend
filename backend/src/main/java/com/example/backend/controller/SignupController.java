package com.example.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Signup;
import com.example.backend.service.SignupService;

@RestController
public class SignupController {

    @Autowired
    SignupService ss;

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @PostMapping("/signup/add")
    public ResponseEntity<?> post(@RequestBody Signup sp) {
    // Lowercase the email and check for existence
        String emailLower = sp.getEmail().toLowerCase();
        System.out.println("Searching for user with email: " + emailLower);

        Optional<Signup> existingUser = ss.findByEmail(emailLower);
        if (existingUser.isPresent()) {
            System.out.println("User found: " + existingUser.get().getEmail());
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }

    // If the email is not taken, create the new user
        sp.setEmail(emailLower); // Ensure the email is stored in lowercase
        Signup obj = ss.create(sp);
        return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @PostMapping("/signup/login")
    public ResponseEntity<?> login(@RequestBody Signup loginRequest) {
        Optional<Signup> existingUser = ss.findByEmail(loginRequest.getEmail().toLowerCase());

        if (existingUser.isPresent()) {
            Signup user = existingUser.get();
            if (ss.checkPassword(loginRequest.getPassword(), user.getPassword())) {

                if (!user.getRole().equalsIgnoreCase(loginRequest.getRole())) {
                    return new ResponseEntity<>("Incorrect role selected", HttpStatus.FORBIDDEN);
                }

                // Generate a unique key (e.g., UUID)
                String generatedKey = UUID.randomUUID().toString();
                
                // Return the key along with the success message
                return new ResponseEntity<>(generatedKey, HttpStatus.CREATED);

            } else {
                return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Email not found", HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @PutMapping("/settings/{email}")
    public ResponseEntity<String> updatePasswordByEmail(
        @PathVariable("email") String email, 
        @RequestBody Map<String, String> passwords) {

        Optional<Signup> userOptional = ss.findByEmail(email.toLowerCase());
        if (userOptional.isPresent()) {
            Signup user = userOptional.get();
            String currentPassword = passwords.get("currentPassword");
            String newPassword = passwords.get("newPassword");

        // Verify the current password
            if (ss.checkPassword(currentPassword, user.getPassword())) {
            // Update the user's password
                user.setPassword(newPassword);
                user.setConfirmPassword(newPassword);

            // Save the updated user back to the database
                ss.updateSignup(user.getSignupId(), user);

                return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Current password is incorrect", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @PutMapping("/adminuser/users/{email}")
    public ResponseEntity<String> updateUser(
        @PathVariable("email") String email, 
        @RequestBody Signup updatedUser) {

        Optional<Signup> userOptional = ss.findByEmail(email.toLowerCase());
        if (userOptional.isPresent()) {
            Signup user = userOptional.get();
    
        // Update user details
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setPassword(updatedUser.getPassword());
            user.setConfirmPassword(updatedUser.getConfirmPassword());
            user.setRole(updatedUser.getRole());

        // Save the updated user back to the database
            ss.updateSignup(user.getSignupId(), user);

            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @GetMapping("/signup/getByEmail/{email}")
    public ResponseEntity<Signup> getByEmail(@PathVariable("email") String email) {
        Optional<Signup> user = ss.findByEmail(email.toLowerCase());
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @GetMapping("/signup/getid/{signupId}")
    public ResponseEntity<Signup> getById(@PathVariable("signupId") int signupId) {
        try {
            Signup obj = ss.getById(signupId);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @GetMapping("/signup/getall")
    public ResponseEntity<List<Signup>> getAll() {
        try {
            List<Signup> obj = ss.getAll();
            return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @PutMapping("/adminuser/register/{signId}")
    public ResponseEntity<Signup> putMethod(@PathVariable("signId") int signId, @RequestBody Signup sp) {
        if (ss.updateSignup(signId, sp) == null) {
            return new ResponseEntity<>(sp, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @GetMapping("/adminuser/users/count")
    public ResponseEntity<Integer> getUserCount() {
        int count = ss.getAll().size(); // Assuming getAll() returns a list of users
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @CrossOrigin(origins = "https://sage-syrniki-02c3b1.netlify.app")
    @DeleteMapping("/adminuser/user/{email}")
    public ResponseEntity<Boolean> deleteMethod(@PathVariable("email") String email) {
        Optional<Signup> user = ss.findByEmail(email.toLowerCase());
        if (user.isPresent()) {
            if (ss.deleteSignup(user.get().getSignupId())) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
