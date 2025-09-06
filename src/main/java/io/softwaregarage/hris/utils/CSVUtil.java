package io.softwaregarage.hris.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class CSVUtil {
    private static final Logger logger = LoggerFactory.getLogger(CSVUtil.class);
    private static CSVUtil INSTANCE;

    private CSVUtil() {
    }

    public synchronized static CSVUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CSVUtil();
        }

        return INSTANCE;
    }

    public static List<String[]> readCSVData(MultiFileMemoryBuffer buffer) {
        List<String[]> csvDataList = new ArrayList<>();

        Set<String> setOfFiles =  buffer.getFiles();
        Iterator<String> iterator = setOfFiles.iterator();

        while (iterator.hasNext()) {
            String fileName = iterator.next();
            InputStream inputStream = buffer.getInputStream(fileName);
            Reader reader = new InputStreamReader(inputStream);

            try {
                CSVReader csvReader = new CSVReader(reader);
                String[] csvLine;

                while ((csvLine = csvReader.readNext()) != null) {
                    if (StringUtil.isNotDate(csvLine[0])) {
                        continue;
                    }

                    logger.info(Arrays.toString(csvLine));
                    csvDataList.add(csvLine);
                }
            } catch (IOException | CsvValidationException exception) {
                logger.error(exception.getMessage());
            }
        }
        return csvDataList;
    }
}
