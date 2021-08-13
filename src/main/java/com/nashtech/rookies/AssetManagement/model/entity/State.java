package com.nashtech.rookies.AssetManagement.model.entity;

import com.nashtech.rookies.AssetManagement.constant.StateName;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class State implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stateId;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private StateName stateName;

}
