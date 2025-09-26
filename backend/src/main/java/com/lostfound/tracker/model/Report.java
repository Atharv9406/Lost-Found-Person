package com.lostfound.tracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    @NotNull
    private ReportType type;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @DBRef
    private User reporter;

    // Person details (for missing person reports)
    private PersonDetails personDetails;

    // Item details (for lost item reports)
    private ItemDetails itemDetails;

    // Location where person/item was last seen
    @NotNull
    private Location lastSeenLocation;

    // Current status of the report
    @NotNull
    private ReportStatus status = ReportStatus.ACTIVE;

    // Images related to the report
    private List<String> imageUrls = new ArrayList<>();

    // AI analysis results
    private AIAnalysisResult aiAnalysis;

    // Contact information
    private String contactPhone;
    private String contactEmail;

    // Reward information
    private Double rewardAmount;

    // Visibility settings
    private boolean isPublic = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // When the person/item was actually lost/missing
    private LocalDateTime incidentDateTime;

    public enum ReportType {
        MISSING_PERSON, LOST_ITEM, FOUND_PERSON, FOUND_ITEM
    }

    public enum ReportStatus {
        ACTIVE, RESOLVED, CANCELLED, EXPIRED
    }

    public static class PersonDetails {
        private String fullName;
        private Integer age;
        private String gender;
        private Double height; // in cm
        private Double weight; // in kg
        private String hairColor;
        private String eyeColor;
        private String complexion;
        private String clothingDescription;
        private String distinguishingMarks;
        private List<String> medicalConditions = new ArrayList<>();
        private String emergencyContactName;
        private String emergencyContactPhone;

        // Constructors, getters and setters
        public PersonDetails() {}

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public Double getHeight() { return height; }
        public void setHeight(Double height) { this.height = height; }
        public Double getWeight() { return weight; }
        public void setWeight(Double weight) { this.weight = weight; }
        public String getHairColor() { return hairColor; }
        public void setHairColor(String hairColor) { this.hairColor = hairColor; }
        public String getEyeColor() { return eyeColor; }
        public void setEyeColor(String eyeColor) { this.eyeColor = eyeColor; }
        public String getComplexion() { return complexion; }
        public void setComplexion(String complexion) { this.complexion = complexion; }
        public String getClothingDescription() { return clothingDescription; }
        public void setClothingDescription(String clothingDescription) { this.clothingDescription = clothingDescription; }
        public String getDistinguishingMarks() { return distinguishingMarks; }
        public void setDistinguishingMarks(String distinguishingMarks) { this.distinguishingMarks = distinguishingMarks; }
        public List<String> getMedicalConditions() { return medicalConditions; }
        public void setMedicalConditions(List<String> medicalConditions) { this.medicalConditions = medicalConditions; }
        public String getEmergencyContactName() { return emergencyContactName; }
        public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
        public String getEmergencyContactPhone() { return emergencyContactPhone; }
        public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    }

    public static class ItemDetails {
        private String itemName;
        private String category;
        private String brand;
        private String model;
        private String color;
        private String size;
        private String serialNumber;
        private String description;
        private Double estimatedValue;

        // Constructors, getters and setters
        public ItemDetails() {}

        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Double getEstimatedValue() { return estimatedValue; }
        public void setEstimatedValue(Double estimatedValue) { this.estimatedValue = estimatedValue; }
    }

    public static class Location {
        private double latitude;
        private double longitude;
        private String address;
        private String city;
        private String state;
        private String country;
        private String postalCode;

        public Location() {}

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        // Getters and setters
        public double getLatitude() { return latitude; }
        public void setLatitude(double latitude) { this.latitude = latitude; }
        public double getLongitude() { return longitude; }
        public void setLongitude(double longitude) { this.longitude = longitude; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    }

    public static class AIAnalysisResult {
        private List<String> faceEncodings = new ArrayList<>();
        private List<String> objectFeatures = new ArrayList<>();
        private Double confidenceScore;
        private String analysisTimestamp;

        // Constructors, getters and setters
        public AIAnalysisResult() {}

        public List<String> getFaceEncodings() { return faceEncodings; }
        public void setFaceEncodings(List<String> faceEncodings) { this.faceEncodings = faceEncodings; }
        public List<String> getObjectFeatures() { return objectFeatures; }
        public void setObjectFeatures(List<String> objectFeatures) { this.objectFeatures = objectFeatures; }
        public Double getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
        public String getAnalysisTimestamp() { return analysisTimestamp; }
        public void setAnalysisTimestamp(String analysisTimestamp) { this.analysisTimestamp = analysisTimestamp; }
    }

    // Constructors
    public Report() {}

    public Report(ReportType type, String title, String description, User reporter) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.reporter = reporter;
    }

    // Main getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public ReportType getType() { return type; }
    public void setType(ReportType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getReporter() { return reporter; }
    public void setReporter(User reporter) { this.reporter = reporter; }

    public PersonDetails getPersonDetails() { return personDetails; }
    public void setPersonDetails(PersonDetails personDetails) { this.personDetails = personDetails; }

    public ItemDetails getItemDetails() { return itemDetails; }
    public void setItemDetails(ItemDetails itemDetails) { this.itemDetails = itemDetails; }

    public Location getLastSeenLocation() { return lastSeenLocation; }
    public void setLastSeenLocation(Location lastSeenLocation) { this.lastSeenLocation = lastSeenLocation; }

    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public AIAnalysisResult getAiAnalysis() { return aiAnalysis; }
    public void setAiAnalysis(AIAnalysisResult aiAnalysis) { this.aiAnalysis = aiAnalysis; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public Double getRewardAmount() { return rewardAmount; }
    public void setRewardAmount(Double rewardAmount) { this.rewardAmount = rewardAmount; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getIncidentDateTime() { return incidentDateTime; }
    public void setIncidentDateTime(LocalDateTime incidentDateTime) { this.incidentDateTime = incidentDateTime; }
}