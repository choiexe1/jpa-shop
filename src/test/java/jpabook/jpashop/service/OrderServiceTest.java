package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.interfaces.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() {
        // GIVEN
        Member member = createMember();
        Item book = createBook("book", 12000, 10);

        // WHEN
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // THEN
        Order order = orderRepository.findById(orderId);
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
        Assertions.assertThat(order.getOrderItems().size()).isEqualTo(1);
        Assertions.assertThat(order.totalPrice()).isEqualTo(book.getPrice() * orderCount);
        Assertions.assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    @Test
    void 상품주문_재고수량_초과() {
        // GIVEN
        Member member = createMember();
        Item book = createBook("book", 12000, 10);

        // WHEN
        int orderCount = 11;

        // THEN
        Assertions.assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount),
                        "재고 수량 예외가 발생해야 한다.")
                .isInstanceOf(NotEnoughStockException.class);
    }

    @Test
    void 주문취소() {
        // GIVEN
        Member member = createMember();
        Item book = createBook("book", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // WHEN
        orderService.cancelOrder(orderId);

        // THEN
        Order order = orderRepository.findById(orderId);
        Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        Assertions.assertThat(book.getStockQuantity()).isEqualTo(10);
    }
    
    private Member createMember() {
        Member member = new Member();
        member.setUsername("test");
        member.setAddress(new Address("seoul", "street", "123"));
        em.persist(member);
        return member;
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book(name, price, stockQuantity, "author", "isbn");
        em.persist(book);
        return book;
    }
}