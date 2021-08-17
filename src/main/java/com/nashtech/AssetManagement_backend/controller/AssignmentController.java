package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.dto.AssignmentDTO;
import com.nashtech.AssetManagement_backend.entity.AssignmentState;
import com.nashtech.AssetManagement_backend.entity.Location;
import com.nashtech.AssetManagement_backend.exception.BadRequestException;
import com.nashtech.AssetManagement_backend.service.AssignmentService;

import com.nashtech.AssetManagement_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/assignment")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;

    @Autowired
    private UserService userService;


    @GetMapping("")
    @ResponseBody
    public ResponseEntity<List<AssignmentDTO>> getAllByAdmimLocation(Authentication authentication) {
        List<AssignmentDTO> assignmentDTOs = assignmentService.getAllByAdmimLocation(authentication.getName());
        if (assignmentDTOs.isEmpty()) {
            return new ResponseEntity<>(assignmentDTOs, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assignmentDTOs, HttpStatus.OK);
    }

    @GetMapping("/home")
    @ResponseBody
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByUser(Authentication authentication){
        List<AssignmentDTO> assignmentDTOs = assignmentService.getAssignmentsByUser(authentication.getName());
        if (assignmentDTOs.isEmpty()){
            return new ResponseEntity<>(assignmentDTOs, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assignmentDTOs, HttpStatus.OK);
    }

    @GetMapping("/{assignmentId}")
    public AssignmentDTO getAssignmentById(@PathVariable Long assignmentId) {
        return assignmentService.getAssignmentById(assignmentId);
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


    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(HttpServletRequest request,
                                                               @PathVariable("assignmentId") Long assignmentId) {

        String userName = (String) request.getAttribute("userName");
        Location location = userService.getLocationByUserName(userName);
        assignmentService.deleteAssignment(assignmentId, location);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PutMapping("/staff/{assignmentId}")
    public ResponseEntity<AssignmentDTO> changeStateStaffAssignment(@PathVariable("assignmentId") Long assignmentId
            , @RequestParam("state") String state, HttpServletRequest request) {
        String userName = (String) request.getAttribute("userName");
        String accept = "accept", decline = "declined";
        if (state.equals(accept))
            return ResponseEntity.ok().body(assignmentService.updateStateAssignment(assignmentId, userName
                    , AssignmentState.ACCEPTED));
        else if (state.equals(decline))
            return ResponseEntity.ok().body(assignmentService.updateStateAssignment(assignmentId, userName
                    , AssignmentState.CANCELED_ASSIGN));
        else
            throw new BadRequestException("Bad Request.");
    }


}
