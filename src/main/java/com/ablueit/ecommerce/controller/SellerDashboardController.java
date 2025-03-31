package com.ablueit.ecommerce.controller;


import com.ablueit.ecommerce.model.Role;
import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.model.User;
import com.ablueit.ecommerce.model.demo.AttributeEntity;
import com.ablueit.ecommerce.model.demo.AttributeTermEntity;
import com.ablueit.ecommerce.model.demo.ProductEntity;
import com.ablueit.ecommerce.model.demo.VariationEntity;
import com.ablueit.ecommerce.repository.RoleRepository;
import com.ablueit.ecommerce.repository.StoreRepository;
import com.ablueit.ecommerce.repository.UserRepository;
import com.ablueit.ecommerce.repository.demo.AttributeEntityRepository;
import com.ablueit.ecommerce.repository.demo.AttributeTermEntityRepository;
import com.ablueit.ecommerce.repository.demo.ProductEntityRepository;
import com.ablueit.ecommerce.repository.demo.VariationEntityRepository;
import com.ablueit.ecommerce.service.SellerService;
import java.security.Principal;
import java.util.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
@PreAuthorize("hasRole('ROLE_SELLER')")
@Slf4j(topic = "SELLER-DASHBOARD-CONTROLLER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerDashboardController {

    StoreRepository storeRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserRepository userService;
    SellerService sellerService;

    @GetMapping("/dashboard")
    public String sellerDashboard(Model model) {

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<User> sellers = userService.findUsersCreatedBySeller(username)
                .stream().filter(User::isEnabled).toList(); // Lấy danh sách nhân viên (role SELLER)
        User seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        List<Store> websites = storeRepository.findStoresByUserId(seller.getId());

        List<Store> websitesEnabled = websites.stream().filter(Store::getEnabled).toList();

        model.addAttribute("role", "Seller");
        model.addAttribute("sellers", sellers);
        model.addAttribute("websites", websitesEnabled);

        return "seller-dashboard/seller";
    }


    @GetMapping("/create-user")
    public ModelAndView showCreateSellerForm() {
        ModelAndView modelAndView = new ModelAndView("seller-dashboard/create-user");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/create-user")
    public ModelAndView createSeller(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView("seller-dashboard/create-user");

        if (userRepository.existsByUsername(user.getUsername())) {
            modelAndView.addObject("errorMessage", "Tài khoản đã tồn tại!");
            return modelAndView;
        }

        // Tạo và gán secret_key

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User createby = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));


        // Tạo mới Seller
        User seller = new User();
        seller.setUsername(user.getUsername());
        seller.setPassword(passwordEncoder.encode(user.getPassword()));  // Mã hóa mật khẩu
        seller.setCreatedBy(createby);

        // Nếu email không được cung cấp, gán email mặc định
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            seller.setEmail(user.getUsername() + "@gmail.com");  // Gán email mặc định
        } else {
            seller.setEmail(user.getEmail());  // Gán email từ form
        }

        // Tạo Role SELLER và gán vào User
        Role sellerRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        seller.getRoles().add(sellerRole);  // Gán Role SELLER cho User

        userRepository.save(seller);  // Lưu Seller vào cơ sở dữ liệu

        modelAndView.addObject("successMessage", "Tạo tài khoản SELLER thành công!");
        return modelAndView;
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        log.info("GET /profile");

        return sellerService.getDetails(model, principal);
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User seller, Principal principal, Model model) {
        log.info("POST /update");

        return sellerService.updateProfile(seller, principal, model);
    }

    @PostMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Long id) {
        log.info("POST /delete/{}", id);

        sellerService.deleteStaff(id);

        return "redirect:/seller/dashboard";
    }

  //
  AttributeEntityRepository attributeEntityRepository;
  AttributeTermEntityRepository attributeTermEntityRepository;
  VariationEntityRepository variationEntityRepository;
  ProductEntityRepository productEntityRepository;

  @GetMapping("/demo")
  public String demo() {
    log.info("create demo data");

    // create

    AttributeEntity attributeSize = AttributeEntity.builder().name("size").slug("size").build();

    AttributeEntity attributeColor = AttributeEntity.builder().name("color").slug("color").build();

    log.info("create new attributeSize={}", attributeSize.toString());
    attributeEntityRepository.saveAll(List.of(attributeSize, attributeColor));

    AttributeTermEntity sizeX =
        AttributeTermEntity.builder().name("X").slug("x").attribute(attributeSize).build();

    AttributeTermEntity sizeY =
        AttributeTermEntity.builder().name("Y").slug("y").attribute(attributeSize).build();

    AttributeTermEntity colorRed =
        AttributeTermEntity.builder().name("red").slug("red").attribute(attributeColor).build();

    AttributeTermEntity colorBlue =
        AttributeTermEntity.builder().name("blue").slug("blue").attribute(attributeColor).build();

    log.info("save attributeSize term = {} {} ", sizeX.toString(), sizeY.toString());
    attributeTermEntityRepository.saveAll(List.of(sizeX, sizeY, colorBlue, colorRed));

    ProductEntity product =
        ProductEntity.builder()
            .name("abc")
            .sku("abbc")
            .price("123.123")
            .attributes(Set.of(attributeSize, attributeColor))
            .build();

    log.info("create product={}", product);
    productEntityRepository.save(product);

    //        log.info("create product={}", product);
    //        productEntityRepository.save(product);

    VariationEntity variation =
        VariationEntity.builder()
            .sku("cc")
            .attributes(Set.of(attributeSize, attributeColor))
            .attributeTerms(Set.of(sizeX, colorRed))
            .product(product)
            .price("123.123")
            .build();

    VariationEntity variation2 =
        VariationEntity.builder()
            .sku("cc22")
            .attributes(Set.of(attributeSize, attributeColor))
            .attributeTerms(Set.of(sizeY, colorBlue))
            .product(product)
            .price("31,31")
            .build();

    log.info("create variation={}", variation.toString());
    variationEntityRepository.saveAll(Set.of(variation, variation2));

    return "redirect:/seller/dashboard";
  }

  @GetMapping("/product/{id}")
  public String getProduct(@PathVariable Long id, Model model) {
    ProductEntity product =
        productEntityRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    Set<VariationEntity> variations = product.getVariations();

    // Lấy danh sách các attribute terms (Size, Color)
    Set<AttributeTermEntity> sizeTerms = new HashSet<>();
    Set<AttributeTermEntity> colorTerms = new HashSet<>();

    Set<AttributeEntity> attributes = product.getAttributes();

    attributes.forEach(
        x -> {
          if (Objects.equals(x.getName(), "size")) sizeTerms.addAll(x.getAttributeTerms());
          else if (Objects.equals(x.getName(), "color")) colorTerms.addAll(x.getAttributeTerms());
        });

    model.addAttribute("product", product);
    model.addAttribute("sizeTerms", sizeTerms);
    model.addAttribute("colorTerms", colorTerms);
    model.addAttribute("variations", variations);

    log.info("abc");

    return "product-dashboard/show-product";
  }
}
