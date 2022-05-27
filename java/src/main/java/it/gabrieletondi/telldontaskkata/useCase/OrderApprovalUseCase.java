package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    public OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        if (order.isShippedAlready()) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (!request.isApproved() && order.isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }


        if (request.isApproved()) order.markAsApproved();
        else order.markAsRejected();

        orderRepository.save(order);
    }

}
