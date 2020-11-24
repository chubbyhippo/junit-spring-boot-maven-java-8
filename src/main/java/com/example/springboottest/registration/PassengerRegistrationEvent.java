package com.example.springboottest.registration;

import org.springframework.context.ApplicationEvent;

import com.example.springboottest.model.Passenger;

public class PassengerRegistrationEvent extends ApplicationEvent {

    private Passenger passenger;

    public PassengerRegistrationEvent(Passenger passenger) {
        super(passenger);
        this.passenger = passenger;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}
