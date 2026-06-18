package com.breno.marketplace_test.enums;

import com.breno.marketplace_test.exceptions.InvalidOrderStatusTransitionException;

public enum OrderStatus {
    AGUARDANDO_PAGAMENTO,
    PAGO,
    ENVIADO,
    ENTREGUE,
    CANCELADO;

    public boolean canTransitTo(OrderStatus nextStatus){
        return switch (this) {
            case AGUARDANDO_PAGAMENTO -> nextStatus == PAGO || nextStatus == CANCELADO;
            case PAGO -> nextStatus == ENVIADO || nextStatus == CANCELADO;
            case ENVIADO -> nextStatus == ENTREGUE;
            case ENTREGUE, CANCELADO -> false;
        };
    }

    public static void validateTransition(OrderStatus from, OrderStatus to){
        if(from == null || to == null){
            throw new IllegalArgumentException("Status de pedido não pode ser nulo");
        }
        if(!from.canTransitTo(to)){
            throw new InvalidOrderStatusTransitionException("Transição de status inválida: " + from + " -> " + to);
        }
    }
}
