package com.ll.medium.global.init;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
@Profile("!prod")
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    @Autowired
    @Lazy
    private NotProd self;
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    @Order(3)
    public ApplicationRunner initNotProd() {
        return args -> {
            // 'user1'이라는 사용자가 이미 존재하면 초기화를 진행하지 않음
            if (memberService.findByUsername("user1").isPresent()) return;

            self.work1();
        };
    }

    @Transactional
    public void work1() {
        //user1부터 100까지 유료 회원, 101-110 무료 회원
        IntStream.rangeClosed(1, 100).forEach(i -> {
            memberService.join("user"+i, "1111", true).getData();
        });
        IntStream.rangeClosed(101, 110).forEach(i -> {
            memberService.join("user"+i, "1111", false).getData();
        });
        // 사용자 i가 1부터 100까지 isPaid=true
        IntStream.rangeClosed(1, 90).forEach(i -> {
            Member member = memberService.findByUsername("user" + i).orElseThrow();
            postService.write(member, "제목 " + i, "내용 " + i, true, true);
        });
        // 사용자 i가 101부터 110까지 isPaid=false인 글
        IntStream.rangeClosed(91, 110).forEach(i -> {
            Member member = memberService.findByUsername("user" + i).orElseThrow();
            postService.write(member, "제목 " + i, "내용 " + i, true, false);
        });
//        Member memberUser1 = memberService.join("user1", "1111", true).getData();
//        Member memberUser2 = memberService.join("user2", "1111", false).getData();
//
//        Post post1 = postService.write(memberUser1, "제목 1", "내용 1", true, true);
//        Post post2 = postService.write(memberUser1, "제목 2", "내용 2", true, false);
//
//        postService.like(memberUser1, post1);
//        postService.like(memberUser2, post1);
//
//        postService.like(memberUser2, post2);
//
    }
}
