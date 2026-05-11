package com.fooddelivery.util;

import com.fooddelivery.entity.Order;
import com.fooddelivery.entity.OrderItem;

public class EmailTemplateUtil {

//     public static String buildOrderEmail(Order order) {

//         StringBuilder itemsHtml = new StringBuilder();

//         for (OrderItem item : order.getItems()) {
//             itemsHtml.append(
//                 "<tr>" +
//                 "<td>" + item.getMenu().getName() + "</td>" +
//                 "<td>" + item.getQuantity() + "</td>" +
//                 "<td>₹" + item.getPrice() + "</td>" +
//                 "</tr>"
//             );
//         }

//         return """
//         <html>
//         <body style="font-family: Arial; background:#f4f4f4; padding:20px;">
//             <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;">
                
//                 <h2 style="color:#ff6600;">🍔 Order Confirmation</h2>

//                 <p>Hi %s,</p>

//                 <p>Your order <b>#%d</b> has been placed successfully.</p>

//                 <h3>📦 Order Details:</h3>

//                 <table style="width:100%%; border-collapse: collapse;">
//                     <tr style="background:#eee;">
//                         <th align="left">Item</th>
//                         <th>Qty</th>
//                         <th>Price</th>
//                     </tr>
//                     %s
//                 </table>

//                 <h3>Total: ₹%.2f</h3>

//                 <p>📍 Delivery Address: %s</p>

//                 <hr/>

//                 <p style="font-size:12px; color:gray;">
//                     Thank you for ordering with us ❤️
//                 </p>

//             </div>
//         </body>
//         </html>
//         """.formatted(
//                 order.getCustomer().getName(),
//                 order.getId(),
//                 itemsHtml.toString(),
//                 order.getTotalAmount(),
//                 order.getDeliveryAddress()
//         );
//     }
// }

public static String buildOrderEmail(Order order) {

    StringBuilder itemsHtml = new StringBuilder();

    for (OrderItem item : order.getItems()) {
        itemsHtml.append(
            "<tr>" +
                "<td style='padding:8px;border-bottom:1px solid #eee;'>" 
                    + item.getMenu().getName() + "</td>" +
                "<td style='padding:8px;text-align:center;border-bottom:1px solid #eee;'>" 
                    + item.getQuantity() + "</td>" +
                "<td style='padding:8px;text-align:right;border-bottom:1px solid #eee;'>₹" 
                    + item.getPrice() + "</td>" +
            "</tr>"
        );
    }

    return """
    <html>
    <body style="margin:0;padding:0;background:#f6f7fb;font-family:Arial,sans-serif;">

        <div style="max-width:600px;margin:auto;background:#ffffff;border-radius:10px;overflow:hidden;">

            <!-- HEADER -->
            <div style="background:#ff6b00;padding:20px;text-align:center;color:white;">
                <h1 style="margin:0;">🍔 Food Delivery</h1>
                <p style="margin:5px 0 0;">Order Confirmation</p>
            </div>

            <!-- BODY -->
            <div style="padding:20px;">

                <p style="font-size:16px;">Hi <b>%s</b>,</p>

                <p>Your order has been placed successfully 🎉</p>

                <div style="background:#f1f3f6;padding:15px;border-radius:8px;margin:15px 0;">
                    <b>Order ID:</b> #%d <br/>
                    <b>Status:</b> PLACED <br/>
                    <b>Address:</b> %s
                </div>

                <!-- ITEMS TABLE -->
                <table style="width:100%%;border-collapse:collapse;">
                    <tr style="background:#f9f9f9;">
                        <th style="text-align:left;padding:10px;">Item</th>
                        <th style="text-align:center;padding:10px;">Qty</th>
                        <th style="text-align:right;padding:10px;">Price</th>
                    </tr>
                    %s
                </table>

                <!-- TOTAL -->
                <div style="margin-top:20px;text-align:right;">
                    <h2 style="color:#ff6b00;">Total: ₹%.2f</h2>
                </div>

                <!-- CTA BUTTON -->
                <div style="text-align:center;margin:25px 0;">
                    <a href="http://localhost:3000/orders/%d"
                       style="background:#ff6b00;color:white;padding:12px 25px;
                              text-decoration:none;border-radius:5px;display:inline-block;">
                        Track Your Order
                    </a>
                </div>

            </div>

            <!-- FOOTER -->
            <div style="background:#f1f1f1;text-align:center;padding:15px;font-size:12px;color:#777;">
                Thanks for ordering with us ❤️ <br/>
                Food Delivery System
            </div>

        </div>

    </body>
    </html>
    """.formatted(
        order.getCustomer().getName(),
        order.getId(),
        order.getDeliveryAddress(),
        itemsHtml.toString(),
        order.getTotalAmount(),
        order.getId()
    );
}




}