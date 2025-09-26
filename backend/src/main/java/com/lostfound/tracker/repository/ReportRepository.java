package com.lostfound.tracker.repository;

import com.lostfound.tracker.model.Report;
import com.lostfound.tracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    
    List<Report> findByReporter(User reporter);
    
    List<Report> findByStatus(Report.ReportStatus status);
    
    List<Report> findByType(Report.ReportType type);
    
    List<Report> findByTypeAndStatus(Report.ReportType type, Report.ReportStatus status);
    
    Page<Report> findByStatusAndIsPublicTrue(Report.ReportStatus status, Pageable pageable);
    
    Page<Report> findByTypeAndStatusAndIsPublicTrue(Report.ReportType type, Report.ReportStatus status, Pageable pageable);
    
    @Query("{'lastSeenLocation': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2 } }, 'status': ?3, 'isPublic': true}")
    List<Report> findReportsNearLocation(double latitude, double longitude, double maxDistanceInMeters, Report.ReportStatus status);
    
    @Query("{'lastSeenLocation': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2 } }, 'type': ?3, 'status': ?4, 'isPublic': true}")
    List<Report> findReportsByTypeNearLocation(double latitude, double longitude, double maxDistanceInMeters, Report.ReportType type, Report.ReportStatus status);
    
    List<Report> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'personDetails.fullName': {$regex: ?0, $options: 'i'}}")
    List<Report> findByPersonNameContaining(String name);
    
    @Query("{'itemDetails.itemName': {$regex: ?0, $options: 'i'}}")
    List<Report> findByItemNameContaining(String itemName);
    
    long countByStatus(Report.ReportStatus status);
    
    long countByType(Report.ReportType type);
}