package Main;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        System.out.println("Quantum book store: Quantum Bookstore Project Initialized.");
        BookstoreTest.run();
    }
}

abstract class Book {
    protected String isbn;
    protected String title;
    protected String author;
    protected int year;
    protected double price;

    public Book(String isbn, String title, String author, int year, double price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public double getPrice() { return price; }

    public abstract double buy(int quantity, String email, String address) throws Exception;
}

class PaperBook extends Book {
    private int stock;

    public PaperBook(String isbn, String title, String author, int year, double price, int stock) {
        super(isbn, title, author, year, price);
        this.stock = stock;
    }

    public int getStock() { return stock; }

    @Override
    public double buy(int quantity, String email, String address) throws Exception {
        if (quantity > stock) {
            throw new Exception("Quantum book store: Not enough stock for PaperBook: " + title);
        }
        stock -= quantity;
        // Simulate shipping
        System.out.println("Quantum book store: Shipping " + quantity + " paper book(s) to " + address);
        return price * quantity;
    }
}

class EBook extends Book {
    private String filetype;

    public EBook(String isbn, String title, String author, int year, double price, String filetype) {
        super(isbn, title, author, year, price);
        this.filetype = filetype;
    }

    public String getFiletype() { return filetype; }

    @Override
    public double buy(int quantity, String email, String address) {
        // Simulate email delivery
        System.out.println("Quantum book store: Sending " + quantity + " ebook(s) to " + email);
        return price * quantity;
    }
}

class ShowcaseBook extends Book {
    public ShowcaseBook(String isbn, String title, String author, int year) {
        super(isbn, title, author, year, 0.0);
    }

    @Override
    public double buy(int quantity, String email, String address) throws Exception {
        throw new Exception("Quantum book store: Showcase books are not for sale: " + title);
    }
}

class Bookstore {
    private Map<String, Book> inventory = new HashMap<>();

    public void addBook(Book book) {
        inventory.put(book.getIsbn(), book);
        System.out.println("Quantum book store: Added book - " + book.getTitle());
    }

    public List<Book> removeOutdated(int currentYear, int years) {
        List<String> toRemove = new ArrayList<>();
        List<Book> removedBooks = new ArrayList<>();

        for (Book book : inventory.values()) {
            if (currentYear - book.getYear() > years) {
                toRemove.add(book.getIsbn());
                removedBooks.add(book);
            }
        }

        for (String isbn : toRemove) {
            Book removed = inventory.remove(isbn);
            System.out.println("Quantum book store: Removed outdated book - " + removed.getTitle());
        }

        return removedBooks;
    }

    public double buyBook(String isbn, int quantity, String email, String address) throws Exception {
        Book book = inventory.get(isbn);
        if (book == null) {
            throw new Exception("Quantum book store: Book not found: " + isbn);
        }
        double paid = book.buy(quantity, email, address);
        System.out.println("Quantum book store: Purchase successful - " + quantity +
                " copies of '" + book.getTitle() + "' for $" + paid);
        return paid;
    }

    public Set<String> getInventoryIsbns() {
        return inventory.keySet();
    }
}

class BookstoreTest {
    public static void run() {
        Bookstore store = new Bookstore();

        // Add books with author names
        store.addBook(new PaperBook("123", "Paper Book", "John Doe", 2010, 20.0, 5));
        store.addBook(new EBook("456", "EBook", "Jane Smith", 2018, 10.0, "pdf"));
        store.addBook(new ShowcaseBook("789", "Showcase", "Famous Author", 2015));

        try {
            System.out.println("\nQuantum book store: --- Testing Paper Book Purchase ---");
            double paid = store.buyBook("123", 2, "user@example.com", "123 Main St");
            System.out.println("Quantum book store: Total paid: $" + paid);
        } catch (Exception e) {
            System.out.println("Quantum book store: Error - " + e.getMessage());
        }

        try {
            System.out.println("\nQuantum book store: --- Testing EBook Purchase ---");
            double paid = store.buyBook("456", 1, "user@example.com", "");
            System.out.println("Quantum book store: Total paid: $" + paid);
        } catch (Exception e) {
            System.out.println("Quantum book store: Error - " + e.getMessage());
        }

        try {
            System.out.println("\nQuantum book store: --- Testing Showcase Book Purchase ---");
            store.buyBook("789", 1, "user@example.com", "");
        } catch (Exception e) {
            System.out.println("Quantum book store: Error - " + e.getMessage());
        }

        System.out.println("\nQuantum book store: --- Testing Remove Outdated Books ---");
        List<Book> removed = store.removeOutdated(Year.now().getValue(), 10);
        System.out.println("Quantum book store: Removed " + removed.size() + " outdated books");
        System.out.println("Quantum book store: Remaining inventory ISBNs: " + store.getInventoryIsbns());
    }
}