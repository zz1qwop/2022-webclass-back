package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.BookEntity;
import com.example.demo.service.BookService;

@RestController
@RequestMapping("book")
public class BookController {

	@Autowired
	private BookService service;
	

	@PostMapping
	public ResponseEntity<?> createBook(@RequestBody BookDTO dto){
		try {
			BookEntity entity = BookDTO.toEntity(dto);
			
			entity.setId(null);
			
			List<BookEntity> entities = service.create(entity);
			
			List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
			
			ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
			
			return ResponseEntity.ok().body(response);
		}catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	//@GetMapping
	@PostMapping("/search")
	public ResponseEntity<?> searchBookList(@RequestBody BookDTO dto){
		
		List<BookEntity> entities = service.search(dto.getTitle());
		
		List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
		
		ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	// 테이블 데이터 불러오기 용도
	@GetMapping("/table")
	public ResponseEntity<?> allBookList(){
		List<BookEntity> entities = service.retrieveAll();
		List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
		
		ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody BookDTO dto){

		BookEntity entity = BookDTO.toEntity(dto);

		List<BookEntity> entities = service.update(entity);
		
		List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());

		ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody BookDTO dto){
		try {
			// 기존 : id로 삭제, 현재 : title로 삭제.
			
			BookEntity entity = BookDTO.toEntity(dto);

			//List<BookEntity> entities = service.delete(entity);
			
			// 변경
			List<BookEntity> original = service.search(dto.getTitle());
			List<BookEntity> entities = service.delete(original.get(0));
			
			List<BookDTO> dtos = entities.stream().map((e)->new BookDTO(e)).collect(Collectors.toList());
			
			ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
			
			return ResponseEntity.ok().body(response);
			
		}catch(Exception e) {
			String error = e.getMessage();
			ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}
