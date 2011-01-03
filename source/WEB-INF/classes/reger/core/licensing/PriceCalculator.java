package reger.core.licensing;

/**
 * Utility class to calculate prices
 */
public class PriceCalculator {



     public static double calculatePrice(double basePrice, double priceper100mbstorage, double pricepergbbandwidth, long bytes, long maxbandwidth){
        double price=0;

        //Storage
        double storagePrice = priceper100mbstorage*((double)bytes/(double)100000000);

        //Bandwidth
        double bandwidthPrice = pricepergbbandwidth*((double)maxbandwidth/(double)1000000000);

        //Price
        price = basePrice + storagePrice + bandwidthPrice;

        return price;
    }



}
