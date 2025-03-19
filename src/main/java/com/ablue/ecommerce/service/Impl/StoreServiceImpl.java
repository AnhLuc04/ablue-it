package com.ablue.ecommerce.service.Impl;

import com.ablue.ecommerce.service.StoreService;
import com.ablue.ecommerce.model.Store;
import com.ablue.ecommerce.model.User;
import com.ablue.ecommerce.repository.StoreRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public List<Store> getStoresByUser(User user) {
        return storeRepository.findStoresByUserId(user.getId());
    }

}
