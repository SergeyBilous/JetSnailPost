package ru.home.post.writer;

import ru.home.post.writer.CreatePackages;
import ru.home.post.writer.commons.CarrierInfo;

public class Writer {
    public static void main(String[] args){
        CarrierInfo carrierInfo=new CarrierInfo("hadoop.home.ru","logistic1","carrier","welcome1");
        CreatePackages packagesCreator=new CreatePackages(carrierInfo,50);
        Thread writerThread=new Thread(packagesCreator);
      //  writerThread.start();
        CreateMovies movesCreator=new CreateMovies(carrierInfo,50);
        Thread movesThread=new Thread(movesCreator);
        movesThread.start();
    }
}
