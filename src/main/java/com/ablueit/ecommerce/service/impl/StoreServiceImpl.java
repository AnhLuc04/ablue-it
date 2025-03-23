package com.ablueit.ecommerce.service.impl;

import com.ablueit.ecommerce.exception.ResourceNotFoundException;
import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.repository.StoreRepository;
import com.ablueit.ecommerce.service.StoreService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SELLER-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoreServiceImpl implements StoreService {

    StoreRepository storeRepository;

    @Override
    public void deleteListStoreByEntity(List<Store> stores) {
        log.info("deleteListStoreByEntity");

        stores.stream().filter(Store::getEnabled).forEach(x -> x.setEnabled(false));

        log.warn("change all store status to disabled");
        storeRepository.saveAll(stores);
    }

    @Override
    public void deleteStoreById(Long id) {
        log.info("deleteStoreById={}", id);

        Store store = storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        log.warn("delete store");
        // just disable store

        store.setEnabled(false);
        storeRepository.save(store);
    }
}
