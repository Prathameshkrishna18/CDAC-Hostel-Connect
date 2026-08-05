package com.cdac.hostelconnect.entity;


import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hostels")
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hostelName;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    private String pincode;

    private String contactNumber;

    private String email;

    private Double latitude;

    private Double longitude;
    
    @ManyToMany
    @JoinTable(
            name = "hostel_facilities",
            joinColumns = @JoinColumn(name = "hostel_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<Facility> facilities = new ArrayList<>();
    
    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HostelStatus status = HostelStatus.PENDING;

    private String rejectionReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Hostel Owner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // CDAC Center
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    private CdacCenter cdacCenter;

    // Constructors
    
    @OneToMany(
            mappedBy = "hostel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(
            mappedBy = "hostel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FoodMenu> foodMenus = new ArrayList<>();

    @OneToMany(
            mappedBy = "hostel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<HostelPhoto> photos = new ArrayList<>();

    public Hostel() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }
    
    public List<Room> getRooms() {
        return rooms;
    }

    public List<FoodMenu> getFoodMenus() {
        return foodMenus;
    }

    public List<HostelPhoto> getPhotos() {
        return photos;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public HostelStatus getStatus() {
        return status;
    }

    public void setStatus(HostelStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public CdacCenter getCdacCenter() {
        return cdacCenter;
    }

    public void setCdacCenter(CdacCenter cdacCenter) {
        this.cdacCenter = cdacCenter;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}