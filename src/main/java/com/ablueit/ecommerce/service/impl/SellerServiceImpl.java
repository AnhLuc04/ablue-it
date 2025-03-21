package com.ablueit.ecommerce.service.impl;

import com.ablueit.ecommerce.model.User;
import com.ablueit.ecommerce.repository.UserRepository;
import com.ablueit.ecommerce.service.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SELLER-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerServiceImpl implements SellerService {

    UserRepository userRepository;

    @Override
    public String getDetails(Model model, Principal principal) {
        log.info("getDetails");

        User seller = getUserByUserName(principal.getName());

        log.info("seller={}", seller.getEmail());

        model.addAttribute("user", seller);

        return "seller-dashboard/profile";
    }

    @Override
    public ModelAndView updateProfile(ModelAndView modelAndView, User seller, Principal principal) {
        log.info("updateProfile={}", seller.getUsername());

        User oldSeller = getUserByUserName(principal.getName());



        if(Objects.nonNull(seller.getEmail()) && userRepository.existsByEmail(seller.getEmail())){
            modelAndView.addObject("errorMessage", "Email existed");
            return modelAndView;
        }



        log.info("updated seller profile to database");
        userRepository.save(oldSeller);

        return modelAndView;
    }

    @Override
    public void deleteStaff(Long id) {
        log.info("deleteStaff={}", id);

        User staff = getUserById(id);

        staff.setEnabled(false);

        userRepository.save(staff);

    }

    User getUserByUserName(String username){
        log.info("getUserByUserName={}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    User getUserById(Long id){
        log.info("getUserById={}", id);

        return userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
