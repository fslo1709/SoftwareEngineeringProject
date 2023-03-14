package com.example.restservice.Drive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class DriveOperator {
    // Input: 
    // file: the file as a multipart file coming from a POST method
    // folderId: Account's folder ID to upload the file to
    // Returns:
    // List of two elements
    // First element is the file id from google drive
    // Second element is the download URL for the frontend to download the file
    public static List<String> uploadFile(MultipartFile file, String folderId, String name, String format) {
        try {
            if (folderId == "" || folderId == null) {
                folderId = "130DakidoW74dLcOt0sj_3-hppX_ia25E";
            }
            File fileMetadata = new File();

            fileMetadata.setName(name + "." + format);
            List<String> parents = Arrays.asList(folderId);
            fileMetadata.setParents(parents);

            String mimeType;
            if (format.equals("wav")) {
                mimeType = "audio/x-wav";
            }
            else if (format.equals("webm")) {
                mimeType = "audio/webm";
            }
            else {
                System.out.println("Not the expected file type");
                throw(new Exception("Not the expected file type"));
            }
            
            Drive driveService = GoogleDriveUtils.getDriveService();
            File fileResponse = driveService.files().create(fileMetadata, new InputStreamContent(
                    mimeType,
                    new ByteArrayInputStream(file.getBytes())
                ))
                .setFields("id, webContentLink").execute();
            System.out.println(fileResponse.getId() + " " + fileResponse.getWebContentLink());
            List<String> responseList = Arrays.asList(fileResponse.getId(), fileResponse.getWebContentLink());
            return responseList;
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return null;
    }

    // To create folder inside transme folder in google drive
    // Input: name of the folder (account name)
    // Returns: String
    // File id of the newly created folder
    public static final String createFolder(String folderName) throws IOException {
        String folderIdParent = "130DakidoW74dLcOt0sj_3-hppX_ia25E";
        
        File fileMetadata = new File();

        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        if (folderIdParent != null) {
            List<String> parents = Arrays.asList(folderIdParent);

            fileMetadata.setParents(parents);
        }
        Drive driveService = GoogleDriveUtils.getDriveService();

        // Create a Folder.
        // Returns File object with id & name fields will be assigned values
        File file = driveService.files().create(fileMetadata).setFields("id, name").execute();

        System.out.println("The folder " + file.getName() + " was created succesfully!");

        return file.getId();
    }

    public static void deleteFile(String fileId) throws Exception {
        Drive driveService = GoogleDriveUtils.getDriveService();

        driveService.files().delete(fileId).execute();

        System.out.println("The file " + fileId + " was deleted succesfully!");
    }

    public static ByteArrayOutputStream downloadFile(String fileId) throws Exception {
        Drive driveService = GoogleDriveUtils.getDriveService();
        OutputStream outputStream = new ByteArrayOutputStream();

        driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);

        return (ByteArrayOutputStream) outputStream;
    }

    // Returns the subfolders of the transme folder
    // com.google.api.services.drive.model.File
    public static final List<String> getSubFolders() throws IOException {
        String folderIdParent = "130DakidoW74dLcOt0sj_3-hppX_ia25E";

        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<String> list = new ArrayList<String>();

        String query = " mimeType = 'application/vnd.google-apps.folder' " //
                + " and '" + folderIdParent + "' in parents";

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file.getId());
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }
}