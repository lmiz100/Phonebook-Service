package io.github.lmizrahi.phonebook.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table(name = "contacts")
public class Contact {
    @Id
    private Long id;

    @Column("first_name")
    @NonNull
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("phone_number")
    @NonNull
    private String phoneNumber;

    @Column("formatted_phone")
    @NonNull
    private String formattedPhoneNumber;

    @Column("address")
    private String address;
}
