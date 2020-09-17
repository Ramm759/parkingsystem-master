package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.util.Calendar;

public class FareCalculatorService {

    // STORY#2
    public void calculateFare(Ticket ticket, double discount){
        calculateFare(ticket);
        ticket.setPrice(ticket.getPrice() - (ticket.getPrice() * discount / 100F)) ;
        //ticket.setPrice(ticket.getPrice() * (1 - discount / 100F)) ;
    }

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        // TODO#1 : Correction du code pour passer les tests

        // duration : temps en ms
        Duration duration = Duration.between(ticket.getInTime().toInstant(), ticket.getOutTime().toInstant());


        //TODO: Some tests are failing here. Need to check if this logic is correct

        // STORY#1 : 30 minutes gratuites
        if (duration.toMinutes() <= 30)
            ticket.setPrice(0.0);

        else {
           switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration.toMinutes() / 60F * Fare.CAR_RATE_PER_HOUR); //F : flottant
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration.toMinutes() / 60F * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
    }
}