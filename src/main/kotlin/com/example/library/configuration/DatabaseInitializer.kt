package com.example.library.configuration

import com.example.library.dto.UserRegistration
import com.example.library.entity.*
import com.example.library.repository.AuthorRepository
import com.example.library.repository.BookRepository
import com.example.library.repository.UserRepository
import com.example.library.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import kotlin.random.Random


// Initialize entities in the database
// Alternatively, use data.sql file, but easier and less error-prone to use ApplicationRunner
// due to many bidirectional relationships existing among entities
@Configuration
class DatabaseInitializer @Autowired constructor (
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
){
    @Bean
    @Profile("dev", "prod")
    fun adminInitializer() = ApplicationRunner {
        val adminEmail = "admin@library.com"
        val adminPassword: String = System.getenv("LIBRARY_ADMIN_PASSWORD")

        // Need to create admin user through service to have a hashed password
        userService.registerNewUser(
            UserRegistration(adminEmail, "Admin", "Astrator", adminPassword))
        val admin = userRepository.findByEmailIgnoreCase(adminEmail)

        // Replace default permission with ADMIN
        admin!!.permission = UserPermission.ADMIN

        userRepository.save(admin)
    }

    @Bean
    @Profile("dev", "prod")
    fun bookInitializer() = ApplicationRunner {
        val authorList = mutableListOf<Author>()
        val booksList = mutableListOf<List<Book>>()

        // Stephen King
        authorList.add(Author("Stephen", "King"))
        booksList.add(listOf(
            Book("9780450040184", "The Shining", "New English Library", "English"),
            Book("9780385199575", "The Stand", "Doubleday Books", "English"),
            Book("9780450417399", "Misery", "New English Library", "English")
        ))

        // Cassandra Clare
        authorList.add(Author("Cassandra", "Clare"))
        booksList.add(listOf(
            Book("9781416914280", "City of Bones", "Margaret K. McElderry Books", "English"),
            Book("9781416914297", "City of Ashes", "Margaret K. McElderry Books", "English"),
            Book("9781416914303", "City of Glass", "Margaret K. McElderry Books", "English")
        ))

        // Neil Gaiman
        authorList.add(Author("Neil", "Gaiman"))
        booksList.add(listOf(
            Book("9780061139376", "Coraline", "William Morrow Books", "English"),
            Book("9780062255655", "The Ocean at the End of the Lane", "William Morrow Books", "English"),
            Book("9780060530921", "The Graveyard Book", "HarperCollins", "English")
        ))

        // Veronica Roth
        authorList.add(Author("Veronica", "Roth"))
        booksList.add(listOf(
            Book("9780062024039", "Divergent", "Katherine Tegen Books", "English"),
            Book("9780007442911", "Insurgent", "HarperCollins", "English"),
            Book("9780007524273", "Allegiant", "HarperCollins", "English")
        ))

        // N. K. Jemisin
        authorList.add(Author("N. K.", "Jemisin"))
        booksList.add(listOf(
            Book("9780316229296", "The Fifth Season", "Orbit", "English"),
            Book("9780316229265", "The Obelisk Gate ", "Orbit", "English"),
            Book("9780316229241", "The Stone Sky", "Orbit", "English")
        ))

        // J. k. Rowling
        authorList.add(Author("J. k.", "Rowling"))
        booksList.add(listOf(
            Book("9780439554930", "Harry Potter and the Sorcerer's Stone", "Scholastic Inc", "English"),
            Book("9780439064866", "Harry Potter and the Chamber of Secrets", "Scholastic Inc", "English"),
            Book("9780439655484", "Harry Potter and the Prisoner of Azkaban", "Scholastic Inc", "English"),
            Book("9780439139595", "Harry Potter and the Goblet of Fire", "Scholastic Inc", "English"),
            Book("9780439358071", "Harry Potter and the Order of the Phoenix", "Scholastic Inc", "English"),
            Book("9780747581086", "Harry Potter and the Half-Blood Prince", "Scholastic Inc", "English"),
            Book("9780545010221", "Harry Potter and the Deathly Hallows", "Scholastic Inc", "English")
        ))

        // Brandon Sanderson
        authorList.add(Author("Brandon", "Sanderson"))
        booksList.add(listOf(
            Book("9780765311788", "The Final Empire", "Tor Books", "English"),
            Book("9780765316882", "The Well of Ascension", "Tor Books", "English"),
            Book("9780765316899", "The Hero of Ages", "Tor Books", "English"),
            Book("9780765350374", "Elantris", "Tor Fantasy", "English"),
            Book("9780765360038", "Warbreaker", "Tor Fantasy", "English"),
            Book("9780765326355", "The Way of Kings", "Tor Books", "English")
        ))

        // Margaret Atwood
        authorList.add(Author("Margaret", "Atwood"))
        booksList.add(listOf(
            Book("9780385490818", "The Handmaid's Tale", "Anchor Books", "English"),
            Book("9780385543781", "The Testaments", "Nan A. Talese", "English"),
            Book("9780385475716", "Alias Grace", "Doubleday Nan A. Talese", "English"),
            Book("9780385721677", "Oryx and Crake", "Anchor Books", "English")
        ))

        // Lizzi Tremayne
        authorList.add(Author("Lizzi", "Tremayne"))
        booksList.add(listOf(
            Book("9780994143143", "A Long Trail Rolling", "Blue Mist Publishing", "English"),
            Book("9780994144737", "The Hills of Gold Unchanging", "Blue Mist Publishing", "English"),
            Book("9780995115781", "A Sea of Green Unfolding ", "Blue Mist Publishing", "English")
        ))

        // Vladimir Nabokov
        authorList.add(Author("Vladimir", "Nabokov"))
        booksList.add(listOf(
            Book("9780141185262", "Pale Fire", "Penguin Books Ltd", "English"),
            Book("9780679723165", "Lolita", "Penguin Modern Classics", "English"),
            Book("9781400041985", "Pnin", "Everyman's Library", "English"),
            Book("9780811216746", "Laughter in the Dark", "New Directions", "English")
        ))

        // Save and generate book copies
        for ((author, books) in authorList.zip(booksList)) {
            saveAuthorsAndBooks(author, books)
            for (book in books) {
                generateAndSaveBookCopies(book, 3, 5)
            }
        }
    }

    // Set up relationship between author and their books and save all entities
    fun saveAuthorsAndBooks(author: Author, books: List<Book>) {
        authorRepository.save(author)
        for (book in books) {
            book.authors.add(author)
            bookRepository.save(book)
        }
    }

    // For a given Book entity, generate a number of BookCopies between the min and max provided (both inclusive)
    // BookStatus for all are set to AVAILABLE, and BookFormat is selected randomly
    fun generateAndSaveBookCopies(book: Book, min: Int, max: Int) {
        val possibleFormats = BookFormat.values()
        val numberOfCopies = Random.nextInt(min, max)
        for (i in 0..numberOfCopies) {
            val copy = BookCopy(
                book = book,
                format = possibleFormats[Random.nextInt(possibleFormats.size)],
                status = BookStatus.AVAILABLE
            )
            book.bookCopies.add(copy)
        }
        bookRepository.save(book)
        // copies saved through cascade
    }
}