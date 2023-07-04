package ru.yandex.practicum.filmorate.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
