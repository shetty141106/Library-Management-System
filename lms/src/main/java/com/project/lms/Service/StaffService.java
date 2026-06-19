package com.project.lms.Service;

import com.project.lms.Dao.StaffDao;
import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.StaffResponse;
import com.project.lms.Entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffDao staffDao;

    private StaffResponse toResponse(Staff staff) {
        return new StaffResponse(
                staff.getStaff_id(),
                staff.getName(),
                staff.getAuthentication().getEmail(),
                staff.getAddress(),
                staff.getPhones()
        );
    }

    public ApiResponse<List<StaffResponse>> getAllStaff() {

        List<Staff> staffList = staffDao.findAll();

        if (staffList.isEmpty())
            return ApiResponse.fail("No staff found.");

        List<StaffResponse> res =
                staffList.stream()
                        .map(this::toResponse)
                        .toList();

        return ApiResponse.ok("All staff fetched.", res);
    }

    public ApiResponse<StaffResponse> getStaffById(int id) {

        Optional<Staff> staffOptional =
                staffDao.findById(id);

        if (staffOptional.isEmpty())
            return ApiResponse.fail("Staff not found.");

        return ApiResponse.ok(
                "Staff fetched.",
                toResponse(staffOptional.get())
        );
    }


    public ApiResponse<StaffResponse> updateStaff(
            int id,
            StaffResponse res) {

        Optional<Staff> staffOptional =
                staffDao.findById(id);

        if (staffOptional.isEmpty())
            return ApiResponse.fail("Staff not found.");

        Staff staff = staffOptional.get();

        if (res.getName() != null &&
                !res.getName().isBlank()) {
            staff.setName(res.getName());
        }

        staffDao.save(staff);

        return ApiResponse.ok(
                "Staff updated.",
                toResponse(staff)
        );
    }

    public ApiResponse<Void> deleteStaff(int id) {

        Optional<Staff> staffOptional =
                staffDao.findById(id);

        if (staffOptional.isEmpty())
            return ApiResponse.fail("Staff not found.");

        staffDao.deleteById(id);

        return ApiResponse.ok(
                "Staff deleted.",
                null
        );
    }
}