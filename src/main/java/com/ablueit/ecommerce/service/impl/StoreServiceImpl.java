//package com.ablueit.ecommerce.service.impl;
//
//import com.ablueit.ecommerce.exception.InvalidDataException;
//import com.ablueit.ecommerce.repository.UserRepository;
//import com.ablueit.ecommerce.service.StoreService;
//import com.ablueit.ecommerce.model.Store;
//import com.ablueit.ecommerce.model.User;
//import com.ablueit.ecommerce.repository.StoreRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.ui.Model;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.List;
//
//
//@Service
//@RequiredArgsConstructor
//@Slf4j(topic = "STORE-SERVICE")
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class StoreServiceImpl implements StoreService {
//
//    StoreRepository storeRepository;
//    UserRepository userRepository;
//
//    @Override
//    public List<Store> getStoresByUser(User user) {
//        return storeRepository.findStoresByUserId(user.getId());
//    }
//
//    @Override
//    public String add(Store store, Model model) {
//        log.info("add={}", store.toString());
//
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            String currentUserName = authentication.getName();
//            User currentUser = userRepository.findByUsername(currentUserName)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//            if (storeRepository.existsByEmail(store.getEmail())) {
//                log.error("Email existed");
//                throw new InvalidDataException("Email existed");
//            }
//
//            if (storeRepository.existsByPhone(store.getPhone())) {
//                log.error("Phone existed");
//                throw new InvalidDataException("Phone existed");
//            }
//
//            if (currentUser.getAuthorities().stream().anyMatch(x -> x.toString().equals("ROLE_SELLER"))) {
//                store.setSeller(currentUser);
//            }
//
//            store.setCreatedBy(currentUser);
//
//            log.info("save store to db");
//            storeRepository.save(store);
//
//            model.addAttribute("successMessage", "Cửa hàng đã được tạo thành công!");
//
//        } catch (UsernameNotFoundException | InvalidDataException exception) {
//            log.error(exception.getMessage());
//            model.addAttribute("errorMessage", exception.getMessage());
//        }
//
//        return "store-dashboard/create-store";
//    }
//
//
//    @Override
//    public ModelAndView getDetail(Long id, ModelAndView modelAndView) {
//        log.info("getDetail={}", id);
//
//        storeRepository.findById(id).ifPresentOrElse(
//                store -> modelAndView.addObject("store", store), // if store present
//                () -> modelAndView.setViewName("error")
//        );
//
//        return modelAndView;
//    }
//
//
//}
