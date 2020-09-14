package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.util.Calendar;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        // ancienne syntaxe :
        //int inHour = ticket.getInTime().getHours();
        // remplacé par ;
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(ticket.getInTime());
        //int inHour = calendar.get(Calendar.HOUR_OF_DAY);

        // ancienne syntaxe :
        //int outHour = ticket.getOutTime().getHours();
        // remplacé par ;
        //calendar.setTime(ticket.getOutTime());
        //int outHour = calendar.get(Calendar.HOUR_OF_DAY);

        // duration : temps en ms
        Duration duration = Duration.between(ticket.getInTime().toInstant(),ticket.getOutTime().toInstant());


        //TODO: Some tests are failing here. Need to check if this logic is correct



        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration.toMinutes()/60F * Fare.CAR_RATE_PER_HOUR); //F : flottant
                break;
            }
            case BIKE: {
                ticket.setPrice(duration.toMinutes()/60F * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}