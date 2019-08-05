package com.jojo.util;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MongoDBUtil {

    private static final String MONGODB_URI = "mongodb://root:root@127.0.0.1:27017/test";

    private static final String ALLOT_IMAGE_DATABASE = "test";

    private static final String ALLOT_IMAGE_BUCKET = "allot_image";

    private static MongoDatabase getMongoDatabase() {
        MongoClientURI mongoClientUri = new MongoClientURI(MONGODB_URI);
        MongoClient mongoClient = new MongoClient(mongoClientUri);
        return mongoClient.getDatabase(ALLOT_IMAGE_DATABASE);
    }

    private static DB getDB() {
        MongoClientURI mongoClientUri = new MongoClientURI(MONGODB_URI);
        MongoClient mongoClient = new MongoClient(mongoClientUri);
        return mongoClient.getDB(ALLOT_IMAGE_DATABASE);
    }

    private static void close(DB db) {
        MongoClient mongoClient = db.getMongoClient();
        IOUtils.closeQuietly(mongoClient);
    }

    public static void saveImage(File file, String newFileName) throws IOException {
        saveImage(new FileInputStream(file), newFileName);
    }

    public static void saveImage(InputStream inputStream, String newFileName) throws IOException {
        DB db = getDB();
        saveImage(db, inputStream, newFileName);
        close(db);
    }

    public static void saveImage(DB db, InputStream inputStream, String newFileName) throws IOException {
        GridFS gridFS = new GridFS(db, ALLOT_IMAGE_BUCKET);
        GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream);
        gridFSInputFile.setFilename(newFileName);
        gridFSInputFile.save();
    }

    public static GridFSDBFile getImage(String newFileName) {
        DB db = getDB();
        GridFS gridFS = new GridFS(db, ALLOT_IMAGE_BUCKET);
        return gridFS.findOne(newFileName);
    }

    public static void deleteImage(String newFileName) {
        DB db = getDB();
        GridFS gridFS = new GridFS(db, ALLOT_IMAGE_BUCKET);
        gridFS.remove(newFileName);
    }

    public static void main(String[] args) throws IOException {

        String filepath = "C:\\Users\\72669\\Desktop\\QQ图片20190801175241.png";
        File imageFile = new File(filepath);
        String newFileName = "test1";

//        saveImage(imageFile, newFileName);


        GridFSDBFile imageForOutput = getImage(newFileName);
        imageForOutput.writeTo("C:\\Users\\72669\\Desktop\\QQ图片20190801175241NEW.png");

        deleteImage(newFileName);

        System.out.println("Done");

    }
}
