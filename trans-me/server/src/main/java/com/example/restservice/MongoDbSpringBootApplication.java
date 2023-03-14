package com.example.restservice;

import com.example.restservice.Drive.DriveOperator;
import com.example.restservice.Model.Account;
import com.example.restservice.Model.AudioFile;
import com.example.restservice.Model.Block;
import com.example.restservice.Model.Term;
import com.example.restservice.Repository.AccountRepository;
import com.example.restservice.Repository.AudioFileRepository;
import com.example.restservice.Repository.BlockRepository;
import com.example.restservice.Repository.TermRepository;
import com.example.restservice.Service.AccountService;
import com.example.restservice.Service.Login;
import com.example.restservice.Service.Transcription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

@SpringBootApplication
@EnableMongoRepositories
public class MongoDbSpringBootApplication implements CommandLineRunner {
    @Autowired
    // TermRepository termRepo;
    AccountRepository accountRepo;

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    AccountService accountService;

    // test
        // @Autowired
        // AudioFileRepository audioFileRepository;
    
    public static void main(String[] args) {
        SpringApplication.run(MongoDbSpringBootApplication.class, args);
        
    }    

    // https://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java
    // not in use because it might cause the system unstable
    protected static void setEnv(Map<String, String> newenv) throws Exception {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
                    .getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }
    
    @Override
    public void run(String... args) throws Exception {

        System.out.println("--------START--------");
        
        // // List all accounts
        // System.out.println("ALL ACCOUNTS: ");
        // List<Account> itemList = returnAllItems();
        // for (Account t: itemList) {
        //     System.out.println(t.toString());
        // }
    
        // // List all blocks
        // System.out.println("All Blocks: ");
        // List<Block> blockList = blockRepository.findAll();
        // for (Block t: blockList) {
        //     System.out.println(t.toString());
        // }

        // test : transcription v2.0
        // modify audioFile first
            // Optional<AudioFile> fetchedFile = audioFileRepository.findByDriveId("1I-gvn7OH59Be-5ltTLrIUM5dlUd0H5Z-");
            // if (fetchedFile.isPresent()) {
            //     fetchedFile.get().setId("0000");
            //     audioFileRepository.save(fetchedFile.get());
            // }
            // else {
            //     System.out.println("fetchedFile is null");
            // }

        // set path to look for google cloud credential
        // Map<String, String> testMap = new HashMap<String, String>();
        // testMap.put(
        //     "GOOGLE_APPLICATION_CREDENTIALS",
        //     // "H:/我的雲端硬碟/Sync/111-1/SWE/Gitlab/Web-Application/trans-me/server/src/main/java/com/example/restservice/Transcription/credential/application_default_credentials.json"
        //         // 已知可 work
        //     // "H:\\我的雲端硬碟\\Sync\\111-1\\SWE\\Gitlab\\Web-Application\\trans-me\\server\\src\\main\\java\\com\\example\\restservice\\Transcription\\credential"
        //     // "./src\main\java\com\example\restservice\Transcription\credential\application_default_credentials.json"
        //     "./src/main/java/com/example/restservice/Transcription/credential/application_default_credentials.json"
        // );
        // setEnv(testMap);
            // side effect 是系統很不穩，一定要關掉 process 才能跑，所以 maybe 還是手動設好

        // // test : Google API
        //     System.out.println("trial 1");
        //     Transcription.asyncRecognizeFile("./src/main/java/com/example/restservice/Transcription/testAudio/test.wav", "zh-TW", 44100);
        //     // System.out.println("trial 2");
        //     // Transcription.asyncRecognizeFile( "C:/Users/user/Desktop/test.wav");
        //     // 兩種都可以
        
        //     System.out.println("trial 3");
        //     String testString = "gs://example-qscgyj830856700/audio-files/test.wav";
        //     Transcription.asyncRecognizeGcs(testString);
        //     // 可以了
            
        //     // sample rate & language 記得改
        //     // 要用 single channel，audacity 要檢查
        
    
        // createItem("000", "test", "meow2");
// delete("test");
        
        // test : deleteBlock
            // ArrayList<String> testList = new ArrayList<String>();
            // String testString = "6384c6e3d227b537c679ea72";  
            // testList.add(testString);
            // testList.add("6384c80bd227b537c679ea73");
            // // accountService.deleteBlocks(testList);
            

        // test : list containing query
        // failed
            // ArrayList<String> testList = new ArrayList<String>();
            // String testString = "638ec68a4ac7443cf2f2891e";
            
            // testList.add(testString);
            // // Account test = accountRepo.findByBlocksIdContaining(testList);
            // // System.out.println("account found : " + test);

            // Account test = accountRepo.findByBlocksIdContaining(testString);
            // System.out.println("account found : " + test);

            // // test = accountRepo.findByUsername("user0");
            // List<Account> test2 = accountRepo.findByUsernameContaining("POST");
            // System.out.println("account found : " + test2);


        // File testFile = new File("testFile.txt");
        // if (testFile.createNewFile()) {
        //     System.out.println("File created: " + testFile.getName());
        // } else {
        //     System.out.println("File already exists.");
        // }
        // FileWriter myWriter = new FileWriter(testFile.getName());
        // myWriter.write("user0 : " + test);
        // myWriter.close();
        // System.out.println("Successfully wrote to the file.");

        
        // // test : updateBlock
        //     ArrayList<Block> test = new ArrayList<Block>();
        //     List<String> test_id = accountRepo.findByUsername("POST").getBlocksID();
        //     test.add(new Block(
        //         test_id.get(0),    
        //         "new content 1",
        //         true
        //     ));
        //     test.add(new Block(
        //         test_id.get(1),    
        //         "new content 2",
        //         false
        //     ));
        //     accountService.updateBlocks(test);
        
        // // test : createBlock
        //     ArrayList<Block> test = new ArrayList<Block>();
        //     test.add(new Block(
        //         "hello block",    
        //         false
        //         ));
        //     // test.add(accountRepo.findByUsername("POST").getBlocksID().get(1));
        //     accountService.createBlocks(test);

        // test : getBlock
            // ArrayList<String> test = new ArrayList<String>();
            // test.add(accountRepo.findByUsername("POST").getBlocksID().get(0));
            // test.add(accountRepo.findByUsername("POST").getBlocksID().get(1));
            // accountService.getBlocks(test);

        // test : create new block    
            // Block testBlock = new Block();
            // testBlock.setContent("test block content");
            // blockRepository.save(testBlock);
            // Account test = accountRepo.findByUsername("POST");
            // test.getBlocksID().add(testBlock.getId());
            // accountRepo.save(test);

        // test : save    
            // Account test = accountRepo.findByUsername("POST");
            // test.setPassword("new password");
            // accountRepo.save(test);

        // test : accountRepo.findByUsername    
            // System.out.println(accountRepo.findByUsername("POST"));
            //     // 有找到
            // System.out.println(accountRepo.findByUsername("GG"));
            //     // null

        // test : ArrayList    
            // ArrayList<String> test = new ArrayList<String>();
            // test.add("testID");

        // test : update    
            // System.out.println(accountService.update(new Account(
            //     "",    
            //     "POST",
            //     "testPW",
            //     test,
            //     new ArrayList<String>()
            // )));

        // test : Login     
            // System.out.println(login.login("POST", ""));
            // System.out.println(login.login("POST", "no"));
            // System.out.println(login.login("test", ""));
    
        // String customName = "name";
        // System.out.println("Find term by name: " + customName);
        // Term termQueryByName = findTermByName(customName);
        // System.out.println("Term description: " + termQueryByName.getDescription());

        // updateTermDescription(customName, "newDescription");

        // System.out.println("Number of terms in collection: " + getCountOfTerms());
    }

