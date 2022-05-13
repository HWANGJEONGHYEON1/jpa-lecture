package jpabook.jpashop.domain.order;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.member.QMember;
import jpabook.jpashop.domain.order.dto.OrderSearch;
import jpabook.jpashop.domain.order.dto.OrderSearchResponse;
import jpabook.jpashop.domain.order.dto.QOrderSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static jpabook.jpashop.domain.member.QMember.*;
import static jpabook.jpashop.domain.order.domain.QOrder.order;
import static jpabook.jpashop.domain.order.domain.QOrderItem.orderItem;


@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositorySearch {

    private final JPAQueryFactory factory;

    @Override
    public List<OrderSearchResponse> orderSearchList(OrderSearch orderSearch) {
        return factory.select(new QOrderSearchResponse(orderItem.id, member.name, orderItem.item.name, orderItem.orderPrice, orderItem.count, order.status, order.orderDate))
                .from(order)
                .leftJoin(order.orderItems, orderItem)
                .join(order.member, member)
                .where(
                        memberNameEq(orderSearch.getMemberName()),
                        orderStatusEq(orderSearch.getOrderStatus())
                ).fetch();
    }

    private BooleanExpression orderStatusEq(OrderStatus orderStatus) {
        return orderStatus != null ? order.status.eq(orderStatus) : null;
    }

    private BooleanExpression memberNameEq(String memberName) {
        return StringUtils.hasText(memberName) ? member.name.eq(memberName) : null;
    }
}
