package com.sdlc.pro.mymbstu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "admission_application")
public class AdmissionApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Applicant name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3-100 characters")
    private String applicantName;

    @Column(unique = true)
    private String appUser;

    @Column
    private String appPass;

    @Column(name = "\"user\"")
    private String user;

    //private String user;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?:\\+88|88)?(01[3-9]\\d{8})$", message = "Invalid Bangladeshi phone number")
    private String phone;

    @NotBlank(message = "Father/Guardian name is required")
    private String fatherGuardian;


    @NotBlank(message = "Present address is required")
    private String presentAddress;

    private String permanentAddress;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    @NotBlank(message = "Religion is required")
    private String religion;

    @NotNull(message = "Birthdate is required")
    private LocalDate birthdate;

    private String photoPath;
    private String photofile;

    public String getPaymentPhotoPath() {
        return paymentPhotoPath;
    }

    public void setPaymentPhotoPath(String paymentPhotoPath) {
        this.paymentPhotoPath = paymentPhotoPath;
    }

    private String paymentPhotoPath;
    private String paymentPhotofile;

    // SSC Information
    @NotBlank(message = "SSC Board is required")
    private String sscBoard;

    @NotBlank(message = "SSC Group is required")
    private String sscGroup;

    @NotBlank(message = "SSC Year is required")
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Invalid year format")
    private String sscYear;

    @NotBlank(message = "SSC Result is required")
    private String sscResult;

    // HSC Information
    @NotBlank(message = "HSC Board is required")
    private String hscBoard;

    @NotBlank(message = "HSC Group is required")
    private String hscGroup;

    @NotBlank(message = "HSC Year is required")
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Invalid year format")
    private String hscYear;

    @NotBlank(message = "HSC Result is required")
    private String hscResult;

    // Graduate Information
    private String gradBoard;
    private String gradGroup;
    private String gradYear;
    private String gradResult;

    @AssertTrue(message = "You must accept the declaration")
    private boolean declaration;

    private Date submissionDate;

    private String paymentConfirm="null";
    private String status = "PENDING";


    // Add proper getter and setter
    public boolean isDeclaration() {
        return declaration;
    }

    public void setDeclaration(boolean declaration) {
        this.declaration = declaration;
    }


    public AdmissionApplication() {

    }

    public String getPhotofile() {
        return photofile;
    }

    public void setPhotofile(String photofile) {
        this.photofile = photofile;
    }

    public String getPaymentPhotofile() {
        return paymentPhotofile;
    }

    public void setPaymentPhotofile(String paymentPhotofile) {
        this.paymentPhotofile = paymentPhotofile;
    }

    public AdmissionApplication(String applicantName, String appUser, String appPass, String user, String phone, String fatherGuardian, String presentAddress, String permanentAddress, String nationality, String religion, LocalDate birthdate, String photoPath, String paymentPhotoPath, String sscBoard, String sscGroup, String sscYear, String sscResult, String hscBoard, String hscGroup, String hscYear, String hscResult, String gradBoard, String gradGroup, String gradYear, String gradResult, boolean declaration, Date submissionDate, String status) {
        this.applicantName = applicantName;
        this.appUser = appUser;
        this.appPass = appPass;
        this.user = user;
        this.phone = phone;
        this.fatherGuardian = fatherGuardian;
        this.presentAddress = presentAddress;
        this.permanentAddress = permanentAddress;
        this.nationality = nationality;
        this.religion = religion;
        this.birthdate = birthdate;
        this.photoPath = photoPath;
        this.paymentPhotoPath = paymentPhotoPath;
        this.sscBoard = sscBoard;
        this.sscGroup = sscGroup;
        this.sscYear = sscYear;
        this.sscResult = sscResult;
        this.hscBoard = hscBoard;
        this.hscGroup = hscGroup;
        this.hscYear = hscYear;
        this.hscResult = hscResult;
        this.gradBoard = gradBoard;
        this.gradGroup = gradGroup;
        this.gradYear = gradYear;
        this.gradResult = gradResult;
        this.declaration = declaration;
        this.submissionDate = submissionDate;
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getappUser() {
        return appUser;
    }

    public void setappUser(String appUser) {
        this.appUser = appUser;
    }

    public String getAppPass() {
        return appPass;
    }

    public void setAppPass(String appPass) {
        this.appPass = appPass;
    }

    public String getAppUser() {
        return appUser;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFatherGuardian() {
        return fatherGuardian;
    }

    public void setFatherGuardian(String fatherGuardian) {
        this.fatherGuardian = fatherGuardian;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getSscBoard() {
        return sscBoard;
    }

    public void setSscBoard(String sscBoard) {
        this.sscBoard = sscBoard;
    }

    public String getSscGroup() {
        return sscGroup;
    }

    public void setSscGroup(String sscGroup) {
        this.sscGroup = sscGroup;
    }

    public String getSscYear() {
        return sscYear;
    }

    public void setSscYear(String sscYear) {
        this.sscYear = sscYear;
    }

    public String getSscResult() {
        return sscResult;
    }

    public void setSscResult(String sscResult) {
        this.sscResult = sscResult;
    }

    public String getHscBoard() {
        return hscBoard;
    }

    public void setHscBoard(String hscBoard) {
        this.hscBoard = hscBoard;
    }

    public String getHscGroup() {
        return hscGroup;
    }

    public void setHscGroup(String hscGroup) {
        this.hscGroup = hscGroup;
    }

    public String getHscYear() {
        return hscYear;
    }

    public void setHscYear(String hscYear) {
        this.hscYear = hscYear;
    }

    public String getHscResult() {
        return hscResult;
    }

    public void setHscResult(String hscResult) {
        this.hscResult = hscResult;
    }

    public String getGradBoard() {
        return gradBoard;
    }

    public void setGradBoard(String gradBoard) {
        this.gradBoard = gradBoard;
    }

    public String getGradGroup() {
        return gradGroup;
    }

    public void setGradGroup(String gradGroup) {
        this.gradGroup = gradGroup;
    }

    public String getGradYear() {
        return gradYear;
    }

    public void setGradYear(String gradYear) {
        this.gradYear = gradYear;
    }

    public String getGradResult() {
        return gradResult;
    }

    public void setGradResult(String gradResult) {
        this.gradResult = gradResult;
    }


    public String getPaymentConfirm() {
        return paymentConfirm;
    }

    public void setPaymentConfirm(String paymentConfirm) {
        this.paymentConfirm = paymentConfirm;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}