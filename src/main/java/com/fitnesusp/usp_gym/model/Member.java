package com.fitnesusp.usp_gym.model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "members")
public class Member {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(
            name = "student_id",
            unique = true
    )
    private String studentId;


    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstName;


    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastName;


    @Column(
            unique = true,
            nullable = false
    )
    private String email;


    private String phone;


    // =====================================================
    // MEMBER TYPE
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType memberType;


    // =====================================================
    // STUDENT VERIFICATION
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private StudentVerificationStatus verificationStatus;


    // =====================================================
    // MEMBERSHIP STATUS
    // =====================================================

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_status")
    private MembershipStatus membershipStatus;


    // =====================================================
    // MEMBERSHIP EXPIRY DATE
    // =====================================================

    @Column(name = "membership_expiry_date")
    private LocalDate membershipExpiryDate;


    // =====================================================
    // USER ACCOUNT
    // =====================================================

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "app_user_id",
            unique = true
    )
    private AppUser appUser;



    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public Member() {
    }


    public Member(
            String studentId,
            String firstName,
            String lastName,
            String email,
            String phone) {

        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }



    // =====================================================
    // ID
    // =====================================================

    public Long getId() {
        return id;
    }



    // =====================================================
    // STUDENT ID
    // =====================================================

    public String getStudentId() {
        return studentId;
    }


    public void setStudentId(
            String studentId) {

        this.studentId = studentId;
    }



    // =====================================================
    // FIRST NAME
    // =====================================================

    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(
            String firstName) {

        this.firstName = firstName;
    }



    // =====================================================
    // LAST NAME
    // =====================================================

    public String getLastName() {
        return lastName;
    }


    public void setLastName(
            String lastName) {

        this.lastName = lastName;
    }



    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {
        return email;
    }


    public void setEmail(
            String email) {

        this.email = email;
    }



    // =====================================================
    // PHONE
    // =====================================================

    public String getPhone() {
        return phone;
    }


    public void setPhone(
            String phone) {

        this.phone = phone;
    }



    // =====================================================
    // APP USER
    // =====================================================

    public AppUser getAppUser() {
        return appUser;
    }


    public void setAppUser(
            AppUser appUser) {

        this.appUser = appUser;
    }



    // =====================================================
    // MEMBER TYPE
    // =====================================================

    public MemberType getMemberType() {
        return memberType;
    }


    public void setMemberType(
            MemberType memberType) {

        this.memberType = memberType;
    }



    // =====================================================
    // STUDENT VERIFICATION STATUS
    // =====================================================

    public StudentVerificationStatus getVerificationStatus() {
        return verificationStatus;
    }


    public void setVerificationStatus(
            StudentVerificationStatus verificationStatus) {

        this.verificationStatus = verificationStatus;
    }



    // =====================================================
    // MEMBERSHIP STATUS
    // =====================================================

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }


    public void setMembershipStatus(
            MembershipStatus membershipStatus) {

        this.membershipStatus = membershipStatus;
    }



    // =====================================================
    // MEMBERSHIP EXPIRY DATE
    // =====================================================

    public LocalDate getMembershipExpiryDate() {
        return membershipExpiryDate;
    }


    public void setMembershipExpiryDate(
            LocalDate membershipExpiryDate) {

        this.membershipExpiryDate =
                membershipExpiryDate;
    }
}