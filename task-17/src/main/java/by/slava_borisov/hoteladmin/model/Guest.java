package by.slava_borisov.hoteladmin.model;

import by.slava_borisov.hoteladmin.converter.PhoneNumberConverter;
import by.slava_borisov.hoteladmin.model.value.PhoneNumber;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Convert(converter = PhoneNumberConverter.class)
    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private PhoneNumber phone;

    @OneToMany(mappedBy = "guest")
    private List<Booking> bookingHistory = new ArrayList<>();
}