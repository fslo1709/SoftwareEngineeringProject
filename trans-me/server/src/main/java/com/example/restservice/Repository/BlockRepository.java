package com.example.restservice.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.restservice.Model.Block;

public interface BlockRepository extends MongoRepository<Block, String> {
    // @Query("{name:'?0'}")
    // Block findItemByName(String name);

    Optional <Block> findById(String id);
    
    boolean existsById(String id);
    void deleteById(String id);
    void deleteAllById(List<String> ids);

    public long count();
}
