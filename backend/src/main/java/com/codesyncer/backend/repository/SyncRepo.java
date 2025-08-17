package com.codesyncer.backend.repository;

import com.codesyncer.backend.model.Diff;
import com.codesyncer.backend.model.Sync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncRepo extends JpaRepository<Sync, Long> {

}
