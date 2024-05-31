package com.rotary.livelocation.service;

import build.buf.gen.meshtastic.Position;
import build.buf.gen.meshtastic.ServiceEnvelope;
import com.google.protobuf.InvalidProtocolBufferException;
import com.rotary.livelocation.model.jpa.Location;
import com.rotary.livelocation.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    @Autowired
    private LocationRepository locationRepository;

    private static final Double PRECISION = 10000000.0;

    public void updateLocation(ServiceEnvelope envelope) throws InvalidProtocolBufferException {
        long id = envelope.getPacket().getId();
        String from = String.valueOf(envelope.getPacket().getFrom());
        Location location = locationRepository.findByNodeF(from);
        Position position = Position.parseFrom(envelope.getPacket().getDecoded().getPayload());
        if (location == null) {
            location = new Location();
        }
        location.setNodeId(String.valueOf(id));
        location.setAltitude((long) position.getAltitude());
        location.setLongitude(position.getLongitudeI() / PRECISION);
        location.setLatitude(position.getLatitudeI() / PRECISION);
        location.setTimestamp(position.getTime());
        location.setNodeF(from);

        locationRepository.save(location);
    }


    public List<Location> getAllLocations () {
        return locationRepository.findAll();
    }


}
