public class CityRecord {
    private int id; 
    private int year;
    private String city;
    private int population;

    public CityRecord(int i, int y, String c,int p){
        id = i; year = y; city = c; population = p; 
    }

    @Override
    public String toString() {
        return "ID: " + id + " ,year: " + year + " ,city: " + city + " ,population: " + population;
    } 

    public int getID(){
        return id;
    }
}
