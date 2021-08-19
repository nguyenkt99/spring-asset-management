package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.dto.RequestDTO;
import com.nashtech.AssetManagement_backend.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/request")
public class RequestController {
    @Autowired
    RequestService requestService;

    @PostMapping
    public RequestDTO create(@Valid @RequestBody RequestDTO requestDTO, HttpServletRequest request) {
        requestDTO.setRequestBy(request.getAttribute("userName").toString());
        return requestService.create(requestDTO);
    }

    @GetMapping
    public List<RequestDTO> getAll(HttpServletRequest request) {
        return requestService.getAllByAdminLocation(request.getAttribute("userName").toString());
    }
}
