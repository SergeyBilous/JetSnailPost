package ru.home.post.writer.repositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.post.writer.entities.TimeSettings;

@Repository

public interface TimeSettingsRepository extends CrudRepository<TimeSettings,Long> {
}
