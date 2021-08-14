//package com.nashtech.AssetManagement_backend.service.Impl;
//
//import com.nashtech.AssetManagement_backend.entity.AssetEntity;
//import com.nashtech.AssetManagement_backend.entity.AssetState;
//import com.nashtech.AssetManagement_backend.entity.AssignmentEntity;
//import com.nashtech.AssetManagement_backend.entity.UsersEntity;
//import com.nashtech.AssetManagement_backend.repository.AssetRepository;
//import com.nashtech.AssetManagement_backend.repository.AssignmentRepository;
//import com.nashtech.AssetManagement_backend.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class AssignmentServiceImplTest {
//    @Mock
//    private AssignmentRepository assignmentRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AssetRepository assetRepository;
//
//    @InjectMocks
//    private AssignmentServiceImpl assignmentService;
//
//    private List<AssignmentEntity> assignments;
//
//
//    @BeforeEach
//    void setUp() {
//        AssetEntity asset1 = new AssetEntity();
//        AssetEntity asset2 = new AssetEntity();
//        UsersEntity assignTo = new UsersEntity();
//        UsersEntity assignBy = new UsersEntity();
//        AssignmentEntity assignment1 = new AssignmentEntity();
//        AssignmentEntity assignment2 = new AssignmentEntity();
//
//        asset1.setAssetCode("SP000008");
//        asset1.setState(AssetState.AVAILABLE);
//        asset2.setAssetCode("LA000002");
//        asset2.setState(AssetState.AVAILABLE);
//        assignTo.setStaffCode("SD0404");
//        assignTo.setUserName("tannv");
//        assignBy.setStaffCode("SD0404");
//        assignBy.setUserName("tannv");
//        assignment1.setId(1L);
//        assignment1.setAssetEntity(asset1);
//        assignment1.setUserAssignTo(assignTo);
//        assignment1.setUserAssignBy(assignBy);
//        assignment2.setId(2L);
//        assignment2.setAssetEntity(asset2);
//        assignment2.setUserAssignTo(assignTo);
//        assignment2.setUserAssignBy(assignBy);
//        assignments = Stream.of(assignment1, assignment2).collect(Collectors.toList());
//    }
//
//    @Test
//    void createAssignment_ReturnAssignmentDto_WhenSuccessful() {
//        Mockito.when(assignmentRepository.save(Mockito.any())).thenReturn(assignments.get(0));
//    }
//}