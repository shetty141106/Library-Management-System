package com.project.lms.Service;

import com.project.lms.Dao.BooksDao;
import com.project.lms.Dao.ReaderDao;
import com.project.lms.Dao.ReservationDao;
import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReservationRequest;
import com.project.lms.Dto.ReservationResponse;
import com.project.lms.Entity.Books;
import com.project.lms.Entity.Reader;
import com.project.lms.Entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private ReaderDao readerDao;

    private ReservationResponse toResponse(Reservation r){
        return new ReservationResponse(
                r.getReservationId(), r.getReservationType(), r.getReader().getUserId(), r.getBook().getIsbn(), r.getStaff().getName(), r.getIssueDate(), r.getDueDate(), r.getReturnDate()
        );
    }

    public ApiResponse<ReservationResponse> issueBook(ReservationRequest req) {
        if (req.getIsbn() == null || req.getUserid() == null) {
            return ApiResponse.fail("Book ID and Reader ID are required.");
        }
        Optional<Books> bookOpt = bookDao.findById(req.getIsbn());
        Optional<Reader> readerOpt = readerDao.findById(req.getUserid());

        if (bookOpt.isEmpty() || readerOpt.isEmpty()) {
            return ApiResponse.fail("Invalid Book ID or Reader ID.");
        }

        Books book = bookOpt.get();
        Reader reader = readerOpt.get();

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setReader(reader);
        reservation.setIssueDate(LocalDate.now());
        reservation.setReturnDate(req.getReturnDate());
        reservation.setDueDate(LocalDate.now().plusDays(14));
        resdao.save(reservation);
        return ApiResponse.ok("Book issued",toResponse(reservation));
    }

    public ApiResponse<ReservationResponse> returnBook(ReservationRequest req) {

    }
}
