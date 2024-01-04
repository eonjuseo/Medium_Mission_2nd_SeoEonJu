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
//        // 사용자 i가 1부터 100까지 홀수면 isPaid=true, 짝수면 isPaid=false
//        IntStream.rangeClosed(1, 100).forEach(i -> {
//            boolean isPaid = i % 2 != 0;
//            memberService.join("user"+i, "1111", isPaid).getData();
//        });
//
//        IntStream.rangeClosed(1, 200).forEach(i -> {
//            Member member = memberService.findByUsername("user" + i).orElseThrow();
//            boolean isPaid = i % 2 != 0;
//            postService.write(member, "제목 " + i, "내용 " + i, true, isPaid);
//        });
        Member memberUser1 = memberService.join("user1", "1111", true).getData();
        Member memberUser2 = memberService.join("user2", "1111", false).getData();
        Member memberUser3 = memberService.join("user3", "1111", true).getData();
        Member memberUser4 = memberService.join("user4", "1111", false).getData();

        Post post1 = postService.write(memberUser1, "제목 1", "내용 1", true, true);
        Post post2 = postService.write(memberUser1, "제목 2", "내용 2", true, false);
        Post post3 = postService.write(memberUser1, "제목 3", "내용 3", false, true);
        Post post4 = postService.write(memberUser1, "제목 4", "내용 4", true, false);

        Post post5 = postService.write(memberUser2, "제목 5", "내용 5", true, false);
        Post post6 = postService.write(memberUser2, "제목 6", "내용 6", false, false);

        Post post7 = postService.write(memberUser3, "제목 7", "내용 7", true, true);
        Post post8 = postService.write(memberUser3, "제목 8", "내용 8", true, true);

        Post post9 = postService.write(memberUser4, "제목 9", "내용 9", true, false);
        Post post10 = postService.write(memberUser4, "제목 10", "내용 10", true, false);


        postService.like(memberUser2, post1);
        postService.like(memberUser3, post1);
        postService.like(memberUser4, post1);

        postService.like(memberUser2, post2);
        postService.like(memberUser3, post2);

        postService.like(memberUser2, post3);
    }
}
