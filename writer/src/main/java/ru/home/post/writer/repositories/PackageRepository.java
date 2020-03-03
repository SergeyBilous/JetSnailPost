package ru.home.post.writer.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.home.post.writer.entities.Parcel;

@Repository("packageRepository")
public interface PackageRepository extends CrudRepository<Parcel,Long> {
}
