package com.project.lms.Service;

import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.ReaderResponse;
import com.project.lms.Dto.UserResponse;
import com.project.lms.Entity.Reader;
import com.project.lms.Dao.ReaderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {

    @Autowired
    private ReaderDao readerDao;

    private ReaderResponse toReaderResponse(Reader reader) {
        return new ReaderResponse(
                reader.getUserId(),
                reader.getName(),
                reader.getEmail(),
                reader.getAddress(),
                reader.getPhones()
        );
    }

    public ApiResponse<List<ReaderResponse>> getAllReaders() {

        List<Reader> readers = readerDao.findAll();

        if (readers.isEmpty()) {
            return ApiResponse.fail("No readers found.");
        }

        List<ReaderResponse> response =
                readers.stream()
                        .map(this::toReaderResponse)
                        .toList();

        return ApiResponse.ok("All readers fetched.", response);
    }

    public ApiResponse<ReaderResponse> getReaderById(Long id) {

        Optional<Reader> readerOptional = readerDao.findById(id);

        return readerOptional.map(reader -> ApiResponse.ok(
                "Reader fetched.",
                toReaderResponse(reader)
        )).orElseGet(() -> ApiResponse.fail("Reader not found."));

    }

    public ApiResponse<ReaderResponse> addReader(Reader reader) {

        if (reader.getName() == null || reader.getName().isBlank()) {
            return ApiResponse.fail("Reader name is required.");
        }

        Optional<Reader> existingReader =
                readerDao.findById(reader.getUserId());

        if (existingReader.isPresent()) {
            return ApiResponse.fail("Reader already exists.");
        }

        Reader savedReader = readerDao.save(reader);

        return ApiResponse.ok(
                "Reader added.",
                toReaderResponse(savedReader)
        );
    }

    public ApiResponse<ReaderResponse> updateReader(Long id, Reader reader) {

        Optional<Reader> readerOptional =
                readerDao.findById(id);

        if (readerOptional.isEmpty()) {
            return ApiResponse.fail("Reader not found.");
        }

        Reader existingReader = readerOptional.get();

        if (reader.getName() != null &&
                !reader.getName().isBlank()) {
            existingReader.setName(reader.getName());
        }

        if (reader.getAddress() != null &&
                !reader.getAddress().isBlank()) {
            existingReader.setAddress(reader.getAddress());
        }

        if (reader.getPhones() != null &&
                !reader.getPhones().isEmpty()) {
            existingReader.setPhones(reader.getPhones());
        }

        readerDao.save(existingReader);

        return ApiResponse.ok(
                "Reader updated.",
                toReaderResponse(existingReader)
        );
    }

    public ApiResponse<Void> deleteReader(Long id) {

        Optional<Reader> readerOptional =
                readerDao.findById(id);

        if (readerOptional.isEmpty()) {
            return ApiResponse.fail("Reader not found.");
        }

        readerDao.deleteById(id);

        return ApiResponse.ok(
                "Reader deleted.",
                null
        );
    }
}