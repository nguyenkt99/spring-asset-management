package com.nashtech.rookies.AssetManagement.service;

import com.nashtech.rookies.AssetManagement.model.entity.State;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StateService {

    ResponseEntity<?> getAllState();

}
