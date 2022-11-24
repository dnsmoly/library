package com.smoly.services;

import com.smoly.models.Book;
import com.smoly.models.Person;
import com.smoly.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Page<Book> findAllPaginated(int page, int itemsPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("year")));
        return booksRepository.findAll(PageRequest.of(page, itemsPerPage));
    }

    public List<Book> findAllSorted() {
        return booksRepository.findAll(Sort.by("year"));
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Optional<Person> getPersonByBookId(int id) {
        Optional<Book> book = booksRepository.findById(id);

        if (!book.isPresent()) {
            return Optional.empty();
        }
        if (book.get().getOwner() != null) {
            return Optional.of(book.get().getOwner());
        }
        return Optional.empty();
    }

    @Transactional
    public void setOwner(int id, Person person) {
        Optional<Book> book = booksRepository.findById(id);
        if (!book.isPresent()) {
            return;
        }
        book.get().setOwner(person);
        book.get().setTakenAt(new Date());
        booksRepository.save(book.get());

    }

    @Transactional
    public void freeBook(int id) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent()) {
            book.get().setOwner(null);
            book.get().setTakenAt(null);
        }
    }

    public List<Book> findByNameContaining(String searchString) {
        return booksRepository.findByNameContainingIgnoreCase(searchString);
    }
}
