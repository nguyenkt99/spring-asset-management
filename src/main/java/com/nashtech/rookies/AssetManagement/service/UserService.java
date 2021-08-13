package com.nashtech.rookies.AssetManagement.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
     ResponseEntity<?> getAll() throws Exception;
     ResponseEntity<?> findById(String id) throws Exception;
     ResponseEntity<?> searchByNameOrId(String searchStr) throws Exception;
}
