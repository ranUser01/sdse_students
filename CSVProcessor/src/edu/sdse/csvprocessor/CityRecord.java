package edu.sdse.csvprocessor;

public class CityRecord {
    private int id; 
    private int year;
    private String city;
    private int population;

    public CityRecord(int i, int y, String c,int p){
        this.id = i; this.year = y; this.city = c; this.population = p; 
    }

    @Override
    public String toString() {
        return "ID: " + id + " ,year: " + year + " ,city: " + city + " ,population: " + population;
    } 

    public int getID(){
        return id;
    }

    public int getYear(){
        return year;
    }

    public String getCity(){
        return city;
    }

    public int getPopulation(){
        return population;
    }
}
