package com.example.restservice.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.restservice.Model.AudioFile;
import com.example.restservice.Model.Block;
import com.mongodb.lang.NonNull;
import java.util.Date;
import java.util.List;
@Document("accounts")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    @Id
    private String id;
    // 指定成 Id mongo 就會把它對應到 _id
    @NonNull
    @Indexed(unique = true)
    private String username;
    
    @NonNull
    private String password;
    
    private List<String> audioFilesId;
    private List<String> blocksId;
    private String driveId;
}