package com.nashtech.AssetManagement_backend.generators;


import com.nashtech.AssetManagement_backend.entity.UsersEntity;
import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;

import javax.persistence.FlushModeType;
import javax.persistence.Query;


public class UsernameGenerator implements ValueGenerator<String> {


    @Override
    public String generateValue(Session session, Object o) {

        StringBuilder initials = new StringBuilder();
        for (String s : ((UsersEntity) o).getLastName().split(" ")) {
            initials.append(s.charAt(0));
        }

        String username = ((UsersEntity) o).getFirstName().toLowerCase() + initials.toString().toLowerCase();


        Query query = session.createQuery("from UsersEntity where userName like :name order by id DESC").setParameter("name", "%" + username + "%").setFlushMode(FlushModeType.COMMIT);


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

        return username;
    }
}
