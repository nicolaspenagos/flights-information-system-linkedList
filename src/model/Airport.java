package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;



public class Airport {
	
	public final static String HC = "HOUR - COMPARATOR"; 
	public final static String DC = "DESTINE - COMPARATOR";
	public final static String DCO ="DATE - COMPARABLE";
	public final static String FNI = "F. NUMBER. - INSERTION";
	public final static String GB = "GATE - BUBBLE";
	public final static String AS = "AIRLINE - SELECTION";
	
	//------------------------------
	// Attributes
	//------------------------------
	private Flight[] flights;
	private ArrayList<Integer> usedNumbers;
	private String stringHour;
	private String[] airlines;
	private String[] destines;
	private String[] airlinesId;
	private String typeOfOrder;
	private String timeOrdering;
	private String timeSearching;
	private int flightNumbers;
	private Flight first;
	
	//------------------------------
	// Constructor 
	//------------------------------
	public Airport() throws IOException {
		
		flightNumbers = 20;
		usedNumbers = new ArrayList<>(); 
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		stringHour = dateFormat.format(date);
		airlines = loadTextFileToArray("data/airlines.txt");
		destines = loadTextFileToArray("data/destines.txt");
		airlinesId = loadTextFileToArray("data/airlinesId.txt");
		generateFlights(20);
		typeOfOrder = "";
		timeOrdering = "";
		timeSearching = "";
	}
	
	//------------------------------
	// Getters and Setters 
	//------------------------------
	public Flight[] getFlights() {
		return flights;
	}
	
	public String[] getDestines() {
		return destines;
	}
	
	public String[] getAirlinesId() {
		return airlinesId;
	}
	
	public String[] getAirlines() {
		return airlines;
	}
	public void setFlights(Flight[] flights) {
		this.flights = flights;
	}
	
	public String getStringHour() {
		return stringHour;
	}

	public void setStringHour(String stringHour) {
		this.stringHour = stringHour;
	}
	
	public String getTimeSearching() {
		return timeSearching;
	}

	public void setTimeSearching(String timeSearching) {
		this.timeSearching = timeSearching;
	}
	
	public String getTypeO() {
		return typeOfOrder; 
	}
	
	public String getTimeO() {
		return timeOrdering; 
	}
	
