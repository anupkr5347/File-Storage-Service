package com.build.distributed.storage.model;

import com.build.distributed.storage.model.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    // Custom query method (optional)
    void deleteByObjectKey(String objectKey);

    FileMetadata findByObjectKey(String objectKey);
}

