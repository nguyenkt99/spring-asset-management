package com.nashtech.AssetManagement_backend.controller;

import com.nashtech.AssetManagement_backend.dto.AssetDTO;

import com.nashtech.AssetManagement_backend.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AssetDTO> create(@Valid @RequestBody AssetDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok().body(assetService.create(dto, request.getAttribute("userName").toString()));
    }
    @GetMapping("/{assetCode}/delete")
    public ResponseEntity<Boolean> canDelete(@PathVariable("assetCode") String assetCode) {
        return ResponseEntity.ok().body(assetService.canDelete(assetCode));
    }

    @DeleteMapping("/{assetCode}")
    public ResponseEntity<Boolean> delete(@PathVariable("assetCode") String assetCode) {
        return ResponseEntity.ok().body(assetService.delete(assetCode));
    }

    @GetMapping("")
    public ResponseEntity<List<AssetDTO>> getAll(){
        List<AssetDTO> assetDTOs = assetService.findAll();
        if (assetDTOs.isEmpty()){
            return new ResponseEntity<>(assetDTOs, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assetDTOs, HttpStatus.OK);
    }

    @GetMapping("/{assetCode}")
    public ResponseEntity<AssetDTO> getAssetByAssetCode(@PathVariable("assetCode") String assetCode){
        AssetDTO assetDTOs = assetService.findbyId(assetCode);
        return new ResponseEntity<>(assetDTOs, HttpStatus.OK);
    }

    @PutMapping("/{assetCode}")
    public ResponseEntity<AssetDTO> update(@Valid @RequestBody AssetDTO dto, @PathVariable("assetCode") String assetCode) {
        dto.setAssetCode(assetCode);
        return ResponseEntity.ok().body(assetService.update(dto));
    }

}

