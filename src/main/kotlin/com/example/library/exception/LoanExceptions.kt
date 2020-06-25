package com.example.library.exception

import org.springframework.http.HttpStatus

class LoanNotFoundException : BasicException {
    constructor(userId: Long, barcode: Long) :
        super("The book with barcode $barcode is not currently on loan to user $userId", HttpStatus.NOT_FOUND)

    constructor(loanId: Long) : super("Could not find loan with id $loanId", HttpStatus.NOT_FOUND)
}

class LoanNotAllowedException : BasicException {
    constructor(barcode: Long) :
        super("The book with barcode $barcode is not available for loan", HttpStatus.FORBIDDEN)

    constructor(fine: Long, maxFine: Long) :
        super("This user may not borrow books until the current fine of $fine cents is reduced to below $maxFine cents",
            HttpStatus.FORBIDDEN)
}

class RenewalNotAllowedException : BasicException {
    constructor(numRenewals: Int) :
        super("This book has already been renewed $numRenewals times and cannot be renewed again", HttpStatus.FORBIDDEN)

    constructor(fine: Long, maxFine: Long) :
        super("This user may not renew books until the current fine of $fine cents is reduced to below $maxFine cents",
            HttpStatus.FORBIDDEN)
}