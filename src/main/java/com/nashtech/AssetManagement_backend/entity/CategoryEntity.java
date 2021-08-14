package com.nashtech.AssetManagement_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="name", unique = true)
    private String name;

    @Column(name = "prefix", unique = true)
    private String prefix;

    @OneToMany(mappedBy = "categoryEntity")
    private List<AssetEntity> assetEntities = new ArrayList<>();

}
