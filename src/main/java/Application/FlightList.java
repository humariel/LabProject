package Application;

import java.util.ArrayList;
import java.util.List;

public class FlightList {
    private List<Object> flights;

    public FlightList(){
        flights = new ArrayList<Object>();
    }

    public List<Object> getFlights() {
        return flights;
    }

    public void setFlights(List<Object> flights) {
        this.flights = flights;
    }

    @Override
    public String toString() {
        return "FlightList{" +
                "flights=" + flights +
                '}';
    }
}
