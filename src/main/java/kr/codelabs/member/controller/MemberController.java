package kr.codelabs.member.controller;

import kr.codelabs.member.entity.Member;
import kr.codelabs.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers() {
        List<Member> members = memberService.getAllMembers();

        return ResponseEntity.ok(members);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<?> getMember(@PathVariable Long id) {
        Member member = memberService.getMember(id);

        return ResponseEntity.ok(member);
    }

    @GetMapping("/members/name")
    public ResponseEntity<?> getMember(@RequestParam String name) {
        Member member = memberService.getMemberByName(name);

        return ResponseEntity.ok(member);
    }

    @PostMapping("/members")
    public ResponseEntity<?> createMember(@RequestBody Member member) {
        return new ResponseEntity<>(memberService.createMember(member), HttpStatus.CREATED);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody Member member) {
        return new ResponseEntity<>(memberService.updateMember(id, member), HttpStatus.OK);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
