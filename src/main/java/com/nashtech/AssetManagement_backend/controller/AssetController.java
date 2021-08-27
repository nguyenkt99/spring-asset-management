package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.dto.AssetDTO;

import com.nashtech.AssetManagement_backend.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping()
    public ResponseEntity<AssetDTO> create(@Valid @RequestBody AssetDTO dto, Authentication authentication) {
        return ResponseEntity.ok().body(assetService.create(dto, authentication.getName()));
    }
    @GetMapping("/{id}/delete")
    public ResponseEntity<Boolean> canDelete(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(assetService.canDelete(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(assetService.delete(id));
    }

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<List<AssetDTO>> getAll(Authentication authentication){
        List<AssetDTO> assetDTOs = assetService.findAllByAdminLocation(authentication.getName());
        if (assetDTOs.isEmpty()){
            return new ResponseEntity<>(assetDTOs, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assetDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getAssetByAssetCode(@PathVariable("id") String id){
        AssetDTO assetDTOs = assetService.findbyId(id);
        return new ResponseEntity<>(assetDTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> update(@Valid @RequestBody AssetDTO dto, @PathVariable("id") String id) {
        dto.setId(id);
        return ResponseEntity.ok().body(assetService.update(dto));
    }

}

