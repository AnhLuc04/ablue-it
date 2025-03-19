package com.example.ablueit.Service;


import com.example.ablueit.model.Store;
import com.example.ablueit.model.User;

import java.util.List;

public interface StoreService {
    List<Store> getStoresByUser(User user);
}
