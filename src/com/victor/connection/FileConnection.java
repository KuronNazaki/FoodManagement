package com.victor.connection;

import java.util.List;

public interface FileConnection<T> {
	List<T> getFromFile();
	boolean saveToFile();
}
