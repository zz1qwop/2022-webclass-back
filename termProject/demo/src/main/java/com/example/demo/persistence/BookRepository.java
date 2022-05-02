package com.example.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String>{
	//@Query("select * from Todo t where t.userId = ?1")
	List<BookEntity> findByUserId(String userId);
	List<BookEntity> findByTitle(String title);
}