	public void setUS(ArrayList<Integer> x) {
		usedNumbers = x;
	}
	//------------------------------
	// Methods
	//------------------------------
	public void updateCurrentTime() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		stringHour = dateFormat.format(date);
	}
	public String[] loadTextFileToArray(String path) throws IOException {
		
		ArrayList<String> list = new ArrayList<>();
		File f = new File(path);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		
	
		
		String line = br.readLine(); 
		while(line != null) {
			list.add(line);
			line = br.readLine();
		}
		
		String[] array = new String[list.size()]; 
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		
		return array;
	}
	
	public Flight[] generateFlights(int size) {
		int cc = 0;
		Flight actual = first;
		
		for (int i = 0; i < size; i++) {
			
			boolean finish = false;
			
			int a = (int) (Math.random() * 19);
			int d = (int) (Math.random() * 14);
			int id = (int) (Math.random() * 9999);;
			int g = (int) (Math.random() * 35)+1;
			while(!finish) {
				if(isUnic(id)) {
					finish=true;
				}else {
					id = (int) (Math.random() * 9999);
				}
			}
			
			if(first==null) {
				first = new Flight(new CustomDate(), new CustomHour(), airlines[a], destines[d], (airlinesId[a]+ id), g);
				cc++;
				first.setPrev(null);
			//	first.setNext(first);
			}else {
		
				actual = getLast();
				Flight nextFlight = new  Flight(new CustomDate(), new CustomHour(), airlines[a], destines[d], (airlinesId[a]+ id), g);
				cc++;
				actual.setNext(nextFlight);
				nextFlight.setPrev(actual);
				nextFlight.setNext(null);
				first.setPrev(null);
				if(cc==2) {
					first.setNext(nextFlight);
				}
			}
			
			
		}

		return flights;
		
	}
	
	public boolean isUnic(int x) {
		for (int i = 0; i < usedNumbers.size(); i++) {
			if(x==usedNumbers.get(i)) {
				return false;
			}
		}
		return true; 
	}
	
	public void sortByFullHour() {
		typeOfOrder = HC;
		long xTime = System.currentTimeMillis();
		Arrays.sort(flights,new CustomHourComparator());
		long yTime = System.currentTimeMillis();
		calculateTime(xTime, yTime);
	}
	
	public void sortByGateBubble() {
		typeOfOrder = GB;
		long timeX = System.currentTimeMillis();
		Flight current = first;
		int cc=0;
		while(current.getNext()!=first&&cc<21) {
			System.out.println(cc);
			Flight second = current.getNext();
			while(second.getNext()!=first&&cc<21) {
				if(current.getGate()>second.getGate()) {
					int temp=current.getGate();
					current.setGate(second.getGate());
					second.setGate(temp);
					
				}
			}
			cc++;
			System.out.println(cc);
		}
		
		long timeY = System.currentTimeMillis();
		calculateTime(timeX, timeY);
	}
	
	public void sortByDateComparable() {
		typeOfOrder = DCO;
		long timeX = System.currentTimeMillis();
		Arrays.sort(flights);
		long timeY = System.currentTimeMillis();
		calculateTime(timeX, timeY);
	}
	
	
	public void calculateTime(long x, long y) {
		long timeT = y-x;
		timeOrdering = timeT + " ms";
	}
	
	public void calculateTime2(long x, long y, String xx) {
		long timeT = y-x;
		setTimeSearching(xx+" "+timeT + " ms");
	}
	
	public void sortByAirlineSelection() {
		typeOfOrder = AS;
		long timeX=System.currentTimeMillis();
		for (int i = 0; i<flights.length; i++) {
			int pos=i;
			String minS= flights[i].getAirline();
			Flight min = flights[i];
			for (int j = i; j < airlines.length; j++) {
				if(minS.compareToIgnoreCase(flights[j].getAirline())>0) {
					min=flights[j];
					minS= flights[j].getAirline();
					pos = j;
				}
			}
			Flight temp = flights[i];
			flights[i]  = min;
			flights[pos]=temp;
		}
		long timeY=System.currentTimeMillis();
		calculateTime(timeX, timeY);
	}
	
	public void sortByDestineComparator() {
		typeOfOrder = DC; 
		long timeX = System.currentTimeMillis();
		Arrays.sort(flights,new DestineComparator());
		long timeY = System.currentTimeMillis();
		calculateTime(timeX, timeY);
	}
	
	public String searchByTimeLinearS(String time) {
		Flight fx = null; 
		long timeX = System.currentTimeMillis();
		Flight actual = first;
		
		while(actual.getNext()!=first&&fx==null) {
			if(actual.getHour().toString().equalsIgnoreCase(time)) {
				fx=actual; 
			}
			actual=actual.getNext();
		}
		
		if(getLast().getHour().toString().equalsIgnoreCase(time)) {
			fx=getLast();
		}
		long timeY = System.currentTimeMillis();
		calculateTime2(timeX, timeY, "(linear)");
		return printF(fx);
	}
	
	public String searchFlightLinearS(String fn) {
		Flight fx = null; 
		long timeX = System.currentTimeMillis();
		Flight actual = first;
		
		while(actual.getNext()!=first&&fx==null) {
			if(actual.getFlightNumber().toString().equalsIgnoreCase(fn)) {
				fx=actual; 
			}
			actual=actual.getNext();
		}
		
		if(getLast().getFlightNumber().toString().equalsIgnoreCase(fn)) {
			fx=getLast();
		}
		
		long timeY = System.currentTimeMillis();
		calculateTime2(timeX, timeY, "(linear)");
		return printF(fx);
	}
	
	public String searchDateLinearS(String fn) {
		Flight fx = null; 
		long timeX = System.currentTimeMillis();
		Flight actual = first;
		
		while(actual.getNext()!=first&&fx==null) {
			if(actual.getDate().toString().equalsIgnoreCase(fn)) {
				fx=actual; 
			}
			actual=actual.getNext();
		}
		if(getLast().getDate().toString().equalsIgnoreCase(fn)) {
			fx=getLast();
		}
		long timeY = System.currentTimeMillis();
		calculateTime2(timeX, timeY, "(linear)");
		return printF(fx);
	}
	public String searchAirlineLinearS(String fn) {
		Flight fx = null; 
		long timeX = System.currentTimeMillis();
		Flight actual = first;
		while(actual.getNext()!=null&&fx==null) {
			if(actual.getAirline().toString().equalsIgnoreCase(fn)) {
				fx=actual; 
			}
			actual=actual.getNext();
		}
		if(getLast().getAirline().toString().equalsIgnoreCase(fn)) {
			fx=getLast();
		}
		long timeY = System.currentTimeMillis();
		calculateTime2(timeX, timeY, "(linear)");
		return printF(fx);
	}
	
	public String searchDestineLinearS(String fn) {
		Flight fx = null; 
		long timeX = System.currentTimeMillis();
		Flight actual = first;
		while(actual.getNext()!=null&&fx==null) {
			if(actual.getDestinationCity().toString().equalsIgnoreCase(fn)) {
				fx=actual; 
			}
			actual=actual.getNext();
		}
		if(getLast().getDestinationCity().toString().equalsIgnoreCase(fn)) {
			fx=getLast();
		}
		long timeY = System.currentTimeMillis();
		calculateTime2(timeX, timeY, "(linear)");
		return printF(fx);
	}
	
	public String searchByGateLinearS(int x){
		Flight fx = null; 
		long timeX = System.currentTimeMillis();
		Flight actual = first;
		while(actual.getNext()!=null&&fx==null) {
			if(actual.getGate()==x) {
				fx=actual; 
			}
			actual=actual.getNext();
		}
		if(getLast().getGate()==x) {
			fx=getLast();
		}
		long timeY = System.currentTimeMillis();
		calculateTime2(timeX, timeY, "(linear)");
		return printF(fx);
	}
	
	
	
	public String printF(Flight fx) {
		String msg = "This flight does not exist";
		if(fx!=null) {
			msg = fx.toString();
		}
		
		return msg;
	}
	
	
	/*public void sortByFlightNumberInsertion() {
		typeOfOrder = FNI; 
		Flight[] toLinkedList = toArray();
		System.out.println(toLinkedList.length);
		long timeX = System.currentTimeMillis();
		for (int i = 1; i < toLinkedList.length; i++) {
			Flight aux = toLinkedList[i];
			int j;
			for (j=i-1; j >=0 && toLinkedList[j].getFlightNumber().compareTo(aux.getFlightNumber())>0; j--) {
				toLinkedList[j+1] = toLinkedList[j];
			}
			toLinkedList[j+1] = aux;
		 }
		
		toLinkedList(toLinkedList);
		long timeY = System.currentTimeMillis();
		calculateTime(timeX, timeY);	
	}*/
	
	public void toLinkedList(Flight[] x) {
		int counter=0;
		first=x[counter];
		Flight current=first;
		while(counter<calculateSize()) {
			current.setNext(x[counter+1]);
			current.setPrev(x[counter-1]);
			counter++;
		}
		
	}
	
	private int calculateSize() {
		Flight current = first;
		int x=1;
		while(current.getNext()!=null) {
			x++;
			current=current.getNext();
		}
		return x;
	}

	public Flight getFirst() {
		return first;
	}
	
	public Flight getLast() {
		Flight current = first;
		while(current.getNext()!=null) {
			current=current.getNext();
		}
		return current;
	}
	
	public Flight getMin() {
		Flight min = first;
		Flight current = first;
		while(current.getNext()!=null) {
			if(current.getGate()>current.getNext().getGate()) {
				min=current.getNext();
			}
		}
		current=current.getNext();
		return min;
	}
	
	public void cocktailSort() {
if(first != null) {
			
			boolean changed = true;
			while(changed) {
				Flight currentNode = first;
				changed = false;
				//System.out.println("== NEW ITERATION A ==");
				while(currentNode.getNext() != null) {
					Flight nextNode = currentNode.getNext();
					//System.out.println(currentNode+" ? "+nextNode);
					if(currentNode.compareTo(nextNode)>0) {
						if(currentNode.getPrev()!=null) {
							currentNode.getPrev().setNext(nextNode);
						}
						if(nextNode.getNext()!=null) {
							nextNode.getNext().setPrev(currentNode);
						}
						
						currentNode.setNext(nextNode.getNext());
						nextNode.setPrev(currentNode.getPrev());
						currentNode.setPrev(nextNode);
						nextNode.setNext(currentNode);
						
						
						if(currentNode==first) {
							first = nextNode;
						}
						
						changed = true;
						
					}else{
						currentNode = currentNode.getNext();
					}
				}
				
				//System.out.println("== NEW ITERATION B ==");
				while(currentNode.getPrev() != null) {
					Flight prevNode = currentNode.getPrev();
					//System.out.println(currentNode+" ? "+prevNode);
					if(currentNode.compareTo(prevNode)<0) {
						if(currentNode.getNext()!=null) {
							currentNode.getNext().setPrev(prevNode);
						}
						if(prevNode.getPrev()!=null) {
							prevNode.getPrev().setNext(currentNode);
						}
						
						currentNode.setPrev(prevNode.getPrev());
						prevNode.setNext(currentNode.getNext());
						currentNode.setNext(prevNode);
						prevNode.setPrev(currentNode);
						
						if(prevNode==first) {
							first = currentNode;
						}
						
						changed = true;
					}else{
						currentNode = currentNode.getPrev();
					}					
				}
			}
		}
	}
}



