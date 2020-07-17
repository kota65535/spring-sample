package com.sre.aip.sample.core.app.repository.pet;

import com.sre.aip.sample.core.app.repository.WithBulkInsert;
import com.sre.aip.sample.core.app.repository.WithInsert;
import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends CrudRepository<PetEntity, Long>,
        WithInsert<PetEntity>, WithBulkInsert<PetEntity> {
}
