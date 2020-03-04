package ru.home.post.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import ru.home.post.writer.*;
import ru.home.post.writer.config.Commons;
import ru.home.post.writer.entities.CurrentDeliveryStatus;
import ru.home.post.writer.entities.TimeSettings;
import ru.home.post.writer.repositories.CurrentDeliveryStatusRepository;
import ru.home.post.writer.repositories.TimeSettingsRepository;


import java.util.*;

public class CreateMoves implements Runnable {
    private Integer days;
    @Autowired
    private Environment environment;
    @Autowired
    private TimeSettingsRepository timeSettingsRepository;
    @Autowired
    private CurrentDeliveryStatusRepository currentDeliveryStatusRepository;

    public CreateMoves(Integer days) {

        this.days = days;
    }

    @Override
    public void run() {
        String sMovesPerDay=environment.getProperty("moves.per.day");
        String sMovesDelta=environment.getProperty("moves.delta");
        int movesPerDay=Integer.valueOf(sMovesPerDay);
        int movesDelta=Integer.valueOf(sMovesDelta);
        Optional<TimeSettings> timeSettings = timeSettingsRepository.findById(1L);
        Date startDate=timeSettings.get().getDate();
        Iterable<CurrentDeliveryStatus> currentStatus=currentDeliveryStatusRepository.findAll();
        for(int iterationNumber=1;iterationNumber<days;iterationNumber++){
            Date currentDate= Commons.addDays(startDate,iterationNumber);
            int moves=Commons.getRandom(movesPerDay-movesDelta/movesPerDay*100,
                    movesPerDay+movesDelta/movesPerDay*100);
        }
    }
}