    public void createItem(String id, String username, String password) {
        accountRepo.save(
            // new Account(id, username, password, 
            //     new ArrayList<String>(), new ArrayList<String>())
            new Account(username = username, password = password)
        );
    }

    public List<Account> returnAllItems() {
        return accountRepo.findAll();
    }

    // public Term findTermByName(String name) {
    //     return termRepo.findItemByName(name);
    // }

    // public long getCountOfTerms() {
    //     return termRepo.count();
    // }

    // public void updateTermDescription(String name, String description) {
    //     Term term = termRepo.findItemByName(name);
    //     term.setDescription(description);
    //     Term updatedTerm = termRepo.save(term);
    //     if (updatedTerm != null) System.out.println("Successfully updated");
    // }

    public void delete(String username) {
        accountRepo.deleteAllByUsername(username);
    }

    // =============================================================

    // public void createItem(String name, String link, String description) {
    //     termRepo.save(new Term(name, link, description));
    // }

    // public List<Term> returnAllItems() {
    //     return termRepo.findAll();
    // }

    // public Term findTermByName(String name) {
    //     return termRepo.findItemByName(name);
    // }

    // public long getCountOfTerms() {
    //     return termRepo.count();
    // }

    // public void updateTermDescription(String name, String description) {
    //     Term term = termRepo.findItemByName(name);
    //     term.setDescription(description);
    //     Term updatedTerm = termRepo.save(term);
    //     if (updatedTerm != null) System.out.println("Successfully updated");
    // }

    // public void deleteTermByName(String name) {
    //     termRepo.deleteById(name);
    // }
}
