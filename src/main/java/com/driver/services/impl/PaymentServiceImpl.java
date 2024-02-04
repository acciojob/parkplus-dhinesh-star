package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        int pricePerHour = reservation.getSpot().getPricePerHour();
        int numberOfHours = reservation.getNumberOfHours();

        int totalPrice = pricePerHour*numberOfHours;
        if(amountSent<totalPrice){
            throw new Exception("Insufficient Amount");
        }

        PaymentMode paymentMode = PaymentMode.CASH;
        if(mode.equalsIgnoreCase("UPI")){
            paymentMode = PaymentMode.UPI;
        }
        else if(mode.equalsIgnoreCase("Card")){
            paymentMode = PaymentMode.CARD;
        }

        if(paymentMode==PaymentMode.CASH && mode.equalsIgnoreCase("Cash")==false){
            throw new Exception("Payment mode not detected");
        }

        Payment payment = new Payment(true,paymentMode);
        payment.setReservation(reservation);

        reservation.setPayment(payment);

        Payment newPaymentAdded = paymentRepository2.save(payment);

        return newPaymentAdded;
    }
}
