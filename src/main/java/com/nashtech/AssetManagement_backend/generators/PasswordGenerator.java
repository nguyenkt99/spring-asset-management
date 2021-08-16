package com.nashtech.AssetManagement_backend.generators;

import com.nashtech.AssetManagement_backend.entity.UsersEntity;
import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import java.text.SimpleDateFormat;


public class PasswordGenerator implements ValueGenerator<String> {


    @Override
    public String generateValue(Session session, Object o) {


        PasswordEncoder encoder = new BCryptPasswordEncoder();


        StringBuilder initials = new StringBuilder();
        for (String s : ((UsersEntity) o).getLastName().split(" ")) {
            initials.append(s.charAt(0));
        }


        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String dateOfBirth = formatter.format(((UsersEntity) o).getDateOfBirth());


        String username = ((UsersEntity) o).getFirstName().toLowerCase() + initials.toString().toLowerCase();


        Query query = session.createQuery("from UsersEntity where userName like :name order by createdDate DESC")
                .setParameter("name", "%" + username + "%").setFlushMode(FlushModeType.COMMIT);

        int count = query.getResultList().size();
        if (count > 0) {
            String abc = ((UsersEntity) query.getResultList().get(0)).getUserName()
                    .replace(username, "");
            if (abc.length() == 0)
                username += 1;
            else {
                int n = Integer.parseInt(abc) + 1;
                username += n;
            }
        }
//        if(count ==1){
//            String exitsUsername = ((UsersEntity)query.getResultList().get(0)).getUserName();
//            if(exitsUsername.replaceAll("[^0-9]", "") != ""){
//                Integer index = Integer.parseInt(exitsUsername.replaceAll("[^0-9]", "")) +1 ;
//                username = username + index;
//            }else{
//                username = username + 1; // Nguyen Kieu Trung => nguyenkt -> nguyenkt1
//            }
//        }
//
//        if(count >1){
//            String exitsUsername = ((UsersEntity)query.getResultList().get(0)).getUserName();
//            Integer index = Integer.parseInt(exitsUsername.replaceAll("[^0-9]", "")) +1 ;
//            System.out.println(index);
//
//            username = username + index;
//        }


        return encoder.encode(username + "@" + dateOfBirth);
    }
}
