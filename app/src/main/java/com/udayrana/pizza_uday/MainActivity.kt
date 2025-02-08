package com.udayrana.pizza_uday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.udayrana.pizza_uday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPlaceOrder.setOnClickListener {

            // Get the selected pizza type
            var type: String = ""
            when(binding.radioGroupPizzaType.checkedRadioButtonId) {
                binding.radioButtonVegetarian.id -> {
                    type = "Vegetarian"
                }
                binding.radioButtonMeat.id -> {
                    type = "Meat"
                }
            }

            // Get the selected delivery mode
            var deliveryMode: String = ""
            when(binding.radioGroupDeliveryMode.checkedRadioButtonId) {
                binding.radioButtonPickup.id -> {
                    deliveryMode = "Pick-up"
                }
                binding.radioButtonDelivery.id -> {
                    deliveryMode = "Meat"
                }
            }

            // Get the other values
            val size = binding.menuPizzaSize.text.toString()
            val quantity = binding.editTextPizzaQuantity.text.toString().toIntOrNull()
            val phone = binding.editTextPhoneNumber.text.toString()
            val notify = binding.checkBoxNotify.isChecked

            // Validate all the necessary fields
            // Validate size
            if (size.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    "Error: Select size",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validate quantity
            if (quantity == null) {
                Snackbar.make(
                    binding.root,
                    "Error: Enter quantity",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (quantity <= 0 || quantity >= 11) {
                Snackbar.make(
                    binding.root,
                    "Error: Quantity must be between 1 and 10",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validate phone number
            if (phone.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    "Error: Enter phone number",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!Regex("^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}\$").matches(phone)) {
                Snackbar.make(
                    binding.root,
                    "Error: Invalid phone number $phone",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Calculate the order details
            /**
             * Subtotal = (quantity) * (cost of pizza of selected size)
             * Discount: Give 10% discount if the subtotal is more than $80.
             * Tax: calculate tax on (Subtotal – Discount). Assume tax is 13%
             * Total Cost = (Subtotal – Discount) + Tax + Delivery Fee (if applicable)
             * */

            // Display the order details in a text view
            binding.textViewOrderInfo.text = """
                ${type}
                ${size}
                ${quantity}
                ${phone}
                ${deliveryMode}
                ${notify}
            """.trimIndent()
        }
    }
}