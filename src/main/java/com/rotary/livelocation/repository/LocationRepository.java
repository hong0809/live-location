package com.rotary.livelocation.repository;


import com.rotary.livelocation.model.jpa.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findByNodeId(String nodeId);

    Location findyByFrom(String from);
}
