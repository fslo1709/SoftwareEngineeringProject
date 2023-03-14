package com.example.restservice.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restservice.Request.account.PutAccountRequest;
import com.example.restservice.Response.Msg;
import com.example.restservice.Service.Payload.Payload;
import com.example.restservice.Model.Account;
import com.example.restservice.Model.Block;
import com.example.restservice.Repository.AccountRepository;
import com.example.restservice.Repository.BlockRepository;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Payload <Msg, Account> updateAccount(PutAccountRequest.RequestData data) {
        
        System.out.println("Update account：");
        
        // if (newAccount.getUsername() == null) {
        if (data.getUsername() == null) { // 這樣是不是就用不到這個 case 了？
            System.out.println("invalid username");

            return new Payload <Msg, Account> (
                new Msg(
                    "error",
                    "invalid username"
                ),
                null
            );
        }
        else {
            System.out.println("username = "+ data.getUsername());

            Account DBAccount = accountRepository.findByUsername(data.getUsername());
            if (DBAccount == null) {
                System.out.println("username not found");

                return new Payload <Msg, Account> (
                    new Msg(
                        "error",
                        "username not found"
                    ),
                    null
                );
            }
            else { // update
                String updatedTerms = "";
                
                // currently don't support
                // if (newAccount.getPassword() != null) {
                //     DBAccount.setPassword(newAccount.getPassword());
                //     updatedTerms += "password, ";
                // }
                
                if (data.getAudioFilesId() != null) {
                    DBAccount.setAudioFilesId(data.getAudioFilesId());
                    updatedTerms += "aduioFiles, ";
                }
                if (data.getBlocksId() != null) {
                    DBAccount.setBlocksId(data.getBlocksId());
                    updatedTerms += "blocks ";
                }

                accountRepository.save(DBAccount);
                System.out.println("updatedTerms : " + updatedTerms);

                return new Payload <Msg, Account> (
                    new Msg(
                       "success",
                       "return user data"
                    ),
                    DBAccount
                );
            }
        }

    }

    // Block operations
    @Autowired
    BlockRepository blockRepository;
    
    
    
    // CRUD，全用 list
    public Payload<Msg, List<Block>> readBlocks( List<String> blocksId) {
        System.out.println("Read blocks：");
        System.out.println("blocksId： " + blocksId);

        ArrayList <Block> blocks = new ArrayList<Block> ();
        // String IdsWithProblem;
        // boolean warning;
        blockRepository.findAllById(blocksId).forEach( (item) -> {
            if (item != null) {
                blocks.add(item);
            }
            // 好像不能這樣用，和 lambda (enclosure) 的 scope 有關
            // else {
            //     warning = true;
            // }

        } );

        if (blocks.size() == 0) {
            System.out.println("All of the blocksId are not found or blocksId is an empty array");
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "error",
                        "All of the blockId are not found or blocksId is an empty array"
                    ),
                    blocks
                );
        }
        else if (blocks.size() != blocksId.size()) {
            System.out.println("Some of the blocksId are not found");
            System.out.println("block data :　" + blocks);
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "warning",
                        // TODO: 要告訴前端 status 有 warning 這種可能
                        "Some of the blockId are not found"
                    ),
                    blocks
                );
        }
        else {
            System.out.println("Success, return all blocks data");
            System.out.println("block data :　" + blocks);
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "success",
                        "return all blocks data"
                        ),
                    blocks
                );
        }
    }

    public Payload<Msg, List<Block>> readBlocksByUsername(String username) {
        System.out.println("Read blocks by username：");
        System.out.println("username： " + username);

        Account user =  accountRepository.findByUsername(username);
        if (user == null) {
            System.out.println("username not found");
            return new Payload <Msg, List<Block>> (
                new Msg(
                    "error",
                    "username not found"
                ),
                new ArrayList<Block>()
            );
        }
        else {
            
            List<String> blocksId = user.getBlocksId();  
            if (blocksId == null) {
                System.out.println("blocksId is null");
                return new Payload <Msg, List<Block>> (
                    new Msg(
                        "warning",
                        "blocksId is null"
                    ),
                    new ArrayList<Block>()
                    // TODO: status "warning"
            );
            }
            else {

                ArrayList <Block> blocks = new ArrayList<Block> ();
                blockRepository.findAllById(blocksId).forEach( (item) -> {
                    if (item != null) {
                        blocks.add(item);
                    }
                } );
        
                if (blocks.size() == blocksId.size()) {
                    if (blocks.size() == 0) {
                        System.out.println("BlocksId is an empty array");
                        return new Payload <Msg, List<Block>> (
                            new Msg(
                                "success",
                                "BlocksId is an empty array"
                            ),
                            blocks
                        );
                    }
                    else {
                        System.out.println("Success, return all blocks data");
                        System.out.println("block data :　" + blocks);
                        return new Payload <Msg, List<Block>> (
                            new Msg(
                                "success",
                                "return all blocks data"
                                ),
                            blocks
                        );
                    }
                    
                }
                else {
                    if ((blocks.size() == 0)) {
                        System.out.println("All of the blocksId are not found");
                        return new Payload <Msg, List<Block>> (
                            new Msg(
                                "error",
                                "All of the blocksId are not found"
                            ),
                            blocks
                        );
                    }
                    else {
                        System.out.println("Some of the blocksId are not found");
                        System.out.println("block data :　" + blocks);
                        return new Payload <Msg, List<Block>> (
                            new Msg(
                                "warning",
                                // TODO: 要告訴前端 status 有 warning 這種可能
                                "Some of the blockId are not found"
                            ),
                            blocks
                        );
                    }

                }
                
            }
           
            
            
        }

    }

    public Payload<Msg, List<Block> > updateBlocks(List<Block> inBlocks) {
        // batch 比較快，但forEach 比較好寫

        System.out.println("Update blocks：");

        List <Block> successfulBlocks = new ArrayList <Block>();

        inBlocks.forEach( (block) -> {
            Optional<Block> fetchedBlock = blockRepository.findById(block.getId());
            if (fetchedBlock.isPresent()) {
                Block DBBlock = fetchedBlock.get();
                DBBlock.setContent(block.getContent());
                DBBlock.setHidden(block.isHidden());
                blockRepository.save(DBBlock);

                successfulBlocks.add(block);
            }
        });

        if (successfulBlocks.size() == 0) {
            System.out.println("All of the blocksId are not found");
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "error",
                        "All of the blockId are not found"
                    ),
                    successfulBlocks
                );
        }
        else if (successfulBlocks.size() != inBlocks.size()) {
            System.out.println("Some of the blocksId are not found");
            System.out.println("successful blocks data : " + successfulBlocks);
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "warning",
                        // TODO: 要告訴前端 status 有 warning 這種可能
                        "Some of the blockId are not found"
                    ),
                    successfulBlocks
                );
        }
        else {
            System.out.println("Success, return all blocks data");
            System.out.println("successful blocks data : " + successfulBlocks);
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "success",
                        "return all blocks data"
                        ),
                    successfulBlocks
                );
        }
    }

    public Payload<Msg, List<Block> > createBlocks(List<Block> inBlocks) {
        // batch 比較快，但forEach 比較好寫

        System.out.println("Create blocks：");

        List <Block> successfulBlocks = new ArrayList <Block>();

        inBlocks.forEach( (block) -> {
            Block savedBlock = blockRepository.save(block);
            // block 沒 id，savedBlock 有
            
            if (savedBlock != null) {
                successfulBlocks.add(savedBlock);
            }
        });

        if (successfulBlocks.size() == 0) {
            System.out.println("All of the operations failed");
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "error",
                        "All of the operations failed"
                    ),
                    successfulBlocks
                );
        }
        else if (successfulBlocks.size() != inBlocks.size()) {
            System.out.println("Some of the operations failed");
            System.out.println("successful blocks data : " + successfulBlocks);
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "warning",
                        // TODO: 要告訴前端 status 有 warning 這種可能
                        "Some of the operations failed"
                    ),
                    successfulBlocks
                );
        }
        else {
            System.out.println("Success, return all blocks data");
            System.out.println("successful blocks data : " + successfulBlocks);
            return new Payload <Msg, List<Block>> (
                    new Msg(
                        "success",
                        "return all blocks data"
                        ),
                    successfulBlocks
                );
        }
    }

    public Payload<Msg, String> deleteBlocks( List<String> blocksId) {
        System.out.println("Delete blocks：");
        System.out.println("blocksId： " + blocksId);

        // blockRepository.deleteAllById(blocksId);
        // TODO: 檢查有沒有成功
        // 不能刪不存在的?
                //  > 就存 username 吧，剛好可以先檢查有沒有這個 block
        // 同時也更新 account
        

        List <Block> successfulBlocks = new ArrayList <Block>();

        blocksId.forEach( (blockId) -> {
            Optional<Block> fetchedBlock = blockRepository.findById(blockId);
            
            // 1st possibility : block doesn't exist
                // do nothing
            // 2nd：block exists, but account doesn't
                // just delete the block (?)
            // 3rd : what if account actually exists but block point wrong?
                    // ...
            if (fetchedBlock.isPresent()) {
                Block DBBlock = fetchedBlock.get();
                if (DBBlock.getUsername() != null) { // support old blocks
                    Account account = accountRepository.findByUsername(DBBlock.getUsername());
                    
                    if (account != null) {
                        System.out.println(" found account ： " + account);
                        
                        account.setBlocksId(
                            account.getBlocksId().stream().filter(
                                (accountBlockId) -> {
                                    return ! accountBlockId.equals(blockId);
                                }
                            ).collect(Collectors.toList())
                        ); 
                        accountRepository.save(account);

                        System.out.println(" modified account ： " + account);
                    }
                    
                }

                blockRepository.delete(DBBlock);
                successfulBlocks.add(DBBlock);
            }
        });

        if (successfulBlocks.size() == 0) {
            System.out.println("All of the blocksId are not found");
            return new Payload <Msg, String> (
                    new Msg(
                        "error",
                        "All of the blockId are not found"
                    ),
                    ""
                );
        }
        else if (successfulBlocks.size() != blocksId.size()) {
            System.out.println("Some of the blocksId are not found");
            System.out.println("successfulBlocks :　" + successfulBlocks);
            return new Payload <Msg, String> (
                    new Msg(
                        "warning",
                        // TODO: 要告訴前端 status 有 warning 這種可能
                        "Some of the blockId are not found"
                    ),
                    ""
                );
        }
        else {
            System.out.println("Success, all operation completed");
            System.out.println("successfulBlocks :　" + successfulBlocks);
            return new Payload <Msg, String> (
                    new Msg(
                        "success",
                        "All operation completed"
                        ),
                    ""
                );
        }
    }


    // + clear()

    // + addBlock(optional content : String) : ID
    //     // return the newly added block
    // + deleteBlocks(blocks : List<ID>)
    // + hideBlocks(blocks : List<ID>)
    // + showBlocks(blocks : List<ID>)
    // + mergeBlocks(blocks : List<ID>) : ID
    //     // return the newly created block
    // + duplicateBlocks(blocks : List<ID>) : List<ID>
    //     // return the newly duplicated block(s)

    
    
}    


        
