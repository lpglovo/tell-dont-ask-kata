package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        Order order = new Order(OrderStatus.CREATED);

        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = productCatalog.getByName(itemRequest.getProductName());

            if (product == null) {
                throw new UnknownProductException();
            }

            addProductTo(order, itemRequest, product);

        }

        orderRepository.save(order);
    }

    private void addProductTo(Order order, SellItemRequest itemRequest, Product product) {
        final BigDecimal itemQuantity = valueOf(itemRequest.getQuantity());
        final BigDecimal taxedAmount = product.getTotalTaxedAmount(itemQuantity);
        final BigDecimal taxAmount = product.getTaxAmount(itemQuantity);

        final OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setTax(taxAmount);
        orderItem.setTaxedAmount(taxedAmount);
        order.getItems().add(orderItem);

        order.setTotal(order.getTotal().add(taxedAmount));
        order.setTax(order.getTax().add(taxAmount));
    }
}
