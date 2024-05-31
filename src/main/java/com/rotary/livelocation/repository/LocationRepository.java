package com.rotary.livelocation.repository;


import com.rotary.livelocation.model.jpa.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findByNodeId(String nodeId);

    Location findByNodeF(String from);
}
