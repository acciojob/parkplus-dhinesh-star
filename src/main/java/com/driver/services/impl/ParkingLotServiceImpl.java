package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot(name,address);
        ParkingLot newParkingLotAdded = parkingLotRepository1.save(parkingLot);
        return newParkingLotAdded;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository1.findById(parkingLotId);
        if(parkingLotOptional.isPresent()==false){
            return null;
        }
        ParkingLot parkingLot = parkingLotOptional.get();

        Spot spot = null;
        if(numberOfWheels==2){
            spot = new Spot(SpotType.TWO_WHEELER,pricePerHour,false);
        }
        else if(numberOfWheels==4){
            spot = new Spot(SpotType.FOUR_WHEELER,pricePerHour,false);
        }
        else if(numberOfWheels>4){
            spot = new Spot(SpotType.OTHERS,pricePerHour,false);
        }

        parkingLot.getSpotList().add(spot);
        spot.setParkingLot(parkingLot);

//        Spot newSpotAdded = spotRepository1.save(spot);
        ParkingLot newParkingLot = parkingLotRepository1.save(parkingLot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot = spotRepository1.findById(spotId).get();
        spot.setPricePerHour(pricePerHour);
        Spot newSpotAdded = spotRepository1.save(spot);
        return newSpotAdded;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
