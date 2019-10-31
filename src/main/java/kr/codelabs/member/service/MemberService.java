package kr.codelabs.member.service;

import kr.codelabs.member.entity.Member;
import kr.codelabs.member.exception.DataException;
import kr.codelabs.member.repository.MemberRepository;
import kr.codelabs.member.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMember(Long id) {
        Member member = memberRepository.findById(id).orElse(null);

        if (member == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member Not Found");
        } else {
            return member;
        }
    }

    public Member getMemberByName(String name) {
        Member member = memberRepository.findByName(name).orElse(null);

        if (member == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member Not Found");
        } else {
            return member;
        }
    }

    public Member createMember(Member member) {
        member.setMemberSeq(RedisUtil.generateMemberSeq());
        member.setCurrentTime();

        return memberRepository.save(member);
    }

    public Member updateMember(Long id, Member member) {
        Member savedMember = memberRepository.findById(id).orElse(null);

        if (savedMember == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member Not Found");
        } else {
            savedMember.update(member);

            return memberRepository.save(savedMember);
        }
    }

    public void deleteMember(Long id) {
        if (memberRepository.findById(id).isPresent()) {
            memberRepository.deleteById(id);
        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member Not Found");
            throw new DataException("9999", "Unexpected Error");
        }
    }
}
