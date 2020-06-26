package com.example.library.constants


// Pulling out strings that are used repeatedly times to improve consistency and ease of refactoring
// I'd prefer if these were in a resource file instead of a kotlin file, but there are issues resolving
// properties in some annotations


// Author
const val AUTHOR_DESCRIPTION = "Author details"
const val AUTHOR_REGISTRATION_DESCRIPTION = "Author registration details"
const val AUTHOR_FIRST_NAME_DESCRIPTION = "First name of author"
const val AUTHOR_LAST_NAME_DESCRIPTION = "Last name of author"
const val AUTHOR_BOOKS_DESCRIPTION = "Books in the library written by the author"
const val AUTHOR_ID_DESCRIPTION = "Identification number of author"

const val AUTHOR_FIRST_NAME_EXAMPLE = "Brandon"
const val AUTHOR_LAST_NAME_EXAMPLE = "Sanderson"
const val AUTHOR_ID_EXAMPLE = "5"


// User
const val USER_DESCRIPTION = "User details"
const val USER_REGISTRATION_DESCRIPTION = "New user creation details"
const val USER_LOGIN_DESCRIPTION = "User login details"
const val USER_UPDATE_DESCRIPTION = "User update details. Only fields being updated need to be included. " +
    "Changing user permissions requires admin authorization."
const val USER_EMAIL_DESCRIPTION = "Email of the user"
const val USER_FIRST_NAME_DESCRIPTION = "First name of the user"
const val USER_LAST_NAME_DESCRIPTION = "Last name of the user"
const val USER_PERMISSION_DESCRIPTION = "Permission level of the user"
const val USER_BOOKS_BORROWED_DESCRIPTION = "Number of books currently borrowed by the user"
const val USER_FINE_DESCRIPTION = "Total fine accrued by the user in cents"
const val USER_PASSWORD_DESCRIPTION = "Plaintext password of the user (stored with encryption)"
const val USER_ID_DESCRIPTION = "Identification number of the user"

const val USER_EMAIL_EXAMPLE = "john@email.com"
const val USER_FIRST_NAME_EXAMPLE = "John"
const val USER_LAST_NAME_EXAMPLE = "Doe"
const val USER_PERMISSION_EXAMPLE = "LIBRARIAN"
const val USER_BOOKS_BORROWED_EXAMPLE = "5"
const val USER_FINE_EXAMPLE = "100"
const val USER_PASSWORD_EXAMPLE = "password"
const val USER_ID_EXAMPLE = "1"


// User authentication
const val AUTHENTICATION_USER_DESCRIPTION = "User being authenticated"
const val AUTHENTICATION_DETAILS_DESCRIPTION = "Authentication token and expiration time"


// Book
const val BOOK_DESCRIPTION = "Book details"
const val BOOK_REGISTRATION_DESCRIPTION = "New book creation details"
const val BOOK_TITLE_DESCRIPTION = "Title of the book"
const val BOOK_ISBN_DESCRIPTION = "ISBN-13 of the book as a string of 13 digits, excluding any hyphens or spaces"
const val BOOK_AUTHOR_IDS_DESCRIPTION = "List of identification numbers for all co-authors of this book"
const val BOOK_PUBLISHER_DESCRIPTION = "Publisher of the book"
const val BOOK_LANGUAGE_DESCRIPTION = "Language of the book"
const val BOOK_AVAILABLE_COPIES_DESCRIPTION = "Number of copies available"
const val BOOK_TOTAL_COPIES_DESCRIPTION = "Total number of copies owned by library"

const val BOOK_TITLE_EXAMPLE = "Warbreaker"
const val BOOK_ISBN_EXAMPLE = "9780765360038"
const val BOOK_AUTHOR_IDS_EXAMPLE = "[5]"
const val BOOK_PUBLISHER_EXAMPLE = "Tor Books"
const val BOOK_LANGUAGE_EXAMPLE = "English"
const val BOOK_AVAILABLE_COPIES_EXAMPLE = "3"
const val BOOK_TOTAL_COPIES_EXAMPLE = "4"


// Book copy
const val BOOK_COPY_DESCRIPTION = "Book copy details"
const val BOOK_COPY_REGISTRATION_DESCRIPTION = "New book copy creation details"
const val BOOK_COPY_FORMAT_DESCRIPTION = "Format of the book copy"
const val BOOK_COPY_STATUS_DESCRIPTION = "Status of the book copy"
const val BOOK_COPY_DATE_ADDED_DESCRIPTION = "When the book copy was added to the library's collection"
const val BOOK_COPY_BARCODE_DESCRIPTION = "Barcode of the book copy"

const val BOOK_COPY_FORMAT_EXAMPLE = "PAPERPACK"
const val BOOK_COPY_STATUS_EXAMPLE = "BORROWED"
const val BOOK_COPY_BARCODE_EXAMPLE = "3"


