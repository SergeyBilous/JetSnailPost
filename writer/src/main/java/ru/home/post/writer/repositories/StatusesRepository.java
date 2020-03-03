package ru.home.post.writer.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.home.post.writer.entities.Statuses;

public interface StatusesRepository extends CrudRepository<Statuses,Long> {
}
