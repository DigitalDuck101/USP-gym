package com.fitnesusp.usp_gym.model;
import jakarta.persistence.*;

/**
 *
 * @author svetik
 */

    



@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", unique = true/*, nullable = false*/)
    private String studentId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType memberType;


    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private StudentVerificationStatus verificationStatus;
    @OneToOne(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(
        name = "app_user_id",
        unique = true
    )
    private AppUser appUser;

    public Member() {
    }

    public Member(String studentId, String firstName,
                  String lastName, String email, String phone) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public AppUser getAppUser() {
        return appUser;
    }

   public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
   public MemberType getMemberType() {
        return memberType;
    }

   public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
   }


    public StudentVerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(
        StudentVerificationStatus verificationStatus) {

       this.verificationStatus = verificationStatus;
    }
   
}

