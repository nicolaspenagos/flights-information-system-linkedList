package modelTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;


import model.Airport;
import model.CustomDate;
import model.CustomHour;
import model.CustomHourComparator;
import model.DestineComparator;
import model.Flight;

class AirportTest {
	
	private Airport a1;
	private Flight f1;

	//-------------------------------------
	// Scenarios
	//-------------------------------------
	
	public void SetUpScenary1() {
	
	}
	
	public void SetUpScenary2() {
		try {
			a1 = new Airport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SetUpScenary3() {
		f1 = new Flight (new CustomDate(),new CustomHour(), "AVIANCAa", "NEW YORKk", "AF45", 0); 
	}
	
	
	@Test 
	void AirportTest() {
		SetUpScenary1();
	
		String[] airlines = {"Avianca", "American Airlines", "EasyFly", "LATAM", "Satena", "Viva Air", "Wingo", "Areolíneas Argentinas", "Aereoméxico", "Air Canada", "Air Europa", "Air France", "Volaris", "Viva Colombia", "Iberia", "JetBlue", "KLM", "Lufthansa", "Spirit", "United Airlines"};
		
		String[] destines = {"BEIJING", "NEW YORK", "LIMA", "CHICAGO", "ROMA","DUBAI", "HOUSTON", "BUENOS AIRES", "MADRID", "LONDRES", "PARIS", "MOSCU", "SINGAPUR", "TORONTO", "BRUSELAS"};
		
		String[] ids = {"AV", "AA", "EF", "LA", "SA", "VA", "WI", "AG", "AM", "AC", "AE", "AF", "VO", "VC", "IB", "JB","KL","LF", "SP", "UA"};
	
		try {
			a1 = new Airport();
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			assertTrue("The ", a1.getStringHour().equals(dateFormat.format(date)));
			assertNotNull("The new Airport object is null", a1);
			for (int i = 0; i < 20; i++) {
				assertTrue("The airlines were not succesfully loaded", airlines[i].equals(a1.getAirlines()[i]));
				if(i<15) {
					assertTrue("The destines were not succesfully loaded", destines[i].equals(a1.getDestines()[i]));
				}
				assertTrue("The airlinesId were not succesfully loaded", ids[i].equals(a1.getAirlinesId()[i]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void gettersAndSettersTest() {
		SetUpScenary2();
		CustomDate datex = new CustomDate();
		String test = "x";	
		a1.setStringHour(test);
		a1.setTimeSearching(test);
		assertTrue("The hour is no working correctly", a1.getStringHour().equals(test));	
		assertTrue("The hour is no working correctly", a1.getTimeSearching().equals(test));	
	}
	
	@Test
	void UpdateTimeTest() {
		SetUpScenary2();
		a1.updateCurrentTime();
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		assertTrue("The date is not updating correctly", a1.getStringHour().equals(dateFormat.format(date)));
	}
	
	@Test
	void generateFlightsTest() {
		SetUpScenary2();
		a1.generateFlights(20);
		Flight first  = a1.getFirst();
		assertTrue("The flights array were not generate", first instanceof Flight);
		Flight current = first;
		Flight next = first.getNext();
		while(current.getNext()!=null) {
			assertTrue("The flights are not different", !(current.getFlightNumber().equals(next.getFlightNumber())));
			current=current.getNext();
			next=next.getNext();
		}
	}
	
	@Test 
	void isUnicTest() {
		SetUpScenary2(); 
		ArrayList<Integer> ints = new ArrayList<>();
		ints.add(1);
		ints.add(2);
		a1.setUS(ints);
		assertTrue("The number is have been already used", !(a1.isUnic(1)));
		assertTrue("The number is have been not used", (a1.isUnic(6)));
	}
	
	@Test
	void sortByFullHourTest() {
		SetUpScenary2(); 
		Flight current = a1.getFirst();
		a1.cocktailSortHour();
		while(current.getNext()!=null) {
			Flight next = current.getNext();
			assertTrue("The array is not ordered", new CustomHourComparator().compare(current, next)<=0);
			current=current.getNext();
		}
	}
	
	@Test
	void sortCocktailGateTest() {
		SetUpScenary2(); 
		Flight current = a1.getFirst();
		a1.cocktailSortGate();
		while(current.getNext()!=null) {
			Flight next = current.getNext();
			assertTrue("The array is not ordered", current.getGate()<=next.getGate() );
			current=current.getNext();
		}
	}
	
	@Test
	void sortCocktailDate() {
		SetUpScenary2();  
		Flight current = a1.getFirst();
		a1.cocktailSortDate();
		while(current.getNext()!=null) {
			Flight next = current.getNext();
			assertTrue("The array is not ordered", current.compareTo(next)<=0 );
			current=current.getNext();
		}
	}
	
	@Test
	void sortByAirlineSelectionTest() {
		SetUpScenary2(); 
		Flight current = a1.getFirst();
		a1.cocktailSortAirline();
	
		while(current.getNext()!=null) {
			Flight next = current.getNext();
			assertTrue("The array is not ordered", current.getAirline().compareTo(next.getAirline())<=0 );
			current=current.getNext();
		}
	}
	
	@Test
	void sortByDestineComparatorTest() {
		SetUpScenary2(); 
		Flight current = a1.getFirst();
		a1.cocktailSortDestine();
		while(current.getNext()!=null) {
			Flight next = current.getNext();
			assertTrue("The array is not ordered", (new DestineComparator().compare(current, next))<=0 );
			current=current.getNext();
		}
	}
	
	@Test
	void sortByFlightNumberTest() {
		SetUpScenary2(); 
		Flight current = a1.getFirst();
		a1.cocktailSortFlightNumber();
		while(current.getNext()!=null) {
			Flight next = current.getNext();
			assertTrue("The array is not ordered", current.getFlightNumber().compareTo(next.getFlightNumber())<0 );
			current=current.getNext();
		}
	}
	
	@Test
	void calculateTime1Test() {
		SetUpScenary2(); 
		long x = System.currentTimeMillis();
		long y = x+100;
		a1.calculateTime(x, y);
		assertTrue("The time is not good", a1.getTimeO().equals((y-x)+" ms"));
	}
	
	@Test
	void calculateTime2Test() {
		SetUpScenary2(); 
		long x = System.currentTimeMillis();
		long y = x+100;
		a1.calculateTime2(x, y, "Linear");
		assertTrue("The time is not good", a1.getTimeSearching().equals("Linear "+(y-x)+" ms"));
	}
	
	@Test 
	void printFTest() {
		SetUpScenary3(); 
		SetUpScenary2();
		Flight f2 = null;

		assertTrue("The method is not working correctly", a1.printF(f1).equals(f1.toString()));
		assertTrue("The method is not working correctly", a1.printF(f2).equals("This flight does not exist"));
	}
	
	@Test
	void linearSTests() {
		SetUpScenary2();
		SetUpScenary3();
		a1.setFirst(f1);
	
		assertTrue("The method is not working correctly", a1.searchAirlineLinearS("AVIANCAa").equals(f1.toString()));
		assertTrue("The method is not working correctly", a1.searchByTimeLinearS(f1.getHour().toString()).equals(f1.toString()));
        assertTrue("The method is not working correctly", a1.searchDestineLinearS("NEW YORKk").equals(f1.toString()));
		assertTrue("The method is not working correctly", a1.searchFlightLinearS("AF45").equals(f1.toString()));
		
	}
	
}