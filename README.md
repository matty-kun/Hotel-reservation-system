# Hotel Transylvania Booking System
Welcome to the **Hotel Transylvania Booking System**, a console-based Java application designed to streamline hotel reservations, provide a user-friendly booking process, and manage customer interactions effectively. This project demonstrates various features relevant to hotel booking, including room selection, customer registration, payment processing, and detailed reservation summaries.
## Features
1. **User Authentication**
    - Register new customers.
    - Login for existing customers.

2. **Room Booking**
    - Select room types based on preferences (Standard, Deluxe, Suite, Family).
    - Real-time availability checks for rooms.
    - Automated handling of conflicts such as unavailable dates or invalid room types.

3. **Reservation System**
    - View your upcoming reservations.
    - Easily navigate through your reservation history.

4. **Payment Integration**
    - Seamless transition to payment processing after booking.
    - Basic receipt generation with payment information for customer reference.

5. **Dynamic Pricing**
    - Calculations for base price, discounts for longer stays, and applicable taxes.

6. **Fun and Intuitive Interface**
    - Welcoming and exiting messages for a better user experience.
    - Styled console outputs with colored texts.

## How to Run the Application
### Prerequisites
1. **Java Installation**
Ensure you have **Java Development Kit (JDK 11 or newer)** installed on your system.
2. **Database Setup**
    - Install and configure **PostgreSQL**.
    - Create a database called `hotel_transylvania`.
    - Use the application to initialize tables and seed initial data.

3. **Dependencies**
This project uses built-in Java utilities and does not require external dependency management.

### Steps to Run
1. Clone the repository:
``` shell
   git clone https://github.com/example/hotel-transylvania.git
   cd hotel-transylvania
```
1. Compile and run:
``` shell
   javac Main.java
   java Main
```
1. Follow the UI prompts to either log in or register as a customer, and start exploring!

## Project Structure
``` 
src/
├── db/                    # Database operations and interactions
├── models/                # Data classes (Customer, Room, Payment, etc.)
├── services/              # Core logic for booking, payments, authentication, etc.
├── utils/                 # Reusable utility methods (e.g., colors, input validation)
└── Main.java              # Application entry point
```
## Key Components Overview
### Booking Process
- **Room Selection**
Allows customers to select room types with detailed descriptions and displays available options.
- **Conflict Resolution**
Handles invalid inputs or unavailable rooms gracefully, offering users the ability to retry.
- **Reservation Creation**
Combines customer data, room availability, and preferences to create a confirmed reservation.

### Payment Processing
- Integrated price breakdown utility () dynamically applies discounts and calculates totals including holidays and long stays. `PriceCalculator.java`
- Receipt generation for customers () with the option to save or view details. `Payment.java`

### Console Interaction
- Styled prompts using enhance the look and feel of the terminal-based UI. `ConsoleColors`
- Offers friendly error handling and detailed instructions for smooth navigation.

## Example Usage
### 1. Login/Register
The home screen allows new users to register or log into the system. Registration involves inputting personal details such as name, email, and password.
### 2. Booking a Room
Customers can:
- View available room types.
- Select based on price and preferences.
- Confirm their reservation.

### 3. Payment
Detailed receipts are generated upon successful payments, showing all relevant details such as room type, stay duration, and the total amount paid, including tax and discounts.
## Future Enhancements
- **Integration with an external payment gateway** for advanced payment options.
- **Web-based UI** for better accessibility.
- Implement more **robust error handling** and logging for system administrators.
- Leverage **API services** for additional features (e.g., sending email receipts to customers).

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.
Thank you for choosing **Hotel Transylvania**! We hope you enjoy your stay. 🏨
