package ru.home.post.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import ru.home.post.writer.*;
import ru.home.post.writer.config.Commons;
import ru.home.post.writer.entities.*;
import ru.home.post.writer.repositories.CurrentDeliveryStatusRepository;
import ru.home.post.writer.repositories.PackageRepository;
import ru.home.post.writer.repositories.StatusesRepository;
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
    @Autowired
    private StatusesRepository statusesRepository;
    @Autowired
    private PackageRepository packageRepository;

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
            createMoves(currentStatus, currentDate,ParcelStatus.ACCEPTED);
            createMoves(currentStatus, currentDate,ParcelStatus.EN_ROUTE);
            createMoves(currentStatus,currentDate,ParcelStatus.WAITING_FOR_TRANSPORT);
        }
    }


    private Statuses getStatusById(Long id) {
        Optional<Statuses> st = statusesRepository.findById(id);
        return st.get();
    }

    private List<CurrentDeliveryStatus> getStatusByParcelsStatus(Iterable<CurrentDeliveryStatus> currentStatus, Date currentDate, ParcelStatus status) {
        List<CurrentDeliveryStatus> accepted = new ArrayList<>();
        Iterator it = currentStatus.iterator();
        while (it.hasNext()) {
            CurrentDeliveryStatus cs = (CurrentDeliveryStatus) it.next();
            if (cs.getOperationDate().compareTo(currentDate) < 0 &&
                    cs.getStatus().getId().equals(status.label)) {
                accepted.add(cs);
            }
        }
        return accepted;
    }

    private void createMoves(Iterable<CurrentDeliveryStatus> currentStatus, Date currentDate,
                             ParcelStatus fromStatus) {
        List<CurrentDeliveryStatus> parcelsToMove = getStatusByParcelsStatus(currentStatus, currentDate, fromStatus);
        if (parcelsToMove.size() == 0)
            return;
        int moves = Commons.getRandom(movesPerDay - (movesDelta * 100) / movesPerDay,
                movesPerDay + (movesDelta * 100) / movesPerDay);
        Iterator<Integer> it = Commons.randomIterator(moves, 0, parcelsToMove.size() - 1);
        while (it.hasNext()) {
            Integer idx = it.next();
            CurrentDeliveryStatus cs = parcelsToMove.get(idx);
            Parcel parcel = cs.getParcel();
            DeliveryPoint nextPoint = Commons.getNextPoint(parcel);
            DeliveryStatus ds = new DeliveryStatus();
            ds.setOperationDate(currentDate);
            ds.setDeliveryPoint(nextPoint);
            ds.setParcel(parcel);
            parcel.getDeliveryStatus().add(ds);
            switch (fromStatus) {
                case ACCEPTED: {
                    ds.setStatus(getStatusById(ParcelStatus.EN_ROUTE.label));
                    break;
                }
                case EN_ROUTE: {
                    if(nextPoint.equals(parcel.getEndPoint()))
                        ds.setStatus(getStatusById(ParcelStatus.READY_FOR_DELIVERY.label));
                    else
                        ds.setStatus(getStatusById(ParcelStatus.WAITING_FOR_TRANSPORT.label));
                    break;

                }
                case WAITING_FOR_TRANSPORT:
                    ds.setStatus(getStatusById(ParcelStatus.EN_ROUTE.label));
            }
            System.out.println(currentDate+"\t"+parcel.getId()+"\t"+fromStatus+"\t"+ ParcelStatus.valueOfLabel(ds.getStatus().getId())+
                    "\t"+new Date() );
            packageRepository.save(parcel);
        }
    }
}
