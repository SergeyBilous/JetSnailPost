package ru.home.post.writer.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.post.writer.entities.CurrentDeliveryStatus;

@Repository
public interface CurrentDeliveryStatusRepository extends CrudRepository<CurrentDeliveryStatus,Long> {
}
