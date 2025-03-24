package com.ablueit.ecommerce.service;


import com.ablueit.ecommerce.model.Store;
import org.springframework.ui.Model;

import java.util.List;

public interface StoreService {
    void deleteListStoreByEntity(List<Store> store);

    void deleteStoreById(Long id);

    Store getStoryById(Long id);

    String updateStore(Long id, Store store, Model model);
}
