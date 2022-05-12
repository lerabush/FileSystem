package ru.c19501.core.repository.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.c19501.core.config.ConfigLoader;
import ru.c19501.core.files.FileRecord;
import ru.c19501.core.files.Views;
import ru.c19501.core.repository.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class JsonRepository extends Repository {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonRepository() {
        super();
        Properties prop = ConfigLoader.properties;
        this.systemFileName = prop.getProperty("fs.systemJSONFileName");
        this.systemRepository = prop.getProperty("fs.systemJSONRepository");
    }

    @Override
    public void writeRepository() {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            File jsonFile = new File(systemRepository + '/' + systemFileName);
            objectMapper.writerWithView(Views.Internal.class).writeValue(new FileOutputStream(jsonFile), this);
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String fileRecordsToString(FileRecord fileRecord) {
        try {
            return objectMapper.writerWithView(Views.Public.class).writeValueAsString(fileRecord);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String fileRecordsToString(List<FileRecord> fileRecords) {
        try {
            return objectMapper.writerWithView(Views.Public.class).writeValueAsString(fileRecords);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void print() {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String s = objectMapper.writerWithView(Views.Public.class).writeValueAsString(this);
            //TODO add logging to spot bugs
            System.out.print(s);
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
