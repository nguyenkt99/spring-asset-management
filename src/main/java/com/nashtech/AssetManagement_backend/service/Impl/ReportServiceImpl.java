package com.nashtech.AssetManagement_backend.service.Impl;

import com.nashtech.AssetManagement_backend.dto.ReportDTO;
import com.nashtech.AssetManagement_backend.dto.StateCounterData;
import com.nashtech.AssetManagement_backend.entity.CategoryEntity;
import com.nashtech.AssetManagement_backend.repository.AssetRepository;
import com.nashtech.AssetManagement_backend.repository.CategoryRepository;
import com.nashtech.AssetManagement_backend.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ReportServiceImpl implements ReportService {
    private final CategoryRepository categoryRepository;
    private final AssetRepository assetRepository;


    public ReportServiceImpl(CategoryRepository categoryRepository, AssetRepository assetRepository) {
        this.categoryRepository = categoryRepository;
        this.assetRepository = assetRepository;
    }
    @Override
    public ResponseEntity<?> getAll() {
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        List<ReportDTO> reportDTOList = new ArrayList<>();
        for(int i = 0; i< categoryList.size();i++){
            reportDTOList.add(getReport(categoryList.get(i)));
        }
        return ResponseEntity.ok(reportDTOList);
    }

    private ReportDTO getReport(CategoryEntity category){
        ReportDTO dto = new ReportDTO(category.getName(),
                assetRepository.countAllByCategoryEntity(category),
                0,0,0,0,0);
        List<StateCounterData> counters = assetRepository.countState(category.getId());
        if(counters.size()<=0){
            return dto;
        }
        for (int i=0;i<counters.size();i++){
            switch (counters.get(i).getState()){
                case "AVAILABLE": dto.setAvailable(counters.get(i).getCount());
                    break;
                case "NOT_AVAILABLE": dto.setNotAvailable(counters.get(i).getCount());
                    break;
                case "ASSIGNED": dto.setAssigned(counters.get(i).getCount());
                    break;
                case "WAITING_FOR_RECYCLING": dto.setWaitingForRecycle(counters.get(i).getCount());
                    break;
                case "RECYCLED": dto.setRecycled(counters.get(i).getCount());
                    break;
            }
        }
        return dto;
    }
}
