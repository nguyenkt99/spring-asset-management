package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;
import com.nashtech.AssetManagement_backend.service.AssignmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/assignment")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<List<AssignmentDTO>> getAllByAdmimLocation(Authentication authentication){
        List<AssignmentDTO> assignmentDTOs = assignmentService.getAllByAdmimLocation(authentication.getName());
        if (assignmentDTOs.isEmpty()){
            return new ResponseEntity<>(assignmentDTOs, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assignmentDTOs, HttpStatus.OK);
    }
    
    @PostMapping
    public AssignmentDTO createAssignment(@RequestBody AssignmentDTO assignmentDTO, HttpServletRequest request) {
        assignmentDTO.setAssignedBy(request.getAttribute("userName").toString());
        return assignmentService.save(assignmentDTO);
    }

    @PutMapping("/{assignmentId}")
    public AssignmentDTO editAssignment(@PathVariable("assignmentId") Long assignmentId, @RequestBody AssignmentDTO assignmentDTO, HttpServletRequest request) {
        assignmentDTO.setAssignedBy(request.getAttribute("userName").toString());
        assignmentDTO.setId(assignmentId);
        return assignmentService.update(assignmentDTO);
    }
}
