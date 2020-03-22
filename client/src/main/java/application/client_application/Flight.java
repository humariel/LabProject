package application.client_application;

import java.io.Serializable;
import java.util.Objects;

public class Flight implements Serializable {

    private String icao24;
    private int firstSeen;
    private String estDepartureAirport;
    private int lastSeen;
    private String estArrivalAirport;
    private String callsign;
    private int estDepartureAirportHorizDistance;
    private int estDepartureAirportVertDistance;
    private int estArrivalAirportHorizDistance;
    private int estArrivalAirportVertDistance;
    private int departureAirportCandidatesCount;
    private int arrivalAirportCandidatesCount;

    public Flight(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return firstSeen == flight.firstSeen &&
                lastSeen == flight.lastSeen &&
                estDepartureAirportHorizDistance == flight.estDepartureAirportHorizDistance &&
                estDepartureAirportVertDistance == flight.estDepartureAirportVertDistance &&
                estArrivalAirportHorizDistance == flight.estArrivalAirportHorizDistance &&
                estArrivalAirportVertDistance == flight.estArrivalAirportVertDistance &&
                departureAirportCandidatesCount == flight.departureAirportCandidatesCount &&
                arrivalAirportCandidatesCount == flight.arrivalAirportCandidatesCount &&
                Objects.equals(icao24, flight.icao24) &&
                Objects.equals(estDepartureAirport, flight.estDepartureAirport) &&
                Objects.equals(estArrivalAirport, flight.estArrivalAirport) &&
                Objects.equals(callsign, flight.callsign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount);
    }

    public String getIcao24() {
        return icao24;
    }

    public void setIcao24(String icao24) {
        this.icao24 = icao24;
    }

    public int getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(int firstSeen) {
        this.firstSeen = firstSeen;
    }

    public String getEstDepartureAirport() {
        return estDepartureAirport;
    }

    public void setEstDepartureAirport(String estDepartureAirport) {
        this.estDepartureAirport = estDepartureAirport;
    }

    public int getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(int lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getEstArrivalAirport() {
        return estArrivalAirport;
    }

    public void setEstArrivalAirport(String estArrivalAirport) {
        this.estArrivalAirport = estArrivalAirport;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public int getEstDepartureAirportHorizDistance() {
        return estDepartureAirportHorizDistance;
    }

    public void setEstDepartureAirportHorizDistance(int estDepartureAirportHorizDistance) {
        this.estDepartureAirportHorizDistance = estDepartureAirportHorizDistance;
    }

    public int getEstDepartureAirportVertDistance() {
        return estDepartureAirportVertDistance;
    }

    public void setEstDepartureAirportVertDistance(int estDepartureAirportVertDistance) {
        this.estDepartureAirportVertDistance = estDepartureAirportVertDistance;
    }

    public int getEstArrivalAirportHorizDistance() {
        return estArrivalAirportHorizDistance;
    }

    public void setEstArrivalAirportHorizDistance(int estArrivalAirportHorizDistance) {
        this.estArrivalAirportHorizDistance = estArrivalAirportHorizDistance;
    }

    public int getEstArrivalAirportVertDistance() {
        return estArrivalAirportVertDistance;
    }

    public void setEstArrivalAirportVertDistance(int estArrivalAirportVertDistance) {
        this.estArrivalAirportVertDistance = estArrivalAirportVertDistance;
    }

    public int getDepartureAirportCandidatesCount() {
        return departureAirportCandidatesCount;
    }

    public void setDepartureAirportCandidatesCount(int departureAirportCandidatesCount) {
        this.departureAirportCandidatesCount = departureAirportCandidatesCount;
    }

    public int getArrivalAirportCandidatesCount() {
        return arrivalAirportCandidatesCount;
    }

    public void setArrivalAirportCandidatesCount(int arrivalAirportCandidatesCount) {
        this.arrivalAirportCandidatesCount = arrivalAirportCandidatesCount;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "icao24='" + icao24 + '\'' +
                ", firstSeen=" + firstSeen +
                ", estDepartureAirport='" + estDepartureAirport + '\'' +
                ", lastSeen=" + lastSeen +
                ", estArrivalAirport='" + estArrivalAirport + '\'' +
                ", callsign='" + callsign + '\'' +
                ", estDepartureAirportHorizDistance=" + estDepartureAirportHorizDistance +
                ", estDepartureAirportVertDistance=" + estDepartureAirportVertDistance +
                ", estArrivalAirportHorizDistance=" + estArrivalAirportHorizDistance +
                ", estArrivalAirportVertDistance=" + estArrivalAirportVertDistance +
                ", departureAirportCandidatesCount=" + departureAirportCandidatesCount +
                ", arrivalAirportCandidatesCount=" + arrivalAirportCandidatesCount +
                '}';
    }
}
