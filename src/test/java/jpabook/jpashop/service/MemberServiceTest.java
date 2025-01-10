package jpabook.jpashop.service;

import jpabook.jpashop.MemberRepository;
import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입() {
        // GIVEN
        Member member = new Member();
        member.setUsername("kim");

        // WHEN
        Long id = memberService.join(member);

        // THEN
        Member byId = memberRepository.findById(id);
        assertThat(byId.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    void 중복_회원_예외() {
        // GIVEN
        Member member1 = new Member();
        member1.setUsername("kim");
        Member member2 = new Member();
        member2.setUsername("kim");

        // WHEN
        memberService.join(member1);

        // THEN
        assertThatThrownBy(() -> memberService.join(member2),
                "유저네임 중복 가입인데 예외 발생 안함")
                .isInstanceOf(IllegalStateException.class);
    }
}