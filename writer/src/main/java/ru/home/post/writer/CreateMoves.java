package ru.home.post.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import ru.home.post.writer.*;
import ru.home.post.writer.config.Commons;
import ru.home.post.writer.entities.CurrentDeliveryStatus;
import ru.home.post.writer.entities.Parcel;
import ru.home.post.writer.entities.ParcelStatus;
import ru.home.post.writer.entities.TimeSettings;
import ru.home.post.writer.repositories.CurrentDeliveryStatusRepository;
import ru.home.post.writer.repositories.TimeSettingsRepository;


import java.util.*;

public class CreateMoves implements Runnable {
    private Integer days;
    private Set<CurrentDeliveryStatus> updatedStatuses;
    private Date startDate;
    private int movesPerDay;
    private int movesDelta;
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
        String sMovesPerDay = environment.getProperty("moves.per.day");
        String sMovesDelta = environment.getProperty("moves.delta");
        movesPerDay = Integer.valueOf(sMovesPerDay);
        movesDelta = Integer.valueOf(sMovesDelta);
        Optional<TimeSettings> timeSettings = timeSettingsRepository.findById(1L);
        startDate = timeSettings.get().getDate();
        for (int iterationNumber = 1; iterationNumber < days; iterationNumber++) {
            updatedStatuses = new HashSet<>();
            Date currentDate = Commons.addDays(startDate, iterationNumber);
            Iterable<CurrentDeliveryStatus> currentStatus = currentDeliveryStatusRepository.findAll();
            sendAccepted(currentStatus, currentDate);
        }
    }

    private void sendAccepted(Iterable<CurrentDeliveryStatus> currentStatus, Date currentDate) {
        List<CurrentDeliveryStatus> accepted = new ArrayList<>();
        Iterator it = currentStatus.iterator();
        while (it.hasNext()) {
            CurrentDeliveryStatus cs = (CurrentDeliveryStatus) it.next();
            if (cs.getOperationDate().compareTo(currentDate) > 0) {
                accepted.add(cs);
            }
        }
        int moves = Commons.getRandom(movesPerDay - movesDelta / movesPerDay * 100,
                movesPerDay + movesDelta / movesPerDay * 100);
        int maxMoves = moves >= accepted.size() ? moves : accepted.size();
        for (int i = 0; i < maxMoves; i++) {
            CurrentDeliveryStatus cs;
            int attempts = 0;
            while (true) {
                int idx = Commons.getRandom(0, accepted.size() - 1);
                cs = accepted.get(idx);
                if (updatedStatuses.contains(cs)) {
                    attempts++;
                    if (attempts > 25) {
                        System.out.println("Cant find element");
                        System.exit(0);
                    }
                    continue;
                }
                updatedStatuses.add(cs);
                break;
            }
            Parcel parcel=cs.getParcel();
        }
    }
}
