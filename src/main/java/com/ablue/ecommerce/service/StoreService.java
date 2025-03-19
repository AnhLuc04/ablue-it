package com.ablue.ecommerce.service;


import com.ablue.ecommerce.model.Store;
import com.ablue.ecommerce.model.User;

import java.util.List;

public interface StoreService {
    List<Store> getStoresByUser(User user);
}
