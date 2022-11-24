package com.smoly.controllers;

import com.smoly.models.Book;
import com.smoly.models.Person;
import com.smoly.services.BooksService;
import com.smoly.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(name = "page") Optional<Integer> page,
                        @RequestParam(name = "books_per_page") Optional<Integer> booksPerPage,
                        @RequestParam(name = "sort_by_year") Optional<Boolean> sortByYear) {
        boolean paginate = page.isPresent() && booksPerPage.isPresent();
        boolean sort = sortByYear.isPresent() && sortByYear.get();
        model.addAttribute("paginate", paginate);
        model.addAttribute("sort", sort);
        if (paginate && sort) {
            Page<Book> bookPage = booksService.findAllPaginated(page.get() - 1,
                    booksPerPage.get(), true);
            model.addAttribute("books", bookPage.getContent());
            model.addAttribute("totalPages", bookPage.getTotalPages());
        }
        else {
            if (paginate) {
                Page<Book> bookPage = booksService.findAllPaginated(page.get() - 1,
                        booksPerPage.get(), false);
                model.addAttribute("books", bookPage.getContent());
                model.addAttribute("totalPages", bookPage.getTotalPages());
            } else if (sort){
                model.addAttribute("books", booksService.findAllSorted());
            } else {
                model.addAttribute("books", booksService.findAll());
            }
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        Optional<Person> owner = booksService.getPersonByBookId(id);

        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        booksService.setOwner(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.freeBook(id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam Optional<String> keyword) {
        if (keyword.isPresent()) {
            model.addAttribute("books", booksService.findByNameContaining(keyword.get()));
            model.addAttribute("keyword", keyword.get());
        }
        return "books/search";
    }
}
