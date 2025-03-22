package com.ablueit.ecommerce.service.impl;

import com.ablueit.ecommerce.model.Store;
import com.ablueit.ecommerce.repository.StoreRepository;
import com.ablueit.ecommerce.service.StoreService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SELLER-SERVICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoreServiceImpl implements StoreService {

    StoreRepository storeRepository;

    @Override
    public void deleteByEntity(Store store) {
        log.info("delete={}", store.getId());

        store.setEnabled(false);

        log.warn("change store status to disable");
        storeRepository.save(store);
    }
}
