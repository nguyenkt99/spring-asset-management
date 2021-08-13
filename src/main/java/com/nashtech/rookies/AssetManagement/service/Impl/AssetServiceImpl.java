package com.nashtech.rookies.AssetManagement.service.Impl;

import com.nashtech.rookies.AssetManagement.exception.*;
import com.nashtech.rookies.AssetManagement.exception.DateException;
import com.nashtech.rookies.AssetManagement.exception.EmptyException;
import com.nashtech.rookies.AssetManagement.exception.NotFoundException;
import com.nashtech.rookies.AssetManagement.exception.NumberErrorException;
import com.nashtech.rookies.AssetManagement.model.dto.AdminAssetListDTO;
import com.nashtech.rookies.AssetManagement.model.dto.AssetDetailDTO;
import com.nashtech.rookies.AssetManagement.model.dto.CreateAssetRequest;
import com.nashtech.rookies.AssetManagement.model.entity.Asset;
import com.nashtech.rookies.AssetManagement.model.entity.Category;
import com.nashtech.rookies.AssetManagement.model.entity.History;
import com.nashtech.rookies.AssetManagement.model.entity.State;
import com.nashtech.rookies.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.AssetManagement.repository.HistoryRepository;
import com.nashtech.rookies.AssetManagement.repository.StateRepository;
import com.nashtech.rookies.AssetManagement.service.AssetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final StateRepository stateRepository;
    private final CategoryRepository categoryRepository;
    private final AuthServiceImpl authService;
    private final ModelMapper mapper;
    private final HistoryRepository historyRepository;
    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository
            , StateRepository stateRepository
            , CategoryRepository categoryRepository
            , AuthServiceImpl authService
            , ModelMapper mapper
            , HistoryRepository historyRepository) {
        this.assetRepository = assetRepository;
        this.stateRepository = stateRepository;
        this.categoryRepository = categoryRepository;
        this.authService = authService;
        this.mapper = mapper;
        this.historyRepository = historyRepository;
    }

    @Override
    public ResponseEntity<Asset> createAsset(CreateAssetRequest request) {
        if(request.getAssetName().isEmpty()){
            throw new EmptyException("Asset name must not empty!");
        } else if(request.getCategoryId().isEmpty()){
            throw new EmptyException("Missing category field");
        } else if(request.getSpecification().isEmpty()){
            throw new EmptyException("Specification must not empty!");
        } else if(request.getStateId().isEmpty()){
            throw new EmptyException("Missing state field");
        } else if(request.getInstalledDate().isEmpty()){
            throw new EmptyException("Missing date field");
        }
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String checkDate = checkDate(request.getInstalledDate());
        if(!checkDate.equals("")){
            throw new DateException(checkDate);
        }
        Asset assetE = new Asset();

        State state = getStateByID(request.getStateId());
        Category category = getCategoryById(request.getCategoryId());
        String location = authService.getLocationAdmin(request.getAdminId());
        assetE.setCategory(category);
        assetE.setState(state);
        assetE.setLocation(location);
        assetE.setAssetName(request.getAssetName());
        assetE.setSpecification(request.getSpecification());
        try {
            assetE.setInstalledDate(format.parse(request.getInstalledDate()));
        } catch (Exception ex){
            throw new DateException("Wrong date Format");
        }

        return ResponseEntity.ok().body(assetRepository.save(assetE));

    }

    @Override
    public ResponseEntity<?> delete(String assetId) {
        Optional<Asset> entity = Optional.ofNullable(assetRepository.findById(assetId)
                .orElseThrow(() -> new NotFoundException(Asset.class, assetId)));
        List<History> historyList = historyRepository.findAllByAsset_AssetId(assetId);
        if (!historyList.isEmpty())
            throw new ForeignKeyException("Cannot delete the asset because it belongs to one or more historical assignments.\n" +
                    "If the asset is not able to be used anymore, please update its state in Edit Asset page");
        assetRepository.delete(entity.get());
        return ResponseEntity.ok().body(entity.get());
    }


