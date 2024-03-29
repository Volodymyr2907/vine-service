package com.mentorship.vineservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_vine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderVine {

    @EmbeddedId
    private OrderVineId orderVineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vineId")
    private Vine vine;

    @Column(name = "vine_amount", nullable = false, length = 5)
    private Integer vineAmount;


    public OrderVine(Order order, Vine vine, Integer vineAmount) {
        this.order = order;
        this.vine = vine;
        this.vineAmount = vineAmount;
        this.orderVineId = new OrderVineId(order.getId(), vine.getId());
    }
}