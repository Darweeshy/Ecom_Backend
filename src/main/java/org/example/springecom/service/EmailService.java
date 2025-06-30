package org.example.springecom.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.example.springecom.model.Order;
import org.example.springecom.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    /**
     * A private helper method to construct and send an email using SendGrid.
     * @param to The recipient's email address.
     * @param subject The subject line of the email.
     * @param body The plain text content of the email.
     */
    private void sendEmail(String to, String subject, String body) {
        // IMPORTANT: You must verify a sender email or domain in your SendGrid account.
        // Replace this with your verified sender address.
        Email toEmail = new Email(to);// Use the email you verified on the SendGrid dashboard
        Email from = new Email("alidarwish618@gmail.com", "Snatch It");
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Email sent to " + to + " with status code: " + response.getStatusCode());
        } catch (IOException ex) {
            System.err.println("Error sending email to " + to + ": " + ex.getMessage());
        }
    }

    /**
     * Sends a welcome email to a new user upon registration.
     * @param user The newly registered user.
     */
    public void sendWelcomeEmail(User user) {
        String subject = "Welcome to Snatch It!";
        String body = "Hi " + user.getUsername() + ",\n\nThank you for creating an account with Snatch It. Get ready to find the best athletic wear for your workouts!";
        sendEmail(user.getEmail(), subject, body);
    }

    /**
     * Sends a password reset link to a user.
     * @param user The user who requested the reset.
     * @param token The unique, single-use reset token.
     */
    public void sendPasswordResetEmail(User user, String token) {
        String subject = "Your Snatch It Password Reset Link";
        // The domain should come from a config file in a production app
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        String body = "Hi " + user.getUsername() + ",\n\nYou requested a password reset. Please click the link below to set a new password. This link is valid for one hour.\n\n" + resetUrl;
        sendEmail(user.getEmail(), subject, body);
    }

    /**
     * Sends an order confirmation email to a customer and a notification to the admin.
     * @param order The successfully placed order.
     */
    public void sendOrderConfirmationEmail(Order order) {
        // Email to the customer
        String customerSubject = "Your Snatch It Order Confirmation #" + order.getOrderTrackingNumber().substring(0, 8).toUpperCase();
        String customerBody = "Hi " + order.getUser().getUsername() + ",\n\nThank you for your order! We've received it and are getting it ready for shipment. You can view your order details in your account.\n\nTotal: " + order.getTotalPrice() + " EGP.";
        sendEmail(order.getUser().getEmail(), customerSubject, customerBody);

        // Notification email to the admin
        sendNewOrderNotificationToAdmin(order);
    }

    /**
     * Sends a shipping notification with a tracking number to the customer.
     * @param order The order that has been shipped.
     * @param trackingNumber The shipment tracking number.
     */
    public void sendShippingNotificationEmail(Order order, String trackingNumber) {
        String subject = "Great News! Your Snatch It Order has Shipped!";
        String body = "Hi " + order.getUser().getUsername() + ",\n\nYour order #" + order.getOrderTrackingNumber().substring(0, 8).toUpperCase() + " is on its way.\n\nYou can track it using this number: " + trackingNumber;
        sendEmail(order.getUser().getEmail(), subject, body);
    }

    /**
     * Sends a new order notification to a hardcoded admin email address.
     * @param order The newly placed order.
     */
    public void sendNewOrderNotificationToAdmin(Order order) {
        // This should be an environment variable in a production app
        String adminEmail = "admin@snatchit.com";
        String subject = "New Order Received! #" + order.getOrderTrackingNumber().substring(0, 8).toUpperCase();
        String body = "A new order was placed by " + order.getUser().getUsername() + " for a total of " + order.getTotalPrice() + " EGP.\n\nPlease log in to the admin dashboard to view the details and process the order.";
        sendEmail(adminEmail, subject, body);
    }
}