package userinterface;

import java.io.IOException;


import customsThreads.GUIUpdateControllThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Airport;
import model.CustomDate;
import model.CustomHour;
import model.Flight;

public class Controller {
	
	private int pageNumber;
	
	private int currentNumber;
	
	private int current;
	
	private Airport airport;
	
    @FXML
    private Label typeOfOrder;
    
    @FXML
    private Label timeSearching;
    
    @FXML
    private Label timeOrdering;
    
    @FXML
    private Label yourFlight;
	
    @FXML
    private ComboBox<String> comboBox;
    
    @FXML
    private TextField criteria;
      
    @FXML
    private Label date;
    
    @FXML
    private TableView<Flight> tableView;
    
    @FXML
    private TableColumn<CustomDate, String> dateT;

    @FXML
    private TableColumn<CustomHour, String> time;

    @FXML
    private TableColumn<Flight, String> airline;

    @FXML
    private TableColumn<Flight, String> flight;

    @FXML
    private TableColumn<Flight, String> to;

    @FXML
    private TableColumn<Flight, String> term;
    

    @FXML
    private TextField flightsNumber;
    
   
    private ObservableList<Flight> oListFlights; 
    
    private GUIUpdateControllThread guiThread;
    
    
    @FXML
    public void initialize() {
    	current = 1;
    
    	try {
			airport = new Airport();
		    oListFlights = updateList(); 
			date.setText(airport.getStringHour());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    	
     
        time.setCellValueFactory(new PropertyValueFactory<>("hour"));
    	airline.setCellValueFactory(new PropertyValueFactory<>("airline"));
    	flight.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
    	to.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
    	term.setCellValueFactory(new PropertyValueFactory<>("gate"));
        dateT.setCellValueFactory(new PropertyValueFactory<>("date"));
    	tableView.setItems(oListFlights);
    	guiThread = new GUIUpdateControllThread(this); 
    	guiThread.setDaemon(true);
    	guiThread.start();
    	airport.cocktailSortHour();
    }
    
    @FXML
    void sortByHour(ActionEvent event) {
    	airport.cocktailSortHour();
    }
    
    @FXML
    void sortByFlightNumber(ActionEvent event) {
    	airport.cocktailSortFlightNumber();
    }

    @FXML
    void sortByAirline(ActionEvent event) {
    	airport.cocktailSortAirline();
    }
    
 
    @FXML
    void sortByTerminal(ActionEvent event) {
    	airport.cocktailSortGate();
    }
    
    @FXML
    void sortByDate(ActionEvent event) {
    	airport.cocktailSortDate();
    }
    @FXML
    void generate(ActionEvent event) {
    
    	try {
    		int size = Integer.parseInt(flightsNumber.getText());
    		airport.generateFlights(size);
    		airport.cocktailSortHour();	
    	}catch(NumberFormatException e){
    		
    	}
    
    }
    
    @FXML
    void sortByDestine(ActionEvent event) {
    	airport.cocktailSortDestine();
    }
    
    public void update() {
    	typeOfOrder.setText(airport.getTypeO());
    	typeOfOrder.setAlignment(Pos.CENTER);
    	timeOrdering.setText(airport.getTimeO());
    	timeOrdering.setAlignment(Pos.CENTER_LEFT);
    	airport.updateCurrentTime();
    	date.setText(airport.getStringHour());
    	tableView.setItems(showF());
    	timeSearching.setText(airport.getTimeSearching());
    	yourFlight.setAlignment(Pos.CENTER);
    	calculatePages();
    	
   
    }
    
    public void calculatePages() {
    	ObservableList<Flight> list = updateList();
    	int pages = list.size()/20;
    	pageNumber = pages;
    	
    }
    
    public ObservableList<Flight> showF() {
    	ObservableList<Flight> list = updateList();
    	
    	ObservableList<Flight> list2 = FXCollections.observableArrayList();;
    	
    	if(current<=pageNumber) {
    		if(current!=1) {
    			
    			int x=20*(current-1);
    			int j1=20*current; 
    			for(int i=x;i<j1; i++) {
        			list2.add(list.get(i));
        		}
    		}else {
    			int j;
    			 j=19;
    		for(int i=0;i<=j; i++) {
    			list2.add(list.get(i));
    		}
    		}
    		
    	}
   
    	return list2;
    }

    public ObservableList<Flight> updateList(){
  
    	ObservableList<Flight> list = FXCollections.observableArrayList();
    	Flight first = airport.getFirst();
    	Flight actual = airport.getFirst();
   
    	
    	while(actual.getNext()!=null) {
    		list.add(actual);
    		actual = actual.getNext();
    	
    	}
    	list.add(actual);
		return list;
    }
    
    @FXML
    void search(ActionEvent event) {
    	String option = comboBox.getValue();
    	String cx = String.valueOf(criteria.getText());
    	if(option.equals("by time")) {
    		yourFlight.setText(airport.searchByTimeLinearS(cx));
    	}else if(option.equals("by airline")) {
    		yourFlight.setText(airport.searchAirlineLinearS(cx));
    	}else if(option.equals("by flight")) {
    		yourFlight.setText(airport.searchFlightLinearS(cx));
    	}else if(option.equals("by destine")) {
    		yourFlight.setText(airport.searchDestineLinearS(cx));
    	}else if(option.equals("by gate")) {
		    yourFlight.setText(airport.searchByGateLinearS(Integer.parseInt(cx)));
    	}else if(option.equals("by date")) {
    		yourFlight.setText(airport.searchDateLinearS(cx));
    	}
    }
    
    @FXML
    void nextb(ActionEvent event) {
    	if(current<pageNumber)
    		current++;
    }

    @FXML
    void prevb(ActionEvent event) {
    	if(current>1)
    		current--;
    }
}
