package com.project.lms.Service;

import com.project.lms.Dao.BooksDao;
import com.project.lms.Dao.ReaderDao;
import com.project.lms.Dao.ReservationDao;
import com.project.lms.Dao.StaffDao;
import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReservationRequest;
import com.project.lms.Dto.ReservationResponse;
import com.project.lms.Entity.Books;
import com.project.lms.Entity.Reader;
import com.project.lms.Entity.Reservation;
import com.project.lms.Entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationDao resdao;

    @Autowired
    private BooksDao bookDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private ReaderDao readerDao;

    private ReservationResponse toResponse(Reservation r){
        return new ReservationResponse(
                r.getReservationId(), r.getReservationType(), r.getReader().getUserId(), r.getBook().getIsbn(), r.getStaff().getName(), r.getIssueDate(), r.getDueDate(), r.getReturnDate()
        );
    }

    public ApiResponse<ReservationResponse> issueBook(ReservationRequest req) {
        if (req.getIsbn() == null || req.getReaderId() == null) {
            return ApiResponse.fail("Book ID and Reader ID are required.");
        }

        boolean bookAlreadyIssued = resdao.isBookAlreadyIssuedToReader(req.getIsbn(), req.getReaderId());
        if(bookAlreadyIssued)
            return ApiResponse.fail("This book is already issued to the user.");

        Optional<Books> bookOpt = bookDao.findById(req.getIsbn());
        Optional<Reader> readerOpt = readerDao.findById(req.getReaderId());
        Optional<Staff> staffOpt = staffDao.findById(req.getStaffId());

        if (bookOpt.isEmpty() || bookOpt.get().getQuantity() <=0) {
            return ApiResponse.fail("Book not found or out of stock.");
        }
        Books book = bookOpt.get();
        Reservation reservation = new Reservation();

        Reader reader;
        if(readerOpt.isPresent()){
            reader = readerOpt.get();
            reservation.setReader(reader);
        }
        else{
            reader = new Reader();
            reader.setUserId(req.getReaderId());
            reader.setName(req.getReaderName());
            reader.setPhones(req.getReaderPhones());
            reservation.setReader(reader);
        }

        if(staffOpt.isEmpty())
            return ApiResponse.fail("No such staff found..");

        reservation.setBook(book);
        reservation.setIssueDate(LocalDate.now());
        reservation.setDueDate(LocalDate.now().plusDays(14));
        reservation.setStaff(staffOpt.get());
        book.setQuantity(book.getQuantity()-1);
        reservation.setBook(book);
        reservation.setReservationType("ISSUED");
        Reservation savedReservation = resdao.save(reservation);
        return ApiResponse.ok("Book issued",toResponse(savedReservation));
    }

    public ApiResponse<ReservationResponse> returnBook(Long resid) {
        Optional<Reservation> resOpt = resdao.findById(resid);
        if(resOpt.isEmpty())
            return ApiResponse.fail("No data found.");
        Reservation reservation = resOpt.get();
        if(Objects.equals(reservation.getReservationType(), "RETURNED"))
            return ApiResponse.fail("Book already returned.");
        Optional<Books> booksOpt = bookDao.findById(reservation.getBook().getIsbn());
        Books book = booksOpt.get();
        book.setQuantity(book.getQuantity()+1);
        reservation.setReservationType("RETURNED");
        reservation.setBook(book);
        reservation.setReturnDate(LocalDate.now());
        reservationDao.save(reservation);
        return ApiResponse.ok("Book returned.", null);
    }
}
