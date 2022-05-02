package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BookEntity;
import com.example.demo.persistence.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookService {
	
	@Autowired
	private BookRepository repository;
	
	// 추가
	public List<BookEntity> retrieveAll(){
		return repository.findAll();
	}
	
	public List<BookEntity> retrieve(final String userId){
		return repository.findByUserId(userId);
	}
	
	public List<BookEntity> create(final BookEntity entity){
		validate(entity);
		repository.save(entity);
		
		log.info("Entity Id : {} is saved.", entity.getId());
		
		return repository.findByUserId(entity.getUserId());
	}
	
	public List<BookEntity> search(final String title){
		return repository.findByTitle(title);
	}
	
	public List<BookEntity> update(final BookEntity entity){
		validate(entity);
		
		final Optional<BookEntity> original = repository.findById(entity.getId());
		
		original.ifPresent(todo -> {
			todo.setTitle(entity.getTitle());
			todo.setAuthor(entity.getAuthor());
			todo.setPublisher(entity.getPublisher());
			todo.setUserId(entity.getUserId());
			
			repository.save(todo);
		});
		
		return retrieve(entity.getUserId());
	}
	
	public List<BookEntity> delete(final BookEntity entity){
		validate(entity);
		
		try {
			repository.delete(entity);
		}catch(Exception e) {
			log.error("error deleting entity", entity.getId(), e);
			
			throw new RuntimeException("error deleting entity "+ entity.getId());
		}
		
		return retrieve(entity.getUserId());
	}
	
	private void validate(final BookEntity entity) {
		// Validations
				if(entity == null) {
					log.warn("Entity cannot be null");
					throw new RuntimeException("Entity cannot be null");
				}
				
// 수정				
//				if(entity.getUserId() == null) {
//					log.warn("Unknown user.");
//					throw new RuntimeException("Unknown user.");
//				}
	}
}
