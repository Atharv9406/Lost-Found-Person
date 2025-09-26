package com.lostfound.tracker.repository;

import com.lostfound.tracker.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    List<User> findByActiveTrue();
    
    @Query("{'lastKnownLocation': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2 } }}")
    List<User> findUsersNearLocation(double latitude, double longitude, double maxDistanceInMeters);
    
    List<User> findByFcmTokenIsNotNull();
}