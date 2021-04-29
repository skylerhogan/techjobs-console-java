package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;
    

//    private static ArrayList<HashMap<String, String>> cloneAllJobs = new ArrayList<HashMap<String, String>>();

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }
        //bonus mission: sort values alphabetically
        Collections.sort(values);

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        //bonus mission: return a copy of allJobs
        ArrayList<HashMap<String, String>> clonedJobs = (ArrayList<HashMap<String, String>>) allJobs.clone();

        return clonedJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     * <p>
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Column that should be searched.
     * @param value  Value of the field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue.toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT))) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    public static ArrayList<HashMap<String, String>> findByValue(String searchTerm) {

        //load data
        loadData();

        // declare, instantiate, and initialize new ArrayList to store jobs
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        //iterates through allJobs and adds them to the jobs ArrayList if found
        for (Map<String, String> entry : allJobs) {
            //this if statement determines whether or not each job contains the given searchTerm by
            //returning each entry as a lowercase string and then comparing said entry to the lowercase searchTerm
            //and adds the entry to jobs ArrayList if present
            if (entry.toString().toLowerCase(Locale.ROOT).contains(searchTerm.toLowerCase(Locale.ROOT))) {
                jobs.add((HashMap<String, String>) entry);
                }
            }
            return jobs;
        }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }
}
