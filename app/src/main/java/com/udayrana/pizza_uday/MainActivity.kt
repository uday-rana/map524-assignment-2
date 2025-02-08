package com.udayrana.pizza_uday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udayrana.pizza_uday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPlaceOrder.setOnClickListener {

            // Get the selected pizza type
            var type = ""
            when(binding.radioGroupPizzaType.checkedRadioButtonId) {
                binding.radioButtonVegetarian.id -> {
                    type = "Vegetarian"
                }
                binding.radioButtonMeat.id -> {
                    type = "Meat"
                }
            }

            // Get the selected delivery mode
            var deliveryMode = ""
            when(binding.radioGroupDeliveryMode.checkedRadioButtonId) {
                binding.radioButtonPickup.id -> {
                    deliveryMode = "Pick-up"
                }
                binding.radioButtonDelivery.id -> {
                    deliveryMode = "Delivery"
                }
            }

            // Get the other values
            val size = binding.menuPizzaSize.text.toString()
            val quantity = binding.editTextPizzaQuantity.text.toString().toIntOrNull()
            val phone = binding.editTextPhoneNumber.text.toString()
            val notify = binding.checkBoxNotify.isChecked

            // Validate all the necessary fields
            var error = false

            // Validate size
            if (size.isEmpty()) {
                binding.menuPizzaSizeLayout.error = "Select size"
                error = true
            }

            // Validate quantity
            if (quantity == null) {
                binding.editTextPizzaQuantityLayout.error = "Enter quantity"
                error = true
            }

            if (quantity != null && (quantity <= 0 || quantity >= 11)) {
                binding.editTextPizzaQuantityLayout.error = "Quantity must be between 1 and 10"
                error = true
            }

            // Validate phone number
            if (phone.isEmpty()) {
                binding.editTextPhoneNumberLayout.error = "Enter phone number"
                error = true
            }

            if (!Regex("^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}\$").matches(phone)) {
                binding.editTextPhoneNumberLayout.error = "Invalid phone number"
                error = true
            }

            if (error) {
                return@setOnClickListener
            }

            // Get size price and name from selection
            var sizeName: String = ""
            var sizePrice: Double = 0.00
            when (size) {
                "Small - \$12.99" -> {
                    sizeName = "Small"
                    sizePrice = 12.99
                }
                "Medium - \$16.99" -> {
                    sizeName = "Medium"
                    sizePrice = 16.99
                }
                "Large - \$20.99" -> {
                    sizeName = "Large"
                    sizePrice = 20.99
                }
                "Party Size - \$26.99" -> {
                    sizeName = "Party Size"
                    sizePrice = 26.99
                }
            }

            /**
             * Calculate the order details
             * Subtotal = (quantity) * (cost of pizza of selected size)
             * Discount: Give 10% discount if the subtotal is more than $80.
             * Tax: calculate tax on (Subtotal – Discount). Assume tax is 13%
             * Total Cost = (Subtotal – Discount) + Tax + Delivery Fee (if applicable)
             * */
            val subtotal = (quantity ?: 0) * sizePrice

            var discount = 0.0
            if (subtotal > 80) {
                discount = subtotal * 90/100
            }

            val tax = subtotal * 13/100

            var total = subtotal + discount + tax

            var deliveryFee = 0.0
            if (deliveryMode.equals("Delivery")) {
                deliveryFee = 3.99
            }
            total += deliveryFee

            // Display the order details in a text view
            var orderDetails = """
                    Subtotal: $${total}
                    Discount: $${discount}
                    Tax: $${tax}
                    Delivery Fee: $${deliveryFee}
                    Total: ${total}
                    
                    Type: ${type}
                    Size: ${sizeName}
                    Quantity: ${quantity}
                    Phone Number: ${phone}
                    Delivery Method: ${deliveryMode}
                    Notify When Ready: ${notify.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}
                    """.trimIndent()


            binding.textViewOrderInfo.text = orderDetails
        }
    }
}