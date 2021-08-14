package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;
import com.nashtech.AssetManagement_backend.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;

    @PostMapping
    public AssignmentDTO createAssignment(@RequestBody AssignmentDTO assignmentDTO, HttpServletRequest request) {
        assignmentDTO.setAssignBy(request.getAttribute("userName").toString());
        return assignmentService.save(assignmentDTO);
    }
}
