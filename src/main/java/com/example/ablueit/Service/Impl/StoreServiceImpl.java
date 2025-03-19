package com.example.ablueit.Service.Impl;

import com.example.ablueit.Service.StoreService;
import com.example.ablueit.model.Store;
import com.example.ablueit.model.User;
import com.example.ablueit.repository.StoreRepository;
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
