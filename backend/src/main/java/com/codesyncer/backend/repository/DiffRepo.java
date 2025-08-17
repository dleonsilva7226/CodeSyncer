package com.codesyncer.backend.repository;
import com.codesyncer.backend.model.Diff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiffRepo extends JpaRepository<Diff, Long> {

//    @Query(value ="select id, content, author, timestamp from \"Diff\"", nativeQuery = true)
//    public Optional<List<Diff>> getAllDiffs();
}