package com.nirmaansetu.backend.config;

import com.nirmaansetu.backend.statemachine.OrderEvent;
import com.nirmaansetu.backend.statemachine.OrderState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states.withStates()
            .initial(OrderState.CREATED)
            .states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        transitions
            .withExternal().source(OrderState.CREATED).target(OrderState.PAID).event(OrderEvent.PAY)
            .and()
            .withExternal().source(OrderState.PAID).target(OrderState.SHIPPED).event(OrderEvent.SHIP)
            .and()
            .withExternal().source(OrderState.SHIPPED).target(OrderState.DELIVERED).event(OrderEvent.DELIVER)
            .and()
            .withExternal().source(OrderState.CREATED).target(OrderState.CANCELLED).event(OrderEvent.CANCEL);
    }
}