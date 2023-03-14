package com.example.restservice.Transcription;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.example.restservice.Drive.DriveOperator;

public class TranscriptionSupport {
    private String fileId;
    private String name;
    private String FILES_LOCATION = "./tmp/";

    public TranscriptionSupport(String fileId, String name) {
        this.fileId = fileId;
        this.name = name;
        File directory = new File(FILES_LOCATION);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String downloadFile() {
        try {
            ByteArrayOutputStream BAoutputStream = DriveOperator.downloadFile(fileId);
            OutputStream outputStream = new FileOutputStream(new File(FILES_LOCATION + name));
            BAoutputStream.writeTo(outputStream);
            System.out.println("Created the file " + name + " at location " + FILES_LOCATION);
            return FILES_LOCATION + name;
        }
        catch (Exception e) {
            // If there's an error downloading the file, will output here and return null
            System.out.println(e);
            return null;
        }
    }

    public void deleteFile() {
        File file = new File(FILES_LOCATION + name);
        if (file.delete()) {
            System.out.println("Deleted the file " + file.getName());
        }
        else {
            System.out.println("Failed to delete the file " + file.getName());
        }
    }
}
