package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Optional<ParkingLot> parkingLotOptional = parkingLotRepository3.findById(parkingLotId);
        if(parkingLotOptional.isPresent()==false){
            throw new Exception("Reservation cannot be made");
        }
        ParkingLot parkingLot = parkingLotOptional.get();

        Optional<User> userOptional = userRepository3.findById(userId);
        if(userOptional.isPresent()==false){
            throw new Exception("Reservation cannot be made");
        }
        User user = userOptional.get();

        Spot spotToBeBooked = null;
        List<Spot> spotList = parkingLot.getSpotList();
        for(Spot spot:spotList){
            int noOfWheels = 2;
            if(spot.getSpotType()==SpotType.FOUR_WHEELER){
                noOfWheels = 4;
            }
            if(spot.getSpotType()==SpotType.OTHERS){
                noOfWheels = numberOfWheels;
            }

            if(numberOfWheels>=noOfWheels){
                if(spotToBeBooked==null){
                    spotToBeBooked=spot;
                }
                else if(spotToBeBooked.getPricePerHour()>spot.getPricePerHour()){
                    spotToBeBooked=spot;
                }
            }
        }

        if(spotToBeBooked==null){
            throw new Exception("Reservation cannot be made");
        }

        Reservation reservation = new Reservation(timeInHours);

        reservation.setSpot(spotToBeBooked);
        reservation.setUser(user);

        user.getReservationList().add(reservation);

        spotToBeBooked.getReservationList().add(reservation);
//        spotToBeBooked.setOccupied(true);

        Reservation newReservationAdded = reservationRepository3.save(reservation);

        return newReservationAdded;
    }
}
