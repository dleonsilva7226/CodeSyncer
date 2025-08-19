package com.codesyncer.backend.repository;

import com.codesyncer.backend.model.Merge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MergeRepo extends JpaRepository<Merge, Long> {

}
