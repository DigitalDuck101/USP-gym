/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.model.StudentVerificationStatus;
import java.util.List;

import com.fitnesusp.usp_gym.model.Role;

/**
 *
 * @author valeriy
 */

public interface MemberRepository
        extends JpaRepository<Member, Long> {

    Optional<Member> findByAppUserUsername(String username);

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    List<Member> findByMemberTypeAndVerificationStatus(
            MemberType memberType,
            StudentVerificationStatus verificationStatus
    );

    List<Member> findByAppUser_Role(Role role);
    
    List<Member> findByMemberTypeOrderByFirstNameAsc(
        MemberType memberType
    );
}