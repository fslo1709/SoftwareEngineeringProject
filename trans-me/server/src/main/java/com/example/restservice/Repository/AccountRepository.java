package com.example.restservice.Repository;

import java.util.List;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restservice.Model.Account;

public interface AccountRepository extends MongoRepository<Account, String> {
    // @Query("{name:'?0', password:'?1'}")
    // Account findItemByName(String name, String password);

    Account findByUsername(String username);
    void deleteAllByUsername(String username);
    // 可以跑一下 main 裡面的 code 就知道了
    
    // 第一種是 override Mongo 裡面的
    // 第二種是 override CRUDrepo

    // 測試資料 @ 費南's DB
    // userID: "test", password: "test"

    boolean existsByUsername(String username);

    
    // Not sure if it works : 
    // // Account findByBlocksIdContaining(List<String> blocksId);
    // Account findByBlocksIdContaining(String blockId);
    // List<Account> findByUsernameContaining(String name);
    //     // non unique 會報錯

    public long count();
}

// 和 tutorial 不一樣
// 沒有 rest 所以沒有改 endopoint
// 內部呢，只是自己定義而已