// Loan
const val LOAN_DESCRIPTION = "Loan details"
const val LOAN_REGISTRATION_DESCRIPTION = "New loan creation details"
const val LOAN_DATE_BORROWED_DESCRIPTION = "When the book copy was borrowed"
const val LOAN_DATE_DUE_DESCRIPTION = "When the book copy is due"
const val LOAN_RENEWALS_DESCRIPTION = "Number of times the book has been renewed"
const val LOAN_ID_DESCRIPTION = "Identification number of the loan"

const val LOAN_RENEWALS_EXAMPLE = "1"
const val LOAN_ID_EXAMPLE = "2"
const val DATE_EXAMPLE = "2020-01-01T00:00:00.000000"


// Validation
const val SIZE_VALIDATION_MESSAGE = "Must be between {min} and {max} characters long"
const val REQUIRED_FIELD_VALIDATION_MESSAGE = "This field is required"
const val USER_PERMISSION_VALIDATION_MESSAGE = "Must be one of 'MEMBER', 'LIBRARIAN', and 'ADMIN'"
const val USER_EMAIL_VALIDATION_MESSAGE = "Must be a valid email format"
const val AUTHOR_ID_LIST_VALIDATION_MESSAGE = "Must be a list of at least one author id number"
const val ISBN_VALIDATION_MESSAGE = "ISBN-13 should be entered as a string of 13 digits, excluding any hyphens or spaces"
const val BOOK_COPY_FORMAT_VALIDATION_MESSAGE = "Must be one of 'HARDCOVER', 'PAPERBACK', 'AUDIO', and 'EBOOK'"
const val BOOK_COPY_STATUS_VALIDATION_MESSAGE = "Must be one of 'AVAILABLE', 'BORROWED', and 'PROCESSING'"
const val USER_ID_VALIDATION_MESSAGE = "Must be a valid user id number"
const val BOOK_COPY_BARCODE_VALIDATION_MESSAGE = "Must be a valid barcode"


// OpenApi documentation

// @Operation description messages (this field in the annotation supports markdown)
const val SELF_OR_LIBRARIAN_REQUIRED_MESSAGE = "<p>You must be the user that this resource relates to " +
    "(e.g. you can access your own account info and loans and no one else's), " +
    "or you must have the LIBRARIAN or ADMIN security role.<p>"
const val LIBRARIAN_REQUIRED_MESSAGE = "<p>You must have the LIBRARIAN OR ADMIN security role.</p>"
const val AUTHENTICATION_REQUIRED_MESSAGE = "<p>Authentication is required to access this.</p>"
const val AUTHENTICATION_STEPS_MESSAGE = "<p>To test in the Swagger UI, you need to add a security token to the " +
    "header of your request by clicking the lock icon in the upper right. If you have already signed up, then POST to " +
    "/users/token with your login info. Otherwise, you can POST to /users/ to create a new account.</p>"
const val REDIRECT_TO_LOGIN_MESSAGE = "<p>If you have already created an account, POST to /users/token to sign in.</p>"
const val REDIRECT_TO_SIGNUP_MESSAGE = "<p>If you do not have an account, POST to /users to create one.</p>"
const val AUTHORIZATION_RESPONSE_MESSAGE = "<p>The JSON response includes an authorization token that can be used to " +
    "access secured endpoints.</p>"
const val PAGINATION_MESSAGE = "<p>Results returned are paginated according to the optional " +
    "\${spring.data.web.pageable.page-parameter} and \${spring.data.web.pageable.size-parameter} query parameters. " +
    "Links to the first, last, and neighboring pages are included in the response if they exist."

// Request info messages
const val REQUEST_BODY_VALIDATION_MESSAGE = "To see validation requirements, click the 'schema' button below."

// Request query parameter messages
const val FILTER_BOOKS_BY_AUTHOR_ID_MESSAGE = "Filter to books written by the author with this id. Intended to be " +
    "leveraged in combination with the /authors endpoint for smooth navigation among related resources. " +
    "If included, other optional queries (excluding pagination) will be ignored."
const val FILTER_BOOKS_BY_TITLE_MESSAGE = "Filter to books with titles containing this string. " +
    "Cannot be used with any other optional query parameters."
const val FILTER_AUTHORS_BY_FIRST_NAME_MESSAGE = "Filter to authors with this first name. " +
    "Only used if a last_name query parameter is also present."
const val FILTER_AUTHORS_BY_LAST_NAME_MESSAGE = "Filter to authors with this last name. " +
    "Can be used without any other query parameters."
const val FILTER_BOOK_COPIES_BY_ISBN_MESSAGE = "Filter to copies of the book with this ISBN-13. " +
    "Enter as 13 digits with no hyphens or spaces."
const val FILTER_LOANS_BY_USER_ID_MESSAGE = "Filter to loans belonging to the user with this id number."


// Pagination
const val PAGINATION_PAGE_MESSAGE = "Zero-based page number (0..N) of the paginated output that is returned"
const val PAGINATION_SIZE_MESSAGE = "Number of items per page used to paginate results"