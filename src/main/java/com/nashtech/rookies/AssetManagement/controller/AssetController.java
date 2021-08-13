package com.nashtech.rookies.AssetManagement.controller;

import com.nashtech.rookies.AssetManagement.model.dto.AdminAssetListDTO;
import com.nashtech.rookies.AssetManagement.model.dto.CreateAssetDTO;
import com.nashtech.rookies.AssetManagement.model.dto.CreateAssetRequest;
import com.nashtech.rookies.AssetManagement.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewAsset(@Valid @RequestBody CreateAssetRequest request){
        return assetService.createAsset(request);
    }
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{assetId}")
    public ResponseEntity<?> delete(@PathVariable String assetId) {
        return assetService.delete(assetId.toUpperCase());
    }

//    @GetMapping("")
//    public ResponseEntity<?> getAllAssetByLocation(@Valid @RequestParam(name = "pageNo") int pageNo,
//                                                   @Valid @RequestParam(name = "pageSize") int pageSize,
//                                                   @RequestParam(name = "location") String location){
//        return assetService.getAllAssetByAdminLocation(pageNo, pageSize, location);
//
//    }

    @GetMapping("")
    public ResponseEntity<?> getAllAsset(@RequestParam(required = false) String location ,@RequestParam(required = false) String searchValue,@RequestParam(required = false) Set<Long> cates ,@RequestParam(required = false) Set<Long> states){
        if(cates!=null&&states!=null)
            return assetService.findAllByStateAndCate(cates,states,location);
         else if(cates!=null)
            return assetService.findAllByCate(cates,location);
         else if(states!=null)
            return assetService.findAllByState(states, location);
        if(searchValue != null){
            return assetService.searchAsset(searchValue);
        }
        return assetService.getAllByLocation(location);
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<?> getAssetDetailById(@PathVariable String assetId){
        return assetService.getAssetById(assetId);
    }


}
