package com.capgemini.addressbookcsvjson;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

public class AddressBookJSONService {
	private static String HOME = "C:\\Users\\shashi7\\eclipse-workspace\\AddressBookCSV-JSON\\src\\main\\java\\com\\capgemini\\addressbookcsvjson\\Address JSON contacts";
	private HashMap<String, List<Contact>> addressBookMap;

	public AddressBookJSONService() {
		addressBookMap = new HashMap<String, List<Contact>>();
		readDataFromAddressBook();
	}

	public void readDataFromAddressBook() {
		try {
			Files.walk(Paths.get(HOME)).filter(Files::isRegularFile).forEach(file -> {
				List<Contact> contactList = new ArrayList<Contact>();
				try {
					Reader reader = Files.newBufferedReader(file.toAbsolutePath());
					contactList.addAll(Arrays.asList(new Gson().fromJson(reader, Contact[].class)));
					String fileName = file.toAbsolutePath().toString().replace(HOME + "\\", "");
					addressBookMap.put(fileName, contactList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public boolean addAddressBook(String bookName) {
		Path addressBooks = Paths.get(HOME + "/" + bookName + ".json");
		if (Files.notExists(Paths.get(HOME + "/" + bookName + ".json"))) {
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
			Writer writer = Files.newBufferedWriter(Paths.get(HOME + "\\" + addressBookName + ".json"));
			List<Contact> contactList = addressBookMap.get(addressBookName);
			if (contactList == null) {
				contactList = new ArrayList<Contact>();
			}
			contactList.add(contactObj);
			new Gson().toJson(contactList, writer);
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void print() {
		addressBookMap.entrySet().stream().map(entry -> entry.getValue()).forEach(System.out::println);
	}

	public HashMap<String, List<Contact>> getAddressBookMap() {
		return addressBookMap;
	}

	public void setAddressBookMap(HashMap<String, List<Contact>> addressBookMap) {
		this.addressBookMap = addressBookMap;
	}
}
