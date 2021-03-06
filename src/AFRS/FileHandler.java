package AFRS;

import AFRS.AirportInfo.AirportInfo;
import AFRS.AirportInfo.LocalAirportInfo;
import AFRS.Model.Airport;
import AFRS.Model.WeatherInformation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHandler {

    private static final String filePath = "AFRS/Data/";
    private static final String airportFile = "airports.txt";
    private static final String connectionFile = "connections.txt";
    private static final String delayFile = "delays.txt";
    private static final String flightFile = "flights.txt";
    private static final String weatherFile = "weather.txt";

    private HashMap<String, AirportInfo> airportInfoMap;

    public HashMap<String, Airport> getAirportMap() {
        return airportMap;
    }

    private HashMap<String, Airport> airportMap;

    private ArrayList<String> flightDataList;

    public HashMap<String, Airport> getAirportInfo(String clientID) {
        return airportInfoMap.get(clientID).getInfo();
    }

    public void removeClient(String clientID) { airportInfoMap.remove(clientID); }

    public ArrayList<String> getFlightDataList() {
        return flightDataList;
    }

    public void setAirportInfo(String clientID) { airportInfoMap.put(clientID, new LocalAirportInfo(airportMap)); }

    public void setAirportInfo(String clientID, AirportInfo airportInfo) {
        airportInfoMap.put(clientID, airportInfo);
    }

    public FileHandler() {
        airportInfoMap = new HashMap<>();
        airportMap = new HashMap<>();
        flightDataList = new ArrayList<>();
        if (!tryBuildAirportMap()) {
            System.err.println("Unable to build airport map. Exiting program.");
            System.exit(1);
        }
        if (!tryBuildFlightDataList()) {
            System.err.println("Unable to build flight data list. Exiting program.");
            System.exit(1);
        }

    }

    private boolean tryBuildAirportMap() {
        BufferedReader br;
        String line;
        try {
            String relativePath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            relativePath = relativePath.substring(relativePath.indexOf(":")+2);
            br = new BufferedReader(new FileReader(relativePath+filePath+airportFile));
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportMap.put(airport[0], new Airport(airport[0], airport[1]));
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read airport file.");
            return false;
        }

        try {
            String relativePath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            relativePath = relativePath.substring(relativePath.indexOf(":")+2);
            br = new BufferedReader(new FileReader(relativePath+filePath+weatherFile));
            line = br.readLine();
            while (line != null) {
                String[] weather = line.split(",");
                String airportCode = weather[0];

                for (int i = 1; i < weather.length; i += 2) {
                    WeatherInformation temp = new WeatherInformation(weather[i], weather[i + 1]);
                    airportMap.get(airportCode).addWeatherToList(temp);
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read weather file.");
            return false;
        }

        try {
            String relativePath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            relativePath = relativePath.substring(relativePath.indexOf(":")+2);
            br = new BufferedReader(new FileReader(relativePath+filePath+delayFile));
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportMap.get(airport[0]).setDelay(airport[1]);
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read delay file.");
            return false;
        }

        try {
            String relativePath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            relativePath = relativePath.substring(relativePath.indexOf(":")+2);
            br = new BufferedReader(new FileReader(relativePath+filePath+connectionFile));
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportMap.get(airport[0]).setConnection(Integer.parseInt(airport[1]));
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read connection file.");
            return false;
        }

        return true;
    }

    private boolean tryBuildFlightDataList() {
        BufferedReader br;
        String line;

        try {
            String relativePath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            relativePath = relativePath.substring(relativePath.indexOf(":")+2);
            br = new BufferedReader(new FileReader(relativePath+filePath+flightFile));
            line = br.readLine();
            while (line != null) {
                flightDataList.add(line);
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read flight file.");
            return false;
        }
        return true;
    }
}
