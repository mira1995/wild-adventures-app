package com.wa.msm.adventure.repository;

import com.wa.msm.adventure.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Iterable<Session> findAllByAdventureId(Long adventureId);

    Iterable<Session> findAllByIdIn(List<Long> sessionsIdList);
}
