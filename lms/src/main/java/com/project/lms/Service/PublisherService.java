package com.project.lms.Service;

import com.project.lms.Dao.PublisherDao;
import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.PublisherResponse;
import com.project.lms.Entity.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    @Autowired
    private PublisherDao publisherDao;

    private PublisherResponse toPublisherResponse(Publisher publisher) {
        return new PublisherResponse(
                publisher.getPublisher_id(),
                publisher.getName(),
                publisher.getBooks()
        );
    }

    public ApiResponse<List<PublisherResponse>> getAllPublishers() {

        List<Publisher> publishers = publisherDao.findAll();

        if (publishers.isEmpty()) {
            return ApiResponse.fail("No publishers found.");
        }

        List<PublisherResponse> response = publishers.stream()
                .map(this::toPublisherResponse)
                .toList();

        return ApiResponse.ok("All publishers fetched.", response);
    }

    public ApiResponse<PublisherResponse> getPublisherById(Integer id) {

        Optional<Publisher> publisherOptional =
                publisherDao.findById(id);

        if (publisherOptional.isEmpty()) {
            return ApiResponse.fail("Publisher not found.");
        }

        return ApiResponse.ok(
                "Publisher fetched.",
                toPublisherResponse(publisherOptional.get())
        );
    }

    public ApiResponse<PublisherResponse> addPublisher(Publisher publisher) {

        Optional<Publisher> existingPublisher =
                publisherDao.findById(publisher.getPublisher_id());

        if (existingPublisher.isPresent()) {
            return ApiResponse.fail("Publisher already exists.");
        }

        Publisher savedPublisher =
                publisherDao.save(publisher);

        return ApiResponse.ok(
                "Publisher added.",
                toPublisherResponse(savedPublisher)
        );
    }

    public ApiResponse<PublisherResponse> updatePublisher(
            Integer id,
            Publisher publisher
    ) {

        Optional<Publisher> publisherOptional =
                publisherDao.findById(id);

        if (publisherOptional.isEmpty()) {
            return ApiResponse.fail("Publisher not found.");
        }

        Publisher existingPublisher =
                publisherOptional.get();

        if (publisher.getName() != null &&
                !publisher.getName().isBlank()) {
            existingPublisher.setName(publisher.getName());
        }

        if (publisher.getBooks() != null &&
                !publisher.getBooks().isEmpty()) {
            existingPublisher.setBooks(
                    publisher.getBooks()
            );
        }

        publisherDao.save(existingPublisher);

        return ApiResponse.ok(
                "Publisher updated.",
                toPublisherResponse(existingPublisher)
        );
    }

    public ApiResponse<Void> deletePublisher(Integer id) {

        Optional<Publisher> publisherOptional =
                publisherDao.findById(id);

        if (publisherOptional.isEmpty()) {
            return ApiResponse.fail("Publisher not found.");
        }

        publisherDao.deleteById(id);

        return ApiResponse.ok(
                "Publisher deleted.",
                null
        );
    }
}