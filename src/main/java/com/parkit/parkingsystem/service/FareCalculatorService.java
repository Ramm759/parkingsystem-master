package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Calendar;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        // ancienne syntaxe :
        //int inHour = ticket.getInTime().getHours();
        // remplacé par ;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ticket.getInTime());
        int inHour = calendar.get(Calendar.HOUR_OF_DAY);

        // ancienne syntaxe :
        //int outHour = ticket.getOutTime().getHours();
        // remplacé par ;
        calendar.setTime(ticket.getOutTime());
        int outHour = calendar.get(Calendar.HOUR_OF_DAY);

        //TODO: Some tests are failing here. Need to check if this logic is correct
        int duration;
        if (outHour>inHour)
            duration = outHour - inHour;
        else
            duration = 24 - inHour + outHour; // Correction si outHour < inHour

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}