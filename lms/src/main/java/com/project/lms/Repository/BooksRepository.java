package com.project.lms.Repository;


import com.project.lms.Entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<Books, String> {
}
