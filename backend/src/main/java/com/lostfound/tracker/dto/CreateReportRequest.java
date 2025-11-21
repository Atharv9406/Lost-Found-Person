package com.lostfound.tracker.dto;

import com.lostfound.tracker.model.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class CreateReportRequest {
    
    @NotNull
    private Report.ReportType type;
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String description;
    
    // Person details (for missing person reports)
    private PersonDetailsDto personDetails;
    
    // Item details (for lost item reports)
    private ItemDetailsDto itemDetails;
    
    // Location where person/item was last seen
    @NotNull
    private LocationDto lastSeenLocation;
    
    // Images related to the report
    private List<String> imageUrls;
    
    // Contact information
    private String contactPhone;
    private String contactEmail;
    
    // Reward information
    private Double rewardAmount;
    
    // Visibility settings
    private boolean isPublic = true;
    
    // When the person/item was actually lost/missing
    private LocalDateTime incidentDateTime;
    
    public static class PersonDetailsDto {
        private String fullName;
        private Integer age;
        private String gender;
        private Double height;
        private Double weight;
        private String hairColor;
        private String eyeColor;
        private String complexion;
        private String clothingDescription;
        private String distinguishingMarks;
        private List<String> medicalConditions;
        private String emergencyContactName;
        private String emergencyContactPhone;
        
        // Getters and setters
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
    
    public static class ItemDetailsDto {
        private String itemName;
        private String category;
        private String brand;
        private String model;
        private String color;
        private String size;
        private String serialNumber;
        private String description;
        private Double estimatedValue;
        
        // Getters and setters
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
    
    public static class LocationDto {
        @NotNull
        private Double latitude;
        @NotNull
        private Double longitude;
        private String address;
        private String city;
        private String state;
        private String country;
        private String postalCode;
        
        // Getters and setters
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
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
    
    // Main getters and setters
    public Report.ReportType getType() { return type; }
    public void setType(Report.ReportType type) { this.type = type; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public PersonDetailsDto getPersonDetails() { return personDetails; }
    public void setPersonDetails(PersonDetailsDto personDetails) { this.personDetails = personDetails; }
    
    public ItemDetailsDto getItemDetails() { return itemDetails; }
    public void setItemDetails(ItemDetailsDto itemDetails) { this.itemDetails = itemDetails; }
    
    public LocationDto getLastSeenLocation() { return lastSeenLocation; }
    public void setLastSeenLocation(LocationDto lastSeenLocation) { this.lastSeenLocation = lastSeenLocation; }
    
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    
    public Double getRewardAmount() { return rewardAmount; }
    public void setRewardAmount(Double rewardAmount) { this.rewardAmount = rewardAmount; }
    
    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    
    public LocalDateTime getIncidentDateTime() { return incidentDateTime; }
    public void setIncidentDateTime(LocalDateTime incidentDateTime) { this.incidentDateTime = incidentDateTime; }
}