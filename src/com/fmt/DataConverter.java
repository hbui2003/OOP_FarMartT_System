package com.fmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DataConverter {
	/**
	 * output person json file
	 */
	public static String personJson(HashMap<String, Person> person, String outputFile) {
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		Gson gSon = gb.create();
		String gsonOutput = gSon.toJson(person);
		File f = new File(outputFile);
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.print(gsonOutput);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return gsonOutput;
	}

	/**
	 * output store json file
	 */
	public static String storeJson(HashMap<String, Store> store, String outputFile) {
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		Gson gSon = gb.create();
		String gsonOutput = gSon.toJson(store);
		File f = new File(outputFile);
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.print(gsonOutput);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return gsonOutput;
	}

	/**
	 * output item json file
	 */
	public static String itemJson(HashMap<String, Item> item, String outputFile) {
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		Gson gSon = gb.create();
		String gsonOutput = gSon.toJson(item);
		File f = new File(outputFile);
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.print(gsonOutput);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return gsonOutput;
	}

	/**
	 * output person xml file
	 */
	public static String personXML(HashMap<String, Person> person, String outputFile) {
		XStream xStream = new XStream(new DomDriver());
		File f = new File(outputFile);
		String toXML = xStream.toXML(person);
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.print(toXML);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return toXML;
	}

	/**
	 * output store xml file
	 */
	public static String storeXML(HashMap<String, Store> store, String outputFile) {
		XStream xStream = new XStream(new DomDriver());
		File f = new File(outputFile);
		String toXML = xStream.toXML(store);
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.print(toXML);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return toXML;
	}

	/**
	 * output item xml file
	 */
	public static String itemXML(HashMap<String, Item> item, String outputFile) {
		XStream xStream = new XStream(new DomDriver());
		File f = new File(outputFile);
		String toXML = xStream.toXML(item);
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.print(toXML);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return toXML;
	}

	public static void main(String args[]) {
		HashMap<String, Person> person = loadData.loadPerson();
		DataConverter.personJson(person, "data/Persons.json");
		DataConverter.personXML(person, "data/Persons.xml");

		HashMap<String, Store> store = loadData.loadStore(person);
		DataConverter.storeJson(store, "data/Stores.json");
		DataConverter.storeXML(store, "data/Stores.xml");

		HashMap<String, Item> item = loadData.loadItem();
		DataConverter.itemJson(item, "data/Items.json");
		DataConverter.itemXML(item, "data/Items.xml");
	}
}
