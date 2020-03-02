package ru.home.post.writer.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.post.writer.entities.DeliveryPoint;

@Repository
public interface DeliveryPointRepository extends CrudRepository<DeliveryPoint,Long> {
}
