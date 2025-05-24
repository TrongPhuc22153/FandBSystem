package com.phucx.phucxfandb.service.notification.impl;

import com.phucx.phucxfandb.constant.*;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.service.notification.NotificationUpdateService;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import com.phucx.phucxfandb.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.phucx.phucxfandb.constant.WebSocketEndpoint.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendOrderNotificationServiceImp implements SendOrderNotificationService {
    private final NotificationUpdateService notificationUpdateService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendNotificationToUser(String orderId, RequestNotificationDTO requestNotificationDTO) {
        NotificationUserDTO notificationDTO = notificationUpdateService.createOrderNotification(
                requestNotificationDTO.getSenderUsername(), orderId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSendToUser(
                requestNotificationDTO.getReceiverUsername(),
                QUEUE_MESSAGES,
                notificationDTO
        );
    }

    @Override
    public void sendNotificationToGroup(String orderId, String topic, RequestNotificationDTO requestNotificationDTO) {
        NotificationUserDTO notificationDTO = notificationUpdateService.createOrderNotification(
                requestNotificationDTO.getSenderUsername(), orderId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSend(
                topic,
                notificationDTO
        );
    }

    @Override
    public void sendNotificationForOrderAction(Authentication authentication, String orderId, OrderAction action, OrderType type, OrderDTO order) {
        String username = authentication.getName();

        switch (action) {
            case PREPARING -> sendPreparingNotification(username, orderId, type, order);
            case READY -> sendReadyNotification(username, orderId, type, order);
            case COMPLETE -> sendCompleteNotification(username, orderId, type, order);
            case CANCEL -> sendCancelNotification(authentication, orderId, type, order);
        }
    }

    @Override
    public void sendPlaceOrderNotification(Authentication authentication, String orderId, OrderType type, String paymentMethod, PaymentStatus paymentStatus) {
        if (paymentMethod.equalsIgnoreCase(PaymentMethodConstants.PAY_PAL) && paymentStatus != PaymentStatus.SUCCESSFUL) {
            log.warn("Order {} notification skipped due to payment status: {}", orderId, paymentStatus);
            return;
        } else if (paymentMethod.equalsIgnoreCase(PaymentMethodConstants.COD) && paymentStatus != PaymentStatus.PENDING) {
            log.warn("Order {} notification skipped due to payment status: {}", orderId, paymentStatus);
            return;
        }

        String username = authentication.getName();
        String readableOrderType = type.name().toLowerCase().replace('_', ' ');
        if (type == OrderType.TAKE_AWAY) {
            String customerMessage = paymentMethod.equalsIgnoreCase(PaymentMethodConstants.COD)
                    ? String.format("Your %s order #%s has been placed. We'll notify you when it's ready!", readableOrderType, orderId)
                    : String.format("Your %s order #%s has been successfully placed and paid. We'll notify you when it's ready!", readableOrderType, orderId);
            RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                    username,
                    NotificationTopic.ORDER,
                    NotificationTitle.ORDER_PLACED,
                    customerMessage
            );
            sendNotificationToUser(orderId, customerNotification);
        }

        String paymentMessage = paymentMethod.equalsIgnoreCase(PaymentMethodConstants.COD)
                ? "(Payment: PENDING - COD)"
                : "(Payment: SUCCESS)";
        RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                username,
                RoleName.EMPLOYEE,
                NotificationTopic.ORDER,
                NotificationTitle.ORDER_PLACED,
                String.format("New %s order #%s %s has been placed by %s",
                        readableOrderType,
                        orderId,
                        paymentMessage,
                        type == OrderType.TAKE_AWAY ? "customer " + username : username)
        );
        sendNotificationToGroup(orderId, TOPIC_KITCHEN, employeeNotification);
    }

    @Override
    public void sendPreparingNotification(String employeeUsername, String orderId, OrderType type, OrderDTO order) {
        if (type == OrderType.TAKE_AWAY) {
            RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                    order.getCustomer().getProfile().getUser().getUsername(),
                    NotificationTopic.ORDER,
                    NotificationTitle.ORDER_PREPARING,
                    String.format("Your order #%s is now being preparing by our staff", orderId)
            );

            this.sendNotificationToUser(orderId, customerNotification);
        }

        RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                employeeUsername,
                RoleName.EMPLOYEE,
                NotificationTopic.ORDER,
                NotificationTitle.ORDER_PREPARING,
                String.format("Order #%s is now being preparing by %s", orderId, employeeUsername)
        );

        this.sendNotificationToGroup(orderId, TOPIC_KITCHEN, employeeNotification);
    }

    @Override
    public void sendReadyNotification(String employeeUsername, String orderId, OrderType type, OrderDTO order) {
        if (type == OrderType.TAKE_AWAY) {
            RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                    order.getCustomer().getProfile().getUser().getUsername(),
                    NotificationTopic.ORDER,
                    NotificationTitle.ORDER_PREPARED,
                    String.format("Good news! Your order #%s is now ready for pickup", orderId)
            );

            this.sendNotificationToUser(orderId, customerNotification);
        }

        RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                employeeUsername,
                RoleName.EMPLOYEE,
                NotificationTopic.ORDER,
                NotificationTitle.ORDER_PREPARED,
                String.format("Order #%s is prepared and ready for %s", orderId,
                        type == OrderType.TAKE_AWAY ? "customer pickup" : "service")
        );

        this.sendNotificationToGroup(orderId, TOPIC_EMPLOYEE, employeeNotification);
    }

    @Override
    public void sendCompleteNotification(String employeeUsername, String orderId, OrderType type, OrderDTO order) {
        if (type == OrderType.TAKE_AWAY) {
            RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                    order.getCustomer().getProfile().getUser().getUsername(),
                    NotificationTopic.ORDER,
                    NotificationTitle.ORDER_COMPLETED,
                    String.format("Your order #%s has been successfully completed. Thank you for your business!", orderId)
            );

            this.sendNotificationToUser(orderId, customerNotification);
        }

        RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                employeeUsername,
                RoleName.EMPLOYEE,
                NotificationTopic.ORDER,
                NotificationTitle.ORDER_COMPLETED,
                String.format("Order #%s has been marked as complete by %s", orderId, employeeUsername)
        );

        this.sendNotificationToGroup(orderId, TOPIC_EMPLOYEE, employeeNotification);
    }

    @Override
    public void sendCancelNotification(Authentication authentication, String orderId, OrderType type, OrderDTO order) {
        String username = authentication.getName();
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE") || a.getAuthority().equals("ROLE_ADMIN"));

        if (isEmployee && type == OrderType.TAKE_AWAY) {
            RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                    order.getCustomer().getProfile().getUser().getUsername(),
                    NotificationTopic.ORDER,
                    NotificationTitle.ORDER_CANCELLED,
                    String.format("Your order #%s has been cancelled by our staff. Please contact us for more information.", orderId)
            );

            this.sendNotificationToUser(orderId, customerNotification);
        }

        if (!isEmployee && type == OrderType.DINE_IN) {
            RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                    username,
                    RoleName.EMPLOYEE,
                    NotificationTopic.ORDER,
                    NotificationTitle.ORDER_CANCELLED,
                    String.format("Order #%s has been cancelled by customer %s", orderId, username)
            );

            this.sendNotificationToGroup(orderId, TOPIC_EMPLOYEE, employeeNotification);
        }
    }
}   