//    public ResponseEntity<?> getAllAssetByAdminLocation(int pageNo, int pageSize, String location) {
//        Pageable paging = PageRequest.of(pageNo, pageSize);
//        Page<Asset> result = assetRepository.getAllByLocation(paging, location);
//        List<Asset> listAssetEntity = result.getContent();
//        List<AdminAssetListDTO> adminViewList = new ArrayList<>();
//        for (Asset assetE: listAssetEntity) {
//            AdminAssetListDTO assetDTO = mapper.map(assetE, AdminAssetListDTO.class);
//            adminViewList.add(assetDTO);
//        }
//
//        return ResponseEntity.ok(adminViewList);
//    }
//
//    public ResponseEntity<?> adminView(int pageNo,
//                                       int pageSize,
//                                       String location,
//                                       Set<String> filterByState, Set<String> filterByCate,
//                                       String sortBy, String follow){
//
//
//    }

    @Override
    public ResponseEntity<?> getAllByLocation(String location) {
        List<Asset> result = assetRepository.findAllByLocation(location);
        List<AdminAssetListDTO> listDTO = new ArrayList<>();
        if(result.isEmpty()){
            return ResponseEntity.ok(listDTO);
        }
        for (Asset assetE: result) {
            AdminAssetListDTO dto = mapper.map(assetE, AdminAssetListDTO.class);
            listDTO.add(dto);
        }
        return ResponseEntity.ok(listDTO);
    }

    @Override
    public ResponseEntity<?> searchAsset(String searchValue) {
        List<Asset> result = new ArrayList<>();
        List<AdminAssetListDTO> listDTO = new ArrayList<>();
        result = assetRepository.findByAssetNameContainingIgnoreCaseOrAssetIdContainingIgnoreCase(searchValue, searchValue);

        for (Asset assetE: result) {
            AdminAssetListDTO dto = mapper.map(assetE, AdminAssetListDTO.class);
            listDTO.add(dto);
        }

        return ResponseEntity.ok(listDTO);
    }

    @Override
    public ResponseEntity<?> getAssetById(String assetId) {
       Asset asset = assetRepository.findAssetByAssetId(assetId);

       if(asset == null){
           throw new NotFoundException("Asset not found");
       }

       AssetDetailDTO dto = mapper.map(asset, AssetDetailDTO.class);
       
       return ResponseEntity.ok().body(dto);
    }

    @Override
    public ResponseEntity<?> findAllByCate(Set<Long> cates, String location) {
        List<Asset> result = assetRepository.findAllByCatesAndLocation(cates, location);
        List<AdminAssetListDTO> listDTO = new ArrayList<>();
        if(result.isEmpty()){
            return ResponseEntity.ok(listDTO);
        }
        for (Asset assetE: result) {
            AdminAssetListDTO dto = mapper.map(assetE, AdminAssetListDTO.class);
            listDTO.add(dto);
        }
        return ResponseEntity.ok(listDTO);
    }

    @Override
    public ResponseEntity<?> findAllByStateAndCate(Set<Long> cates, Set<Long> states, String location) {
        List<Asset> result = assetRepository.findAllByCatesOrByStateAndLocation(cates,states,location);
        List<AdminAssetListDTO> listDTO = new ArrayList<>();
        if(result.isEmpty()){
            return ResponseEntity.ok(listDTO);
        }
        for (Asset assetE: result) {
            AdminAssetListDTO dto = mapper.map(assetE, AdminAssetListDTO.class);
            listDTO.add(dto);
        }
        return ResponseEntity.ok(listDTO);
    }

    @Override
    public ResponseEntity<?> findAllByState(Set<Long> states, String location) {
        List<Asset> result = assetRepository.findAllByStatesAndLocation(states, location);
        List<AdminAssetListDTO> listDTO = new ArrayList<>();
        if(result.isEmpty()){
            return ResponseEntity.ok(listDTO);
        }
        for (Asset assetE: result) {
            AdminAssetListDTO dto = mapper.map(assetE, AdminAssetListDTO.class);
            listDTO.add(dto);
        }
        return ResponseEntity.ok(listDTO);
    }

    private String checkDate(String date){
        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        format.setLenient(false);
        Date installedDate = new Date();
        try{
           installedDate  = format.parse(date);
        }catch (Exception ex){
            return "Wrong date! (MM/dd/yyyy)";
        }

        long diff = TimeUnit.DAYS.convert(installedDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);
        if(diff > 0){
            return "Installed Date greater than current date";
        }
        return "";
    }

    private State getStateByID(String stateId){
        State state = new State();
        try {
            long id = Long.parseLong(stateId);
            state = stateRepository.getStateByStateId(id);
            if (state == null){
                throw new NotFoundException("No State found!!");
            }
        } catch (Exception ex){
            throw new NumberErrorException("Wrong State Id format!!");
        }

        return state;
    }

    private Category getCategoryById(String cateId){
        Category category = new Category();
        try {
            long id = Long.parseLong(cateId);
            category = categoryRepository.getCategoryByCateId(id);
            if (category == null){
                throw new NotFoundException("No category found!!");
            }
        } catch (Exception ex){
            throw new NumberErrorException("Wrong category id format!!");
        }

        return category;
    }

}
