package com.victor.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {
	Optional<T> find(ID id);
	List<T> findAll();
	boolean add(T o);
	boolean delete(T o);
}
