package com.nashtech.rookies.AssetManagement.service.Impl;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.nashtech.rookies.AssetManagement.constant.Gender;
import com.nashtech.rookies.AssetManagement.exception.*;
import com.nashtech.rookies.AssetManagement.model.dto.JwtResponse;
import com.nashtech.rookies.AssetManagement.model.dto.LoginRequest;
import com.nashtech.rookies.AssetManagement.model.dto.CreateUserRequest;
import com.nashtech.rookies.AssetManagement.model.entity.Account;
import com.nashtech.rookies.AssetManagement.model.entity.History;
import com.nashtech.rookies.AssetManagement.model.entity.Role;
import com.nashtech.rookies.AssetManagement.model.entity.UserDetail;
import com.nashtech.rookies.AssetManagement.repository.AccountRepository;
import com.nashtech.rookies.AssetManagement.repository.HistoryRepository;
import com.nashtech.rookies.AssetManagement.repository.UserDetailRepository;
import com.nashtech.rookies.AssetManagement.security.jwt.JwtUtils;
import com.nashtech.rookies.AssetManagement.security.service.AccountDetailImpl;
import com.nashtech.rookies.AssetManagement.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Service
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final UserDetailRepository userDetailRepository;
    private final PasswordEncoder encoder;
    private final HistoryRepository historyRepository;
    private final long ROLE_ADMIN_ID = 1;
    private final long ROLE_STAFF_ID = 2;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils,
                           AccountRepository accountRepository,
                           UserDetailRepository userDetailRepository,
                           PasswordEncoder encoder,
                           HistoryRepository historyRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.accountRepository = accountRepository;
        this.userDetailRepository = userDetailRepository;
        this.encoder = encoder;
        this.historyRepository = historyRepository;
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest request, HttpServletRequest servletRequest) throws Exception {
        checkUserExistByUsername(request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtUtils.generateJwtToken(authentication);

        AccountDetailImpl userDetails = (AccountDetailImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                                                         .map(GrantedAuthority::getAuthority)
                                                         .collect(Collectors.toList());
        Account account = accountRepository.findByUsername(request.getUsername());
        if(account == null){
            throw new NotFoundException("Username or password is incorrect. Please try again");
        }
        servletRequest.getSession().setAttribute("account", account);
        String location = getLocationAdmin(userDetails.getId().toString());
        return ResponseEntity.ok(new JwtResponse(jwt,
                                                 userDetails.getId(),
                                                 userDetails.getUsername(),
                                                 userDetails.isFirstLogin(),
                                                 location,
                                                 roles));
    }

    @Override
    public ResponseEntity<?> changePasswordOnFirstLogin(String newPassword, String userId){
        if(userId.isEmpty()){
            throw new EmptyException("UserId is missing");
        }
        long searchId = Long.parseLong(userId);
        Account account = accountRepository.getUserById(searchId);
        if(account == null){
            throw new NotFoundException("Username or password is incorrect. Please try again");
        }
        if (!account.isFirstLogin()){
            throw new UnauthorizedException("This is not your first Login!!");
        }
        if(newPassword.isEmpty()){
            throw new EmptyException("Password must not empty!!");
        }
        newPassword = encoder.encode(newPassword);
        account.setPassword(newPassword);
        account.setFirstLogin(Boolean.FALSE);
        return ResponseEntity.ok().body(accountRepository.save(account));
    }

    @Override
    public ResponseEntity<?> delete(String userId) {
        Optional<UserDetail> entity = Optional.ofNullable(userDetailRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserDetail.class, userId)));
        entity.get().setIsDisable(true);
        System.out.println(entity.get().getAccount().getAccountId());
        List<History> historyList = this.historyRepository.findAllByUserId(entity.get().getAccount().getAccountId());
        System.out.println(historyList.size());
        //System.out.println();
        if (!historyList.isEmpty())
            throw new ForeignKeyException("There are valid assignments belonging to this user. Please close all assignments before disabling user.");
        userDetailRepository.save(entity.get());
        return ResponseEntity.ok().body(entity.get());
    }

    private void checkUserExistByUsername(String username) throws Exception {
        if(!this.accountRepository.existsByUsername(username)){
            throw new NotFoundException("Username or password is incorrect. Please try again");
        }
    }

    @Override
    public ResponseEntity<?> createNew(@Valid CreateUserRequest request,String action) throws Exception {
        if(!request.getGender().equals("MALE") && !request.getGender().equals("FEMALE")){
            throw new NotSupplyException("Gender must be MALE or FEMALE!");
        }
        if(!request.getRole().equals("Admin") && !request.getRole().equals("Staff")){
           throw new NotSupplyException("Role must be Admin or Staff!");
        }
        String dateCheck = this.checkDate(request);
        if( !dateCheck.equals("ok")){
            throw new NotSupplyException(dateCheck);
        }
        accountRepository.findByAccountId(Long.parseLong(request.getAccountId()))
                .orElseThrow(()->new NotFoundException(Account.class,request.getAccountId()));
        if(action.equals("create")){
            UserDetail user = new UserDetail();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setGender(request.getGender().equals("MALE")? Gender.MALE:Gender.FEMALE);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            user.setJoinDate(format.parse(request.getJoinDate()));
            user.setDateOfBirth(format.parse(request.getDayOfBirth()));
            String username = getUsername(request.getFirstName(), request.getLastName());
            String [] dob = request.getDayOfBirth().split("/");
            String password = username + "@" + dob[1].toLowerCase() + dob[0].toLowerCase() + dob[2].toLowerCase();
            password = encoder.encode(password);
            Role role = new Role();
            role.setRoleId(request.getRole().equals("Admin")?ROLE_ADMIN_ID:ROLE_STAFF_ID);
            Account account = new Account();
            account.setRole(role);
            account.setPassword(password);
            account.setUsername(username);
            account.setFirstLogin(Boolean.TRUE);
            user.setAccount(accountRepository.save(account));
            user.setLocation(getLocationAdmin(request.getAccountId()));
            return ResponseEntity.ok(userDetailRepository.save(user));
        }else{
            return update(request);
        }
    }

    private ResponseEntity<?> update(CreateUserRequest request) throws Exception{
        Account ac = accountRepository.findByAccountId(Long.parseLong(request.getAccountId())).get();
        UserDetail user = userDetailRepository.findByAccount(ac).get();
        user.setGender(request.getGender().equals("MALE")?Gender.MALE:Gender.FEMALE);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        user.setJoinDate(format.parse(request.getJoinDate()));
        user.setDateOfBirth(format.parse(request.getDayOfBirth()));
        Role role = new Role();
        role.setRoleId(request.getRole().equals("Admin")?ROLE_ADMIN_ID:ROLE_STAFF_ID);
        ac.setRole(role);
        accountRepository.save(ac);
        return ResponseEntity.ok(userDetailRepository.save(user));
    }


    private String checkDate(CreateUserRequest request){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        format.setLenient(false);
        Date dob = new Date();
        try{
            dob = format.parse(request.getDayOfBirth());
        }catch (Exception ex){
            return "Wrong date! (MM/dd/yyyy)";
        }
        Date joinDate = new Date();
        try{
            joinDate = format.parse(request.getJoinDate());
        }catch (Exception ex){
            return "Wrong date! (MM/dd/yyyy)";
        }
            Date now = new Date();
            Period per = Period.between(parseDate(dob),parseDate(now)) ;
            if(per.getYears()<18){
                return "User is under 18. Please select a different date";
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(joinDate);
            if(calendar.get(Calendar.DAY_OF_WEEK)==7 || calendar.get(Calendar.DAY_OF_WEEK)==1){
                return "Joined date is Saturday or Sunday. Please select a different date";
            }
            long diff = TimeUnit.DAYS.convert(dob.getTime() - joinDate.getTime(),TimeUnit.MILLISECONDS);
            if(diff >= 0){
                return "Joined date is not later than Date of Birth. Please select a different date";
            }
        return "ok";
    }


    LocalDate parseDate(Date date){
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    String getUsername(String firstName, String lastName){
        String username = lastName.toLowerCase();
        String [] list = firstName.split(" ");
        for (int i=0;i<list.length;i++){
            username+=list[i].toLowerCase().charAt(0);
        }
        Random rand = new Random();
        while(true){
            String userNameTemp = username;
            userNameTemp+=Integer.toString(rand.nextInt(100));
            if(!accountRepository.existsByUsername(username)){
                username = userNameTemp;
                break;
            }
        }
        return username;
    }

    String getLocationAdmin(String accountID){
        Account account = new Account();
        account.setAccountId(Long.parseLong(accountID));
        UserDetail userDetail = userDetailRepository.findByAccount(account)
                .orElseThrow(()->new NotFoundException(UserDetail.class,accountID));

        return userDetail.getLocation();
    }

    
}
