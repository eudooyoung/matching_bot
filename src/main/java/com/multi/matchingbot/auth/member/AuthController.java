//package com.multi.matchingbot.auth;
//
//import com.multi.matchingbot.auth.member.MemberDto;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@PostMapping("/signup")
//public ResponseEntity<MemberDto> signup(@RequestBody MemberDto memberDto) {
//    MemberDto saved = AuthService.signup(memberDto);
//    return ResponseEntity
//            .status(HttpStatus.CREATED)
//            .body(new MemberDto(HttpStatus.CREATED, "회원가입 성공", saved));
//}