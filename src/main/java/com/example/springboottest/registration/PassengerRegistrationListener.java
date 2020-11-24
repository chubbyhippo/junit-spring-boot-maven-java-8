package com.example.springboottest.registration;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PassengerRegistrationListener {

    @EventListener
    public void confirmRegistration(PassengerRegistrationEvent passengerRegistrationEvent) {
        passengerRegistrationEvent.getPassenger().setIsRegistered(true);
        System.out.println("Confirming the registration for the passenger: "
                + passengerRegistrationEvent.getPassenger());
    }
}
