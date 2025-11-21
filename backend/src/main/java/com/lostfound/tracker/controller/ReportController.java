package com.lostfound.tracker.controller;

import com.lostfound.tracker.dto.CreateReportRequest;
import com.lostfound.tracker.model.Report;
import com.lostfound.tracker.model.User;
import com.lostfound.tracker.repository.ReportRepository;
import com.lostfound.tracker.repository.UserRepository;
import com.lostfound.tracker.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createReport(@Valid @RequestBody CreateReportRequest request) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User reporter = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

            Report report = new Report();
            report.setType(request.getType());
            report.setTitle(request.getTitle());
            report.setDescription(request.getDescription());
            report.setReporter(reporter);
            report.setContactPhone(request.getContactPhone());
            report.setContactEmail(request.getContactEmail());
            report.setRewardAmount(request.getRewardAmount());
            report.setPublic(request.isPublic());
            report.setIncidentDateTime(request.getIncidentDateTime());
            report.setImageUrls(request.getImageUrls());

            // Set location
            if (request.getLastSeenLocation() != null) {
                Report.Location location = new Report.Location();
                location.setLatitude(request.getLastSeenLocation().getLatitude());
                location.setLongitude(request.getLastSeenLocation().getLongitude());
                location.setAddress(request.getLastSeenLocation().getAddress());
                location.setCity(request.getLastSeenLocation().getCity());
                location.setState(request.getLastSeenLocation().getState());
                location.setCountry(request.getLastSeenLocation().getCountry());
                location.setPostalCode(request.getLastSeenLocation().getPostalCode());
                report.setLastSeenLocation(location);
            }

            // Set person details for missing person reports
            if (request.getPersonDetails() != null) {
                Report.PersonDetails personDetails = new Report.PersonDetails();
                personDetails.setFullName(request.getPersonDetails().getFullName());
                personDetails.setAge(request.getPersonDetails().getAge());
                personDetails.setGender(request.getPersonDetails().getGender());
                personDetails.setHeight(request.getPersonDetails().getHeight());
                personDetails.setWeight(request.getPersonDetails().getWeight());
                personDetails.setHairColor(request.getPersonDetails().getHairColor());
                personDetails.setEyeColor(request.getPersonDetails().getEyeColor());
                personDetails.setComplexion(request.getPersonDetails().getComplexion());
                personDetails.setClothingDescription(request.getPersonDetails().getClothingDescription());
                personDetails.setDistinguishingMarks(request.getPersonDetails().getDistinguishingMarks());
                personDetails.setMedicalConditions(request.getPersonDetails().getMedicalConditions());
                personDetails.setEmergencyContactName(request.getPersonDetails().getEmergencyContactName());
                personDetails.setEmergencyContactPhone(request.getPersonDetails().getEmergencyContactPhone());
                report.setPersonDetails(personDetails);
            }

            // Set item details for lost item reports
            if (request.getItemDetails() != null) {
                Report.ItemDetails itemDetails = new Report.ItemDetails();
                itemDetails.setItemName(request.getItemDetails().getItemName());
                itemDetails.setCategory(request.getItemDetails().getCategory());
                itemDetails.setBrand(request.getItemDetails().getBrand());
                itemDetails.setModel(request.getItemDetails().getModel());
                itemDetails.setColor(request.getItemDetails().getColor());
                itemDetails.setSize(request.getItemDetails().getSize());
                itemDetails.setSerialNumber(request.getItemDetails().getSerialNumber());
                itemDetails.setDescription(request.getItemDetails().getDescription());
                itemDetails.setEstimatedValue(request.getItemDetails().getEstimatedValue());
                report.setItemDetails(itemDetails);
            }

            Report savedReport = reportRepository.save(report);
            return ResponseEntity.ok(savedReport);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthController.MessageResponse("Error creating report: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<Report>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Report.ReportType type,
            @RequestParam(required = false) Report.ReportStatus status) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                    Sort.by(sortBy).descending() : 
                    Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Report> reports;
        if (type != null && status != null) {
            reports = reportRepository.findByTypeAndStatusAndIsPublicTrue(type, status, pageable);
        } else if (status != null) {
            reports = reportRepository.findByStatusAndIsPublicTrue(status, pageable);
        } else {
            reports = reportRepository.findByStatusAndIsPublicTrue(Report.ReportStatus.ACTIVE, pageable);
        }

        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable String id) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            return ResponseEntity.ok(report.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my-reports")
    public ResponseEntity<List<Report>> getMyReports() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User reporter = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            
            List<Report> reports = reportRepository.findByReporter(reporter);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Report>> getNearbyReports(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10000") double radiusInMeters,
            @RequestParam(required = false) Report.ReportType type) {

        List<Report> reports;
        if (type != null) {
            reports = reportRepository.findReportsByTypeNearLocation(
                    latitude, longitude, radiusInMeters, type, Report.ReportStatus.ACTIVE);
        } else {
            reports = reportRepository.findReportsNearLocation(
                    latitude, longitude, radiusInMeters, Report.ReportStatus.ACTIVE);
        }

        return ResponseEntity.ok(reports);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateReportStatus(@PathVariable String id, @RequestParam Report.ReportStatus status) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

            Optional<Report> reportOpt = reportRepository.findById(id);
            if (reportOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Report report = reportOpt.get();
            
            // Check if user is the reporter or an admin
            if (!report.getReporter().getId().equals(currentUser.getId()) && 
                !currentUser.getRoles().contains(User.Role.ADMIN)) {
                return ResponseEntity.badRequest().body(new AuthController.MessageResponse("Not authorized to update this report"));
            }

            report.setStatus(status);
            reportRepository.save(report);

            return ResponseEntity.ok(new AuthController.MessageResponse("Report status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthController.MessageResponse("Error updating report status: " + e.getMessage()));
        }
    }
}