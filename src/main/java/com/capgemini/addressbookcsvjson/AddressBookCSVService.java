package com.capgemini.addressbookcsvjson;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

public class AddressBookCSVService {
	private static String HOME;
	private List<Contact> contactList;
	private HashMap<String, List<Contact>> addressBookMap;

	public AddressBookCSVService() {
		HOME = "C:\\Users\\shashi7\\eclipse-workspace\\AddressBookCSV-JSON\\src\\main\\java\\com\\capgemini\\addressbookcsvjson\\Address CSV contacts";
		addressBookMap = new HashMap<String, List<Contact>>();
		readDataFromAddressBook();
	}

	public void readDataFromAddressBook() {
		try {
			Files.walk(Paths.get(HOME)).filter(Files::isRegularFile).forEach(file -> {
				int ctr = 0;
				contactList = new ArrayList<Contact>();
				try {
					Reader reader = Files.newBufferedReader(file.toAbsolutePath());
					CSVReader csvReader = new CSVReader(reader);
					String[] nextRecord;
					while ((nextRecord = csvReader.readNext()) != null) {
						if (ctr != 0) {
							String address = nextRecord[0];
							String city = nextRecord[1];
							String email = nextRecord[2];
							String firstName = nextRecord[3];
							String lastName = nextRecord[4];
							String phoneNo = nextRecord[5];
							String state = nextRecord[6];
							String zip = nextRecord[7];
							Contact contact = new Contact(firstName, lastName, address, city, state, zip, phoneNo,
									email);
							contactList.add(contact);
						}
						ctr++;
					}
					String fileName = file.toAbsolutePath().toString().replace(HOME + "\\", "");
					addressBookMap.put(fileName, contactList);

				} catch (IOException | CsvValidationException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public boolean addAddressBook(String bookName) {
		Path addressBooks = Paths.get(HOME + "/" + bookName + ".csv");
		if (Files.notExists(Paths.get(HOME + "/" + bookName + ".csv"))) {
			try {
				Files.createFile(addressBooks);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public void writeContactToAddressBook(Contact contactObj, String addressBookName) {
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(HOME + "\\" + addressBookName + ".csv"));
			StatefulBeanToCsv<Contact> beanToCsv = new StatefulBeanToCsvBuilder<Contact>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			contactList.add(contactObj);
			try {
				beanToCsv.write(contactList);
				writer.close();
			} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void print() {
		addressBookMap.entrySet().stream().map(entry -> entry.getValue()).forEach(System.out::println);
	}

	public static String getHOME() {
		return HOME;
	}

	public static void setHOME(String hOME) {
		HOME = hOME;
	}

	public HashMap<String, List<Contact>> getAddressBookMap() {
		return addressBookMap;
	}

	public void setAddressBookMap(HashMap<String, List<Contact>> addressBookMap) {
		this.addressBookMap = addressBookMap;
	}

}
