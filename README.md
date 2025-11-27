# Snatch-It — Full-Stack E-Commerce Platform

Snatch-It is a modern e-commerce web application with a public storefront and a secure admin dashboard for managing products, orders, media, and content. Built using React and Spring Boot, it offers flexibility, performance, and a clean UI/UX.

---

##  Access the Application

[https://snatch-it-frontend.vercel.app](https://snatch-it-frontend.vercel.app)

---

## Clone Backend
https://github.com/Darweeshy/Ecom_Backend

##  Features

### Admin Dashboard
- Secure login with JWT authentication
- Add, edit, and delete products with support for variants (color, size, price, stock)
- Manage categories with image upload and nested structure
- View and update customer orders with live status tracking
- Upload and organize images in folders using the media library
- Create and manage discount coupons (fixed or percentage-based)
- Manage static content pages (e.g., About Us, Privacy Policy)
- Dashboard overview with key store metrics like revenue and stock levels

### Storefront
- Browse products by category and apply filters (e.g., price, brand)
- Add products to cart and proceed to checkout
- Support for guest checkout and registered user accounts
- Pay using Cash on Delivery or online via Paymob
- Track order status using a tracking number
- Reset forgotten passwords via email

---

##  Tech Stack

### Backend
- Java 21
- Spring Boot 3.5
- Spring Security with JWT
- Spring Data JPA (Hibernate)
- PostgreSQL

### Frontend
- React (Vite)
- React Router
- Axios
- Tailwind CSS

### Deployment
- Backend: Render (Docker)
- Frontend: Vercel
- Database: Supabase (PostgreSQL)

---

##  Getting Started (Development Setup)

### Requirements
- Java 21
- Node.js v18+
- PostgreSQL
- Maven

---

###  Backend Setup

1. Clone the backend repository.
2. Create a file at `src/main/resources/application-local.properties` with your local config:

DB_URL=jdbc:postgresql://localhost:5432/your_db_name
DB_USERNAME=your_user
DB_PASSWORD=your_password
JWT_SECRET=your_secret
SENDGRID_API_KEY=your_sendgrid_api_key
PAYMOB_API_KEY=your_paymob_api_key
PAYMOB_INTEGRATION_ID=your_paymob_integration_id
PAYMOB_HMAC_SECRET=your_paymob_hmac_secret


3. Start the backend server:

```bash
./mvnw spring-boot:run
App will be running at: http://localhost:8080

Frontend Setup
Clone this frontend repository.

Navigate to the project directory and install dependencies:


npm install
Create a .env.local file in the root directory:

=
Edit
VITE_API_BASE_URL=http://localhost:8080
VITE_PAYMOB_IFRAME_ID=your_paymob_iframe_id
Run the development server:

bash
Copy
Edit
npm run dev
Frontend will be available at: http://localhost:5173

🔐 Admin Access
To test the admin dashboard, you can log in with:


Username: ali1
Password: ali1
You can change the password or role later in the database.
