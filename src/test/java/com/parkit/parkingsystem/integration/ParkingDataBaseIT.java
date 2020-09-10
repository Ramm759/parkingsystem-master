package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1); // 1 : voiture
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries(); // clear et reprépare Vl
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        // On récupère la prochaine place disponible
        int nextPlace = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        //vérifier qu'un ticket est effectivement enregistré dans la base de données et que la table de
        // stationnement est mise à jour avec la disponibilité

        // étape 1 : on récupère le ticket du véhicule immatriculé ABCDEF
        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        // étape 2 : on vérifie l'existance du ticket ( not Null)
        Assertions.assertNotNull(ticket);

        // étape 3 : on récupère id parking et on vérifie que ParkingSpot existe bien
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        Assertions.assertNotNull(parkingSpot);

        // étape 4 : on vérifie que la colonne avalaible = false
        Assertions.assertFalse(parkingSpot.isAvailable());

        // étape 5 : on vérifie que la place qui était disponible est bien celle utilisée
        Assertions.assertEquals(nextPlace, parkingSpot.getId());
    }

    @Test
    public void testParkingLotExit() throws InterruptedException {
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        Thread.sleep(1000); // On simule un temps entre entrée et sortie pour eviter l'exeption

        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
        //vérifier que le tarif généré et l'heure de départ sont correctement renseignés dans la base de données

        // étape 1 : on récupère le ticket du véhicule immatriculé ABCDEF
        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        // étape 2 : on vérifie l'existance du ticket ( not Null)
        Assertions.assertNotNull(ticket);

        // étape 3 : on vérifie que le ticket à bien une date de sortie (outTime)
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        Assertions.assertNotNull(ticket.getOutTime());

        // étape 4 : on vériefie si le tarif du ticket est correct
        Assertions.assertEquals( 0.0, ticket.getPrice() );
    }

}
