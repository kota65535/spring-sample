package com.sre.aip.sample.core.app.repository.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("pets")
public class PetEntity {
    @Id
    private Long id;

    private String name;

    private String tag;
}
