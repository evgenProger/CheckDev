package ru.checkdev.mock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.checkdev.mock.domain.FilterProfile;

@Repository
public interface FilterProfileRepository extends JpaRepository<FilterProfile, Integer> {
}
