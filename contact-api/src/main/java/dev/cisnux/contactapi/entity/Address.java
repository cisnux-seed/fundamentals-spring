package dev.cisnux.contactapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "addresses")
@NoArgsConstructor
public class Address {
    @Id
    private String id;

    private String street;
    private String city;

    private String country;
    @Column(name = "postal_code")
    private String postalCode;
    private String province;

    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;
}