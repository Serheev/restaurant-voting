package com.serheev.restaurant.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
public class Restaurant extends BaseEntity implements Serializable {
    @Column(name = "name")
    @Size(max = 128)
    private String name;

    @Column(name = "address")
    @Size(max = 128)
    private String address;

    public Restaurant(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
