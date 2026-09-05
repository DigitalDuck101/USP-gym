

/**
 *
 * @author svetik
 */
package com.fitnesusp.usp_gym.service;

import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import com.fitnesusp.usp_gym.model.AppUser;
import com.fitnesusp.usp_gym.model.Role;
import com.fitnesusp.usp_gym.repository.AppUserRepository;
import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.model.StudentVerificationStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    
    private final MemberRepository memberRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(
        MemberRepository memberRepository,
        AppUserRepository appUserRepository,
        PasswordEncoder passwordEncoder) {

        this.memberRepository = memberRepository;
        this.appUserRepository = appUserRepository;
         this.passwordEncoder = passwordEncoder;
    }

    public List<Member> getAllMembers() {

         return memberRepository.findByAppUser_Role( Role.MEMBER);
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
    
    public Member getMemberById(Long id) {
    return memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));
    } 
    
    /*public Member createMember(
        Member member,
        String username,
        String password) {

        if (appUserRepository.existsByUsername(username)) {
           throw new RuntimeException(
                "Username already exists: " + username);
        }

        AppUser account = new AppUser(
            username,
            passwordEncoder.encode(password),
            Role.MEMBER,
            true);

        member.setAppUser(account);

         return memberRepository.save(member);
    }*/
    
    public Member createMember( Member member, String username, String password) {

        if (appUserRepository.existsByUsername(username)) {

            throw new IllegalStateException( "Username already exists.");
        }


        if (member.getMemberType() == MemberType.STUDENT) {

            // Student must have Student ID
            if (member.getStudentId() == null || member.getStudentId().isBlank()) {

                throw new IllegalStateException( "Student ID is required for students." );
            }


            // Check if Student ID already exists
            if (memberRepository.existsByStudentId( member.getStudentId())) {

                throw new IllegalStateException( "Student ID already exists.");
            }


        /*
         * ADMIN is creating this student,
         * therefore Student ID is considered verified.
         */
        member.setVerificationStatus( StudentVerificationStatus.APPROVED);

    } else if (member.getMemberType() == MemberType.RENTER) {

        // Renters do not have Student ID
        member.setStudentId(null);
        member.setVerificationStatus( StudentVerificationStatus.NOT_REQUIRED);

    } else {

        throw new IllegalStateException( "Member type is required.");
    }


        AppUser account = new AppUser(
            username,
            passwordEncoder.encode(password),
            Role.MEMBER,
            true
        );

        member.setAppUser(account);


        return memberRepository.save(member);
    }
    
    public Member getMemberByUsername(String username) {

        return memberRepository
            .findByAppUserUsername(username)
            .orElseThrow(() -> new RuntimeException("Member account not found"));
    }
    
    public List<Member> getPendingStudents() {

        return memberRepository
            .findByMemberTypeAndVerificationStatus( MemberType.STUDENT, StudentVerificationStatus.PENDING);
    }
    
    @Transactional
    public void approveStudent(Long id) {

        Member member = getMemberById(id);


        if (member.getMemberType() != MemberType.STUDENT) {

            throw new IllegalStateException( "This member is not a student.");
         }

        member.setVerificationStatus( StudentVerificationStatus.APPROVED);
        member.getAppUser().setEnabled(true);


        memberRepository.save(member);
    }
    
    @Transactional
    public void rejectStudent(Long id) {

            Member member = getMemberById(id);


            if (member.getMemberType() != MemberType.STUDENT) {

                throw new IllegalStateException( "This member is not a student." );
            }

            member.setVerificationStatus( StudentVerificationStatus.REJECTED);
            member.getAppUser().setEnabled(false);


            memberRepository.save(member);
    }
    
    public List<Member> getAllRenters() {

        return memberRepository.findByMemberTypeOrderByFirstNameAsc(MemberType.RENTER);
    }
}
