package com.example.restservice.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;

import com.example.restservice.Model.AudioFile;

public interface AudioFileRepository extends MongoRepository<AudioFile, String> {
    // @Query("{name:'?0'}")
    // AudioFile findItemByName(String name);

    // for testing
    Optional <AudioFile> findByDriveId(String driveId);
    // List<AudioFile> findByDriveId(String driveId);


    public long count();
}
