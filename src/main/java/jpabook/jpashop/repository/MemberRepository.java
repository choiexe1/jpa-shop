package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;

import java.util.List;

public interface MemberRepository extends IRepository<Member, Long> {
    List<Member> findByName(String name);
}
